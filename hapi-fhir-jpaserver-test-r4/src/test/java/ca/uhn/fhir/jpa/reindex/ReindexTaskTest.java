package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
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
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.dao.JpaPidFk;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.PatientReindexTestHelper;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import com.google.common.base.Charsets;
import jakarta.annotation.PostConstruct;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.jobs.reindex.ReindexUtils.JOB_REINDEX;
import static ca.uhn.fhir.jpa.api.dao.ReindexParameters.OptimizeStorageModeEnum.ALL_VERSIONS;
import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SuppressWarnings("SqlDialectInspection")
public class ReindexTaskTest extends BaseJpaR4Test {

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
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setStoreMetaSourceInformation(defaults.getStoreMetaSourceInformation());
		myStorageSettings.setPreserveRequestIdInResourceBody(defaults.isPreserveRequestIdInResourceBody());
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(defaults.isAccessMetaSourceInformationFromProvenanceTable());
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

		// Move resource text to compressed storage, which we don't write to anymore but legacy
		// data may exist that was previously stored there, so we're simulating that.
		List<ResourceHistoryTable> allHistoryEntities = runInTransaction(() -> myResourceHistoryTableDao.findAll());
		allHistoryEntities.forEach(t->relocateResourceTextToCompressedColumn(t.getResourceId().toFk(), t.getVersion()));

		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			for (ResourceHistoryTable history : myResourceHistoryTableDao.findAll()) {
				assertNull(history.getResourceTextVc());
				assertNotNull(history.getResource());
			}
		});

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.CURRENT_VERSION)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			ResourceHistoryTable history = myResourceHistoryTableDao.findAll().get(0);
			if (history.getResourceId().equals(JpaPid.fromId(patientId.getIdPartAsLong())) && history.getVersion() < 11) {
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

		// Move resource text to compressed storage, which we don't write to anymore but legacy
		// data may exist that was previously stored there, so we're simulating that.
		List<ResourceHistoryTable> allHistoryEntities = runInTransaction(() -> myResourceHistoryTableDao.findAll());
		allHistoryEntities.forEach(t->relocateResourceTextToCompressedColumn(t.getResourceId().toFk(), t.getVersion()));

		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			for (ResourceHistoryTable history : myResourceHistoryTableDao.findAll()) {
				assertNull(history.getResourceTextVc());
				assertNotNull(history.getResource());
			}
		});

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ALL_VERSIONS)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			for (ResourceHistoryTable history : myResourceHistoryTableDao.findAll()) {
				assertNotNull(history.getResourceTextVc(), ()->"Null history on: " + history);
				assertNull(history.getResource());
			}
		});
		Patient patient = myPatientDao.read(patientId, mySrd);
		assertTrue(patient.getActive());

	}

	@Test
	public void testOptimizeStorage_AllVersions_SingleResourceWithMultipleVersion() {

		// this difference of this test from testOptimizeStorage_AllVersions is that this one has only 1 resource
		// (with multiple versions) in the db. There was a bug where if only one resource were being re-indexed, the
		// resource wasn't processed for optimize storage.

		// Setup
		IIdType patientId = createPatient(withActiveTrue());
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId(patientId.toUnqualifiedVersionless());
			p.setActive(true);
			p.addIdentifier().setValue(String.valueOf(i));
			myPatientDao.update(p, mySrd);
		}

		// Move resource text to compressed storage, which we don't write to anymore but legacy
		// data may exist that was previously stored there, so we're simulating that.
		List<ResourceHistoryTable> allHistoryEntities = runInTransaction(() -> myResourceHistoryTableDao.findAll());
		allHistoryEntities.forEach(t->relocateResourceTextToCompressedColumn(t.getResourceId().toFk(), t.getVersion()));

		runInTransaction(()->{
			assertEquals(11, myResourceHistoryTableDao.count());
			for (ResourceHistoryTable history : myResourceHistoryTableDao.findAll()) {
				assertNull(history.getResourceTextVc());
				assertNotNull(history.getResource());
			}
		});

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ALL_VERSIONS)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		runInTransaction(()->{
			assertEquals(11, myResourceHistoryTableDao.count());
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
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(true);
		myStorageSettings.setPreserveRequestIdInResourceBody(true);

		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId("PATIENT" + i);
			p.addIdentifier().setValue(String.valueOf(i));
			myPatientDao.update(p, mySrd);

			p.setActive(true);
			myPatientDao.update(p, mySrd);
		}

		runInTransaction(()->{
			List<ResourceHistoryTable> versions = myResourceHistoryTableDao.findAll();
			for (var version : versions) {
				ResourceHistoryProvenanceEntity provenance = new ResourceHistoryProvenanceEntity();
				provenance.setResourceTable(version.getResourceTable());
				provenance.setResourceHistoryTable(version);
				provenance.setSourceUri("http://foo");
				provenance.setRequestId("bar");
				myResourceHistoryProvenanceDao.save(provenance);
			}
		});

		runInTransaction(()->{
			assertEquals(20, myResourceHistoryTableDao.count());
			assertEquals(20, myResourceHistoryProvenanceDao.count());
		});

		runInTransaction(()-> {
			for (var next : myResourceHistoryProvenanceDao.findAll()) {
				assertEquals("bar", next.getRequestId());
				assertEquals("http://foo", next.getSourceUri());
			}
			for (var next : myResourceHistoryTableDao.findAll()) {
				assertThat(next.getRequestId()).isBlank();
				assertThat(next.getSourceUri()).isBlank();
			}
		});

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ALL_VERSIONS)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		runInTransaction(()-> {
			assertEquals(0, myResourceHistoryProvenanceDao.count());
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

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(
			new ReindexJobParameters()
				.setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.CURRENT_VERSION)
				.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
		);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
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
		assertThat(myReindexTestHelper.getAlleleObservationIds()).hasSize(0);

		// Only reindex one of them
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl("Observation?status=final");

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(res);

		// validate
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd).size());

		// Now one of them should be indexed
		List<String> alleleObservationIds = myReindexTestHelper.getAlleleObservationIds();
		assertThat(alleleObservationIds).hasSize(1);
		assertEquals(obsFinalId.getIdPart(), alleleObservationIds.get(0));

		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(reindexPropertyCache);
	}

	@Nested
	class ReindexDeletedResourcesTest {
		@BeforeEach
		public void beforeEach() {
			// make sure the resources don't get auto-reindexed when the search parameter is created
			myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);
		}

		@AfterEach
		public void afterEach() {
			myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(new JpaStorageSettings().isMarkResourcesForReindexingUponSearchParameterChange());
		}

		@ParameterizedTest
		@EnumSource(SearchIncludeDeletedEnum.class)
		public void testServerReindexOptimizeStorage_targetingDeletedResources_optimizesVersionsOfDeletedResources(SearchIncludeDeletedEnum theIncludeDeleted) {
			// Given
			Observation o = myReindexTestHelper.buildObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
			String obsResBody = myFhirContext.newJsonParser().encodeResourceToString(o);

			o.setId(new IdType("obs-to-delete"));
			DaoMethodOutcome toDeleteObs = myObservationDao.update(o);

			o.setId(new IdType("obs-to-keep"));
			DaoMethodOutcome toKeepObs = myObservationDao.update(o);

			DaoMethodOutcome deleteOutcome = myObservationDao.delete(toDeleteObs.getId(), mySrd);

			JpaPid obsDeletedJpaPid = (JpaPid) toDeleteObs.getPersistentId();
			JpaPid obsKeptJpaPid = (JpaPid) toKeepObs.getPersistentId();
			JpaPid obsAfterDeleteJpaPid = (JpaPid) deleteOutcome.getPersistentId();

			Patient p = new Patient().setActive(true);
			p.setId("p1");
			String patResBody = myFhirContext.newJsonParser().encodeResourceToString(p);
			JpaPid patientPid = createPatientAndMaybeDeletePatient(p, !theIncludeDeleted.equals(SearchIncludeDeletedEnum.FALSE));

			movePatientResourceBodyToResTextLob(patResBody, 1);
			moveObservationResourceBodyToResTextLob(obsResBody, 2);

			// When
			doOptimizeStorageReindexWithUrl("?_includeDeleted=" + theIncludeDeleted.getCode());

			// Then
			switch (theIncludeDeleted) {
				case TRUE -> assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid, patientPid), List.of(obsKeptJpaPid), List.of(obsAfterDeleteJpaPid), obsResBody, patResBody);
				case FALSE -> assertCorrectResourcesReindexed(List.of(obsKeptJpaPid, patientPid), List.of(obsDeletedJpaPid), List.of(obsAfterDeleteJpaPid), obsResBody, patResBody);
				case BOTH -> assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid, obsKeptJpaPid, patientPid), List.of(), List.of(obsAfterDeleteJpaPid), obsResBody, patResBody);
			}
		}

		@ParameterizedTest
		@EnumSource(SearchIncludeDeletedEnum.class)
		public void testServerReindexOptimizeStorage_targetingDeletedResourcesWithLastUpdatedParam_optimizesVersionsOfDeletedResources(SearchIncludeDeletedEnum theIncludeDeleted) {
			// Given
			Observation o = myReindexTestHelper.buildObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
			String obsResBody = myFhirContext.newJsonParser().encodeResourceToString(o);

			o.setId(new IdType("obs-to-delete"));
			DaoMethodOutcome toDeleteObs = myObservationDao.update(o);
			DaoMethodOutcome obsToDeleteOutcome = myObservationDao.delete(toDeleteObs.getId(), mySrd);

			o.setId(new IdType("obs-to-keep"));
			DaoMethodOutcome toKeepObs = myObservationDao.update(o);

			BaseDateTimeDt date = new DateTimeDt().setValue(new Date());
			sleepAtLeast(1000);

			o.setId(new IdType("obs-to-keep-2"));
			DaoMethodOutcome toKeepObs2 = myObservationDao.update(o);

			o.setId(new IdType("obs-to-delete-2"));
			DaoMethodOutcome toDeleteObs2 = myObservationDao.update(o);
			DaoMethodOutcome obsToDeleteOutcome2 = myObservationDao.delete(toDeleteObs2.getId(), mySrd);

			JpaPid obsDeletedJpaPid = (JpaPid) toDeleteObs.getPersistentId();
			JpaPid obsKeptJpaPid = (JpaPid) toKeepObs.getPersistentId();

			JpaPid obsKeptJpaPid2 = (JpaPid) toKeepObs2.getPersistentId();
			JpaPid obsDeletedJpaPid2 = (JpaPid) toDeleteObs2.getPersistentId();

			JpaPid obsDeletedVersionJpaPid = (JpaPid) obsToDeleteOutcome.getPersistentId();
			JpaPid obsDeletedVersionJpaPid2 = (JpaPid) obsToDeleteOutcome2.getPersistentId();

			moveObservationResourceBodyToResTextLob(obsResBody, 4);

			// When
			String theLastUpdatedParam = "&" + Constants.PARAM_LASTUPDATED + "=le" + date.getValueAsString();
			String theFullUrl = "?_includeDeleted=" + theIncludeDeleted.getCode() + theLastUpdatedParam;
			ourLog.info("Reindexing with URL: " + theFullUrl);

			doOptimizeStorageReindexWithUrl(theFullUrl);

			// Then
			switch (theIncludeDeleted) {
				case TRUE -> assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid), List.of(obsKeptJpaPid, obsKeptJpaPid2, obsDeletedJpaPid2), List.of(obsDeletedVersionJpaPid, obsDeletedVersionJpaPid2), obsResBody, "");
				case FALSE -> assertCorrectResourcesReindexed(List.of(obsKeptJpaPid), List.of(obsDeletedJpaPid, obsKeptJpaPid2, obsDeletedJpaPid2), List.of(obsDeletedVersionJpaPid, obsDeletedVersionJpaPid2), obsResBody, "");
				case BOTH -> assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid, obsKeptJpaPid), List.of(obsDeletedJpaPid2, obsKeptJpaPid2), List.of(obsDeletedVersionJpaPid, obsDeletedVersionJpaPid2), obsResBody, "");
			}
		}

		@ParameterizedTest
		@ValueSource(strings = {Constants.PARAM_TAG, Constants.PARAM_SECURITY})
		public void testServerReindexOptimizeStorage_targetingDeletedResourcesWithUnsupportedParams_failsWithMessage(String theAdditionalParams) {
			// Given
			ReindexJobParameters parameters = new ReindexJobParameters();
			parameters.addUrl("?_includeDeleted=true&" + theAdditionalParams + "=abc");
			parameters.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE);
			parameters.setOptimizeStorage(ALL_VERSIONS);

			// When
			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(JOB_REINDEX);
			startRequest.setParameters(parameters);
			Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
			JobInstance job = myBatch2JobHelper.awaitJobFailure(res);

			// Then
			assertThat(job.getErrorMessage()).contains("HAPI-2744: The _includeDeleted parameter is only compatible with the following parameters:");
		}

		@ParameterizedTest
		@EnumSource(SearchIncludeDeletedEnum.class)
		public void testResourceTypeReindexOptimizeStorage_targetingDeletedResources_optimizesVersionsOfDeletedResources(SearchIncludeDeletedEnum theIncludeDeleted) {
			// Given
			Observation o = myReindexTestHelper.buildObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
			String obsResBody = myFhirContext.newJsonParser().encodeResourceToString(o);

			o.setId(new IdType("obs-to-delete"));
			DaoMethodOutcome toDeleteObs = myObservationDao.update(o);

			o.setId(new IdType("obs-to-keep"));
			DaoMethodOutcome toKeepObs = myObservationDao.update(o);

			DaoMethodOutcome afterDeleteObsVersion = myObservationDao.delete(toDeleteObs.getId(), mySrd);

			JpaPid obsDeletedJpaPid = (JpaPid) toDeleteObs.getPersistentId();
			JpaPid obsKeptJpaPid = (JpaPid) toKeepObs.getPersistentId();
			JpaPid obsDeletedVersionJpaPid = (JpaPid) afterDeleteObsVersion.getPersistentId();

			Patient p = new Patient().setActive(true);
			p.setId("p1");
			String patResBody = myFhirContext.newJsonParser().encodeResourceToString(p);
			JpaPid patientPid = createPatientAndMaybeDeletePatient(p, !theIncludeDeleted.equals(SearchIncludeDeletedEnum.FALSE));

			movePatientResourceBodyToResTextLob(patResBody, 1);
			moveObservationResourceBodyToResTextLob(obsResBody, 2);

			// When
			doOptimizeStorageReindexWithUrl("Observation?_includeDeleted=" + theIncludeDeleted.getCode());

			// Then
			switch (theIncludeDeleted) {
				case TRUE -> assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid), List.of(obsKeptJpaPid, patientPid), List.of(obsDeletedVersionJpaPid), obsResBody, patResBody);
				case FALSE -> assertCorrectResourcesReindexed(List.of(obsKeptJpaPid), List.of(obsDeletedJpaPid, patientPid), List.of(obsDeletedVersionJpaPid), obsResBody, patResBody);
				case BOTH -> assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid, obsKeptJpaPid), List.of(patientPid), List.of(obsDeletedVersionJpaPid), obsResBody, patResBody);
			}
		}

		@ParameterizedTest
		@MethodSource("reindexDeletedResourcesBySearchUrlParams")
		public void testResourceTypeReindexOptimizeStorage_targetingDeletedResourceWithLastUpdatedAndId_optimizesVersionsOfDeletedResource(SearchIncludeDeletedEnum theIncludeDeleted, String theLastUpdatedParam, String theIdParam) {
			// Given
			Observation o = myReindexTestHelper.buildObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
			String obsResBody = myFhirContext.newJsonParser().encodeResourceToString(o);

			o.setId(new IdType("obs-to-delete"));
			DaoMethodOutcome toDeleteObs = myObservationDao.update(o);
			DaoMethodOutcome deletedObsVersionBeforeDate = myObservationDao.delete(toDeleteObs.getId(), mySrd);

			o.setId(new IdType("obs-to-keep"));
			DaoMethodOutcome toKeepObs = myObservationDao.update(o);

			Patient p = new Patient().setActive(true);
			p.setId("p1");
			String patResBody = myFhirContext.newJsonParser().encodeResourceToString(p);
			JpaPid patientPid = createPatientAndMaybeDeletePatient(p, !theIncludeDeleted.equals(SearchIncludeDeletedEnum.FALSE));

			BaseDateTimeDt date = new DateTimeDt().setValue(new Date());
			sleepAtLeast(1000);

			o.setId(new IdType("obs-to-keep-2"));
			DaoMethodOutcome toKeepObs2 = myObservationDao.update(o);

			o.setId(new IdType("obs-to-delete-2"));
			DaoMethodOutcome toDeleteObs2 = myObservationDao.update(o);
			DaoMethodOutcome deletedObsVersionAfterDate = myObservationDao.delete(toDeleteObs2.getId(), mySrd);

			JpaPid obsDeletedJpaPid = (JpaPid) toDeleteObs.getPersistentId();
			JpaPid obsKeptJpaPid = (JpaPid) toKeepObs.getPersistentId();

			JpaPid obsKeptJpaPid2 = (JpaPid) toKeepObs2.getPersistentId();
			JpaPid obsDeletedJpaPid2 = (JpaPid) toDeleteObs2.getPersistentId();

			JpaPid deletedObsVersionBeforeDateJpaPid = (JpaPid) deletedObsVersionBeforeDate.getPersistentId();
			JpaPid deletedObsVersionAfterDateJpaPid = (JpaPid) deletedObsVersionAfterDate.getPersistentId();

			movePatientResourceBodyToResTextLob(patResBody, 1);
			moveObservationResourceBodyToResTextLob(obsResBody, 4);

			// When
			String theFullUrl = "Observation?_includeDeleted=" + theIncludeDeleted.getCode() + buildAdditionalSearchParams(theIncludeDeleted, theLastUpdatedParam, theIdParam, date);
			ourLog.info("Reindexing with URL: " + theFullUrl);
			doOptimizeStorageReindexWithUrl(theFullUrl);

			// Then
			switch (theIncludeDeleted) {
				case TRUE ->
					assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid), List.of(obsKeptJpaPid, obsKeptJpaPid2, obsDeletedJpaPid2, patientPid), List.of(deletedObsVersionBeforeDateJpaPid, deletedObsVersionAfterDateJpaPid), obsResBody, patResBody);
				case FALSE ->
					assertCorrectResourcesReindexed(List.of(obsKeptJpaPid), List.of(obsDeletedJpaPid, obsKeptJpaPid2, obsDeletedJpaPid2, patientPid), List.of(deletedObsVersionBeforeDateJpaPid, deletedObsVersionAfterDateJpaPid), obsResBody, patResBody);
				case BOTH -> {
					if (theFullUrl.contains(Constants.PARAM_ID)) {
						assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid), List.of(obsDeletedJpaPid2, obsKeptJpaPid2, patientPid), List.of(deletedObsVersionBeforeDateJpaPid, deletedObsVersionAfterDateJpaPid), obsResBody, patResBody);
					} else {
						assertCorrectResourcesReindexed(List.of(obsDeletedJpaPid, obsKeptJpaPid), List.of(obsDeletedJpaPid2, obsKeptJpaPid2, patientPid), List.of(deletedObsVersionBeforeDateJpaPid, deletedObsVersionAfterDateJpaPid), obsResBody, patResBody);
					}
				}
			}
		}

		// Matrix of all supported search params (_lastUpdated, _id) with valid values of the _includeDeleted param
		private static Stream<Arguments> reindexDeletedResourcesBySearchUrlParams() {
			String lastUpdatedParamSuffix = "&" + Constants.PARAM_LASTUPDATED + "=le%s";
			String idParamSuffix = "&" + Constants.PARAM_ID + "=%s";

			return Arrays.stream(SearchIncludeDeletedEnum.values()).flatMap(theDeletedMode ->
				Stream.of(
					Arguments.of(theDeletedMode, lastUpdatedParamSuffix, null),
					Arguments.of(theDeletedMode, null, idParamSuffix),
					Arguments.of(theDeletedMode, lastUpdatedParamSuffix, idParamSuffix)
				));
		}

		private String buildAdditionalSearchParams(SearchIncludeDeletedEnum theIncludeDeleted, String theLastUpdatedParam, String theIdParam, BaseDateTimeDt date) {
			StringBuilder theAdditionalSearchUrlParams = new StringBuilder();

			if (theLastUpdatedParam != null) {
				theAdditionalSearchUrlParams.append(String.format(theLastUpdatedParam, date.getValueAsString()));
			}
			if (theIdParam != null) {
				String theIdToUse = theIncludeDeleted.equals(SearchIncludeDeletedEnum.FALSE) ? "obs-to-keep" : "obs-to-delete";
				theAdditionalSearchUrlParams.append(String.format(theIdParam, theIdToUse));
			}
			return theAdditionalSearchUrlParams.toString();
		}

		@ParameterizedTest
		@ValueSource(strings = {Constants.PARAM_TAG, Constants.PARAM_SECURITY, "name"})
		public void testResourceTypeReindexOptimizeStorage_targetingDeletedResourcesWithUnsupportedParams_failsWithMessage(String theAdditionalParams) {
			// Given
			ReindexJobParameters parameters = new ReindexJobParameters();
			parameters.addUrl("Patient?_includeDeleted=true&" + theAdditionalParams + "=abc");
			parameters.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE);
			parameters.setOptimizeStorage(ALL_VERSIONS);

			// When
			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(JOB_REINDEX);
			startRequest.setParameters(parameters);
			Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
			JobInstance job = myBatch2JobHelper.awaitJobFailure(res);

			// Then
			assertThat(job.getErrorMessage()).contains("HAPI-2744: The _includeDeleted parameter is only compatible with the following parameters:");
		}

		@Test
		public void testResourceTypeReindexOptimizeStorage_targetingDeletedResourceMultipleResourceTypesAndIds_optimizesVersionsOfDeletedResource() {
			// Given
			Observation o = myReindexTestHelper.buildObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
			String obsResBody = myFhirContext.newJsonParser().encodeResourceToString(o);

			o.setId(new IdType("obs1"));
			DaoMethodOutcome obs1 = myObservationDao.update(o);
			JpaPid obs1Pid = (JpaPid) obs1.getPersistentId();
			DaoMethodOutcome obs1Deleted = myObservationDao.delete(obs1.getId(), mySrd);
			JpaPid obs1DeletedPid = (JpaPid) obs1Deleted.getPersistentId();

			o.setId(new IdType("obs2"));
			DaoMethodOutcome obs2 = myObservationDao.update(o);
			JpaPid obs2Pid = (JpaPid) obs2.getPersistentId();
			DaoMethodOutcome obs2Deleted = myObservationDao.delete(obs2.getId(), mySrd);
			JpaPid obs2DeletedPid = (JpaPid) obs2Deleted.getPersistentId();

			o.setId(new IdType("obs3"));
			DaoMethodOutcome obs3 = myObservationDao.update(o);
			JpaPid obs3Pid = (JpaPid) obs3.getPersistentId();
			DaoMethodOutcome obs3Deleted = myObservationDao.delete(obs3.getId(), mySrd);
			JpaPid obs3DeletedPid = (JpaPid) obs3Deleted.getPersistentId();

			Patient p = new Patient().setActive(true);
			p.setId("p1");
			String patResBody = myFhirContext.newJsonParser().encodeResourceToString(p);
			JpaPid p1Pid = createPatientAndMaybeDeletePatient(p, true);

			p.setId("p2");
			JpaPid p2Pid = createPatientAndMaybeDeletePatient(p, true);

			movePatientResourceBodyToResTextLob(patResBody, 2);
			moveObservationResourceBodyToResTextLob(obsResBody, 3);

			// When: a reindex job with multiple URLs targeting multiple resource types
			ReindexJobParameters parameters = new ReindexJobParameters();
			parameters.addUrl("Observation?_includeDeleted=true&_id=obs1,obs2");
			parameters.addUrl("Patient?_includeDeleted=true&_id=p1");
			parameters.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE);
			parameters.setOptimizeStorage(ALL_VERSIONS);

			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(JOB_REINDEX);
			startRequest.setParameters(parameters);
			Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
			myBatch2JobHelper.awaitJobCompletion(res);

			// Then
			assertCorrectResourcesReindexed(List.of(obs1Pid, obs2Pid, p1Pid), List.of(obs3Pid, p2Pid), List.of(obs1DeletedPid, obs2DeletedPid, obs3DeletedPid), obsResBody, patResBody);
		}

		private JpaPid createPatientAndMaybeDeletePatient(Patient thePatient, boolean theShouldDeletePatient) {
			DaoMethodOutcome patientOutcome = myPatientDao.update(thePatient, mySrd);
			JpaPid patientPid = (JpaPid) patientOutcome.getPersistentId();
			if (theShouldDeletePatient) {
				myPatientDao.delete(patientOutcome.getId(), mySrd);
			}
			return patientPid;
		}

		/**
		 * Removes the RES_TEXT column contents and populates the RES_TEXT_VC column with the provided resource body
		 * This is to mimic older versions of HAPI where the RES_TEXT_VC column was populated instead of RES_TEXT
		 *
		 * @param theResBody the body of the resource, in String form, that will be put into the RES_TEXT_VC column
		 * @param theExpectedNumberOfResourcesChanged the expected number of resources that will be updated as a result of the query
		 */
		private void moveResourceBodyToRestTextLob(String theResBody, int theExpectedNumberOfResourcesChanged, String theResourceType) {
			runInTransaction(() -> {
				// Only the versions that are not deleted should have the resource text fields populated
				assertEquals(theExpectedNumberOfResourcesChanged, myEntityManager.createNativeQuery("UPDATE HFJ_RES_VER SET RES_TEXT = :param WHERE RES_TYPE = '" + theResourceType + "' AND RES_DELETED_AT IS null").setParameter("param", theResBody.getBytes(Charsets.UTF_8)).executeUpdate());
				assertEquals(theExpectedNumberOfResourcesChanged, myEntityManager.createNativeQuery("UPDATE HFJ_RES_VER SET RES_TEXT_VC = null WHERE RES_TYPE = '" + theResourceType + "' AND RES_DELETED_AT IS null").executeUpdate());
			});
		}

		private void moveObservationResourceBodyToResTextLob(String theObsResBody, int theExpectedNumberOfResourcesChanged) {
			moveResourceBodyToRestTextLob(theObsResBody, theExpectedNumberOfResourcesChanged, "Observation");
		}

		private void movePatientResourceBodyToResTextLob(String theObsResBody, int theExpectedNumberOfResourcesChanged) {
			moveResourceBodyToRestTextLob(theObsResBody, theExpectedNumberOfResourcesChanged, "Patient");
		}

		private void doOptimizeStorageReindexWithUrl(String theUrl) {
			ReindexJobParameters parameters = new ReindexJobParameters();
			parameters.addUrl(theUrl);
			parameters.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE);
			parameters.setOptimizeStorage(ALL_VERSIONS);

			// execute
			JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
			startRequest.setJobDefinitionId(JOB_REINDEX);
			startRequest.setParameters(parameters);
			Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
			myBatch2JobHelper.awaitJobCompletion(res);
		}

		private void assertCorrectResourcesReindexed(List<JpaPid> theReindexedIds, List<JpaPid> theNotReindexedIds, List<JpaPid> theDeletedVersionIds, String theExpectedObservationResourceBody, String theExpectedPatientResourceBody) {
			runInTransaction(() -> {
				// Ensure that the reindexed resources have the RES_TEXT populated
				for (JpaPid theReindexedId : theReindexedIds) {
					ResourceHistoryTable resourceHistoryEntity = myResourceHistoryTableDao.findForIdAndVersion(JpaPidFk.fromId(theReindexedId.getId()), theReindexedId.getVersion());
					assertThat(resourceHistoryEntity.getResource()).isNull();
					assertThat(resourceHistoryEntity.getResourceTextVc()).isNotNull();

					if (resourceHistoryEntity.getResourceType().equals("Observation")) {
						assertThat(resourceHistoryEntity.getResourceTextVc()).isEqualTo(theExpectedObservationResourceBody);
					} else if (resourceHistoryEntity.getResourceType().equals("Patient")) {
						assertThat(resourceHistoryEntity.getResourceTextVc()).isEqualTo(theExpectedPatientResourceBody);
					}
				}

				// Ensure that the non-reindexed resources have the RES_TEXT_VC populated
				for (JpaPid theNotReindexedId : theNotReindexedIds) {
					ResourceHistoryTable resourceHistoryEntity = myResourceHistoryTableDao.findForIdAndVersion(JpaPidFk.fromId(theNotReindexedId.getId()), theNotReindexedId.getVersion());
					assertThat(resourceHistoryEntity.getResourceTextVc()).isNull();
					assertThat(resourceHistoryEntity.getResource()).isNotNull();

					if (resourceHistoryEntity.getResourceType().equals("Observation")) {
						assertThat(resourceHistoryEntity.getResource()).isEqualTo(theExpectedObservationResourceBody.getBytes());
					} else if (resourceHistoryEntity.getResourceType().equals("Patient")) {
						assertThat(resourceHistoryEntity.getResource()).isEqualTo(theExpectedPatientResourceBody.getBytes());
					}
				}

				// Ensure that the deleted versions in the history table have neither column populated
				for (JpaPid theDeletedVersionId : theDeletedVersionIds) {
					ResourceHistoryTable resourceHistoryEntity = myResourceHistoryTableDao.findForIdAndVersion(JpaPidFk.fromId(theDeletedVersionId.getId()), theDeletedVersionId.getVersion());
					assertThat(resourceHistoryEntity.getResourceTextVc()).isNull();
					assertThat(resourceHistoryEntity.getResource()).isNull();
				}
			});
		}
	}

	@Test
	public void testReindex_byMultipleUrls_indexesMatchingResources() {
		// setup
		createObservation(withStatus(Observation.ObservationStatus.FINAL.toCode()));
		createObservation(withStatus(Observation.ObservationStatus.CANCELLED.toCode()));
		createPatient(withActiveTrue());
		createPatient(withActiveFalse());

		// Only reindex one of them
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl("Observation?status=final");
		parameters.addUrl("Patient?");

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
		JobInstance jobInstance = myBatch2JobHelper.awaitJobCompletion(res);

		assertThat(jobInstance.getCombinedRecordsProcessed()).isEqualTo(3);
	}

	@Test
	public void testReindexDeletedResources_byUrl_willRemoveDeletedResourceEntriesFromIndexTables(){
		IIdType obsId = myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);

		runInTransaction(() -> {
			int entriesInSpIndexTokenTable = myResourceIndexedSearchParamTokenDao.countForResourceId(JpaPid.fromId(obsId.getIdPartAsLong()));
			assertEquals(1, entriesInSpIndexTokenTable);

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
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(res);

		// then
		runInTransaction(() -> {
			int entriesInSpIndexTokenTablePostReindexing = myResourceIndexedSearchParamTokenDao.countForResourceId(JpaPid.fromId(obsId.getIdPartAsLong()));
			assertEquals(0, entriesInSpIndexTokenTablePostReindexing);
		});
	}

	@Test
	public void testReindex_Everything() {
		// setup

		for (int i = 0; i < 50; ++i) {
			myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		}

		sleepUntilTimeChange();

		myReindexTestHelper.createAlleleSearchParameter();
		mySearchParamRegistry.forceRefresh();

		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd).size());
		// The search param value is on the observation, but it hasn't been indexed yet
		assertThat(myReindexTestHelper.getAlleleObservationIds()).hasSize(0);

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(startResponse);

		// validate
		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd).size());
		// Now all of them should be indexed
		assertThat(myReindexTestHelper.getAlleleObservationIds()).hasSize(50);
	}

	@Test
	public void testReindex_DuplicateResourceBeforeEnforceUniqueShouldSaveWarning() {
		myReindexTestHelper.createObservationWithStatusAndCode();
		myReindexTestHelper.createObservationWithStatusAndCode();

		DaoMethodOutcome searchParameter = myReindexTestHelper.createUniqueCodeSearchParameter();

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
		JobInstance myJob = myBatch2JobHelper.awaitJobCompletion(startResponse);

		assertEquals(StatusEnum.COMPLETED, myJob.getStatus());
		assertNotNull(myJob.getWarningMessages());
		assertThat(myJob.getWarningMessages()).contains("Failed to reindex resource because unique search parameter " + searchParameter.getEntity().getIdDt().toVersionless().toString());
	}

	/**
	 * This test will fail and can be deleted if we make the hash columns on
	 * the unique index table non-nullable.
	 */
	@Test
	public void testReindex_ComboUnique_HashesShouldBePopulated() {
		myReindexTestHelper.createUniqueCodeSearchParameter();
		myReindexTestHelper.createObservationWithStatusAndCode();
		logAllUniqueIndexes();

		// Clear hashes
		runInTransaction(()->{
			assertEquals(1, myEntityManager.createNativeQuery("UPDATE HFJ_IDX_CMP_STRING_UNIQ SET HASH_COMPLETE = null WHERE HASH_COMPLETE IS NOT NULL").executeUpdate());
			assertEquals(0, myEntityManager.createNativeQuery("UPDATE HFJ_IDX_CMP_STRING_UNIQ SET HASH_COMPLETE = null WHERE HASH_COMPLETE IS NOT NULL").executeUpdate());
			assertEquals(1, myEntityManager.createNativeQuery("UPDATE HFJ_IDX_CMP_STRING_UNIQ SET HASH_COMPLETE_2 = null WHERE HASH_COMPLETE_2 IS NOT NULL").executeUpdate());
			assertEquals(0, myEntityManager.createNativeQuery("UPDATE HFJ_IDX_CMP_STRING_UNIQ SET HASH_COMPLETE_2 = null WHERE HASH_COMPLETE_2 IS NOT NULL").executeUpdate());
		});

		// Run a reindex
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
		JobInstance myJob = myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId(), 999);
		assertEquals(StatusEnum.COMPLETED, myJob.getStatus());

		// Verify that hashes are repopulated
		runInTransaction(()->{
			List<ResourceIndexedComboStringUnique> indexes = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, indexes.size());
			assertThat(indexes.get(0).getHashComplete()).isNotNull().isNotZero();
			assertThat(indexes.get(0).getHashComplete2()).isNotNull().isNotZero();
		});
	}

	/**
	 * This test will fail and can be deleted if we make the hash columns on
	 * the unique index table non-nullable.
	 */
	@Test
	public void testReindex_ComboNonUnique_HashesShouldBePopulated() {
		myReindexTestHelper.createNonUniqueStatusAndCodeSearchParameter();
		myReindexTestHelper.createObservationWithStatusAndCode();
		logAllNonUniqueIndexes();

		// Set hash wrong
		runInTransaction(()->{
			assertEquals(1, myEntityManager.createNativeQuery("UPDATE HFJ_IDX_CMB_TOK_NU SET HASH_COMPLETE = 0 WHERE HASH_COMPLETE != 0").executeUpdate());
			assertEquals(0, myEntityManager.createNativeQuery("UPDATE HFJ_IDX_CMB_TOK_NU SET HASH_COMPLETE = 0 WHERE HASH_COMPLETE != 0").executeUpdate());
		});

		// Run a reindex
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
		JobInstance myJob = myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId(), 999);
		assertEquals(StatusEnum.COMPLETED, myJob.getStatus());

		// Verify that hashes are repopulated
		runInTransaction(()->{
			List<ResourceIndexedComboTokenNonUnique> indexes = myResourceIndexedComboTokensNonUniqueDao.findAll();
			assertEquals(1, indexes.size());
			assertEquals(-4763890811650597657L, indexes.get(0).getHashComplete());
		});
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
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
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
		startRequest.setJobDefinitionId(JOB_REINDEX);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);
		JobInstance outcome = myBatch2JobHelper.awaitJobFailure(startResponse);

		// Verify

		assertEquals(StatusEnum.FAILED, outcome.getStatus());
		assertEquals("java.lang.Error: foo message", outcome.getErrorMessage());
	}

	@Test
	public void testReindex_withReindexingUponSearchParameterChangeEnabled_reindexJobCompleted() {
		List<JobInstance> jobInstances = myJobPersistence.fetchInstancesByJobDefinitionId(JOB_REINDEX, 10, 0);
		assertThat(jobInstances).isEmpty();

		// make sure the resources auto-reindex after the search parameter update is enabled
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(true);

		// create an Observation resource and SearchParameter for it to trigger re-indexing
		myReindexTestHelper.createObservationWithStatusAndCode();
		myReindexTestHelper.createCodeSearchParameter();

		// check that reindex job was created
		jobInstances = myJobPersistence.fetchInstancesByJobDefinitionId(JOB_REINDEX, 10, 0);
		assertThat(jobInstances).hasSize(1);

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
