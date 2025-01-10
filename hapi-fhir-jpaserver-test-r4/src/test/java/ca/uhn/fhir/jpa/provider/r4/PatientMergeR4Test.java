package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER_RESPOND_ASYNC;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_PARAM_RESULT_PATIENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatientMergeR4Test extends BaseResourceProviderR4Test {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientMergeR4Test.class);

	@RegisterExtension
	MyExceptionHandler ourExceptionHandler = new MyExceptionHandler();

	@Autowired
	Batch2JobHelper myBatch2JobHelper;
	
	ReplaceReferencesTestHelper myTestHelper;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setDefaultTransactionEntriesForWrite(new JpaStorageSettings().getDefaultTransactionEntriesForWrite());
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStorageSettings.setAllowMultipleDelete(true);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		// keep the version on Provenance.target fields to verify that Provenance resources were saved
		// with versioned target references
		myFhirContext.getParserOptions()
			.setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		myTestHelper.beforeEach();
	}

	@ParameterizedTest(name = "{index}: deleteSource={0}, resultPatient={1}, preview={2}, async={3}")
	@CsvSource({
		// withDelete, withInputResultPatient, withPreview, isAsync
		"true, true, true, false",
		"true, false, true, false",
		"false, true, true, false",
		"false, false, true, false",
		"true, true, false, false",
		"true, false, false, false",
		"false, true, false, false",
		"false, false, false, false",

		"true, true, true, true",
		"true, false, true, true",
		"false, true, true, true",
		"false, false, true, true",
		"true, true, false, true",
		"true, false, false, true",
		"false, true, false, true",
		"false, false, false, true",
	})
	public void testMerge(boolean withDelete, boolean withInputResultPatient, boolean withPreview, boolean isAsync) {
		// setup
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		myTestHelper.setSourceAndTarget(inParams);
		inParams.deleteSource = withDelete;
		if (withInputResultPatient) {
			inParams.resultPatient = myTestHelper.createResultPatient(withDelete);
		}
		if (withPreview) {
			inParams.preview = true;
		}

		Parameters inParameters = inParams.asParametersResource();

		// exec
		Parameters outParams = callMergeOperation(inParameters, isAsync);

		// validate
		// in async mode, there will be an additional task resource in the output params
		assertThat(outParams.getParameter()).hasSizeBetween(3, 4);

		// Assert input
		Parameters input = (Parameters) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_INPUT).getResource();
		if (withInputResultPatient) { // if the following assert fails, check that these two patients are identical
			Patient p1 = (Patient) inParameters.getParameter(OPERATION_MERGE_PARAM_RESULT_PATIENT).getResource();
			Patient p2 = (Patient) input.getParameter(OPERATION_MERGE_PARAM_RESULT_PATIENT).getResource();
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p1));
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p2));
		}
		assertTrue(input.equalsDeep(inParameters));


		List<Identifier> expectedIdentifiersOnTargetAfterMerge =
			myTestHelper.getExpectedIdentifiersForTargetAfterMerge(withInputResultPatient);


		// Assert Task inAsync mode, unless it is preview in which case we don't return a task
		if (isAsync && !withPreview) {
			assertThat(getLastHttpStatusCode()).isEqualTo(HttpServletResponse.SC_ACCEPTED);

			Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
			assertNull(task.getIdElement().getVersionIdPart());
			ourLog.info("Got task {}", task.getId());
			String jobId = myTestHelper.getJobIdFromTask(task);
			myBatch2JobHelper.awaitJobCompletion(jobId);

			Task taskWithOutput = myTaskDao.read(task.getIdElement(), mySrd);
			assertThat(taskWithOutput.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
			ourLog.info("Complete Task: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(taskWithOutput));

			Task.TaskOutputComponent taskOutput = taskWithOutput.getOutputFirstRep();

			// Assert on the output type
			Coding taskType = taskOutput.getType().getCodingFirstRep();
			assertEquals(RESOURCE_TYPES_SYSTEM, taskType.getSystem());
			assertEquals("Bundle", taskType.getCode());

			List<Resource> containedResources = taskWithOutput.getContained();
			assertThat(containedResources)
				.hasSize(1)
				.element(0)
				.isInstanceOf(Bundle.class);

			Bundle containedBundle = (Bundle) containedResources.get(0);

			Reference outputRef = (Reference) taskOutput.getValue();
			Bundle patchResultBundle = (Bundle) outputRef.getResource();
			assertTrue(containedBundle.equalsDeep(patchResultBundle));
			ReplaceReferencesTestHelper.validatePatchResultBundle(patchResultBundle,
				ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES,
				List.of("Observation", "Encounter", "CarePlan"));

			OperationOutcome outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
			assertThat(outcome.getIssue())
				.hasSize(1)
				.element(0)
				.satisfies(issue -> {
					assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
					assertThat(issue.getDetails().getText()).isEqualTo("Merge request is accepted, and will be " +
						"processed asynchronously. See task resource returned in this response for details.");
				});

		} else { // Synchronous case
			// Assert outcome
			OperationOutcome outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();

			if (withPreview) {
				assertThat(outcome.getIssue())
					.hasSize(1)
					.element(0)
					.satisfies(issue -> {
						assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
						assertThat(issue.getDetails().getText()).isEqualTo("Preview only merge operation - no issues detected");
						assertThat(issue.getDiagnostics()).isEqualTo("Merge would update 25 resources");
					});
			} else {
				assertThat(outcome.getIssue())
					.hasSize(1)
					.element(0)
					.satisfies(issue -> {
						assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
						assertThat(issue.getDetails().getText()).isEqualTo("Merge operation completed successfully.");
					});
			}

			// Assert Merged Patient
			Patient mergedPatient = (Patient) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_RESULT).getResource();
			List<Identifier> identifiers = mergedPatient.getIdentifier();

			// TODO ED We can also validate that result patient returned here has the same id as the target patient.
			// And maybe in not preview case, we should also read the target patient from the db and assert it equals to the result returned.
			myTestHelper.assertIdentifiers(identifiers, expectedIdentifiersOnTargetAfterMerge);
		}

		// Check that the linked resources were updated
		if (withPreview) {
			myTestHelper.assertNothingChanged();
		} else {
			myTestHelper.assertAllReferencesUpdated(withDelete);
			myTestHelper.assertSourcePatientUpdatedOrDeleted(withDelete);
			myTestHelper.assertTargetPatientUpdated(withDelete, expectedIdentifiersOnTargetAfterMerge);
			myTestHelper.assertMergeProvenance(withDelete);
		}
	}

	@Test
	void testMerge_smallResourceLimit() {
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		myTestHelper.setSourceAndTarget(inParams);

		inParams.resourceLimit = 5;
		Parameters inParameters = inParams.asParametersResource();

		// exec
		assertThatThrownBy(() -> callMergeOperation(inParameters, false))
			.isInstanceOf(PreconditionFailedException.class)
			.satisfies(ex -> assertThat(extractFailureMessage((BaseServerResponseException) ex)).isEqualTo("HAPI-2597: Number of resources with references to "+ myTestHelper.getSourcePatientId() + " exceeds the resource-limit 5. Submit the request asynchronsly by adding the HTTP Header 'Prefer: respond-async'."));
	}

	@ParameterizedTest(name = "{index}: deleteSource={0}, async={1}")
	@CsvSource({
		"true, false",
		"false, false",
		"true, true",
		"false, true",
	})
	void testMerge_sourceResourceWithoutAnyReference(boolean theDeleteSource, boolean theAsync) {

		Patient sourcePatient = new Patient();
		sourcePatient = (Patient)myPatientDao.create(sourcePatient, mySrd).getResource();


		Patient targetPatient = new Patient();
		targetPatient = (Patient) myPatientDao.create(targetPatient, mySrd).getResource();

		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		inParams.sourcePatient = new Reference(sourcePatient.getIdElement().toVersionless());
		inParams.targetPatient = new Reference(targetPatient.getIdElement().toVersionless());
		if (theDeleteSource) {
			inParams.deleteSource = true;
		}

		Parameters outParams = callMergeOperation(inParams.asParametersResource(), theAsync);

		if (theAsync) {
			assertThat(getLastHttpStatusCode()).isEqualTo(HttpServletResponse.SC_ACCEPTED);
			Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
			assertNull(task.getIdElement().getVersionIdPart());
			ourLog.info("Got task {}", task.getId());
			String jobId = myTestHelper.getJobIdFromTask(task);
			myBatch2JobHelper.awaitJobCompletion(jobId);
		}

		IIdType theExpectedTargetIdWithVersion = targetPatient.getIdElement().withVersion("2");
		if (theDeleteSource) {
			// when the source resource is being deleted and since there is no identifiers to copy over to the target
			// in this test, the target is not actually updated, so its version will remain the same
			theExpectedTargetIdWithVersion = targetPatient.getIdElement().withVersion("1");
		}

		myTestHelper.assertMergeProvenance(theDeleteSource,
			sourcePatient.getIdElement().withVersion("2"),
			theExpectedTargetIdWithVersion,
			0,
			Collections.EMPTY_SET);
	}


	@Test
	void testMerge_SourceResourceCannotBeDeletedBecauseAnotherResourceReferencingSourceWasAddedWhileJobIsRunning_JobFails() {
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		myTestHelper.setSourceAndTarget(inParams);
		inParams.deleteSource = true;
		//using a small batch size that would result in multiple chunks to ensure that
		//the job runs a bit slowly so that we have sometime to add a resource that references the source
		//after the first step
		myStorageSettings.setDefaultTransactionEntriesForWrite(5);
		Parameters inParameters = inParams.asParametersResource();

		// exec
		Parameters outParams = callMergeOperation(inParameters, true);
		Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
		assertNull(task.getIdElement().getVersionIdPart());
		ourLog.info("Got task {}", task.getId());
		String jobId = myTestHelper.getJobIdFromTask(task);

		// wait for first step of the job to finish
		await()
			.until(() -> {
				myBatch2JobHelper.runMaintenancePass();
				String currentGatedStepId = myJobCoordinator.getInstance(jobId).getCurrentGatedStepId();
				return !"query-ids".equals(currentGatedStepId);
			});

		Encounter enc = new Encounter();
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		enc.getSubject().setReferenceElement(myTestHelper.getSourcePatientId());
		myEncounterDao.create(enc, mySrd);

		myBatch2JobHelper.awaitJobFailure(jobId);


		Task taskAfterJobFailure = myTaskDao.read(task.getIdElement().toVersionless(), mySrd);
		assertThat(taskAfterJobFailure.getStatus()).isEqualTo(Task.TaskStatus.FAILED);
	}

	@ParameterizedTest(name = "{index}: deleteSource={0}, resultPatient={1}, preview={2}")
	@CsvSource({
		// withDelete, withInputResultPatient, withPreview
		"true, true, true",
		"true, false, true",
		"false, true, true",
		"false, false, true",
		"true, true, false",
		"true, false, false",
		"false, true, false",
		"false, false, false",
	})
	public void testMultipleTargetMatchesFails(boolean withDelete, boolean withInputResultPatient, boolean withPreview) {
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = myTestHelper.buildMultipleTargetMatchParameters(withDelete, withInputResultPatient, withPreview);

		Parameters inParameters = inParams.asParametersResource();

		assertUnprocessibleEntityWithMessage(inParameters, "Multiple resources found matching the identifier(s) specified in 'target-patient-identifier'");
	}


	@ParameterizedTest(name = "{index}: deleteSource={0}, resultPatient={1}, preview={2}")
	@CsvSource({
		// withDelete, withInputResultPatient, withPreview
		"true, true, true",
		"true, false, true",
		"false, true, true",
		"false, false, true",
		"true, true, false",
		"true, false, false",
		"false, true, false",
		"false, false, false",
	})
	public void testMultipleSourceMatchesFails(boolean withDelete, boolean withInputResultPatient, boolean withPreview) {
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = myTestHelper.buildMultipleSourceMatchParameters(withDelete, withInputResultPatient, withPreview);

		Parameters inParameters = inParams.asParametersResource();

		assertUnprocessibleEntityWithMessage(inParameters, "Multiple resources found matching the identifier(s) specified in 'source-patient-identifier'");
	}

	@Test
	void test_MissingRequiredParameters_Returns400BadRequest() {
		assertThatThrownBy(() -> callMergeOperation(new Parameters())
		).isInstanceOf(InvalidRequestException.class)
			.extracting(InvalidRequestException.class::cast)
			.extracting(BaseServerResponseException::getStatusCode)
			.isEqualTo(400);
	}

	private void assertUnprocessibleEntityWithMessage(Parameters inParameters, String theExpectedMessage) {
		assertThatThrownBy(() ->
			callMergeOperation(inParameters))
			.isInstanceOf(UnprocessableEntityException.class)
			.extracting(UnprocessableEntityException.class::cast)
			.extracting(this::extractFailureMessage)
			.isEqualTo(theExpectedMessage);
	}

	private void callMergeOperation(Parameters inParameters) {
		this.callMergeOperation(inParameters, false);
	}

	private Parameters callMergeOperation(Parameters inParameters, boolean isAsync) {
		IOperationUntypedWithInput<Parameters> request = myClient.operation()
			.onType("Patient")
			.named(OPERATION_MERGE)
			.withParameters(inParameters);

		if (isAsync) {
			request.withAdditionalHeader(HEADER_PREFER, HEADER_PREFER_RESPOND_ASYNC);
		}

		return request
			.returnResourceType(Parameters.class)
			.execute();
	}

	class MyExceptionHandler implements TestExecutionExceptionHandler {
		@Override
		public void handleTestExecutionException(ExtensionContext theExtensionContext, Throwable theThrowable) throws Throwable {
			if (theThrowable instanceof BaseServerResponseException ex) {
				String message = extractFailureMessage(ex);
				throw ex.getClass().getDeclaredConstructor(String.class, Throwable.class).newInstance(message, ex);
			}
			throw theThrowable;
		}
	}

	private @Nonnull String extractFailureMessage(BaseServerResponseException ex) {
		String body = ex.getResponseBody();
		if (body != null) {
			Parameters outParams = myFhirContext.newJsonParser().parseResource(Parameters.class, body);
			OperationOutcome outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
			return outcome.getIssue().stream()
				.map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
				.collect(Collectors.joining(", "));
		} else {
			return "null";
		}
	}

	@Override
	protected boolean verboseClientLogging() {
		return true;
	}
}
