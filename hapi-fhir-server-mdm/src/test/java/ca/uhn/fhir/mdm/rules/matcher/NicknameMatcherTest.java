package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.jpa.searchparam.matcher.IMdmFieldMatcher;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameServiceFactory;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.NicknameMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NicknameMatcherTest {
	IMdmFieldMatcher matcher;

	NicknameServiceFactory myNicknameServiceFactory = new NicknameServiceFactory();

	@BeforeEach
	public void begin() {
		matcher = new NicknameMatcher(myNicknameServiceFactory.getNicknameSvc());
	}

	@Test
	public void testMatches() {
		Assertions.assertTrue(matcher.matches("Ken", "ken"));
		Assertions.assertTrue(matcher.matches("ken", "Ken"));
		Assertions.assertTrue(matcher.matches("Ken", "Ken"));
		Assertions.assertTrue(matcher.matches("Kenneth", "Ken"));
		Assertions.assertTrue(matcher.matches("Kenneth", "Kenny"));
		Assertions.assertTrue(matcher.matches("Ken", "Kenneth"));
		Assertions.assertTrue(matcher.matches("Kenny", "Kenneth"));
		Assertions.assertTrue(matcher.matches("Jim", "Jimmy"));
		Assertions.assertTrue(matcher.matches("Jimmy", "Jim"));
		Assertions.assertTrue(matcher.matches("Jim", "James"));
		Assertions.assertTrue(matcher.matches("Jimmy", "James"));
		Assertions.assertTrue(matcher.matches("James", "Jimmy"));
		Assertions.assertTrue(matcher.matches("James", "Jim"));

		Assertions.assertFalse(matcher.matches("Ken", "Bob"));
		// These aren't nickname matches.  If you want matches like these use a phonetic matcher
		Assertions.assertFalse(matcher.matches("Allen", "Allan"));
	}
}
