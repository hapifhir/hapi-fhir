package ca.uhn.fhir.test.utilities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static ca.uhn.fhir.test.utilities.CustomMatchersUtil.assertDoesNotContainAnyOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomMatchersUtilTest {
	private List<String> data = List.of("A", "B", "C");

	@Test
	public void testAssertDoesNotContainAllOf_withItemsNotInData() {
		assertDoesNotContainAnyOf(data, List.of("D", "E"));
	}

	@Test
	public void testAssertDoesNotContainAllOf_withItemsInData() {
		assertThrows(AssertionError.class, () -> {
			assertDoesNotContainAnyOf(data, List.of("A", "B"));
		});
	}

	@Test
	public void testAssertDoesNotContainAllOf_withSomeItemsInData() {
		assertThrows(AssertionError.class, () -> {
			assertDoesNotContainAnyOf(data, List.of("A", "E"));
		});
	}
}
