package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.ValidationMessageSuppressingInterceptor;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.assertj.core.api.Assertions.assertThat;

class ValidationMessageSuppressingInterceptorSliceMessageTest {

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private ValidationMessageSuppressingInterceptor myInterceptor;

	@BeforeEach
	void beforeEach(){
		myInterceptor = new ValidationMessageSuppressingInterceptor();
	}

	@Test
	void testMessageSuppression_withNoSliceMessagesAndNoSuppressionText_isUnchanged(){
		SingleValidationMessage singleMessage = createSingleMessage("message");
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		invokeInterceptor(myInterceptor, validationResult);
		assertThat(validationResult.getMessages()).hasSize(1);
		verifySingleMessage(validationResult.getMessages().get(0), "message", List.of());
	}

	@Test
	void testMessageSuppression_withNoSliceMessagesAndSuppressionText_removesSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message");
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("message");
		invokeInterceptor(myInterceptor, validationResult);
		assertThat(validationResult.getMessages()).isEmpty();
	}

	@Test
	void testMessageSuppression_withSingleSliceMessageAndNoSuppressionText_isUnchanged(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1"));
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		invokeInterceptor(myInterceptor, validationResult);
		assertThat(validationResult.getMessages()).hasSize(1);
		verifySingleMessage(validationResult.getMessages().get(0), "message", List.of("slice-message-1"));
	}

	@Test
	void testMessageSuppression_withSingleSliceMessageAndMessageSuppressionText_removeSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1"));
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("message");
		invokeInterceptor(myInterceptor, validationResult);
		assertThat(validationResult.getMessages()).isEmpty();
	}

	@Test
	void testMessageSuppression_withSingleSliceMessageAndSliceSuppressionText_removeSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1"));
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("slice-message-1");
		invokeInterceptor(myInterceptor, validationResult);
		assertThat(validationResult.getMessages()).isEmpty();
	}

	@Test
	void testMessageSuppression_withMultipleSliceMessagesAndNoSuppressionText_isUnchanged(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1", "slice-message-2"));
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		invokeInterceptor(myInterceptor, validationResult);
		assertThat(validationResult.getMessages()).hasSize(1);
		verifySingleMessage(validationResult.getMessages().get(0), "message", List.of("slice-message-1", "slice-message-2"));
	}

	@Test
	void testMessageSuppression_withMultipleSliceMessagesAndMessageSuppressionText_removesSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1", "slice-message-2"));
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("message");
		invokeInterceptor(myInterceptor, validationResult);
		assertThat(validationResult.getMessages()).isEmpty();
	}

	@Test
	void testMessageSuppression_withMultipleSliceMessagesAndSingleSuppressionText_keepsSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1", "slice-message-2"));
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("slice-message-1");
		invokeInterceptor(myInterceptor, validationResult);
		verifySingleMessage(validationResult.getMessages().get(0), "message", List.of("slice-message-2"));
	}

	@Test
	void testMessageSuppression_withMultipleSliceMessagesAndMultipleSuppressionText_removesSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1", "slice-message-2"));
		ValidationResult validationResult = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("slice-message-1", "slice-message-2");
		invokeInterceptor(myInterceptor, validationResult);
		assertThat(validationResult.getMessages()).isEmpty();
	}

	private SingleValidationMessage createSingleMessage(String theMessage){
		return createSingleMessage(theMessage, List.of());
	}

	private SingleValidationMessage createSingleMessage(String theMessage, List<String> theSlicesMessages){
		SingleValidationMessage singleMessage = new SingleValidationMessage();
		singleMessage.setMessage(theMessage);

		if (isNotEmpty(theSlicesMessages)) {
			singleMessage.setSliceMessages(theSlicesMessages);
			assertThat(singleMessage.getSliceMessages()).isNotEmpty();
		} else {
			assertThat(singleMessage.getSliceMessages()).isNullOrEmpty();
		}

		return singleMessage;
	}

	private void invokeInterceptor(ValidationMessageSuppressingInterceptor theInterceptor, ValidationResult theValidationResult){
		// the return value should be null so that we can continue processing other VALIDATION_COMPLETED interceptors
		ValidationResult returnValue = theInterceptor.handle(theValidationResult);
		assertThat(returnValue).isNull();
	}

	private void verifySingleMessage(SingleValidationMessage theActualMessage, String theExpectedMessageText, List<String> theExpectedSliceMessages) {
		assertThat(theActualMessage).isNotNull();
		assertThat(theActualMessage.getMessage()).isEqualTo(theExpectedMessageText);

		int numSliceMessage = theExpectedSliceMessages.size();
		if (numSliceMessage == 0) {
			assertThat(theActualMessage.getSliceMessages()).isNullOrEmpty();
		} else {
			assertThat(theActualMessage.getSliceMessages()).hasSize(numSliceMessage);
			for (int i = 0; i < numSliceMessage; i++) {
				assertThat(theActualMessage.getSliceMessages().get(i)).isEqualTo(theExpectedSliceMessages.get(i));
			}
		}
	}
}
