package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
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
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ReindexJobTest extends BaseJpaR4Test {

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Autowired
	private IJobPersistence myJobPersistence;

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
		myStorageSettings.setInlineResourceTextBelowSize(new JpaStorageSettings().getInlineResourceTextBelowSize());
		myStorageSettings.setStoreMetaSourceInformation(new JpaStorageSettings().getStoreMetaSourceInformation());
		myStorageSettings.setPreserveRequestIdInResourceBody(new JpaStorageSettings().isPreserveRequestIdInResourceBody());
	}

	@Test
	public void testOptimizeStorage_CurrentVersion() {
		// Setup
		IIdType patientId = createPatient(withActiveTrue());
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId(patientId.toUnqualifiedVersionless());
			p.setActive(true);
			p.addIdentifier().setValue(String.valueOf(i));
			myPatientDao.update(p, mySrd);
		}
		for (int i = 0; i < 9; i++) {
			createPatient(withActiveTrue());
		}

		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			for (ResourceHistoryTable history : myResourceHistoryTableDao.findAll()) {
				assertNull(history.getResourceTextVc());
				assertNotNull(history.getResource());
			}
		});

		myStorageSettings.setInlineResourceTextBelowSize(10000);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.CURRENT_VERSION)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			ResourceHistoryTable history = myResourceHistoryTableDao.findAll().get(0);
			if (history.getResourceId().equals(patientId.getIdPartAsLong()) && history.getVersion() < 11) {
				assertNull(history.getResourceTextVc());
				assertNotNull(history.getResource());
			} else {
				assertNotNull(history.getResourceTextVc());
				assertNull(history.getResource());
			}
		});
		Patient patient = myPatientDao.read(patientId, mySrd);
		assertTrue(patient.getActive());

	}


	@Test
	public void testOptimizeStorage_AllVersions() {
		// Setup
		IIdType patientId = createPatient(withActiveTrue());
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId(patientId.toUnqualifiedVersionless());
			p.setActive(true);
			p.addIdentifier().setValue(String.valueOf(i));
			myPatientDao.update(p, mySrd);
		}
		for (int i = 0; i < 9; i++) {
			createPatient(withActiveTrue());
		}

		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			for (ResourceHistoryTable history : myResourceHistoryTableDao.findAll()) {
				assertNull(history.getResourceTextVc());
				assertNotNull(history.getResource());
			}
		});

		myStorageSettings.setInlineResourceTextBelowSize(10000);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.ALL_VERSIONS)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			for (ResourceHistoryTable history : myResourceHistoryTableDao.findAll()) {
				assertNotNull(history.getResourceTextVc());
				assertNull(history.getResource());
			}
		});
		Patient patient = myPatientDao.read(patientId, mySrd);
		assertTrue(patient.getActive());

	}

	@Test
	public void testOptimizeStorage_AllVersions_CopyProvenanceEntityData() {
		// Setup
		myStorageSettings.setStoreMetaSourceInformation(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);
		myStorageSettings.setPreserveRequestIdInResourceBody(true);

		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId("PATIENT" + i);
			p.getMeta().setSource("http://foo#bar");
			p.addIdentifier().setValue(String.valueOf(i));
			myPatientDao.update(p, mySrd);

			p.addIdentifier().setSystem("http://blah");
			myPatientDao.update(p, mySrd);
		}

		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			assertEquals(20, myResourceHistoryProvenanceDao.count());
			Query query = myEntityManager.createQuery("UPDATE " + ResourceHistoryTable.class.getSimpleName() + " p SET p.mySourceUri = NULL, p.myRequestId = NULL");
			assertEquals(20, query.executeUpdate());
		});

		runInTransaction(()-> {
			for (var next : myResourceHistoryProvenanceDao.findAll()) {
				assertEquals("bar", next.getRequestId());
				assertEquals("http://foo", next.getSourceUri());
			}
			for (var next : myResourceHistoryTableDao.findAll()) {
				assertThat(next.getRequestId(), blankOrNullString());
				assertThat(next.getSourceUri(), blankOrNullString());
			}
		});

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.ALL_VERSIONS)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		runInTransaction(()-> {
			for (var next : myResourceHistoryProvenanceDao.findAll()) {
				assertEquals("bar", next.getRequestId());
				assertEquals("http://foo", next.getSourceUri());
			}
			for (var next : myResourceHistoryTableDao.findAll()) {
				assertEquals("bar", next.getRequestId());
				assertEquals("http://foo", next.getSourceUri());
			}
		});

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

		myStorageSettings.setInlineResourceTextBelowSize(10000);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.CURRENT_VERSION)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
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

	@Test
	public void testReindex_ByUrl() {
		// setup

		// make sure the resources don't get auto-reindexed when the search parameter is created
		boolean reindexPropertyCache = myStorageSettings.isMarkResourcesForReindexingUponSearchParameterChange();
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);

		IIdType obsFinalId = myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.CANCELLED);

		myReindexTestHelper.createAlleleSearchParameter();

		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd).size());
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
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd).size());

		// Now one of them should be indexed
		List<String> alleleObservationIds = myReindexTestHelper.getAlleleObservationIds();
		assertThat(alleleObservationIds, hasSize(1));
		assertEquals(obsFinalId.getIdPart(), alleleObservationIds.get(0));

		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(reindexPropertyCache);
	}

	@Test
	public void testReindexDeletedResources_byUrl_willRemoveDeletedResourceEntriesFromIndexTables(){
		IIdType obsId = myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);

		runInTransaction(() -> {
			int entriesInSpIndexTokenTable = myResourceIndexedSearchParamTokenDao.countForResourceId(obsId.getIdPartAsLong());
			assertThat(entriesInSpIndexTokenTable, equalTo(1));

			// simulate resource deletion
			ResourceTable resource = myResourceTableDao.findById(obsId.getIdPartAsLong()).orElseThrow();
			Date currentDate = new Date();
			resource.setDeleted(currentDate);
			resource.setUpdated(currentDate);
			resource.setHashSha256(null);
			resource.setVersionForUnitTest(2L);
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

		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd).size());
		// The search param value is on the observation, but it hasn't been indexed yet
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(0));

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd).size());
		// Now all of them should be indexed
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(50));
	}

	@Test
	public void testReindex_DuplicateResourceBeforeEnforceUniqueShouldSaveWarning() {
		myReindexTestHelper.createObservationWithCode();
		myReindexTestHelper.createObservationWithCode();

		DaoMethodOutcome searchParameter = myReindexTestHelper.createUniqueCodeSearchParameter();

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(startRequest);
		JobInstance myJob = myBatch2JobHelper.awaitJobCompletion(startResponse);

		assertEquals(StatusEnum.COMPLETED, myJob.getStatus());
		assertNotNull(myJob.getWarningMessages());
		assertTrue(myJob.getWarningMessages().contains("Failed to reindex resource because unique search parameter " + searchParameter.getEntity().getIdDt().toVersionless().toString()));
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
		JobInstance outcome = myBatch2JobHelper.awaitJobCompletion(startResponse);

		// Verify

		assertEquals(StatusEnum.COMPLETED, outcome.getStatus());
		assertNull(outcome.getErrorMessage());
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
	public void testReindex_withReindexingUponSearchParameterChangeEnabled_reindexJobCompleted() {
		// make sure the resources auto-reindex after the search parameter update is enabled
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(true);

		// create an Observation resource and SearchParameter for it to trigger re-indexing
		myReindexTestHelper.createObservationWithCode();
		myReindexTestHelper.createCodeSearchParameter();

		// check that reindex job was created
		List<JobInstance> jobInstances = myJobPersistence.fetchInstancesByJobDefinitionId(ReindexAppCtx.JOB_REINDEX, 10, 0);
		assertEquals(1, jobInstances.size());

		// check that the job is completed (not stuck in QUEUED status)
		myBatch2JobHelper.awaitJobCompletion(jobInstances.get(0).getInstanceId());
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
