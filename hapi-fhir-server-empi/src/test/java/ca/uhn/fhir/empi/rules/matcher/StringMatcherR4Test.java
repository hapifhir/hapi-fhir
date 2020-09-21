package ca.uhn.fhir.empi.rules.matcher;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringMatcherR4Test extends BaseMatcherR4Test {
	@Test
	public void testMetaphone() {
		assertTrue(match(EmpiMatcherEnum.METAPHONE, new StringType("Durie"), new StringType("dury")));
		assertTrue(match(EmpiMatcherEnum.METAPHONE, new StringType("Balo"), new StringType("ballo")));
		assertTrue(match(EmpiMatcherEnum.METAPHONE, new StringType("Hans Peter"), new StringType("Hanspeter")));
		assertTrue(match(EmpiMatcherEnum.METAPHONE, new StringType("Lawson"), new StringType("Law son")));

		assertFalse(match(EmpiMatcherEnum.METAPHONE, new StringType("Allsop"), new StringType("Allsob")));
		assertFalse(match(EmpiMatcherEnum.METAPHONE, new StringType("Gevne"), new StringType("Geve")));
		assertFalse(match(EmpiMatcherEnum.METAPHONE, new StringType("Bruce"), new StringType("Bruch")));
		assertFalse(match(EmpiMatcherEnum.METAPHONE, new StringType("Smith"), new StringType("Schmidt")));
		assertFalse(match(EmpiMatcherEnum.METAPHONE, new StringType("Jyothi"), new StringType("Jyoti")));
	}

	@Test
	public void testDoubleMetaphone() {
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Durie"), new StringType("dury")));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Balo"), new StringType("ballo")));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Hans Peter"), new StringType("Hanspeter")));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Lawson"), new StringType("Law son")));
		assertTrue(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Allsop"), new StringType("Allsob")));

		assertFalse(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Gevne"), new StringType("Geve")));
		assertFalse(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Bruce"), new StringType("Bruch")));
		assertFalse(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Smith"), new StringType("Schmidt")));
		assertFalse(match(EmpiMatcherEnum.DOUBLE_METAPHONE, new StringType("Jyothi"), new StringType("Jyoti")));
	}

	@Test
	public void testNormalizeCase() {
		assertTrue(match(EmpiMatcherEnum.STRING, new StringType("joe"), new StringType("JoE")));
		assertTrue(match(EmpiMatcherEnum.STRING, new StringType("MCTAVISH"), new StringType("McTavish")));

		assertFalse(match(EmpiMatcherEnum.STRING, new StringType("joey"), new StringType("joe")));
		assertFalse(match(EmpiMatcherEnum.STRING, new StringType("joe"), new StringType("joey")));
	}

	@Test
	public void testExactString() {
		assertTrue(EmpiMatcherEnum.STRING.match(ourFhirContext, new StringType("Jilly"), new StringType("Jilly"), true));

		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new StringType("MCTAVISH"), new StringType("McTavish"), true));
		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new StringType("Durie"), new StringType("dury"), true));
	}

	@Test
	public void testExactBoolean() {
		assertTrue(EmpiMatcherEnum.STRING.match(ourFhirContext, new BooleanType(true), new BooleanType(true), true));

		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new BooleanType(true), new BooleanType(false), true));
		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new BooleanType(false), new BooleanType(true), true));
	}

	@Test
	public void testExactDateString() {
		assertTrue(EmpiMatcherEnum.STRING.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-08-09"), true));

		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-09-08"), true));
	}


	@Test
	public void testExactGender() {
		Enumeration<Enumerations.AdministrativeGender> male = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		male.setValue(Enumerations.AdministrativeGender.MALE);

		Enumeration<Enumerations.AdministrativeGender> female = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		female.setValue(Enumerations.AdministrativeGender.FEMALE);

		assertTrue(EmpiMatcherEnum.STRING.match(ourFhirContext, male, male, true));

		assertFalse(EmpiMatcherEnum.STRING.match(ourFhirContext, male, female, true));
	}

	@Test
	public void testSoundex() {
		assertTrue(match(EmpiMatcherEnum.SOUNDEX, new StringType("Gail"), new StringType("Gale")));
		assertTrue(match(EmpiMatcherEnum.SOUNDEX, new StringType("John"), new StringType("Jon")));
		assertTrue(match(EmpiMatcherEnum.SOUNDEX, new StringType("Thom"), new StringType("Tom")));

		assertFalse(match(EmpiMatcherEnum.SOUNDEX, new StringType("Fred"), new StringType("Frank")));
		assertFalse(match(EmpiMatcherEnum.SOUNDEX, new StringType("Thomas"), new StringType("Tom")));
	}


	@Test
	public void testCaverphone1() {
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE1, new StringType("Gail"), new StringType("Gael")));
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE1, new StringType("John"), new StringType("Jon")));

		assertFalse(match(EmpiMatcherEnum.CAVERPHONE1, new StringType("Gail"), new StringType("Gale")));
		assertFalse(match(EmpiMatcherEnum.CAVERPHONE1, new StringType("Fred"), new StringType("Frank")));
		assertFalse(match(EmpiMatcherEnum.CAVERPHONE1, new StringType("Thomas"), new StringType("Tom")));
	}

	@Test
	public void testCaverphone2() {
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE2, new StringType("Gail"), new StringType("Gael")));
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE2, new StringType("John"), new StringType("Jon")));
		assertTrue(match(EmpiMatcherEnum.CAVERPHONE2, new StringType("Gail"), new StringType("Gale")));

		assertFalse(match(EmpiMatcherEnum.CAVERPHONE2, new StringType("Fred"), new StringType("Frank")));
		assertFalse(match(EmpiMatcherEnum.CAVERPHONE2, new StringType("Thomas"), new StringType("Tom")));
	}

	@Test
	public void testNormalizeSubstring() {
		assertTrue(match(EmpiMatcherEnum.SUBSTRING, new StringType("BILLY"), new StringType("Bill")));
		assertTrue(match(EmpiMatcherEnum.SUBSTRING, new StringType("Bill"), new StringType("Billy")));
		assertTrue(match(EmpiMatcherEnum.SUBSTRING, new StringType("FRED"), new StringType("Frederik")));

		assertFalse(match(EmpiMatcherEnum.SUBSTRING, new StringType("Fred"), new StringType("Friederik")));
	}

	private boolean match(EmpiMatcherEnum theMatcher, StringType theLeft, StringType theRight) {
		return theMatcher.match(ourFhirContext, theLeft, theRight, false);
	}
}
