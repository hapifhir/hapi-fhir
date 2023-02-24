package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.JpaResourceDaoObservation;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.ThreadPoolFactory;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ContextConfiguration(classes = {
	DaoTestDataBuilder.Config.class,
	TestDaoSearch.Config.class
})
public class ReindexRaceBugTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexRaceBugTest.class);
	@Autowired
	EntityManager myEntityManager;
	TransactionTemplate myTransactionTemplate;
	@Autowired
	DaoTestDataBuilder myTDB;
	@Autowired
	TestDaoSearch myDaoSearch;
//	@Autowired
//	JpaResourceDaoObservation myRawDao;
	@Autowired
	HapiTransactionService myHapiTransactionService;

	@BeforeEach
	void setUp() {
		myTransactionTemplate = new TransactionTemplate(getTxManager(), new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
	}

	// wipmb try overlapping job step reindex with resource deletion. if job step has 100 resources, then the delete could finish
	@Test
	void deleteOverlapsWithReindex_leavesIndexRowsP() throws InterruptedException {

		CountDownLatch latchReindexStart = new CountDownLatch(1);
		CountDownLatch latchDeleteFinished = new CountDownLatch(1);
		CountDownLatch latchReindexFinished = new CountDownLatch(1);

		ourLog.info("An observation is created");

		var observationCreateOutcome = callInFreshTx((rd, tx) -> {
			Observation o = (Observation) myTDB.buildResource("Observation", myTDB.withEffectiveDate("2021-01-01T00:00:00"));
			return myObservationDao.create(o, rd);
		});
		IIdType observationId = observationCreateOutcome.getId().toVersionless();
		long observationPid = Long.parseLong(observationCreateOutcome.getId().getIdPart());

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
		DaoMethodOutcome spId = callInFreshTx((rd, tx) -> {
			DaoMethodOutcome result = mySearchParameterDao.update(sp, rd);
			mySearchParamRegistry.forceRefresh();
			return result;
		});

		int beforeRowCount = getSPIDXDateCount(observationPid);
		assertEquals(1, beforeRowCount, "date index row for date");


		// suppose reindex job step starts here and loads the resource and ResourceTable entity
		ExecutorService backgroundReindexThread = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(@NotNull Runnable r) {
				return new Thread(r, "Reindex-thread");
			}
		});
		backgroundReindexThread.submit(()->{

			callInFreshTx((rd, tx) -> {
				try {

					ourLog.info("Starting background $reindex");
					ResourceTable observationRow = myEntityManager.find(ResourceTable.class, observationPid);
					Observation observation = myObservationDao.read(observationId, rd);

					// load the params now.
//			ourLog.info("Fetching date info");
//			observationRow.getParamsDate();

					// then finishes the reindex after the deletion.
					ourLog.info("Run $reindex and release main thread");
					myObservationDao.reindex(observation, observationRow);
					latchReindexStart.countDown();
					// but is paused here
					ourLog.info("Wait for latch");
					latchDeleteFinished.await();
					ourLog.info("Commit");

				} catch (Exception e) {
					ourLog.error("$reindex failed", e);
				}
				return 0;
			});
			latchReindexFinished.countDown();
		});

		ourLog.info("Wait for $reindex to start");
		latchReindexStart.await();

		// then the resource is deleted
		ourLog.info("Deleting observation");
		DaoMethodOutcome deleteResult = callInFreshTx((rd, tx) ->
			myObservationDao.delete(observationId, rd));

		assertEquals(0, getSPIDXDateCount(observationPid), "A deleted resource should have 0 index rows");

		try {
			// confirm deleted
			Observation readBack = callInFreshTx((rd, tx)->
				myObservationDao.read(observationId, rd, false));
			ourLog.debug("Observation readBack {}", myFhirContext.newJsonParser().encodeResourceToString(readBack));
			fail("Read deleted resource");
		} catch (ResourceGoneException e) {
			// expected
		}

		// then the reindex call finishes
		ourLog.info("Release $reindex");
		latchDeleteFinished.countDown();


		ourLog.info("Await $reindex finish");
		latchReindexFinished.await();

		assertEquals(0, getSPIDXDateCount(observationPid), "A deleted resource should still have 0 index rows, after $reindex completes");

	}


	@Test
	void testDeleteThenRead_throwsException() {
	    // given
		var observationCreate = callInFreshTx((rd, tx) -> {
			Observation o = (Observation) myTDB.buildResource("Observation", myTDB.withEffectiveDate("2021-01-01T00:00:00"));
			return myObservationDao.create(o, rd);
		});
		IIdType observationId = observationCreate.getId().toVersionless();

		// when
		ourLog.info("Deleting observation");
		DaoMethodOutcome deleteResult = callInFreshTx((rd, tx) ->
			myObservationDao.delete(observationId, rd));
		assertTrue(deleteResult.getEntity().isDeleted(), "row is deleted");
		assertEquals(2, deleteResult.getEntity().getVersion(), "version changed");

		try {
			// confirm deleted
			Observation readBack = callInFreshTx((rd, tx) ->
				myObservationDao.read(observationId, rd, false));
			fail("Read deleted resource");
		} catch (ResourceGoneException e) {
			// expected
		}
	}

	private <T> T callInFreshTx(BiFunction<RequestDetails, TransactionStatus, T> theCallback) {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		return myHapiTransactionService.withRequest(requestDetails)
			.withTransactionDetails(new TransactionDetails())
			.withPropagation(Propagation.REQUIRES_NEW)
			.execute(tx-> theCallback.apply(requestDetails, tx));
	}


	@Nullable
	private Integer getSPIDXDateCount(long observationPid) {
		return callInFreshTx((rd, tx) ->
			myResourceIndexedSearchParamDateDao.findAllForResourceId(observationPid).size());
	}

}
