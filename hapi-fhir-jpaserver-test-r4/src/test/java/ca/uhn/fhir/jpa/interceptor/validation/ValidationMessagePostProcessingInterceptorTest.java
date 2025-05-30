package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.ValidationMessagePostProcessingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.validation.ValidationPostProcessingRuleJson;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationMessagePostProcessingInterceptorTest {

	private final FhirContext myFhirCtx = FhirContext.forR4Cached();

	private final ValidationMessagePostProcessingInterceptor testedInterceptor = new ValidationMessagePostProcessingInterceptor();

	@Test
	void handle() {
		ValidationPostProcessingRuleJson jsonRule1 = new ValidationPostProcessingRuleJson(
			"Terminology_TX_Error_CodeableConcept",
			null,
			List.of(ResultSeverityEnum.ERROR),
			List.of("Failed to parse response from server", "SocketTimeoutException"),
			ResultSeverityEnum.FATAL);

		ValidationPostProcessingRuleJson jsonRule2 = new ValidationPostProcessingRuleJson(
			null,
			"Terminology_TX_Binding.*",
			List.of(ResultSeverityEnum.INFORMATION, ResultSeverityEnum.WARNING),
			List.of("detail-1", "detail-2"),
			ResultSeverityEnum.ERROR);

		ValidationPostProcessingRuleJson jsonRule3 = new ValidationPostProcessingRuleJson(
			null,
			"Terminology_TX.*",
			List.of(ResultSeverityEnum.INFORMATION),
			null,
			ResultSeverityEnum.WARNING);

		testedInterceptor.addPostProcessingPatterns(jsonRule1, jsonRule2, jsonRule3);

		ValidationResult validationResult = buildValidationResult();

		// execute
		ValidationResult postProcessedResult = testedInterceptor.handle(validationResult);

		ValidationResult expectedMessageResult = buildExpectedPostProcessedResult();
		assertThat(postProcessedResult.getMessages()).hasSameElementsAs(expectedMessageResult.getMessages());
	}

	private ValidationResult buildExpectedPostProcessedResult() {
		ValidationResult expectedValidationResult = buildValidationResult();

		expectedValidationResult.getMessages().get(3).setSeverity(ResultSeverityEnum.FATAL);
		expectedValidationResult.getMessages().get(7).setSeverity(ResultSeverityEnum.ERROR);
		expectedValidationResult.getMessages().get(8).setSeverity(ResultSeverityEnum.ERROR);

		return expectedValidationResult;
	}

	private ValidationResult buildValidationResult() {
		// doesn't match message id
		SingleValidationMessage message0 = new SingleValidationMessage();
		message0.setMessageId("msg-id-1");
		message0.setSeverity(ResultSeverityEnum.FATAL);

		// doesn't match severity
		SingleValidationMessage message1 = new SingleValidationMessage();
		message1.setMessageId("Terminology_TX_Error_CodeableConcept");
		message1.setSeverity(ResultSeverityEnum.WARNING);

		// doesn't match second fragment
		SingleValidationMessage message2 = new SingleValidationMessage();
		message2.setMessageId("Terminology_TX_Error_CodeableConcept");
		message2.setSeverity(ResultSeverityEnum.ERROR);
		message2.setMessage("Error HAPI-1361: Failed to parse response from server when performing POST");

		// matches rule 1 - must be updated
		SingleValidationMessage message3 = new SingleValidationMessage();
		message3.setMessageId("Terminology_TX_Error_CodeableConcept");
		message3.setSeverity(ResultSeverityEnum.ERROR);
		message3.setMessage("Error HAPI-1361: Failed to parse response from server when performing POST to URL ... - java.net.SocketTimeoutException: Read timed out validating CodeableConcept");

		// doesn't match message id
		SingleValidationMessage message4 = new SingleValidationMessage();
		message4.setMessageId("Terminology_TX_Confirm_2_CC");
		message4.setSeverity(ResultSeverityEnum.ERROR);
		message4.setMessage("Some error took place");

		// doesn't match severity
		SingleValidationMessage message5 = new SingleValidationMessage();
		message5.setMessageId("Terminology_TX_Binding_CantCheck");
		message5.setSeverity(ResultSeverityEnum.FATAL);
		message5.setMessage("Some error took place when fetching ValueSet");

		// doesn't match fragment
		SingleValidationMessage message6 = new SingleValidationMessage();
		message6.setMessageId("Terminology_TX_Binding_CantCheck");
		message6.setSeverity(ResultSeverityEnum.WARNING);
		message6.setMessage("Some error took place when fetching ValueSet - detail-2 detail-3");

		// matches rule 2 - must be updated
		SingleValidationMessage message7 = new SingleValidationMessage();
		message7.setMessageId("Terminology_TX_Binding_CantCheck");
		message7.setSeverity(ResultSeverityEnum.WARNING);
		message7.setMessage("Some error took place when fetching ValueSet - detail-2 detail-1");

		// matches both rules 2 and 3 - must be updated by rule 2
		SingleValidationMessage message8 = new SingleValidationMessage();
		message8.setMessageId("Terminology_TX_Binding_CantCheck");
		message8.setSeverity(ResultSeverityEnum.INFORMATION);
		message8.setMessage("Some error took place when fetching ValueSet - detail-2 detail-1");

		return new ValidationResult(myFhirCtx, List.of(
			message0, message1, message2, message3, message4, message5, message6, message7, message8));
	}
}
