package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexStep;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.INDEX_STATUS_INDEXED;
import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.INDEX_STATUS_INDEXING_FAILED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReindexStepTest extends BaseJpaR4Test {

	@Autowired
	private ReindexStep myReindexStep;

	@Mock
	private IJobDataSink<VoidModel> myDataSink;

	@Captor
	private ArgumentCaptor<String> myErrorCaptor;

	@AfterEach
	public void after() {
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
	}

	@Test
	public void testReindex_NoActionNeeded() {

		// Setup

		Long id0 = createPatient(withActiveTrue(), withFamily("SIMPSON")).getIdPartAsLong();
		Long id1 = createPatient(withActiveTrue(), withFamily("FLANDERS")).getIdPartAsLong();

		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson();
		data.addTypedPid("Patient", id0);
		data.addTypedPid("Patient", id1);

		// Execute

		myCaptureQueriesListener.clear();
		RunOutcome outcome = myReindexStep.doReindex(data, myDataSink, "index-id", "chunk-id", new ReindexJobParameters());

		// Verify
		assertEquals(2, outcome.getRecordsProcessed());
		assertEquals(5, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}


	@Test
	public void testReindex_NoActionNeeded_IndexMissingFieldsEnabled() {

		// Setup

		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		Long id0 = createPatient(withActiveTrue(), withFamily("SIMPSON")).getIdPartAsLong();
		Long id1 = createPatient(withActiveTrue(), withFamily("FLANDERS")).getIdPartAsLong();

		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson();
		data.addTypedPid("Patient", id0);
		data.addTypedPid("Patient", id1);

		// Execute

		myCaptureQueriesListener.clear();
		RunOutcome outcome = myReindexStep.doReindex(data, myDataSink, "index-id", "chunk-id", new ReindexJobParameters());

		// Verify
		assertEquals(2, outcome.getRecordsProcessed());
		assertEquals(7, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}


	@Test
	public void testReindex_IndexesWereMissing() {

		// Setup

		Long id0 = createPatient(withActiveTrue(), withFamily("SIMPSON")).getIdPartAsLong();
		Long id1 = createPatient(withActiveTrue(), withFamily("FLANDERS")).getIdPartAsLong();

		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson();
		data.addTypedPid("Patient", id0);
		data.addTypedPid("Patient", id1);

		runInTransaction(() -> {
			myResourceIndexedSearchParamStringDao.deleteByResourceId(id0);
			myResourceIndexedSearchParamTokenDao.deleteByResourceId(id0);
		});

		// Execute

		myCaptureQueriesListener.clear();
		RunOutcome outcome = myReindexStep.doReindex(data, myDataSink, "index-id", "chunk-id", new ReindexJobParameters());

		// Verify
		assertEquals(2, outcome.getRecordsProcessed());
		assertEquals(5, myCaptureQueriesListener.logSelectQueries().size());
		// name, family, phonetic, deceased, active
		assertEquals(5, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}


	@Test
	public void testReindex_IndexesAddedAndRemoved_IndexMissingFieldsEnabled() {

		// Setup

		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
		boolean markResourcesForReindexingUponSearchParameterChange = myStorageSettings.isMarkResourcesForReindexingUponSearchParameterChange();
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);	// if this were true, it would set up a lot of reindex jobs extraneous to the one we're trying to test

		IIdType orgId = createOrganization(withId("ORG"));
		Long id0 = createPatient(withActiveTrue(), withFamily("SIMPSON"), withOrganization(orgId)).getIdPartAsLong();
		Long id1 = createPatient(withActiveTrue(), withFamily("FLANDERS"), withOrganization(orgId)).getIdPartAsLong();

		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson();
		data.addTypedPid("Patient", id0);
		data.addTypedPid("Patient", id1);

		SearchParameter sp = new SearchParameter();
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("family2");
		sp.setExpression("Patient.name.family");
		mySearchParameterDao.create(sp);

		sp = new SearchParameter();
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode(Patient.SP_ORGANIZATION + "2");
		sp.setExpression("Patient.managingOrganization");
		mySearchParameterDao.create(sp);

		sp = new SearchParameter();
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.RETIRED);
		sp.setCode("family");
		sp.setExpression("Patient.name.family");
		mySearchParameterDao.create(sp);

		sp = new SearchParameter();
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.RETIRED);
		sp.setCode(Patient.SP_ORGANIZATION);
		sp.setExpression("Patient.managingOrganization");
		mySearchParameterDao.create(sp);

		mySearchParamRegistry.forceRefresh();

		// Execute

		myCaptureQueriesListener.clear();
		RunOutcome outcome = myReindexStep.doReindex(data, myDataSink, "index-id", "chunk-id", new ReindexJobParameters());

		// Verify
		assertEquals(2, outcome.getRecordsProcessed());
		assertEquals(9, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());

		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(markResourcesForReindexingUponSearchParameterChange);
	}

	@Test
	public void testReindex_OneResourceReindexFailedButOthersSucceeded() {

		// Setup

		Long id0 = createPatient(withActiveTrue(), withFamily("SIMPSON")).getIdPartAsLong();
		Long id1 = createPatient(withActiveTrue(), withFamily("FLANDERS")).getIdPartAsLong();
		Long idPatientToInvalidate = createPatient().getIdPartAsLong();
		Long idObservation = createObservation(withSubject(new IdType("Patient/" + idPatientToInvalidate))).getIdPartAsLong();

		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson();
		data.addTypedPid("Patient", id0);
		data.addTypedPid("Patient", id1);
		data.addTypedPid("Patient", idPatientToInvalidate);
		data.addTypedPid("Observation", idObservation);

		runInTransaction(() -> {
			// Swap in some invalid text, which will cause an error when we go to reindex
			assertEquals(1, myEntityManager.createNativeQuery("UPDATE HFJ_RES_VER SET RES_TEXT = null WHERE RES_ID = " + idPatientToInvalidate).executeUpdate());
			assertEquals(1, myEntityManager.createNativeQuery("UPDATE HFJ_RES_VER SET RES_TEXT_VC = 'ABCDEFG' WHERE RES_ID = " + idPatientToInvalidate).executeUpdate());

			// Also set the current index status to errored on one, so it can be reset
			assertEquals(1, myEntityManager.createNativeQuery("UPDATE HFJ_RESOURCE SET SP_INDEX_STATUS = 2 WHERE RES_ID = " + id0).executeUpdate());

			myResourceIndexedSearchParamStringDao.deleteByResourceId(id0);
			myResourceIndexedSearchParamTokenDao.deleteByResourceId(id0);
		});

		// Execute

		myCaptureQueriesListener.clear();
		RunOutcome outcome = myReindexStep.doReindex(data, myDataSink, "index-id", "chunk-id", new ReindexJobParameters());

		// Verify
		assertEquals(4, outcome.getRecordsProcessed());
		assertEquals(8, myCaptureQueriesListener.logSelectQueries().size());
		assertEquals(5, myCaptureQueriesListener.countInsertQueries());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());

		verify(myDataSink, times(1)).recoveredError(myErrorCaptor.capture());
		String message = myErrorCaptor.getValue();
		message = message.replace("Observation.subject.where(resolve() is Patient)", "Observation.subject"); // depending on whether subject or patient gets indexed first
		assertThat(message).contains("HAPI-0928: Failed to parse database resource");

		runInTransaction(() -> {
			ResourceTable table = myResourceTableDao.findById(idPatientToInvalidate).orElseThrow();
			assertEquals(INDEX_STATUS_INDEXING_FAILED, table.getIndexStatus());

			table = myResourceTableDao.findById(id0).orElseThrow();
			assertEquals(INDEX_STATUS_INDEXED, table.getIndexStatus());
		});
	}


}
