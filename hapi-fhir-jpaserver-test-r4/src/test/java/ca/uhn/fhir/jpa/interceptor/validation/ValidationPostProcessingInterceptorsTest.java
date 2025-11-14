package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.server.interceptor.validation.ValidationMessagePostProcessingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.validation.ValidationMessageSuppressingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.validation.ValidationMessageUnknownCodeSystemPostProcessingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.validation.ValidationPostProcessingRuleJson;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.apache.commons.lang3.ObjectUtils.isEmpty;

public class ValidationPostProcessingInterceptorsTest extends BaseResourceProviderR4Test {

	private static final String SUPPRESSION_MESSAGE = "Best Practice Recommendation: In general, all observations should have a performer";

	private static final IssueSeverity UNKNOWN_CODE_SYSTEM_NEW_SEVERITY_ERROR = IssueSeverity.ERROR;
	private static final String UNKNOWN_CODE_SYSTEM_MESSAGE = "CodeSystem is unknown and can't be validated: http://loinc.org for 'http://loinc.org#8867-4'";

	private static final String POST_PROCESSING_MESSAGE_ID = "All_observations_should_have_a_subject";
	private static final String POST_PROCESSING_MESSAGE = "Best Practice Recommendation: In general, all observations should have a subject";
	private static final ResultSeverityEnum POST_PROCESSING_NEW_SEVERITY_INFO = ResultSeverityEnum.INFORMATION;

	private ValidationMessageSuppressingInterceptor mySuppressingInterceptor;
	private ValidationMessageUnknownCodeSystemPostProcessingInterceptor myUnknownCodeSystemInterceptor;
	private ValidationMessagePostProcessingInterceptor myPostProcessingInterceptor;
	private FirstValidationResultCapturingInterceptor myCapturingInterceptor;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

//		for debugging
//		myFhirContext.getRestfulClientFactory().setSocketTimeout((int) TimeUnit.MINUTES.toMillis(10));

		mySuppressingInterceptor = new ValidationMessageSuppressingInterceptor();
		mySuppressingInterceptor.addMessageSuppressionPatterns(SUPPRESSION_MESSAGE);

		myUnknownCodeSystemInterceptor = new ValidationMessageUnknownCodeSystemPostProcessingInterceptor(UNKNOWN_CODE_SYSTEM_NEW_SEVERITY_ERROR);

		myPostProcessingInterceptor = new ValidationMessagePostProcessingInterceptor();
		myPostProcessingInterceptor.addPostProcessingPatterns(
			new ValidationPostProcessingRuleJson(
				POST_PROCESSING_MESSAGE_ID,
				null,
				List.of(ResultSeverityEnum.WARNING),
				List.of(POST_PROCESSING_MESSAGE),
				POST_PROCESSING_NEW_SEVERITY_INFO
			)
		);

		myCapturingInterceptor = new FirstValidationResultCapturingInterceptor();
	}

	@Test
	void testAllValidationPostProcessingInterceptors(){
		// setup
		myServer.getRestfulServer().getInterceptorService().registerInterceptors(
			List.of(
				mySuppressingInterceptor,
				myUnknownCodeSystemInterceptor,
				myPostProcessingInterceptor,
				myCapturingInterceptor
			)
		);

		Observation observation = buildObservation();

		// execute
		OperationOutcome processed = (OperationOutcome) myClient.validate().resource(observation).execute().getOperationOutcome();

		// verify
		ValidationResult original = myCapturingInterceptor.getOriginalValidationResult();
		verifyValidationMessageSuppressingInterceptor(original, processed);
		verifyValidationMessageUnknownCodeSystemPostProcessingInterceptor(original, processed);
		verifyValidationMessagePostProcessingInterceptor(original, processed);
	}

	private Observation buildObservation() {
		Observation observation = new Observation();

		// issue handled by mySuppressingInterceptor
		observation.addPerformer(null);

		// issue handled by myUnknownCodeSystemInterceptor
		CodeableConcept code = new CodeableConcept(
			new Coding()
				.setSystem("http://loinc.org")
				.setCode("8867-4")
				.setDisplay("Heart rate")
		);
		observation.setCode(code);

		// issue handled by myPostProcessingInterceptor
		observation.setSubject(null);
		return observation;
	}

	private void verifyValidationMessageSuppressingInterceptor(ValidationResult theOriginal, OperationOutcome theProcessed) {
		// original (has issue)
		List<SingleValidationMessage> originalIssues = filterByMessageText(theOriginal.getMessages(), SUPPRESSION_MESSAGE);
		assertThat(originalIssues).hasSize(1);
		assertThat(originalIssues.get(0).getSeverity()).isEqualTo(ResultSeverityEnum.WARNING);

		// post-processing (issue suppressed)
		List<OperationOutcomeIssueComponent> processedIssues = filterByIssueDiagnostic(theProcessed.getIssue(), SUPPRESSION_MESSAGE);
		assertThat(processedIssues).isEmpty();
	}

	private void verifyValidationMessageUnknownCodeSystemPostProcessingInterceptor(ValidationResult theOriginal, OperationOutcome theProcessed) {
		// original (WARNING)
		List<SingleValidationMessage> originalIssues = filterByMessageText(theOriginal.getMessages(), UNKNOWN_CODE_SYSTEM_MESSAGE);
		assertThat(originalIssues).hasSize(1);
		assertThat(originalIssues.get(0).getSeverity()).isEqualTo(ResultSeverityEnum.WARNING);

		// post-processing (ERROR)
		List<OperationOutcomeIssueComponent> processedIssues = filterByIssueDiagnostic(theProcessed.getIssue(), UNKNOWN_CODE_SYSTEM_MESSAGE);
		assertThat(processedIssues).hasSize(1);
		assertThat(processedIssues.get(0).getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.ERROR);
	}

	private void verifyValidationMessagePostProcessingInterceptor(ValidationResult theOriginal, OperationOutcome theProcessed) {
		// original (WARNING)
		List<SingleValidationMessage> originalIssues = filterByMessageText(theOriginal.getMessages(), POST_PROCESSING_MESSAGE);
		assertThat(originalIssues).hasSize(1);
		assertThat(originalIssues.get(0).getSeverity()).isEqualTo(ResultSeverityEnum.WARNING);

		// post-processing (INFORMATION)
		List<OperationOutcomeIssueComponent> processedIssues = filterByIssueDiagnostic(theProcessed.getIssue(), POST_PROCESSING_MESSAGE);
		assertThat(processedIssues).hasSize(1);
		assertThat(processedIssues.get(0).getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
	}

	private List<SingleValidationMessage> filterByMessageText(List<SingleValidationMessage> theMessages, String theMessageText) {
		return theMessages.stream()
			.filter(message -> message.getMessage().contains(theMessageText))
			.toList();
	}

	private List<OperationOutcomeIssueComponent> filterByIssueDiagnostic(List<OperationOutcomeIssueComponent> theIssues,
																		 String theDiagnosticText) {
		return theIssues.stream()
			.filter(issue -> issue.getDiagnostics().contains(theDiagnosticText))
			.toList();
	}

	@Interceptor
	static class FirstValidationResultCapturingInterceptor {

		private ValidationResult myOriginalValidationResult;

		@Hook(value = Pointcut.VALIDATION_COMPLETED, order = -1)
		public ValidationResult captureOriginalValidationResult(ValidationResult theResult) {
			List<SingleValidationMessage> copiedMessages = copyMessages(theResult.getMessages());

			myOriginalValidationResult = new ValidationResult(theResult.getContext(), copiedMessages);
			myOriginalValidationResult.setErrorDisplayLimit(theResult.getErrorDisplayLimit());

			// keep processing
			return null;
		}

		public ValidationResult getOriginalValidationResult() {
			return myOriginalValidationResult;
		}

		private List<SingleValidationMessage> copyMessages(List<SingleValidationMessage> theMessages) {
			if (isEmpty(theMessages)) {
				return Collections.emptyList();
			}

			List<SingleValidationMessage> retVal = new ArrayList<>();
			for (SingleValidationMessage message : theMessages) {
				SingleValidationMessage copy = new SingleValidationMessage(message);
				retVal.add(copy);
			}
			return retVal;
		}
	}
}
