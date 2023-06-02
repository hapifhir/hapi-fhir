package ca.uhn.fhir.mdm.rules.matcher;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NicknameMatcherTest {
	IMdmStringMatcher matcher = new NicknameMatcher();

	@Test
	public void testMatches() {
		assertTrue(matcher.matches("Ken", "ken"));
		assertTrue(matcher.matches("ken", "Ken"));
		assertTrue(matcher.matches("Ken", "Ken"));
		assertTrue(matcher.matches("Kenneth", "Ken"));
		assertTrue(matcher.matches("Kenneth", "Kenny"));
		assertTrue(matcher.matches("Ken", "Kenneth"));
		assertTrue(matcher.matches("Kenny", "Kenneth"));
		assertTrue(matcher.matches("Jim", "Jimmy"));
		assertTrue(matcher.matches("Jimmy", "Jim"));
		assertTrue(matcher.matches("Jim", "James"));
		assertTrue(matcher.matches("Jimmy", "James"));
		assertTrue(matcher.matches("James", "Jimmy"));
		assertTrue(matcher.matches("James", "Jim"));

		assertFalse(matcher.matches("Ken", "Bob"));
		// These aren't nickname matches.  If you want matches like these use a phonetic matcher
		assertFalse(matcher.matches("Allen", "Allan"));
	}
}
