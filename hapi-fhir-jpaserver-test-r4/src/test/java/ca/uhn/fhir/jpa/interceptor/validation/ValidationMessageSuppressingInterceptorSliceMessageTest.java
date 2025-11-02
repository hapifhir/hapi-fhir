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
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled).isNull(); // unchanged
	}

	@Test
	void testMessageSuppression_withNoSliceMessagesAndSuppressionText_removesSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message");
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("message");
		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled.getMessages()).isEmpty();
	}

	@Test
	void testMessageSuppression_withSingleSliceMessageAndNoSuppressionText_isUnchanged(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1"));
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled).isNull(); // unchanged
	}

	@Test
	void testMessageSuppression_withSingleSliceMessageAndMessageSuppressionText_removeSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1"));
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("message");
		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled.getMessages()).isEmpty();
	}

	@Test
	void testMessageSuppression_withSingleSliceMessageAndSliceSuppressionText_removeSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1"));
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("slice-message-1");
		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled.getMessages()).isEmpty();

		// Verify original ValidationResult is unchanged
		assertThat(original.getMessages()).hasSize(1);
		assertThat(original.getMessages().get(0).getSliceMessages()).hasSize(1);
		assertThat(original.getMessages().get(0).getSliceMessages().get(0)).isEqualTo("slice-message-1");
	}

	@Test
	void testMessageSuppression_withMultipleSliceMessagesAndNoSuppressionText_isUnchanged(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1", "slice-message-2"));
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled).isNull(); // unchanged
	}

	@Test
	void testMessageSuppression_withMultipleSliceMessagesAndMessageSuppressionText_removesSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1", "slice-message-2"));
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("message");
		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled.getMessages()).isEmpty();
	}

	@Test
	void testMessageSuppression_withMultipleSliceMessagesAndSingleSuppressionText_keepsSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1", "slice-message-2"));
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("slice-message-1");
		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled.getMessages()).hasSize(1);
		assertThat(handled.getMessages().get(0).getMessage()).isEqualTo("message");
		assertThat(handled.getMessages().get(0).getSliceMessages()).hasSize(1);
		assertThat(handled.getMessages().get(0).getSliceMessages().get(0)).isEqualTo("slice-message-2");

		// Verify original ValidationResult is unchanged (still has 2 slice messages)
		assertThat(original.getMessages()).hasSize(1);
		assertThat(original.getMessages().get(0).getSliceMessages()).hasSize(2);
		assertThat(original.getMessages().get(0).getSliceMessages()).containsExactly("slice-message-1", "slice-message-2");
	}

	@Test
	void testMessageSuppression_withMultipleSliceMessagesAndMultipleSuppressionText_removesSingleMessage(){
		SingleValidationMessage singleMessage = createSingleMessage("message", List.of("slice-message-1", "slice-message-2"));
		ValidationResult original = new ValidationResult(ourFhirContext, List.of(singleMessage));

		myInterceptor.addMessageSuppressionPatterns("slice-message-1", "slice-message-2");
		ValidationResult handled = myInterceptor.handle(original);
		assertThat(handled.getMessages()).isEmpty();

		// Verify original ValidationResult is unchanged (still has 2 slice messages)
		assertThat(original.getMessages()).hasSize(1);
		assertThat(original.getMessages().get(0).getSliceMessages()).hasSize(2);
		assertThat(original.getMessages().get(0).getSliceMessages()).containsExactly("slice-message-1", "slice-message-2");
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
}
