package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.provider.merge.PatientUndoMergeOperationInputParameters;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.gclient.IOperationUntypedWithInput;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.RESOURCE_TYPES_EXPECTED_TO_BE_PATCHED;
import static ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesLargeTestData.TOTAL_EXPECTED_PATCHES;
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



	private void validateSyncOutcome(Parameters theOutParams) {
		// Assert outcome
		OperationOutcome outcome = (OperationOutcome) theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		assertThat(outcome.getIssue())
			.hasSize(1)
			.element(0)
			.satisfies(issue -> {
				assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
				assertThat(issue.getDetails().getText()).isEqualTo("Merge operation completed successfully.");
			});

		// In sync mode, the result patient is returned in the output,
		// assert what is returned is the same as the one in the db
		Patient targetPatientInOutput = (Patient) theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_RESULT).getResource();
		Patient targetPatientReadFromDB = myTestHelper.readPatient(myLargeTestData.getTargetPatientId());
		IParser parser = myFhirContext.newJsonParser();
		assertThat(parser.encodeResourceToString(targetPatientInOutput)).isEqualTo(parser.encodeResourceToString(targetPatientReadFromDB));
	}


	@Test
	void testUndoMerge(){
		// setup
		myLargeTestData.createTestResources();
		ReplaceReferencesTestHelper.PatientMergeInputParameters inParams = new ReplaceReferencesTestHelper.PatientMergeInputParameters();
		myTestHelper.setSourceAndTarget(inParams, myLargeTestData);

		Parameters inParameters = inParams.asParametersResource();

		// exec
		Parameters outParams = myTestHelper.callMergeOperation(myClient, inParameters, false);




		List<Identifier> expectedIdentifiersOnTargetAfterMerge =
			myLargeTestData.getExpectedIdentifiersForTargetAfterMerge(withInputResultPatient);


		validateSyncOutcome(outParams);

		// Check that the linked resources were updated
		myTestHelper.assertAllReferencesUpdated(true, withDelete, myLargeTestData);
		myTestHelper.assertSourcePatientUpdatedOrDeletedAfterMerge(myLargeTestData.getSourcePatientId(), myLargeTestData.getTargetPatientId(), withDelete);
		myTestHelper.assertTargetPatientUpdatedAfterMerge(myLargeTestData.getTargetPatientId(), myLargeTestData.getSourcePatientId(), withDelete, expectedIdentifiersOnTargetAfterMerge);
		myTestHelper.assertMergeProvenance(withDelete, myLargeTestData,  null);
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
