package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.batch.listener.PidReaderCounterListener;
import ca.uhn.fhir.jpa.batch.writer.SqlExecutorWriter;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
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
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class DeleteExpungeDaoTest extends BaseJpaR4Test {
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	BatchJobHelper myBatchJobHelper;

	@BeforeEach
	public void before() {
		myDaoConfig.setAllowMultipleDelete(true);
		myDaoConfig.setExpungeEnabled(true);
		myDaoConfig.setDeleteExpungeEnabled(true);
		myDaoConfig.setInternalSynchronousSearchSize(new DaoConfig().getInternalSynchronousSearchSize());
	}

	@AfterEach
	public void after() {
		DaoConfig defaultDaoConfig = new DaoConfig();
		myDaoConfig.setAllowMultipleDelete(defaultDaoConfig.isAllowMultipleDelete());
		myDaoConfig.setExpungeEnabled(defaultDaoConfig.isExpungeEnabled());
		myDaoConfig.setDeleteExpungeEnabled(defaultDaoConfig.isDeleteExpungeEnabled());
		myDaoConfig.setExpungeBatchSize(defaultDaoConfig.getExpungeBatchSize());
	}

	@Test
	public void testDeleteCascadeExpungeReturns400() {
		// Create new organization
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId));
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		// Try to delete _cascade and _expunge on the organization
		BaseServerResponseException e = assertThrows(BaseServerResponseException.class, () -> {
			myOrganizationDao
				.deleteByUrl("Organization?" + "_cascade=delete&" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);
		});

		// Get not implemented HTTP 400 error
		assertEquals(Constants.STATUS_HTTP_400_BAD_REQUEST, e.getStatusCode());
		assertEquals(Msg.code(964) + "_expunge cannot be used with _cascade", e.getMessage());


		// Try to delete with header 'X-Cascade' = delete
		when(mySrd.getHeader(Constants.HEADER_CASCADE)).thenReturn(Constants.CASCADE_DELETE);
		e = assertThrows(BaseServerResponseException.class, () -> {
			myOrganizationDao
				.deleteByUrl("Organization?" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);
		});

		// Get not implemented HTTP 400 error
		assertEquals(Constants.STATUS_HTTP_400_BAD_REQUEST, e.getStatusCode());
		assertEquals(Msg.code(964) + "_expunge cannot be used with _cascade", e.getMessage());
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
		Long jobExecutionId = jobExecutionIdFromOutcome(outcome);
		JobExecution job = myBatchJobHelper.awaitJobExecution(jobExecutionId);

		// validate
		assertEquals(BatchStatus.FAILED, job.getStatus());
		assertThat(job.getExitStatus().getExitDescription(), containsString("DELETE with _expunge=true failed.  Unable to delete " + organizationId.toVersionless() + " because " + patientId.toVersionless() + " refers to it via the path Patient.managingOrganization"));
	}

	private Long jobExecutionIdFromOutcome(DeleteMethodOutcome theResult) {
		OperationOutcome operationOutcome = (OperationOutcome) theResult.getOperationOutcome();
		String diagnostics = operationOutcome.getIssueFirstRep().getDiagnostics();
		String[] parts = diagnostics.split("Delete job submitted with id ");
		return Long.valueOf(parts[1]);
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
		myDaoConfig.setExpungeBatchSize(10);

		// execute
		DeleteMethodOutcome outcome = myOrganizationDao.deleteByUrl("Organization?" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);
		Long jobId = jobExecutionIdFromOutcome(outcome);
		JobExecution job = myBatchJobHelper.awaitJobExecution(jobId);

		// validate
		assertEquals(BatchStatus.FAILED, job.getStatus());
		assertThat(job.getExitStatus().getExitDescription(), containsString("DELETE with _expunge=true failed.  Unable to delete "));
	}

	@Test
	public void testDeleteExpungeRespectsExpungeBatchSize() {
		// setup
		myDaoConfig.setExpungeBatchSize(3);
		for (int i = 0; i < 10; ++i) {
			Patient patient = new Patient();
			myPatientDao.create(patient);
		}

		// execute
		DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?" + JpaConstants.PARAM_DELETE_EXPUNGE + "=true", mySrd);

		// validate
		Long jobExecutionId = jobExecutionIdFromOutcome(outcome);
		JobExecution job = myBatchJobHelper.awaitJobExecution(jobExecutionId);

		// 10 / 3 rounded up = 4
		assertEquals(4, myBatchJobHelper.getReadCount(jobExecutionId));
		assertEquals(4, myBatchJobHelper.getWriteCount(jobExecutionId));

		assertEquals(30, job.getExecutionContext().getLong(SqlExecutorWriter.ENTITY_TOTAL_UPDATED_OR_DELETED));
		assertEquals(10, job.getExecutionContext().getLong(PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED));
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
		Long jobExecutionId = jobExecutionIdFromOutcome(outcome);
		JobExecution job = myBatchJobHelper.awaitJobExecution(jobExecutionId);
		assertEquals(1, myBatchJobHelper.getReadCount(jobExecutionId));
		assertEquals(1, myBatchJobHelper.getWriteCount(jobExecutionId));

		assertEquals(30, job.getExecutionContext().getLong(SqlExecutorWriter.ENTITY_TOTAL_UPDATED_OR_DELETED));
		assertEquals(10, job.getExecutionContext().getLong(PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED));
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
		Long jobExecutionId = jobExecutionIdFromOutcome(outcome);
		JobExecution job = myBatchJobHelper.awaitJobExecution(jobExecutionId);

		// validate
		assertEquals(1, myBatchJobHelper.getReadCount(jobExecutionId));
		assertEquals(1, myBatchJobHelper.getWriteCount(jobExecutionId));

		assertEquals(7, job.getExecutionContext().getLong(SqlExecutorWriter.ENTITY_TOTAL_UPDATED_OR_DELETED));
		assertEquals(2, job.getExecutionContext().getLong(PidReaderCounterListener.RESOURCE_TOTAL_PROCESSED));
	}

}
