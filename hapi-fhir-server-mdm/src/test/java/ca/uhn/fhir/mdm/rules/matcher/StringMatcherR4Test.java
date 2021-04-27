package ca.uhn.fhir.mdm.rules.matcher;

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
	public static final String LEFT_NAME = "namadega";
	public static final String RIGHT_NAME = "namaedga";

	@Test
	public void testNamadega() {
		String left = LEFT_NAME;
		String right = RIGHT_NAME;
		assertTrue(match(MdmMatcherEnum.COLOGNE, left, right));
		assertTrue(match(MdmMatcherEnum.DOUBLE_METAPHONE, left, right));
		assertTrue(match(MdmMatcherEnum.MATCH_RATING_APPROACH, left, right));
		assertTrue(match(MdmMatcherEnum.METAPHONE, left, right));
		assertTrue(match(MdmMatcherEnum.SOUNDEX, left, right));
		assertTrue(match(MdmMatcherEnum.METAPHONE, left, right));

		assertFalse(match(MdmMatcherEnum.CAVERPHONE1, left, right));
		assertFalse(match(MdmMatcherEnum.CAVERPHONE2, left, right));
		assertFalse(match(MdmMatcherEnum.NYSIIS, left, right));
		assertFalse(match(MdmMatcherEnum.REFINED_SOUNDEX, left, right));
		assertFalse(match(MdmMatcherEnum.STRING, left, right));
		assertFalse(match(MdmMatcherEnum.SUBSTRING, left, right));
	}

	@Test
	public void testNumeric() {
		assertTrue(match(MdmMatcherEnum.NUMERIC, "4169671111", "(416) 967-1111"));
		assertFalse(match(MdmMatcherEnum.NUMERIC, "5169671111", "(416) 967-1111"));
		assertFalse(match(MdmMatcherEnum.NUMERIC, "4169671111", "(416) 967-1111x123"));
	}

	@Test
	public void testMetaphone() {
		assertTrue(match(MdmMatcherEnum.METAPHONE, "Durie", "dury"));
		assertTrue(match(MdmMatcherEnum.METAPHONE, "Balo", "ballo"));
		assertTrue(match(MdmMatcherEnum.METAPHONE, "Hans Peter", "Hanspeter"));
		assertTrue(match(MdmMatcherEnum.METAPHONE, "Lawson", "Law son"));

		assertFalse(match(MdmMatcherEnum.METAPHONE, "Allsop", "Allsob"));
		assertFalse(match(MdmMatcherEnum.METAPHONE, "Gevne", "Geve"));
		assertFalse(match(MdmMatcherEnum.METAPHONE, "Bruce", "Bruch"));
		assertFalse(match(MdmMatcherEnum.METAPHONE, "Smith", "Schmidt"));
		assertFalse(match(MdmMatcherEnum.METAPHONE, "Jyothi", "Jyoti"));
	}

	@Test
	public void testDoubleMetaphone() {
		assertTrue(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Durie", "dury"));
		assertTrue(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Balo", "ballo"));
		assertTrue(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Hans Peter", "Hanspeter"));
		assertTrue(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Lawson", "Law son"));
		assertTrue(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Allsop", "Allsob"));

		assertFalse(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Gevne", "Geve"));
		assertFalse(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Bruce", "Bruch"));
		assertFalse(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Smith", "Schmidt"));
		assertFalse(match(MdmMatcherEnum.DOUBLE_METAPHONE, "Jyothi", "Jyoti"));
	}

	@Test
	public void testNormalizeCase() {
		assertTrue(match(MdmMatcherEnum.STRING, "joe", "JoE"));
		assertTrue(match(MdmMatcherEnum.STRING, "MCTAVISH", "McTavish"));

		assertFalse(match(MdmMatcherEnum.STRING, "joey", "joe"));
		assertFalse(match(MdmMatcherEnum.STRING, "joe", "joey"));
	}

	@Test
	public void testExactString() {
		assertTrue(MdmMatcherEnum.STRING.match(ourFhirContext, new StringType("Jilly"), new StringType("Jilly"), true, null));

		assertFalse(MdmMatcherEnum.STRING.match(ourFhirContext, new StringType("MCTAVISH"), new StringType("McTavish"), true, null));
		assertFalse(MdmMatcherEnum.STRING.match(ourFhirContext, new StringType("Durie"), new StringType("dury"), true, null));
	}

	@Test
	public void testExactBoolean() {
		assertTrue(MdmMatcherEnum.STRING.match(ourFhirContext, new BooleanType(true), new BooleanType(true), true, null));

		assertFalse(MdmMatcherEnum.STRING.match(ourFhirContext, new BooleanType(true), new BooleanType(false), true, null));
		assertFalse(MdmMatcherEnum.STRING.match(ourFhirContext, new BooleanType(false), new BooleanType(true), true, null));
	}

	@Test
	public void testExactDateString() {
		assertTrue(MdmMatcherEnum.STRING.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-08-09"), true, null));

		assertFalse(MdmMatcherEnum.STRING.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-09-08"), true, null));
	}


	@Test
	public void testExactGender() {
		Enumeration<Enumerations.AdministrativeGender> male = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		male.setValue(Enumerations.AdministrativeGender.MALE);

		Enumeration<Enumerations.AdministrativeGender> female = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		female.setValue(Enumerations.AdministrativeGender.FEMALE);

		assertTrue(MdmMatcherEnum.STRING.match(ourFhirContext, male, male, true, null));

		assertFalse(MdmMatcherEnum.STRING.match(ourFhirContext, male, female, true, null));
	}

	@Test
	public void testSoundex() {
		assertTrue(match(MdmMatcherEnum.SOUNDEX, "Gail", "Gale"));
		assertTrue(match(MdmMatcherEnum.SOUNDEX, "John", "Jon"));
		assertTrue(match(MdmMatcherEnum.SOUNDEX, "Thom", "Tom"));

		assertFalse(match(MdmMatcherEnum.SOUNDEX, "Fred", "Frank"));
		assertFalse(match(MdmMatcherEnum.SOUNDEX, "Thomas", "Tom"));
	}


	@Test
	public void testCaverphone1() {
		assertTrue(match(MdmMatcherEnum.CAVERPHONE1, "Gail", "Gael"));
		assertTrue(match(MdmMatcherEnum.CAVERPHONE1, "John", "Jon"));

		assertFalse(match(MdmMatcherEnum.CAVERPHONE1, "Gail", "Gale"));
		assertFalse(match(MdmMatcherEnum.CAVERPHONE1, "Fred", "Frank"));
		assertFalse(match(MdmMatcherEnum.CAVERPHONE1, "Thomas", "Tom"));
	}

	@Test
	public void testCaverphone2() {
		assertTrue(match(MdmMatcherEnum.CAVERPHONE2, "Gail", "Gael"));
		assertTrue(match(MdmMatcherEnum.CAVERPHONE2, "John", "Jon"));
		assertTrue(match(MdmMatcherEnum.CAVERPHONE2, "Gail", "Gale"));

		assertFalse(match(MdmMatcherEnum.CAVERPHONE2, "Fred", "Frank"));
		assertFalse(match(MdmMatcherEnum.CAVERPHONE2, "Thomas", "Tom"));
	}

	@Test
	public void testNormalizeSubstring() {
		assertTrue(match(MdmMatcherEnum.SUBSTRING, "BILLY", "Bill"));
		assertTrue(match(MdmMatcherEnum.SUBSTRING, "Bill", "Billy"));
		assertTrue(match(MdmMatcherEnum.SUBSTRING, "FRED", "Frederik"));

		assertFalse(match(MdmMatcherEnum.SUBSTRING, "Fred", "Friederik"));
	}

	private boolean match(MdmMatcherEnum theMatcher, String theLeft, String theRight) {
		return theMatcher.match(ourFhirContext, new StringType(theLeft), new StringType(theRight), false, null);
	}
}
