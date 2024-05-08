package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.EmptyFieldMatcher;
import ca.uhn.fhir.model.primitive.StringDt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmptyFieldMatcherTest extends BaseMatcherR4Test {

	@Test
	public void testEmptyFieldMatch() {
		StringDt left = new StringDt("other value");
		StringDt leftEmpty = new StringDt("");
		StringDt rightEmpty = new StringDt("");
		StringDt right = new StringDt("a value");

		EmptyFieldMatcher fieldMatch = new EmptyFieldMatcher();

		assertTrue(fieldMatch.matches(null, null, null));
		assertTrue(fieldMatch.matches(null, rightEmpty, null));
		assertTrue(fieldMatch.matches(leftEmpty, null, null));
		assertTrue(fieldMatch.matches(leftEmpty, rightEmpty, null));
		assertFalse(fieldMatch.matches(null, right, null));
		assertFalse(fieldMatch.matches(left, null, null));
		assertFalse(fieldMatch.matches(left, right, null));
	}


}
