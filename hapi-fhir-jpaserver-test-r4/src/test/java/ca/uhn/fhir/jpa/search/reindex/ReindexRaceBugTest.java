package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.test.concurrency.LockstepEnumPhaser;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = {
	DaoTestDataBuilder.Config.class
})
class ReindexRaceBugTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexRaceBugTest.class);
	@Autowired
	HapiTransactionService myHapiTransactionService;

	enum Steps {
		STARTING, RUN_REINDEX, RUN_DELETE, COMMIT_REINDEX, FINISHED
	}

	/**
	 * Simulate reindex job step overlapping with resource deletion.
	 * The $reindex step processes several resources in a single tx.
	 * The tested sequence here is: job step $reindexes a resouce, then another thread DELETEs the resource,
	 * then later, the $reindex step finishes the rest of the resources and commits AFTER the DELETE commits.
	 * This scenario could insert index rows into HFJ_SPIDX_TOKEN even though the resource was gone.
	 * This is an illegal state for our index.  Deleted resources should never have content in HFJ_SPIDX_*
	 * Fixed by taking an optimistic lock on hfj_resource even though $reindex is read-only on that table.
	 */
	@Test
	void deleteOverlapsWithReindex_leavesIndexRowsP() {
		LockstepEnumPhaser<Steps> phaser = new LockstepEnumPhaser<>(2, Steps.class);

		ourLog.info("An observation is created");

		var observationCreateOutcome = callInFreshTx((tx, rd) -> {
			Observation o = (Observation) buildResource("Observation", withEffectiveDate("2021-01-01T00:00:00"));
			return myObservationDao.create(o, rd);
		});
		IIdType observationId = observationCreateOutcome.getId().toVersionless();
		long observationPid = Long.parseLong(observationCreateOutcome.getId().getIdPart());

		assertThat(getSPIDXDateCount(observationPid)).as("date index row for date").isEqualTo(1);

		ourLog.info("Then a SP is created after that matches data in the Observation");
		SearchParameter sp = myFhirContext.newJsonParser().parseResource(SearchParameter.class, """
			{
			  "resourceType": "SearchParameter",
			  "id": "observation-date2",
			  "status": "active",
			  "code": "date2",
			  "name": "date2",
			  "base": [ "Observation" ],
			  "type": "date",
			  "expression": "Observation.effective"
			}
			""");
		this.myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);
		callInFreshTx((tx, rd) -> {
			DaoMethodOutcome result = mySearchParameterDao.update(sp, rd);
			mySearchParamRegistry.forceRefresh();
			return result;
		});


		assertThat(getSPIDXDateCount(observationPid)).as("still only one index row before reindex").isEqualTo(1);

		ReindexParameters reindexParameters = new ReindexParameters();
		reindexParameters.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.ALL);
		reindexParameters.setOptimisticLock(true);
		reindexParameters.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.NONE);

		// suppose reindex job step starts here and loads the resource and ResourceTable entity
		ExecutorService backgroundReindexThread = Executors.newSingleThreadExecutor(new BasicThreadFactory.Builder().namingPattern("Reindex-thread-%d").build());
		Future<Integer> backgroundResult = backgroundReindexThread.submit(() -> {
			try {
				callInFreshTx((tx, rd) -> {
					try {
						ourLog.info("Starting background $reindex");
						phaser.arriveAndAwaitSharedEndOf(Steps.STARTING);

						phaser.assertInPhase(Steps.RUN_REINDEX);
						ourLog.info("Run $reindex");
						myObservationDao.reindex(JpaPid.fromIdAndResourceType(observationPid, "Observation"), reindexParameters, rd, new TransactionDetails());

						ourLog.info("$reindex done release main thread to delete");
						phaser.arriveAndAwaitSharedEndOf(Steps.RUN_REINDEX);

						ourLog.info("Wait for delete to finish");
						phaser.arriveAndAwaitSharedEndOf(Steps.RUN_DELETE);

						phaser.assertInPhase(Steps.COMMIT_REINDEX);
						ourLog.info("Commit $reindex now that delete is finished");
						// commit happens here at end of block
					} catch (Exception e) {
						ourLog.error("$reindex thread failed", e);
					}
					return 0;
				});
			} finally {
				ourLog.info("$reindex commit complete");
				phaser.arriveAndAwaitSharedEndOf(Steps.COMMIT_REINDEX);
			}
			return 1;
		});

		ourLog.info("Wait for $reindex to start");
		phaser.arriveAndAwaitSharedEndOf(Steps.STARTING);

		phaser.arriveAndAwaitSharedEndOf(Steps.RUN_REINDEX);

		// then the resource is deleted
		ourLog.info("Deleting observation");
		phaser.assertInPhase(Steps.RUN_DELETE);
		callInFreshTx((tx, rd) -> myObservationDao.delete(observationId, rd));
		assertResourceDeleted(observationId);
		assertThat(getSPIDXDateCount(observationPid)).as("A deleted resource should have 0 index rows").isEqualTo(0);

		ourLog.info("Let $reindex commit");
		phaser.arriveAndAwaitSharedEndOf(Steps.RUN_DELETE);

		// then the reindex call finishes
		ourLog.info("Await $reindex commit");
		phaser.arriveAndAwaitSharedEndOf(Steps.COMMIT_REINDEX);

		assertThat(getSPIDXDateCount(observationPid)).as("A deleted resource should still have 0 index rows, after $reindex completes").isEqualTo(0);

		// Verify the exception from $reindex
		// In a running server, we expect UserRequestRetryVersionConflictsInterceptor to cause a retry inside the ReindexStep
		// But here in the test, we have not configured any retry logic.
		ExecutionException e = assertThrows(ExecutionException.class, backgroundResult::get, "Optimistic locking detects the DELETE and rolls back");
		assertThat(e.getCause()).as("Hapi maps conflict exception type").isInstanceOf(ResourceVersionConflictException.class);
	}

	void assertResourceDeleted(IIdType observationId) {
		try {
			// confirm deleted
			callInFreshTx((tx, rd)->
				myObservationDao.read(observationId, rd, false));
			fail("Read deleted resource");
		} catch (ResourceGoneException e) {
			// expected
		}
	}

	<T> T callInFreshTx(BiFunction<TransactionStatus, RequestDetails, T> theCallback) {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		return myHapiTransactionService.withRequest(requestDetails)
			.withTransactionDetails(new TransactionDetails())
			.withPropagation(Propagation.REQUIRES_NEW)
			.execute(tx-> theCallback.apply(tx, requestDetails));
	}


	int getSPIDXDateCount(long observationPid) {
		return callInFreshTx((rd, tx) ->
			myResourceIndexedSearchParamDateDao.findAllForResourceId(observationPid).size());
	}

}
