package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.jpa.nickname.INicknameSvc;
import ca.uhn.fhir.jpa.nickname.NicknameSvc;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.NicknameMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NicknameMatcherTest {
	IMdmFieldMatcher matcher;

	INicknameSvc myNicknameSvc = new NicknameSvc();

	@BeforeEach
	public void begin() {
		matcher = new NicknameMatcher(myNicknameSvc);
	}

	@Test
	public void testMatches() {
		Assertions.assertTrue(match("Ken", "ken"));
		Assertions.assertTrue(match("ken", "Ken"));
		Assertions.assertTrue(match("Ken", "Ken"));
		Assertions.assertTrue(match("Kenneth", "Ken"));
		Assertions.assertTrue(match("Kenneth", "Kenny"));
		Assertions.assertTrue(match("Ken", "Kenneth"));
		Assertions.assertTrue(match("Kenny", "Kenneth"));
		Assertions.assertTrue(match("Jim", "Jimmy"));
		Assertions.assertTrue(match("Jimmy", "Jim"));
		Assertions.assertTrue(match("Jim", "James"));
		Assertions.assertTrue(match("Jimmy", "James"));
		Assertions.assertTrue(match("James", "Jimmy"));
		Assertions.assertTrue(match("James", "Jim"));

		Assertions.assertFalse(match("Ken", "Bob"));
		// These aren't nickname matches.  If you want matches like these use a phonetic matcher
		Assertions.assertFalse(match("Allen", "Allan"));
	}

	private boolean match(String theFirst, String theSecond) {
		MdmMatcherJson json = new MdmMatcherJson();
		json.setExact(true);
		return matcher.matches(new StringType(theFirst), new StringType(theSecond), json);
	}
}
