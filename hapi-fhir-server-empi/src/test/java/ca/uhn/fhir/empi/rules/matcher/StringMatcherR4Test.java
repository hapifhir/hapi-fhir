package ca.uhn.fhir.empi.rules.matcher;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringMatcherR4Test extends BaseMatcherR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(StringMatcherR4Test.class);
	public static final String LEFT = "namadega";
	public static final String RIGHT = "namaedga";

	@Test
	public void testNamadega() {
		assertTrue(match(EmpiMatcherEnum.COLOGNE, LEFT, RIGHT));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, LEFT, RIGHT));
		assertTrue(match(EmpiMatcherEnum.MATCH_RATING_APPROACH, LEFT, RIGHT));
		assertTrue(match(EmpiMatcherEnum.METAPHONE, LEFT, RIGHT));
		assertTrue(match(EmpiMatcherEnum.SOUNDEX, LEFT, RIGHT));
		assertTrue(match(EmpiMatcherEnum.METAPHONE, LEFT, RIGHT));

		assertFalse(match(EmpiMatcherEnum.CAVERPHONE1, LEFT, RIGHT));
		assertFalse(match(EmpiMatcherEnum.CAVERPHONE2, LEFT, RIGHT));
		assertFalse(match(EmpiMatcherEnum.NYSIIS, LEFT, RIGHT));
		assertFalse(match(EmpiMatcherEnum.REFINED_SOUNDEX, LEFT, RIGHT));
		assertFalse(match(EmpiMatcherEnum.STRING, LEFT, RIGHT));
		assertFalse(match(EmpiMatcherEnum.SUBSTRING, LEFT, RIGHT));
	}

	@Test
	public void testMetaphone() {
		assertTrue(match(EmpiMatcherEnum.METAPHONE, "Durie", "dury"));
		assertTrue(match(EmpiMatcherEnum.METAPHONE, "Balo", "ballo"));
		assertTrue(match(EmpiMatcherEnum.METAPHONE, "Hans Peter", "Hanspeter"));
		assertTrue(match(EmpiMatcherEnum.METAPHONE, "Lawson", "Law son"));

		assertFalse(match(EmpiMatcherEnum.METAPHONE, "Allsop", "Allsob"));
		assertFalse(match(EmpiMatcherEnum.METAPHONE, "Gevne", "Geve"));
		assertFalse(match(EmpiMatcherEnum.METAPHONE, "Bruce", "Bruch"));
		assertFalse(match(EmpiMatcherEnum.METAPHONE, "Smith", "Schmidt"));
		assertFalse(match(EmpiMatcherEnum.METAPHONE, "Jyothi", "Jyoti"));
	}

	@Test
	public void testDoubleMetaphone() {
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Durie", "dury"));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Balo", "ballo"));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Hans Peter", "Hanspeter"));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Lawson", "Law son"));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Allsop", "Allsob"));

		assertFalse(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Gevne", "Geve"));
		assertFalse(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Bruce", "Bruch"));
		assertFalse(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Smith", "Schmidt"));
		assertFalse(match(EmpiMatcherEnum.DOUBLE_METAPHONE, "Jyothi", "Jyoti"));
	}

	@Test
	public void testNormalizeCase() {
		assertTrue(match(EmpiMatcherEnum.STRING, "joe", "JoE"));
		assertTrue(match(EmpiMatcherEnum.STRING, "MCTAVISH", "McTavish"));

		assertFalse(match(EmpiMatcherEnum.STRING, "joey", "joe"));
		assertFalse(match(EmpiMatcherEnum.STRING, "joe", "joey"));
	}

	@Test
	public void testExactString() {
		assertTrue(EmpiMatcherEnum.STRING.match(ourFhirContext, new StringType("Jilly"), new StringType("Jilly"), true, null));

		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new StringType("MCTAVISH"), new StringType("McTavish"), true, null));
		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new StringType("Durie"), new StringType("dury"), true, null));
	}

	@Test
	public void testExactBoolean() {
		assertTrue(EmpiMatcherEnum.STRING.match(ourFhirContext, new BooleanType(true), new BooleanType(true), true, null));

		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new BooleanType(true), new BooleanType(false), true, null));
		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new BooleanType(false), new BooleanType(true), true, null));
	}

	@Test
	public void testExactDateString() {
		assertTrue(EmpiMatcherEnum.STRING.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-08-09"), true, null));

		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-09-08"), true, null));
	}


	@Test
	public void testExactGender() {
		Enumeration<Enumerations.AdministrativeGender> male = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		male.setValue(Enumerations.AdministrativeGender.MALE);

		Enumeration<Enumerations.AdministrativeGender> female = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		female.setValue(Enumerations.AdministrativeGender.FEMALE);

		assertTrue(EmpiMatcherEnum.STRING.match(ourFhirContext, male, male, true, null));

		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, male, female, true, null));
	}

	@Test
	public void testSoundex() {
		assertTrue(match(EmpiMatcherEnum.SOUNDEX, "Gail", "Gale"));
		assertTrue(match(EmpiMatcherEnum.SOUNDEX, "John", "Jon"));
		assertTrue(match(EmpiMatcherEnum.SOUNDEX, "Thom", "Tom"));

		assertFalse(match(EmpiMatcherEnum.SOUNDEX, "Fred", "Frank"));
		assertFalse(match(EmpiMatcherEnum.SOUNDEX, "Thomas", "Tom"));
	}


	@Test
	public void testCaverphone1() {
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE1, "Gail", "Gael"));
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE1, "John", "Jon"));

		assertFalse(match(EmpiMatcherEnum.CAVERPHONE1, "Gail", "Gale"));
		assertFalse(match(EmpiMatcherEnum.CAVERPHONE1, "Fred", "Frank"));
		assertFalse(match(EmpiMatcherEnum.CAVERPHONE1, "Thomas", "Tom"));
	}

	@Test
	public void testCaverphone2() {
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE2, "Gail", "Gael"));
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE2, "John", "Jon"));
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE2, "Gail", "Gale"));

		assertFalse(match(EmpiMatcherEnum.CAVERPHONE2, "Fred", "Frank"));
		assertFalse(match(EmpiMatcherEnum.CAVERPHONE2, "Thomas", "Tom"));
	}

	@Test
	public void testNormalizeSubstring() {
		assertTrue(match(EmpiMatcherEnum.SUBSTRING, "BILLY", "Bill"));
		assertTrue(match(EmpiMatcherEnum.SUBSTRING, "Bill", "Billy"));
		assertTrue(match(EmpiMatcherEnum.SUBSTRING, "FRED", "Frederik"));

		assertFalse(match(EmpiMatcherEnum.SUBSTRING, "Fred", "Friederik"));
	}

	private boolean match(EmpiMatcherEnum theMatcher, String theLeft, String theRight) {
		return theMatcher.match(ourFhirContext, new StringType(theLeft), new StringType(theRight), false, null);
	}
}
