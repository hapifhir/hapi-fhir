package ca.uhn.fhir.test.utilities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static ca.uhn.fhir.test.utilities.CustomMatchersUtil.assertDoesNotContainAnyOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class CustomMatchersUtilTest {
	private List<String> data = List.of("A", "B", "C");

	@Test
	public void testAssertDoesNotContainAllOf_withItemsNotInData() {
		assertDoesNotContainAnyOf(data, List.of("D", "E"));
	}

	@Test
	public void testAssertDoesNotContainAllOf_withItemsInData() {
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
			assertDoesNotContainAnyOf(data, List.of("A", "B"));
		});
	}

	@Test
	public void testAssertDoesNotContainAllOf_withSomeItemsInData() {
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
			assertDoesNotContainAnyOf(data, List.of("A", "E"));
		});
	}
}
