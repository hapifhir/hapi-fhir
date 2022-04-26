package ca.uhn.fhir.mdm.rules.matcher;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NicknameMatcherTest {
	IMdmStringMatcher matcher = new NicknameMatcher();

	@Test
	public void testMatches() {
		assertTrue(matcher.matches("ken", "ken"));
		assertTrue(matcher.matches("kenneth", "ken"));
		assertTrue(matcher.matches("ken", "kenneth"));

		assertFalse(matcher.matches("ken", "bob"));
		// These aren't nickname matches.  If you want matches like these use a phonetic matcher
		assertFalse(matcher.matches("allen", "allan"));
	}
}
