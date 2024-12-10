package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.r4.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
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
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.r4.replacereferences.ReplaceReferencesTestHelper.EXPECTED_SMALL_BATCHES;
import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER;
import static ca.uhn.fhir.rest.api.Constants.HEADER_PREFER_RESPOND_ASYNC;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_RESULT_PATIENT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK;
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
	
	ReplaceReferencesTestHelper myTestHelper;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
			}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
		myStorageSettings.setAllowMultipleDelete(true);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myClient, myDaoRegistry);
		myTestHelper.beforeEach();
	}

	@ParameterizedTest
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
	public void testMergeWithoutResult(boolean withDelete, boolean withInputResultPatient, boolean withPreview, boolean isAsync) throws Exception {
		// setup

		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		myTestHelper.setSourceAndTarget(inParams);
		inParams.deleteSource = withDelete;
		if (withInputResultPatient) {
			myTestHelper.setResultPatient(inParams);
		}
		if (withPreview) {
			inParams.preview = true;
		}

		Parameters inParameters = inParams.asParametersResource();

		// exec
		Parameters outParams = callMergeOperation(inParameters, isAsync);

		// validate
		assertThat(outParams.getParameter()).hasSize(3);

		// Assert input
		Parameters input = (Parameters) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_INPUT).getResource();
		if (withInputResultPatient) { // if the following assert fails, check that these two patients are identical
			Patient p1 = (Patient) inParameters.getParameter(OPERATION_MERGE_RESULT_PATIENT).getResource();
			Patient p2 = (Patient) input.getParameter(OPERATION_MERGE_RESULT_PATIENT).getResource();
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p1));
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(p2));
		}
		assertTrue(input.equalsDeep(inParameters));


		// Assert Task
		if (isAsync) {
			Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
			assertNull(task.getIdElement().getVersionIdPart());
			ourLog.info("Got task {}", task.getId());
			await().until(() -> taskCompleted(task.getIdElement()));

			Task taskWithOutput = myTaskDao.read(task.getIdElement(), mySrd);
			ourLog.info("Complete Task: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(taskWithOutput));

			// FIXME KHS the rest of these asserts will likely need to be tweaked
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
			validatePatchResultBundle(patchResultBundle, ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES);
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
			if (withInputResultPatient) {
				assertThat(identifiers).hasSize(1);
				assertThat(identifiers.get(0).getSystem()).isEqualTo("SYS1A");
				assertThat(identifiers.get(0).getValue()).isEqualTo("VAL1A");
			} else {
				assertThat(identifiers).hasSize(5);
				assertThat(identifiers)
					.extracting(Identifier::getSystem)
					.containsExactlyInAnyOrder("SYS1A", "SYS1B", "SYS2A", "SYS2B", "SYSC");
				assertThat(identifiers)
					.extracting(Identifier::getValue)
					.containsExactlyInAnyOrder("VAL1A", "VAL1B", "VAL2A", "VAL2B", "VALC");
			}
			if (!withPreview && !withDelete) {
				// assert source has link to target
				Patient source = myTestHelper.readSourcePatient();
				assertThat(source.getLink())
					.hasSize(1)
					.element(0)
					.extracting(link -> link.getOther().getReferenceElement())
					.isEqualTo(myTestHelper.getTargetPatientId());
			}
		}

		// Check that the linked resources were updated

		Bundle bundle = myTestHelper.getTargetEverythingBundle();

		assertNull(bundle.getLink("next"));

		Set<IIdType> actual = new HashSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless());
		}

		ourLog.info("Found IDs: {}", actual);

		if (withPreview) {
			myTestHelper.assertNothingChanged(actual);
		} else {
			myTestHelper.assertContainsAllResources(actual, withDelete);
		}
	}

	private Boolean taskCompleted(IdType theTaskId) {
		Task updatedTask = myTaskDao.read(theTaskId, mySrd);
		ourLog.info("Task {} status is {}", theTaskId, updatedTask.getStatus());
		return updatedTask.getStatus() == Task.TaskStatus.COMPLETED;
	}

	@ParameterizedTest
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


	@ParameterizedTest
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

	@Test
	void test_MissingRequiredParameters_Returns400BadRequest() {
		assertThatThrownBy(() -> callMergeOperation(new Parameters())
		).isInstanceOf(InvalidRequestException.class)
			.extracting(InvalidRequestException.class::cast)
			.extracting(BaseServerResponseException::getStatusCode)
			.isEqualTo(400);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferences(boolean isAsync) throws IOException {
		// exec
		Parameters outParams = myTestHelper.callReplaceReferences(isAsync);

		assertThat(outParams.getParameter()).hasSize(1);

		Bundle patchResultBundle;
		if (isAsync) {
			Task task = (Task) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK).getResource();
			assertNull(task.getIdElement().getVersionIdPart());
			ourLog.info("Got task {}", task.getId());
			await().until(() -> taskCompleted(task.getIdElement()));

			Task taskWithOutput = myTaskDao.read(task.getIdElement(), mySrd);
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
			patchResultBundle = (Bundle) outputRef.getResource();
			assertTrue(containedBundle.equalsDeep(patchResultBundle));
		} else {
			patchResultBundle = (Bundle) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_OUTCOME).getResource();
		}

		// validate
		validatePatchResultBundle(patchResultBundle, ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES);

		// Check that the linked resources were updated

		validateLinksUsingEverything();
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	void testReplaceReferencesSmallBatchSize(boolean isAsync) throws IOException {
		// exec
		Parameters outParams = myTestHelper.callReplaceReferencesWithBatchSize(isAsync, ReplaceReferencesTestHelper.SMALL_BATCH_SIZE);


		assertThat(outParams.getParameter()).hasSize(1);

		Bundle patchResultBundle;
		Task task = (Task) outParams.getParameter(OPERATION_REPLACE_REFERENCES_OUTPUT_PARAM_TASK).getResource();
		assertNull(task.getIdElement().getVersionIdPart());
		ourLog.info("Got task {}", task.getId());
		await().until(() -> taskCompleted(task.getIdElement()));

		Task taskWithOutput = myTaskDao.read(task.getIdElement(), mySrd);
		ourLog.info("Complete Task: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(taskWithOutput));

		assertThat(taskWithOutput.getOutput()).hasSize(EXPECTED_SMALL_BATCHES);
		List<Resource> containedResources = taskWithOutput.getContained();

		assertThat(containedResources)
			.hasSize(EXPECTED_SMALL_BATCHES)
			.element(0)
			.isInstanceOf(Bundle.class);

		int entriesLeft = ReplaceReferencesTestHelper.TOTAL_EXPECTED_PATCHES;
		for (int i = 1; i < EXPECTED_SMALL_BATCHES; i++) {

			Task.TaskOutputComponent taskOutput = taskWithOutput.getOutput().get(i);

			// Assert on the output type
			Coding taskType = taskOutput.getType().getCodingFirstRep();
			assertEquals(RESOURCE_TYPES_SYSTEM, taskType.getSystem());
			assertEquals("Bundle", taskType.getCode());

			Bundle containedBundle = (Bundle) containedResources.get(i);

			Reference outputRef = (Reference) taskOutput.getValue();
			patchResultBundle = (Bundle) outputRef.getResource();
			assertTrue(containedBundle.equalsDeep(patchResultBundle));

			// validate
			entriesLeft -= ReplaceReferencesTestHelper.SMALL_BATCH_SIZE;
			int expectedNumberOfEntries = Math.min(entriesLeft, ReplaceReferencesTestHelper.SMALL_BATCH_SIZE);
			validatePatchResultBundle(patchResultBundle, expectedNumberOfEntries);
		}

		// Check that the linked resources were updated

		validateLinksUsingEverything();
	}

	private void validateLinksUsingEverything() throws IOException {
		Bundle everythingBundle = myTestHelper.getTargetEverythingBundle();

		assertNull(everythingBundle.getLink("next"));

		Set<IIdType> actual = new HashSet<>();
		for (BundleEntryComponent nextEntry : everythingBundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless());
		}

		ourLog.info("Found IDs: {}", actual);

		myTestHelper.assertContainsAllResources(actual, false);
	}

	private static void validatePatchResultBundle(Bundle patchResultBundle, int theTotalExpectedPatches) {
		Pattern expectedPatchIssuePattern = Pattern.compile("Successfully patched resource \"(Observation|Encounter|CarePlan)/\\d+/_history/\\d+\".");
		assertThat(patchResultBundle.getEntry()).hasSize(theTotalExpectedPatches)
			.allSatisfy(entry ->
				assertThat(entry.getResponse().getOutcome())
					.isInstanceOf(OperationOutcome.class)
					.extracting(OperationOutcome.class::cast)
					.extracting(OperationOutcome::getIssue)
					.satisfies(issues ->
						assertThat(issues).hasSize(1)
							.element(0)
							.extracting(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
							.satisfies(diagnostics -> assertThat(diagnostics).matches(expectedPatchIssuePattern))));
	}

	// FIXME KHS look at PatientEverythingR4Test for ideas for other tests

	class MyExceptionHandler implements TestExecutionExceptionHandler {
		@Override
		public void handleTestExecutionException(ExtensionContext theExtensionContext, Throwable theThrowable) throws Throwable {
			if (theThrowable instanceof BaseServerResponseException) {
				BaseServerResponseException ex = (BaseServerResponseException) theThrowable;
				String message = extractFailureMessage(ex);
				throw ex.getClass().getDeclaredConstructor(String.class, Throwable.class).newInstance(message, ex);
			}
			throw theThrowable;
		}
	}

	private @Nonnull String extractFailureMessage(BaseServerResponseException ex) {
		String body = ex.getResponseBody();
		Parameters outParams = myFhirContext.newJsonParser().parseResource(Parameters.class, body);
		OperationOutcome outcome = (OperationOutcome) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		return outcome.getIssue().stream()
			.map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
			.collect(Collectors.joining(", "));
	}
}
