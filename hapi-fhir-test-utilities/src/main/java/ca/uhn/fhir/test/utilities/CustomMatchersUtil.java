package ca.uhn.fhir.test.utilities;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class CustomMatchersUtil {

	/**
	 * Asserts that none of the items in theShouldNotContain are in theActual
	 * @param theActual the actual results
	 * @param theShouldNotContain the items that should not be in theActual
	 */
	public static <T> void assertDoesNotContainAnyOf(List<T> theActual, List<T> theShouldNotContain) {
		for (T item : theShouldNotContain) {
			assertThat(theActual, not(hasItem(item)));
		}
	}
}
