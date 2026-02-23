package ca.uhn.fhir.validation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class SingleValidationMessageTest {

	@Test
	void testCopyConstructor_copiesAllFields() {
		// Setup
		SingleValidationMessage original = new SingleValidationMessage();
		original.setLocationCol(10);
		original.setLocationLine(20);
		original.setLocationString("Patient.name");
		original.setMessage("Validation error");
		original.setMessageId("msg-123");
		original.setSeverity(ResultSeverityEnum.ERROR);

		List<String> sliceMessages = new ArrayList<>();
		sliceMessages.add("Slice message 1");
		sliceMessages.add("Slice message 2");
		original.setSliceMessages(sliceMessages);

		// Execute
		SingleValidationMessage copy = new SingleValidationMessage(original);

		// Verify
		assertEquals(original.getLocationCol(), copy.getLocationCol());
		assertEquals(original.getLocationLine(), copy.getLocationLine());
		assertEquals(original.getLocationString(), copy.getLocationString());
		assertEquals(original.getMessage(), copy.getMessage());
		assertEquals(original.getMessageId(), copy.getMessageId());
		assertEquals(original.getSeverity(), copy.getSeverity());
		assertEquals(original.getSliceMessages(), copy.getSliceMessages());
	}

	@Test
	void testCopyConstructor_deepCopiesSliceMessages() {
		// Setup
		SingleValidationMessage original = new SingleValidationMessage();
		List<String> sliceMessages = new ArrayList<>();
		sliceMessages.add("Slice message 1");
		sliceMessages.add("Slice message 2");
		original.setSliceMessages(sliceMessages);

		// Execute
		SingleValidationMessage copy = new SingleValidationMessage(original);

		// Verify slice messages list is a deep copy (not same instance)
		assertNotSame(original.getSliceMessages(), copy.getSliceMessages());
		assertEquals(original.getSliceMessages(), copy.getSliceMessages());

		// Modify original slice messages and verify copy is not affected
		original.getSliceMessages().add("Slice message 3");
		assertThat(copy.getSliceMessages()).hasSize(2);
		assertThat(original.getSliceMessages()).hasSize(3);
	}

	@Test
	void testCopyConstructor_handlesNullSliceMessages() {
		// Setup
		SingleValidationMessage original = new SingleValidationMessage();
		original.setLocationCol(5);
		original.setMessage("Error message");
		original.setSliceMessages(null);

		// Execute
		SingleValidationMessage copy = new SingleValidationMessage(original);

		// Verify
		assertNull(copy.getSliceMessages());
		assertEquals(original.getLocationCol(), copy.getLocationCol());
		assertEquals(original.getMessage(), copy.getMessage());
	}

	@Test
	void testCopyConstructor_copiesEmptySliceMessages() {
		// Setup
		SingleValidationMessage original = new SingleValidationMessage();
		original.setSliceMessages(new ArrayList<>());

		// Execute
		SingleValidationMessage copy = new SingleValidationMessage(original);

		// Verify
		assertNotSame(original.getSliceMessages(), copy.getSliceMessages());
		assertThat(copy.getSliceMessages()).isEmpty();
	}

	@Test
	void testCopyConstructor_copiedObjectIsEqual() {
		// Setup
		SingleValidationMessage original = new SingleValidationMessage();
		original.setLocationCol(15);
		original.setLocationLine(25);
		original.setLocationString("Observation.value");
		original.setMessage("Value is required");
		original.setMessageId("obs-123");
		original.setSeverity(ResultSeverityEnum.WARNING);

		// Execute
		SingleValidationMessage copy = new SingleValidationMessage(original);

		// Verify
		assertEquals(original, copy);
	}
}
