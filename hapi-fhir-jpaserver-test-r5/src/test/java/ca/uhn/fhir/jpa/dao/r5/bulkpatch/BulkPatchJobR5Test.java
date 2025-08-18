package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulkPatchJobR5Test extends BaseBulkPatchR5Test {

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
	}

	@Test
	public void testBulkPatch() {
		// Setup
		createPatient(withId("A"), withIdentifier("http://blah", "A1"));
		createPatient(withId("B"), withIdentifier("http://blah", "B1"));
		createPatient(withId("C"), withIdentifier("http://blah", "C1"));
		// This one already has the right system
		createPatient(withId("D"), withIdentifier("http://foo", "D1"));

		// Test
		Parameters patchDocument = createPatchWithModifyPatientIdentifierSystem();
		String jobId = initiateAllPatientJobAndAwaitCompletion(patchDocument);

		// Verify
		Patient actualPatientA = myPatientDao.read(new IdType("Patient/A"), newSrd());
		assertEquals("http://foo", actualPatientA.getIdentifier().get(0).getSystem());
		assertEquals("A1", actualPatientA.getIdentifier().get(0).getValue());
		assertEquals("2", actualPatientA.getMeta().getVersionId());
		Patient actualPatientB = myPatientDao.read(new IdType("Patient/B"), newSrd());
		assertEquals("http://foo", actualPatientB.getIdentifier().get(0).getSystem());
		assertEquals("B1", actualPatientB.getIdentifier().get(0).getValue());
		assertEquals("2", actualPatientB.getMeta().getVersionId());
		// This patient was already right
		Patient actualPatientD = myPatientDao.read(new IdType("Patient/D"), newSrd());
		assertEquals("http://foo", actualPatientD.getIdentifier().get(0).getSystem());
		assertEquals("D1", actualPatientD.getIdentifier().get(0).getValue());
		assertEquals("1", actualPatientD.getMeta().getVersionId());

		JobInstance instance = myJobCoordinator.getInstance(jobId);
		BulkModifyResourcesResultsJson report = JsonUtil.deserialize(instance.getReport(), BulkModifyResourcesResultsJson.class);
		ourLog.info("Report: {}", report.getReport());

		assertThat(report.getReport()).containsSubsequence(
			"Total Resources Changed   : 3 ",
			"Total Resources Unchanged : 1 ",
			"Total Failed Changes      : 0 "
		);
	}

	/**
	 * Patch a group of resources where one of them can't be stored due to a failure
	 * such as a validation failure
	 */
	@Test
	public void testBulkPatch_SomePatchingFails() {
		// Setup
		createPatient(withId("A"), withIdentifier("http://blah", "A1"));
		createPatient(withId("B"), withIdentifier("http://blah", "B1"));
		createPatient(withId("C"), withIdentifier("http://blah", "C1"));

		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, new IAnonymousInterceptor() {
			@Override
			public void invoke(IPointcut thePointcut, HookParams theArgs) {
				IBaseResource newResource = theArgs.get(IBaseResource.class, 1);
				if (newResource.getIdElement().getIdPart().equals("B")) {
					throw new PreconditionFailedException("Simulated validation failed message");
				}
			}
		});

		// Test
		Parameters patchDocument = createPatchWithModifyPatientIdentifierSystem();
		String jobId = initiateAllPatientJobAndAwaitFailure(patchDocument);

		// Verify
		Patient actualPatientA = myPatientDao.read(new IdType("Patient/A"), newSrd());
		assertEquals("http://foo", actualPatientA.getIdentifier().get(0).getSystem());
		assertEquals("A1", actualPatientA.getIdentifier().get(0).getValue());
		assertEquals("2", actualPatientA.getMeta().getVersionId());
		Patient actualPatientB = myPatientDao.read(new IdType("Patient/B"), newSrd());
		assertEquals("http://blah", actualPatientB.getIdentifier().get(0).getSystem());
		assertEquals("B1", actualPatientB.getIdentifier().get(0).getValue());
		assertEquals("1", actualPatientB.getMeta().getVersionId());

		JobInstance instance = myJobCoordinator.getInstance(jobId);
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertEquals("Bulk Patch had 1 failure(s). See report for details.", instance.getErrorMessage());
		BulkModifyResourcesResultsJson report = JsonUtil.deserialize(instance.getReport(), BulkModifyResourcesResultsJson.class);
		ourLog.info("Report: {}", report.getReport());

		assertThat(report.getReport()).containsSubsequence(
			"Bulk Patch Report",
			"Total Resources Changed   : 2 ",
			"Total Resources Unchanged : 0 ",
			"Total Failed Changes      : 1 ",
			"ResourceType[Patient]",
			"Changed   : 2",
			"Failures  : 1",
			"Failures:",
			"Patient/B/_history/1: Simulated validation failed message"
		);
	}


	private String initiateAllPatientJobAndAwaitCompletion(Parameters patchDocument) {
		String jobId = initiateAllPatientJob(patchDocument);
		myBatch2JobHelper.awaitJobCompletion(jobId);
		return jobId;
	}

	private String initiateAllPatientJobAndAwaitFailure(Parameters patchDocument) {
		String jobId = initiateAllPatientJob(patchDocument);
		myBatch2JobHelper.awaitJobFailure(jobId);
		return jobId;
	}

	private String initiateAllPatientJob(Parameters patchDocument) {
		BulkPatchJobParameters jobParameters = new BulkPatchJobParameters();
		jobParameters.addUrl("Patient?");
		jobParameters.setFhirPatch(myFhirContext, patchDocument);

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(BulkPatchJobAppCtx.JOB_ID);
		startRequest.setParameters(jobParameters);
		startRequest.setUseCache(false);
		String jobId = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest).getInstanceId();
		return jobId;
	}

	@Nonnull
	static Parameters createPatchWithModifyPatientIdentifierSystem() {
		Parameters patchDocument = new Parameters();
		Parameters.ParametersParameterComponent comp = patchDocument.addParameter().setName("operation");
		comp.addPart().setName("type").setValue(new CodeType("replace"));
		comp.addPart().setName("path").setValue(new StringType("Patient.identifier.system"));
		comp.addPart().setName("value").setValue(new UriType("http://foo"));
		return patchDocument;
	}

}
