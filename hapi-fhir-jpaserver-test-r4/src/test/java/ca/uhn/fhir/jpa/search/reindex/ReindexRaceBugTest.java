package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
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

	/**
	 * Simulate reindex job step overlapping with resource deletion.
	 * If job step has 100 resources, then the DELETE transaction could finish before $reindex commits.
	 */
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
		callInFreshTx((rd, tx) -> {
			DaoMethodOutcome result = mySearchParameterDao.update(sp, rd);
			mySearchParamRegistry.forceRefresh();
			return result;
		});

		assertEquals(1, getSPIDXDateCount(observationPid), "still only one index row before reindex");


		// suppose reindex job step starts here and loads the resource and ResourceTable entity
		ThreadFactory loggingThreadFactory = r -> new Thread(() -> {
			try {
				r.run();
			} catch (Exception e) {
				ourLog.error("Background thread failed", e);
			}
		}, "Reindex-thread");
		ExecutorService backgroundReindexThread = Executors.newSingleThreadExecutor(loggingThreadFactory);
		backgroundReindexThread.submit(()->{
			try {
				callInFreshTx((rd, tx) -> {
					try {

						ourLog.info("Starting background $reindex");

						ourLog.info("Run $reindex");
						myObservationDao.reindex(JpaPid.fromIdAndResourceType(observationPid, "Observation"), rd, new TransactionDetails());

						ourLog.info("$reindex done release main thread to delete");
						latchReindexStart.countDown();

						ourLog.info("Wait for delete to finish");
						latchDeleteFinished.await();

						ourLog.info("Commit $reindex now that delete is finished");

					} catch (Exception e) {
						ourLog.error("$reindex failed", e);
					}
					return 0;
				});
			} finally {
				latchReindexFinished.countDown();
			}
		});

		ourLog.info("Wait for $reindex to start");
		latchReindexStart.await();

		// then the resource is deleted
		ourLog.info("Deleting observation");
		callInFreshTx((rd, tx) -> myObservationDao.delete(observationId, rd));
		assertResourceDeleted(observationId);

		assertEquals(0, getSPIDXDateCount(observationPid), "A deleted resource should have 0 index rows");

		// then the reindex call finishes
		ourLog.info("Let $reindex commit");
		latchDeleteFinished.countDown();

		ourLog.info("Await $reindex commit");
		latchReindexFinished.await();

		assertEquals(0, getSPIDXDateCount(observationPid), "A deleted resource should still have 0 index rows, after $reindex completes");

	}

	private void assertResourceDeleted(IIdType observationId) {
		try {
			// confirm deleted
			callInFreshTx((rd, tx)->
				myObservationDao.read(observationId, rd, false));
			fail("Read deleted resource");
		} catch (ResourceGoneException e) {
			// expected
		}
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
			callInFreshTx((rd, tx) ->
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


	private int getSPIDXDateCount(long observationPid) {
		return callInFreshTx((rd, tx) ->
			myResourceIndexedSearchParamDateDao.findAllForResourceId(observationPid).size());
	}

}
