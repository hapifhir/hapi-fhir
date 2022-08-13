package ca.uhn.fhir.test.utilities;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class CustomMatchersUtil {
	public static <T> void assertDoesNotContainAllOf(List<T> theActual, List<T> theShouldNotContain) {
		for (T item : theShouldNotContain) {
			assertThat(theActual, not(hasItem(item)));
		}
	}
}
