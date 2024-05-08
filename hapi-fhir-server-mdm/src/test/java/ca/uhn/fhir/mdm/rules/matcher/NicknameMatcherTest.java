package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.jpa.nickname.INicknameSvc;
import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.NicknameMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NicknameMatcherTest {
	IMdmFieldMatcher matcher;

	INicknameSvc myNicknameSvc = new NicknameSvc();

	@BeforeEach
	public void begin() {
		matcher = new NicknameMatcher(myNicknameSvc);
	}

	@Test
	public void testMatches() {
		assertTrue(match("Ken", "ken"));
		assertTrue(match("ken", "Ken"));
		assertTrue(match("Ken", "Ken"));
		assertTrue(match("Kenneth", "Ken"));
		assertTrue(match("Kenneth", "Kenny"));
		assertTrue(match("Ken", "Kenneth"));
		assertTrue(match("Kenny", "Kenneth"));
		assertTrue(match("Jim", "Jimmy"));
		assertTrue(match("Jimmy", "Jim"));
		assertTrue(match("Jim", "James"));
		assertTrue(match("Jimmy", "James"));
		assertTrue(match("James", "Jimmy"));
		assertTrue(match("James", "Jim"));

		assertFalse(match("Ken", "Bob"));
		// These aren't nickname matches.  If you want matches like these use a phonetic matcher
		assertFalse(match("Allen", "Allan"));
	}

	private boolean match(String theFirst, String theSecond) {
		MdmMatcherJson json = new MdmMatcherJson();
		json.setExact(true);
		return matcher.matches(new StringType(theFirst), new StringType(theSecond), json);
	}
}
