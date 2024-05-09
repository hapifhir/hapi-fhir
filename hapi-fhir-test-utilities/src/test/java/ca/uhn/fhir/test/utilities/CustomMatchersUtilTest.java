package ca.uhn.fhir.test.utilities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CustomMatchersUtilTest {
	private List<String> data = List.of("A", "B", "C");

	@Test
	public void testAssertDoesNotContainAllOf_withItemsNotInData() {
        assertThat(data).doesNotContainAnyElementsOf(List.of("D", "E"));
    }

	@Test
	public void testAssertDoesNotContainAllOf_withItemsInData() {
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
            assertThat(data).doesNotContainAnyElementsOf(List.of("A", "B"));
        });
	}

	@Test
	public void testAssertDoesNotContainAllOf_withSomeItemsInData() {
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
            assertThat(data).doesNotContainAnyElementsOf(List.of("A", "E"));
        });
	}
}
