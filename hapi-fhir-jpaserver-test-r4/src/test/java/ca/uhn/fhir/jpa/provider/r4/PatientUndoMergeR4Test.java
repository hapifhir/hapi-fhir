package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PatientUndoMergeR4Test extends BaseResourceProviderR4Test {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientUndoMergeR4Test.class);

	@RegisterExtension
	MyExceptionHandler ourExceptionHandler = new MyExceptionHandler();


	ReplaceReferencesTestHelper myTestHelper;

	ReplaceReferencesLargeTestData myLargeTestData;

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
		// we need to keep the version on Provenance.target fields to
		// verify that Provenance resources were saved with versioned target references
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		myLargeTestData = new ReplaceReferencesLargeTestData(myDaoRegistry);
	}



	private void validateSuccessOutcome(Parameters theOutParams, int theExpectedResourceCount) {
		// Assert outcome
		OperationOutcome outcome = (OperationOutcome) theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		assertThat(outcome.getIssue())
			.hasSize(1)
			.element(0)
			.satisfies(issue -> {
				assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
				String detailsTxt = issue.getDetails().getText();
				assertThat(detailsTxt).matches(format("Successfully restored %d resources to their previous versions based on the Provenance resource: Provenance/[0-9]+/_history/1", theExpectedResourceCount));
			});
	}


	@ParameterizedTest
	@CsvSource(
		value = {
			"true",
			"false"
		})
	void testUndoMerge(boolean theDeleteSource){
		// setup
		myLargeTestData.createTestResources();

		Patient sourceBeforeMerge = myTestHelper.readPatient(myLargeTestData.getSourcePatientId());
		Patient targetBeforeMerge = myTestHelper.readPatient(myLargeTestData.getTargetPatientId());

		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		myTestHelper.setSourceAndTarget(inParams, myLargeTestData);
		if (theDeleteSource) {
			inParams.deleteSource = true;
		}

		Parameters inParametersMerge = inParams.asParametersResource();

		// exec
		 myTestHelper.callMergeOperation(myClient, inParametersMerge, false);

		 Parameters inParametersUndoMerge = inParams.asUndoParametersResource();
		 Parameters outParams = myTestHelper.callUndoMergeOperation(myClient, inParametersUndoMerge);
		 validateSuccessOutcome(outParams, TOTAL_EXPECTED_PATCHES + 2);


		Patient sourceAfterUnmerge = myTestHelper.readPatient(myLargeTestData.getSourcePatientId());
		Patient targetAfterUnmerge = myTestHelper.readPatient(myLargeTestData.getTargetPatientId());
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(sourceBeforeMerge, sourceAfterUnmerge);
		assertResourcesAreEqualIgnoringVersionAndLastUpdated(targetBeforeMerge, targetAfterUnmerge);

		myTestHelper.assertReferencesHaveNotChanged(myLargeTestData);

	}

	private void assertResourcesAreEqualIgnoringVersionAndLastUpdated(Patient theBefore, Patient theAfter) {

		// the resources should have the same versionless id
		assertThat(theBefore.getIdElement().toVersionless()).isEqualTo(theAfter.getIdElement().toVersionless());

		//create a copy of the before since we will modify some of its meta data to match the after resource
		Patient copyOfTheBefore = theBefore.copy();

		copyOfTheBefore.getMeta().setLastUpdated(theAfter.getMeta().getLastUpdated());
		copyOfTheBefore.getMeta().setVersionId(theAfter.getMeta().getVersionId());
		copyOfTheBefore.getMeta().setSource(theAfter.getMeta().getSource());
		copyOfTheBefore.setId(theAfter.getIdElement());

		String before = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(copyOfTheBefore);
		String after = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(theAfter);
		assertThat(after).isEqualTo(before);
		// compare using the equalsDeep method as well, just to be sure
		assertThat(theAfter.equalsDeep(copyOfTheBefore)).isTrue();
	}

	@Test
	void test_MissingRequiredParameters_Returns400BadRequest() {
		Parameters params = new Parameters();
		assertThatThrownBy(() -> myTestHelper.callMergeOperation(myClient, params, false))
			.isInstanceOf(InvalidRequestException.class)
			.extracting(InvalidRequestException.class::cast)
			.extracting(BaseServerResponseException::getStatusCode)
			.isEqualTo(400);
	}

	private void assertUnprocessibleEntityWithMessage(Parameters inParameters, String theExpectedMessage) {
		assertThatThrownBy(() -> myTestHelper.callMergeOperation(myClient, inParameters, false))
			.isInstanceOf(UnprocessableEntityException.class)
			.extracting(UnprocessableEntityException.class::cast)
			.extracting(this::extractFailureMessage)
			.isEqualTo(theExpectedMessage);
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
