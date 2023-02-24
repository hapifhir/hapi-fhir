package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.JpaResourceDaoObservation;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.ThreadPoolFactory;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

	// wipmb try overlapping job step reindex with resource deletion. if job step has 100 resources, then the delete could finish
	@Test
	void deleteOverlapsWithReindex_leavesIndexRowsP() throws InterruptedException {
		myTransactionTemplate = new TransactionTemplate(getTxManager());
		CountDownLatch latchReindexStart = new CountDownLatch(1);
		CountDownLatch latchDeleteFinished = new CountDownLatch(1);
		CountDownLatch latchReindexFinished = new CountDownLatch(1);

		ourLog.info("An observation is created");

		var observationId = myTransactionTemplate.execute((tx) -> {
			Observation o = (Observation) myTDB.buildResource("Observation", myTDB.withEffectiveDate("2021-01-01T00:00:00"));
			return myObservationDao.create(o, mySrd);
		});
		long observationPid = Long.parseLong(observationId.getId().getIdPart());
		IBundleProvider bundleProvider = myDaoSearch.searchForBundleProvider("/SearchParameter?base=Observation");

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
		DaoMethodOutcome spId = myTransactionTemplate.execute((tx) ->
			mySearchParameterDao.update(sp, mySrd));
		mySearchParamRegistry.forceRefresh();

		int beforeRowCount = getSPIDXDateCount(observationPid);
		assertEquals(1, beforeRowCount, "date index row for date");


		// suppose reindex job step starts here and loads the resource and ResourceTable entity
		Observation observation = myObservationDao.read(observationId.getId(), mySrd);
		ExecutorService backgroundReindexThread = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(@NotNull Runnable r) {
				return new Thread(r, "Reindex-thread");
			}
		});
		backgroundReindexThread.submit(()->{

			myTransactionTemplate.execute((tx) -> {
				try {

					ourLog.info("Starting background $reindex");
					ResourceTable observationRow = myEntityManager.find(ResourceTable.class, observationPid);

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
		myTransactionTemplate.execute(tx->
			myObservationDao.delete(observationId.getId()));

		// then the reindex call finishes
		ourLog.info("Release $reindex");
		latchDeleteFinished.countDown();


		ourLog.info("Await $reindex finish");
		latchReindexFinished.await();

		int afterRowCount = getSPIDXDateCount(observationPid);

		assertEquals(0, afterRowCount, "no rows after indexing");


	}

	@Nullable
	private Integer getSPIDXDateCount(long observationPid) {
		return myTransactionTemplate.execute((tx) ->
			myResourceIndexedSearchParamDateDao.findAllForResourceId(observationPid).size());
	}

}
