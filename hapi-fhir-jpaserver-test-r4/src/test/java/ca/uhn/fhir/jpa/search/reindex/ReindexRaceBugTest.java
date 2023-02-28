package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.test.concurrency.LockstepEnumPhaser;
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

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
	 *
	 * This was inserting new index rows into HFJ_SPIDX_TOKEN even though the resource was gone.
	 * This is an illegal state for our index.  Deleted resources should never have content in HFJ_SPIDX_*
	 */
	@Test
	void deleteOverlapsWithReindex_leavesIndexRowsP() throws InterruptedException, ExecutionException {
		LockstepEnumPhaser<Steps> phaser = new LockstepEnumPhaser<>(2, Steps.class);

		ourLog.info("An observation is created");

		var observationCreateOutcome = callInFreshTx((tx, rd) -> {
			Observation o = (Observation) buildResource("Observation", withEffectiveDate("2021-01-01T00:00:00"));
			return myObservationDao.create(o, rd);
		});
		IIdType observationId = observationCreateOutcome.getId().toVersionless();
		long observationPid = Long.parseLong(observationCreateOutcome.getId().getIdPart());

		assertEquals(1, getSPIDXDateCount(observationPid), "date index row for date");

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

		assertEquals(1, getSPIDXDateCount(observationPid), "still only one index row before reindex");


		// suppose reindex job step starts here and loads the resource and ResourceTable entity
		ThreadFactory loggingThreadFactory = getLoggingThreadFactory("Reindex-thread");
		ExecutorService backgroundReindexThread = Executors.newSingleThreadExecutor(loggingThreadFactory);
		Future<Integer> backgroundResult = backgroundReindexThread.submit(() -> {
			try {
				callInFreshTx((tx, rd) -> {
					try {
						ourLog.info("Starting background $reindex");
						phaser.arriveAndAwaitSharedEndOf(Steps.STARTING);

						phaser.assertInPhase(Steps.RUN_REINDEX);
						ourLog.info("Run $reindex");
						myObservationDao.reindex(JpaPid.fromIdAndResourceType(observationPid, "Observation"), rd, new TransactionDetails());

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
				phaser.arriveAndAwaitSharedEndOf(Steps.COMMIT_REINDEX);
			}
			return 1;
		});

		ourLog.info("Wait for $reindex to start");
		phaser.arriveAndAwaitSharedEndOf(Steps.STARTING);

		phaser.arriveAndAwaitSharedEndOf(Steps.RUN_REINDEX);

		// then the resource is deleted
		phaser.assertInPhase(Steps.RUN_DELETE);

		ourLog.info("Deleting observation");
		callInFreshTx((tx, rd) -> myObservationDao.delete(observationId, rd));
		assertResourceDeleted(observationId);
		assertEquals(0, getSPIDXDateCount(observationPid), "A deleted resource should have 0 index rows");

		ourLog.info("Let $reindex commit");
		phaser.arriveAndAwaitSharedEndOf(Steps.RUN_DELETE);

		// then the reindex call finishes
		ourLog.info("Await $reindex commit");
		phaser.arriveAndAwaitSharedEndOf(Steps.COMMIT_REINDEX);

		assertEquals(0, getSPIDXDateCount(observationPid), "A deleted resource should still have 0 index rows, after $reindex completes");
	}

	@Nonnull
	static ThreadFactory getLoggingThreadFactory(String theThreadName) {
		ThreadFactory loggingThreadFactory = r -> new Thread(() -> {
			boolean success = false;
			try {
				r.run();
				success = true;
			} finally {
				if (!success) {
					ourLog.error("Background thread failed");
				}
			}
		}, theThreadName);
		return loggingThreadFactory;
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
