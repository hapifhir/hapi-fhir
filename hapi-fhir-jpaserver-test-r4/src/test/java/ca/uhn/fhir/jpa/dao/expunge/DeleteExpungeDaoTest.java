package ca.uhn.fhir.jpa.dao.expunge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DeleteExpungeDaoTest extends BaseJpaR4Test {

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setDeleteExpungeEnabled(true);
		myStorageSettings.setInternalSynchronousSearchSize(new JpaStorageSettings().getInternalSynchronousSearchSize());
	}

	@AfterEach
	public void after() {
		JpaStorageSettings defaultStorageSettings = new JpaStorageSettings();
		myStorageSettings.setAllowMultipleDelete(defaultStorageSettings.isAllowMultipleDelete());
		myStorageSettings.setExpungeEnabled(defaultStorageSettings.isExpungeEnabled());
		myStorageSettings.setDeleteExpungeEnabled(defaultStorageSettings.isDeleteExpungeEnabled());
		myStorageSettings.setExpungeBatchSize(defaultStorageSettings.getExpungeBatchSize());
	}

	@Test
	public void testCascade_MultiLevel_Success() {
		// Setup

		// Create a chain of dependent references
		IIdType p1 = createPatient(withActiveTrue());
		IIdType o1 = createObservation(withSubject(p1));
		IIdType o1b = createObservation(withReference("hasMember", o1));
		IIdType o1c = createObservation(withReference("hasMember", o1b));

		// validate precondition
		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(3, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		// execute
		String url = "Patient?" +
			JpaConstants.PARAM_DELETE_EXPUNGE + "=true";
		when(mySrd.getParameters()).thenReturn(Map.of(
			Constants.PARAMETER_CASCADE_DELETE, new String[]{Constants.CASCADE_DELETE},
			JpaConstants.PARAM_DELETE_EXPUNGE, new String[]{"true"},
			Constants.PARAMETER_CASCADE_DELETE_MAX_ROUNDS, new String[]{"10"}
		));
		DeleteMethodOutcome outcome = myOrganizationDao.deleteByUrl(url, mySrd);
		String jobId = jobExecutionIdFromOutcome(outcome);
		JobInstance job = myBatch2JobHelper.awaitJobCompletion(jobId);

		// Validate
		assertEquals(4, job.getCombinedRecordsProcessed());
		assertDoesntExist(p1);
		assertDoesntExist(o1);
		assertDoesntExist(o1b);
		assertDoesntExist(o1c);
	}

	@Test
	public void testCascade_MultiLevel_NotEnoughRounds() {
		// Setup

		// Create a chain of dependent references
		IIdType p1 = createPatient(withActiveTrue());
		IIdType o1 = createObservation(withSubject(p1));
		IIdType o1b = createObservation(withReference("hasMember", o1));
		IIdType o1c = createObservation(withReference("hasMember", o1b));

		// validate precondition
		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(3, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		String url = "Patient?" +
			JpaConstants.PARAM_DELETE_EXPUNGE + "=true";
		when(mySrd.getParameters()).thenReturn(Map.of(
			Constants.PARAMETER_CASCADE_DELETE, new String[]{Constants.CASCADE_DELETE},
			JpaConstants.PARAM_DELETE_EXPUNGE, new String[]{"true"},
			Constants.PARAMETER_CASCADE_DELETE_MAX_ROUNDS, new String[]{"2"}
		));
		DeleteMethodOutcome outcome = myOrganizationDao.deleteByUrl(url, mySrd);
		String jobId = jobExecutionIdFromOutcome(outcome);
		JobInstance job = myBatch2JobHelper.awaitJobFailure(jobId);

		// Validate
		assertThat(job.getErrorMessage()).contains("Unable to delete");
		assertNotGone(p1);
		assertNotGone(o1);
		assertNotGone(o1b);
		assertNotGone(o1c);
	}


	@Test
	public void testDeleteExpungeThrowExceptionIfForeignKeyLinksExists() {
		// setup
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		// execute
		DeleteMethodOutcome outcome = myOrganizationDao.deleteByUrl("Organization?" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);
		String jobExecutionId = jobExecutionIdFromOutcome(outcome);
		JobInstance job = myBatch2JobHelper.awaitJobFailure(jobExecutionId);

		// validate
		assertEquals(StatusEnum.ERRORED, job.getStatus());
		assertThat(job.getErrorMessage()).contains("DELETE with _expunge=true failed.  Unable to delete " + organizationId.toVersionless() + " because " + patientId.toVersionless() + " refers to it via the path Patient.managingOrganization");
	}

	private String jobExecutionIdFromOutcome(DeleteMethodOutcome theResult) {
		OperationOutcome operationOutcome = (OperationOutcome) theResult.getOperationOutcome();
		String diagnostics = operationOutcome.getIssueFirstRep().getDiagnostics();
		String[] parts = diagnostics.split("Delete job submitted with id ");
		return parts[1];
	}

	@Test
	public void testDeleteWithExpungeFailsIfConflictsAreGeneratedByMultiplePartitions() {
		//See https://github.com/hapifhir/hapi-fhir/issues/2661

		// setup
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 20; i++) {
			Organization o = new Organization();
			o.setId("Organization/O-" + i);
			Patient p = new Patient();
			p.setId("Patient/P-" + i);
			p.setManagingOrganization(new Reference(o.getId()));
			builder.addTransactionUpdateEntry(o);
			builder.addTransactionUpdateEntry(p);
		}
		mySystemDao.transaction(new SystemRequestDetails(), (Bundle) builder.getBundle());
		myStorageSettings.setExpungeBatchSize(10);

		// execute
		DeleteMethodOutcome outcome = myOrganizationDao.deleteByUrl("Organization?" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);
		String jobId = jobExecutionIdFromOutcome(outcome);
		JobInstance job = myBatch2JobHelper.awaitJobFailure(jobId);

		// validate
		assertEquals(StatusEnum.ERRORED, job.getStatus());
		assertThat(job.getErrorMessage()).contains("DELETE with _expunge=true failed.  Unable to delete ");
	}

	@Test
	public void testDeleteExpungeRespectsExpungeBatchSize() {
		// setup
		myStorageSettings.setExpungeBatchSize(3);
		for (int i = 0; i < 10; ++i) {
			Patient patient = new Patient();
			myPatientDao.create(patient);
		}

		// execute
		DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);

		// validate
		String jobId = jobExecutionIdFromOutcome(outcome);
		JobInstance job = myBatch2JobHelper.awaitJobCompletion(jobId);

		assertEquals(10, myBatch2JobHelper.getCombinedRecordsProcessed(jobId));

		// TODO KHS replace these with a report
//		assertEquals(30, job.getExecutionContext().getLong(SqlExecutorWriter.ENTITY_TOTAL_UPDATED_OR_DELETED));
//		assertEquals(10, job.getExecutionContext().getLong(PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED));
	}

	@Test
	public void testDeleteExpungeWithDefaultExpungeBatchSize() {
		// setup
		for (int i = 0; i < 10; ++i) {
			Patient patient = new Patient();
			myPatientDao.create(patient);
		}

		// execute
		DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);

		// validate
		String jobId = jobExecutionIdFromOutcome(outcome);
		JobInstance job = myBatch2JobHelper.awaitJobCompletion(jobId);
		assertEquals(10, myBatch2JobHelper.getCombinedRecordsProcessed(jobId));

		// TODO KHS replace these with a report
//		assertEquals(30, job.getExecutionContext().getLong(SqlExecutorWriter.ENTITY_TOTAL_UPDATED_OR_DELETED));
//		assertEquals(10, job.getExecutionContext().getLong(PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED));
	}

	@Test
	public void testDeleteExpungeNoThrowExceptionWhenLinkInSearchResults() {
		// setup
		Patient mom = new Patient();
		IIdType momId = myPatientDao.create(mom).getId().toUnqualifiedVersionless();

		Patient child = new Patient();
		List<Patient.PatientLinkComponent> link;
		child.addLink().setOther(new Reference(mom));
		IIdType childId = myPatientDao.create(child).getId().toUnqualifiedVersionless();

		//execute
		DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);
		String jobId = jobExecutionIdFromOutcome(outcome);
		JobInstance job = myBatch2JobHelper.awaitJobCompletion(jobId);

		// validate
		assertEquals(2, myBatch2JobHelper.getCombinedRecordsProcessed(jobId));

		// TODO KHS replace these with a report
//		assertEquals(7, job.getExecutionContext().getLong(SqlExecutorWriter.ENTITY_TOTAL_UPDATED_OR_DELETED));
//		assertEquals(2, job.getExecutionContext().getLong(PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED));
	}

}
