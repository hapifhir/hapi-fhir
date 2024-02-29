package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.PatientReindexTestHelper;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ReindexJobTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReindexJobTest.class);

	@Autowired
	private IJobCoordinator myJobCoordinator;

	private ReindexTestHelper myReindexTestHelper;
	private PatientReindexTestHelper myPatientReindexTestHelper;

	@PostConstruct
	public void postConstruct() {
		myReindexTestHelper = new ReindexTestHelper(myFhirContext, myDaoRegistry, mySearchParamRegistry);
		boolean incrementVersionAfterReindex = false;
		myPatientReindexTestHelper = new PatientReindexTestHelper(myJobCoordinator, myBatch2JobHelper, myPatientDao, incrementVersionAfterReindex);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
		myDaoConfig.setInlineResourceTextBelowSize(new DaoConfig().getInlineResourceTextBelowSize());
	}

	@Test
	public void testReindex_ByUrl() {
		// setup

		// make sure the resources don't get auto-reindexed when the search parameter is created
		boolean reindexPropertyCache = myDaoConfig.isMarkResourcesForReindexingUponSearchParameterChange();
		myDaoConfig.setMarkResourcesForReindexingUponSearchParameterChange(false);

		IIdType obsFinalId = myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.CANCELLED);

		myReindexTestHelper.createAlleleSearchParameter();

		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// The search param value is on the observation, but it hasn't been indexed yet
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(0));

		// Only reindex one of them
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl("Observation?status=final");

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(res);

		// validate
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		// Now one of them should be indexed
		List<String> alleleObservationIds = myReindexTestHelper.getAlleleObservationIds();
		assertThat(alleleObservationIds, hasSize(1));
		assertEquals(obsFinalId.getIdPart(), alleleObservationIds.get(0));

		myDaoConfig.setMarkResourcesForReindexingUponSearchParameterChange(reindexPropertyCache);
	}

	@Test
	public void testReindexDeletedResources_byUrl_willRemoveDeletedResourceEntriesFromIndexTables(){
		IIdType obsId = myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);

		runInTransaction(() -> {
			int entriesInSpIndexTokenTable = myResourceIndexedSearchParamTokenDao.countForResourceId(obsId.getIdPartAsLong());
			assertThat(entriesInSpIndexTokenTable, equalTo(1));

			// simulate resource deletion
			ResourceTable resource = myResourceTableDao.findById(obsId.getIdPartAsLong()).get();
			Date currentDate = new Date();
			resource.setDeleted(currentDate);
			resource.setUpdated(currentDate);
			resource.setHashSha256(null);
			resource.setVersion(2L);
			myResourceTableDao.save(resource);
		});

		// execute reindexing
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl("Observation?status=final");

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(res);

		// then
		runInTransaction(() -> {
			int entriesInSpIndexTokenTablePostReindexing = myResourceIndexedSearchParamTokenDao.countForResourceId(obsId.getIdPartAsLong());
			assertThat(entriesInSpIndexTokenTablePostReindexing, equalTo(0));
		});
	}

	@Test
	public void testReindex_Everything() {
		// setup

		for (int i = 0; i < 50; ++i) {
			myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		}

		sleepUntilTimeChanges();

		myReindexTestHelper.createAlleleSearchParameter();
		mySearchParamRegistry.forceRefresh();

		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// The search param value is on the observation, but it hasn't been indexed yet
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(0));

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// Now all of them should be indexed
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(50));
	}


	@Test
	public void testReindex_ExceptionThrownDuringWrite() {
		// setup

		myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		myReindexTestHelper.createAlleleSearchParameter();
		mySearchParamRegistry.forceRefresh();

		// Throw an exception during reindex

		IAnonymousInterceptor exceptionThrowingInterceptor = (pointcut, args) -> {
			throw new NullPointerException("foo message");
		};
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_INFO, exceptionThrowingInterceptor);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		JobInstance outcome = myBatch2JobHelper.awaitJobFailure(startResponse);

		// Verify

		assertEquals(StatusEnum.ERRORED, outcome.getStatus());
		assertEquals("foo message", outcome.getErrorMessage());
	}

	@Test
	public void testReindex_FailureThrownDuringWrite() {
		// setup

		myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		myReindexTestHelper.createAlleleSearchParameter();
		mySearchParamRegistry.forceRefresh();

		// Throw an error (will be treated as unrecoverable) during reindex

		IAnonymousInterceptor exceptionThrowingInterceptor = (pointcut, args) -> {
			throw new Error("foo message");
		};
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_INFO, exceptionThrowingInterceptor);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		JobInstance outcome = myBatch2JobHelper.awaitJobFailure(startResponse);

		// Verify

		assertEquals(StatusEnum.FAILED, outcome.getStatus());
		assertEquals("java.lang.Error: foo message", outcome.getErrorMessage());
	}


	@Test
	public void testOptimizeStorage() {
		// Setup
		IIdType patientId = createPatient(withActiveTrue());
		for (int i = 0; i < 9; i++) {
			createPatient(withActiveTrue());
		}

		runInTransaction(()->{
			assertEquals(10, myResourceHistoryTableDao.count());
			ResourceHistoryTable history = myResourceHistoryTableDao.findAll().get(0);
			assertNull(history.getResourceTextVc());
			assertNotNull(history.getResource());
		});

		myDaoConfig.setInlineResourceTextBelowSize(10000);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(true)
				.setReindexSearchParameters(false)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		runInTransaction(()->{
			assertEquals(10, myResourceHistoryTableDao.count());
			ResourceHistoryTable history = myResourceHistoryTableDao.findAll().get(0);
			assertNotNull(history.getResourceTextVc());
			assertNull(history.getResource());
		});
		Patient patient = myPatientDao.read(patientId, mySrd);
		assertTrue(patient.getActive());

	}


	@Test
	public void testOptimizeStorage_DeletedRecords() {
		// Setup
		IIdType patientId = createPatient(withActiveTrue());
		myPatientDao.delete(patientId, mySrd);
		for (int i = 0; i < 9; i++) {
			IIdType nextId = createPatient(withActiveTrue());
			myPatientDao.delete(nextId, mySrd);
		}

		myDaoConfig.setInlineResourceTextBelowSize(10000);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(true)
				.setReindexSearchParameters(false)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		JobInstance outcome = myBatch2JobHelper.awaitJobCompletion(startResponse);
		assertEquals(10, outcome.getCombinedRecordsProcessed());

		try {
			myPatientDao.read(patientId, mySrd);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}

	}


	private static Stream<Arguments> numResourcesParams(){
		return PatientReindexTestHelper.numResourcesParams();
	}

	@ParameterizedTest
	@MethodSource("numResourcesParams")
	@Disabled//TODO Nathan, this is failing intermittently in CI.
	public void testReindex(int theNumResources){
		myPatientReindexTestHelper.testReindex(theNumResources);
	}

	@ParameterizedTest
	@MethodSource("numResourcesParams")
	@Disabled//TODO Nathan, this is failing intermittently in CI.
	public void testSequentialReindexOperation(int theNumResources){
		myPatientReindexTestHelper.testSequentialReindexOperation(theNumResources);
	}

	@ParameterizedTest
	@MethodSource("numResourcesParams")
	@Disabled//TODO Nathan, this is failing intermittently in CI.
	public void testParallelReindexOperation(int theNumResources){
		myPatientReindexTestHelper.testParallelReindexOperation(theNumResources);
	}

}
