package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ValidationResultTest {

	private final @Mock FhirContext myFhirContext = mock(FhirContext.class);

	@Test
	void testLessThanDefaultDisplayQty() {
		List<SingleValidationMessage> validationMessages = getTestValidationErrors(2);
		ValidationResult vr = new ValidationResult(myFhirContext, validationMessages);
		String toStringValue = vr.toString();
		assertTrue(toStringValue.contains("Error message #" + 1));
		assertFalse(toStringValue.contains("Error message #" + 2));
	}

	@Test
	void testMoreThanDefaultDisplayQty() {
		List<SingleValidationMessage> validationMessages =
			getTestValidationErrors(ValidationResult.ERROR_DISPLAY_LIMIT_DEFAULT * 2);
		ValidationResult vr = new ValidationResult(myFhirContext, validationMessages);
		String toStringValue = vr.toString();
		assertTrue(toStringValue.contains("Error message #" + ValidationResult.ERROR_DISPLAY_LIMIT_DEFAULT));
		assertFalse(toStringValue.contains("Error message #" + (ValidationResult.ERROR_DISPLAY_LIMIT_DEFAULT + 1)));
	}

	@Test
	void testDisplayLimitSet() {
		List<SingleValidationMessage> validationMessages = getTestValidationErrors(10);
		ValidationResult vr = new ValidationResult(myFhirContext, validationMessages);
		vr.setErrorDisplayLimit(8);
		String toStringValue = vr.toString();
		assertTrue(toStringValue.contains("Error message #" + 8));
		assertFalse(toStringValue.contains("Error message #" + 9));
	}

	private List<SingleValidationMessage> getTestValidationErrors(int theSize) {
		List<SingleValidationMessage> msgList = new ArrayList<>();
		for (int i = 0; i < theSize; i++) {
			SingleValidationMessage validationMsg = new SingleValidationMessage();
			validationMsg.setLocationCol(i);
			validationMsg.setLocationLine(1);
			validationMsg.setLocationString("Error #" + (i+1));
			validationMsg.setMessage("Error message #" + (i+1));
			validationMsg.setSeverity(ResultSeverityEnum.ERROR);
			msgList.add(validationMsg);
		}
		return msgList;
	}

}
