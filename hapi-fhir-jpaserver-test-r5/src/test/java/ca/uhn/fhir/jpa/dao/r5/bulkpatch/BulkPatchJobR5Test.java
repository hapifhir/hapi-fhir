package ca.uhn.fhir.jpa.dao.r5.bulkpatch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobAppCtx;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.FhirPatchBuilder;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.function.Consumer;

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
		Patient actualPatientA = readPatient("Patient/A");
		assertEquals("http://foo", actualPatientA.getIdentifier().get(0).getSystem());
		assertEquals("A1", actualPatientA.getIdentifier().get(0).getValue());
		assertEquals("2", actualPatientA.getMeta().getVersionId());
		Patient actualPatientB = readPatient("Patient/B");
		assertEquals("http://foo", actualPatientB.getIdentifier().get(0).getSystem());
		assertEquals("B1", actualPatientB.getIdentifier().get(0).getValue());
		assertEquals("2", actualPatientB.getMeta().getVersionId());
		// This patient was already right
		Patient actualPatientD = readPatient("Patient/D");
		assertEquals("http://foo", actualPatientD.getIdentifier().get(0).getSystem());
		assertEquals("D1", actualPatientD.getIdentifier().get(0).getValue());
		assertEquals("1", actualPatientD.getMeta().getVersionId());

		JobInstance instance = myJobCoordinator.getInstance(jobId);
		BulkModifyResourcesResultsJson report = JsonUtil.deserialize(instance.getReport(), BulkModifyResourcesResultsJson.class);
		ourLog.info("Report: {}", report.getReport());

		assertThat(report.getReport()).containsSubsequence(
			"Total Resources Changed   : 3 ",
			"Total Resources Unchanged : 1 "
		);
	}

	/**
	 * Patch a group of resources where one of them can't be stored due to a failure
	 * such as a validation failure
	 */
	@Test
	public void testBulkPatch_PatchingFails_AtCommitTime() {
		// Setup
		createPatient(withId("A"), withIdentifier("http://blah", "A1"));
		createPatient(withId("B"), withIdentifier("http://blah", "B1"));
		createPatient(withId("C"), withIdentifier("http://blah", "C1"));

		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, (thePointcut, theArgs) -> {
			IBaseResource newResource = theArgs.get(IBaseResource.class, 1);
			if (newResource.getIdElement().getIdPart().equals("B")) {
				throw new PreconditionFailedException("Simulated validation failed message");
			}
		});

		// Test
		Parameters patchDocument = createPatchWithModifyPatientIdentifierSystem();
		String jobId = initiateAllPatientJobAndAwaitFailure(patchDocument);

		// Verify
		Patient actualPatientA = readPatient("Patient/A");
		assertEquals("http://foo", actualPatientA.getIdentifier().get(0).getSystem());
		assertEquals("A1", actualPatientA.getIdentifier().get(0).getValue());
		assertEquals("2", actualPatientA.getMeta().getVersionId());
		Patient actualPatientB = readPatient("Patient/B");
		assertEquals("http://blah", actualPatientB.getIdentifier().get(0).getSystem());
		assertEquals("B1", actualPatientB.getIdentifier().get(0).getValue());
		assertEquals("1", actualPatientB.getMeta().getVersionId());

		JobInstance instance = myJobCoordinator.getInstance(jobId);
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertEquals("HAPI-2790: Bulk Patch had 1 failure(s). See report for details.", instance.getErrorMessage());
		BulkModifyResourcesResultsJson report = JsonUtil.deserialize(instance.getReport(), BulkModifyResourcesResultsJson.class);
		ourLog.info("Report: {}", report.getReport());

		assertThat(report.getReport()).containsSubsequence(
			"Bulk Patch Report",
			"Total Resources Changed   : 2 ",
			"Total Resources Unchanged : 0 ",
			"Total Resources Failed    : 1 ",
			"ResourceType[Patient]",
			"Changed   : 2",
			"Failures  : 1",
			"Failures:",
			"Patient/B/_history/1: ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException: Simulated validation failed message"
		);
	}

	/**
	 * Patch a group of resources where one of them can't be stored due to a failure
	 * such as a validation failure
	 */
	@Test
	public void testBulkPatch_PatchingFails_AtPatchTime() {
		/*
		 * Setup
		 */

		// These will fail because they have 2 identifiers
		createPatient(withId("A"), withIdentifier("http://blah", "A1"), withIdentifier("http://blah", "A2"));
		createPatient(withId("B"), withIdentifier("http://blah", "B1"), withIdentifier("http://blah", "B2"));

		// This will succeed, it only has 1 identifier
		createPatient(withId("C"), withIdentifier("http://blah", "C1"));

		/*
		 * Test
		 */

		FhirPatchBuilder patchBuilder = new FhirPatchBuilder(myFhirContext);
		// Per the spec, delete should fail if there are multiple matches to the path
		patchBuilder.delete().path("Patient.identifier");
		String jobId = initiateAllPatientJobAndAwaitFailure((Parameters) patchBuilder.build());

		/*
		 * Verify
		 */

		Patient actualPatientA = readPatient("Patient/A");
		assertEquals(2, actualPatientA.getIdentifier().size());
		assertEquals("http://blah", actualPatientA.getIdentifier().get(0).getSystem());
		assertEquals("A1", actualPatientA.getIdentifier().get(0).getValue());
		assertEquals("http://blah", actualPatientA.getIdentifier().get(0).getSystem());
		assertEquals("A2", actualPatientA.getIdentifier().get(1).getValue());

		Patient actualPatientB = readPatient("Patient/B");
		assertEquals(2, actualPatientB.getIdentifier().size());
		assertEquals("http://blah", actualPatientB.getIdentifier().get(0).getSystem());
		assertEquals("B1", actualPatientB.getIdentifier().get(0).getValue());
		assertEquals("http://blah", actualPatientB.getIdentifier().get(0).getSystem());
		assertEquals("B2", actualPatientB.getIdentifier().get(1).getValue());

		Patient actualPatientC = readPatient("Patient/C");
		assertEquals(0, actualPatientC.getIdentifier().size());

		JobInstance instance = myJobCoordinator.getInstance(jobId);
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertEquals("HAPI-2790: Bulk Patch had 2 failure(s). See report for details.", instance.getErrorMessage());
		BulkModifyResourcesResultsJson report = JsonUtil.deserialize(instance.getReport(), BulkModifyResourcesResultsJson.class);
		ourLog.info("Report: {}", report.getReport());

		assertThat(report.getReport()).containsSubsequence(
			"Bulk Patch Report",
			"Total Resources Changed   : 1 ",
			"Total Resources Unchanged : 0 ",
			"Total Resources Failed    : 2 ",
			"ResourceType[Patient]",
			"Changed   : 1",
			"Failures  : 2",
			"Failures:",
			"Patient/A/_history/1: ca.uhn.fhir.rest.server.exceptions.InvalidRequestException: HAPI-1267: [Multiple elements found at Patient.identifier when deleting]",
			"Patient/B/_history/1: ca.uhn.fhir.rest.server.exceptions.InvalidRequestException: HAPI-1267: [Multiple elements found at Patient.identifier when deleting]"
		);
	}


	@ParameterizedTest
	@ValueSource(ints = {1, 2, 100})
	public void testBulkPatch_DryRun_DryRunModeCollectChanges(int theLimitResourceCount) {
		// Setup
		createPatient(withId("A"), withIdentifier("http://blah", "A1"));
		createPatient(withId("B"), withIdentifier("http://blah", "B1"));
		createPatient(withId("C"), withIdentifier("http://blah", "C1"));
		// This one already has the right system
		createPatient(withId("D"), withIdentifier("http://foo", "D1"));

		// Test
		Parameters patchDocument = createPatchWithModifyPatientIdentifierSystem();
		String jobId = initiateAllPatientJobAndAwaitCompletion(patchDocument, request -> {
			request.setDryRun(true);
			request.setDryRunMode(BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED);
			request.setLimitResourceCount(theLimitResourceCount);
		});

		// Verify

		// Resources on disk should not have been touched
		assertEquals("http://blah", readPatient("Patient/A").getIdentifier().get(0).getSystem());
		assertEquals("http://blah", readPatient("Patient/B").getIdentifier().get(0).getSystem());
		assertEquals("http://blah", readPatient("Patient/C").getIdentifier().get(0).getSystem());
		assertEquals("http://foo", readPatient("Patient/D").getIdentifier().get(0).getSystem());

		String reportJson = myJobCoordinator.getInstance(jobId).getReport();
		BulkModifyResourcesResultsJson report = JsonUtil.deserialize(reportJson, BulkModifyResourcesResultsJson.class);

		// Verify changed resources in report JSON
		List<String> resources = report.getResourcesChangedBodies()
			.stream()
			.map(t -> t.replaceFirst(",\"lastUpdated\":\".*?\"", ""))
			.sorted()
			.toList();
		List<String> expected = List.of(
			"{\"resourceType\":\"Patient\",\"id\":\"A\",\"meta\":{\"versionId\":\"1\"},\"identifier\":[{\"system\":\"http://foo\",\"value\":\"A1\"}]}",
			"{\"resourceType\":\"Patient\",\"id\":\"B\",\"meta\":{\"versionId\":\"1\"},\"identifier\":[{\"system\":\"http://foo\",\"value\":\"B1\"}]}",
			"{\"resourceType\":\"Patient\",\"id\":\"C\",\"meta\":{\"versionId\":\"1\"},\"identifier\":[{\"system\":\"http://foo\",\"value\":\"C1\"}]}"
		);
		expected = expected.subList(0, Math.min(expected.size(), theLimitResourceCount));
		assertThat(resources).containsExactly(expected.toArray(new String[0]));

		// Verify text report
		String textReport = report.getReport();
		ourLog.info("Text report:\n{}", textReport);
		assertThat(textReport).contains(
			"Total Resources Changed   : " + expected.size() + " ",
			"Bulk Patch Dry-Run Report"
		);
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, 1, 2, 100})
	public void testBulkPatch_DryRun_DryRunModeCount(int theLimitResourceCount) {
		// Setup
		createPatient(withId("A"), withIdentifier("http://blah", "A1"));
		createPatient(withId("B"), withIdentifier("http://blah", "B1"));
		createPatient(withId("C"), withIdentifier("http://blah", "C1"));
		// This one already has the right system
		createPatient(withId("D"), withIdentifier("http://foo", "D1"));

		// Test
		Parameters patchDocument = createPatchWithModifyPatientIdentifierSystem();
		String jobId = initiateAllPatientJobAndAwaitCompletion(patchDocument, request -> {
			request.setDryRun(true);
			request.setDryRunMode(BaseBulkModifyJobParameters.DryRunMode.COUNT);
			if (theLimitResourceCount >= 0) {
				request.setLimitResourceCount(theLimitResourceCount);
			}
		});

		// Verify

		// Resources on disk should not have been touched
		assertEquals("http://blah", readPatient("Patient/A").getIdentifier().get(0).getSystem());
		assertEquals("http://blah", readPatient("Patient/B").getIdentifier().get(0).getSystem());
		assertEquals("http://blah", readPatient("Patient/C").getIdentifier().get(0).getSystem());
		assertEquals("http://foo", readPatient("Patient/D").getIdentifier().get(0).getSystem());

		String reportJson = myJobCoordinator.getInstance(jobId).getReport();
		BulkModifyResourcesResultsJson report = JsonUtil.deserialize(reportJson, BulkModifyResourcesResultsJson.class);

		int expectedChanged = 3;
		if (theLimitResourceCount >= 0) {
			expectedChanged = Math.min(expectedChanged, theLimitResourceCount);
		}

		// Verify report JSON
		assertThat(report.getResourcesChangedBodies()).isNullOrEmpty();
		assertEquals(expectedChanged, report.getResourcesChangedCount());

		// Verify text report
		String textReport = report.getReport();
		ourLog.info("Text report:\n{}", textReport);
		assertThat(textReport).contains(
			"Total Resources Changed   : " + expectedChanged + " ",
			"Bulk Patch Dry-Run Report"
		);
	}


	@SafeVarargs
	private String initiateAllPatientJobAndAwaitCompletion(Parameters patchDocument, Consumer<BulkPatchJobParameters>... theCustomizers) {
		String jobId = initiateAllPatientJob(patchDocument, theCustomizers);
		myBatch2JobHelper.awaitJobCompletion(jobId);
		return jobId;
	}

	private String initiateAllPatientJobAndAwaitFailure(Parameters patchDocument) {
		String jobId = initiateAllPatientJob(patchDocument);
		myBatch2JobHelper.awaitJobFailure(jobId);
		return jobId;
	}

	@SafeVarargs
	private String initiateAllPatientJob(Parameters patchDocument, Consumer<BulkPatchJobParameters>... theCustomizers) {
		BulkPatchJobParameters jobParameters = new BulkPatchJobParameters();
		jobParameters.addUrl("Patient?_sort=_id");
		jobParameters.setFhirPatch(myFhirContext, patchDocument);

		for (Consumer<BulkPatchJobParameters> nextCustomizer : theCustomizers) {
			nextCustomizer.accept(jobParameters);
		}

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(BulkPatchJobAppCtx.JOB_ID);
		startRequest.setParameters(jobParameters);
		startRequest.setUseCache(false);
		return myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest).getInstanceId();
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
