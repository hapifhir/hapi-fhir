package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
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
		assertTrue(match(EmpiMetricEnum.METAPHONE, new StringType("Durie"), new StringType("dury")));
		assertTrue(match(EmpiMetricEnum.METAPHONE, new StringType("Balo"), new StringType("ballo")));
		assertTrue(match(EmpiMetricEnum.METAPHONE, new StringType("Hans Peter"), new StringType("Hanspeter")));
		assertTrue(match(EmpiMetricEnum.METAPHONE, new StringType("Lawson"), new StringType("Law son")));

		assertFalse(match(EmpiMetricEnum.METAPHONE, new StringType("Allsop"), new StringType("Allsob")));
		assertFalse(match(EmpiMetricEnum.METAPHONE, new StringType("Gevne"), new StringType("Geve")));
		assertFalse(match(EmpiMetricEnum.METAPHONE, new StringType("Bruce"), new StringType("Bruch")));
		assertFalse(match(EmpiMetricEnum.METAPHONE, new StringType("Smith"), new StringType("Schmidt")));
		assertFalse(match(EmpiMetricEnum.METAPHONE, new StringType("Jyothi"), new StringType("Jyoti")));
	}

	@Test
	public void testDoubleMetaphone() {
		assertTrue(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Durie"), new StringType("dury")));
		assertTrue(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Balo"), new StringType("ballo")));
		assertTrue(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Hans Peter"), new StringType("Hanspeter")));
		assertTrue(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Lawson"), new StringType("Law son")));
		assertTrue(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Allsop"), new StringType("Allsob")));

		assertFalse(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Gevne"), new StringType("Geve")));
		assertFalse(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Bruce"), new StringType("Bruch")));
		assertFalse(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Smith"), new StringType("Schmidt")));
		assertFalse(match(EmpiMetricEnum.DOUBLE_METAPHONE, new StringType("Jyothi"), new StringType("Jyoti")));
	}

	@Test
	public void testNormalizeCase() {
		assertTrue(match(EmpiMetricEnum.STRING, new StringType("joe"), new StringType("JoE")));
		assertTrue(match(EmpiMetricEnum.STRING, new StringType("MCTAVISH"), new StringType("McTavish")));

		assertFalse(match(EmpiMetricEnum.STRING, new StringType("joey"), new StringType("joe")));
		assertFalse(match(EmpiMetricEnum.STRING, new StringType("joe"), new StringType("joey")));
	}

	@Test
	public void testExactString() {
		assertTrue(EmpiMetricEnum.STRING.match(ourFhirContext, new StringType("Jilly"), new StringType("Jilly"), true));

		assertFalse(EmpiMetricEnum.STRING.match(ourFhirContext, new StringType("MCTAVISH"), new StringType("McTavish"), true));
		assertFalse(EmpiMetricEnum.STRING.match(ourFhirContext, new StringType("Durie"), new StringType("dury"), true));
	}

	@Test
	public void testExactBoolean() {
		assertTrue(EmpiMetricEnum.STRING.match(ourFhirContext, new BooleanType(true), new BooleanType(true), true));

		assertFalse(EmpiMetricEnum.STRING.match(ourFhirContext, new BooleanType(true), new BooleanType(false), true));
		assertFalse(EmpiMetricEnum.STRING.match(ourFhirContext, new BooleanType(false), new BooleanType(true), true));
	}

	@Test
	public void testExactDateString() {
		assertTrue(EmpiMetricEnum.STRING.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-08-09"), true));

		assertFalse(EmpiMetricEnum.STRING.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-09-08"), true));
	}


	@Test
	public void testExactGender() {
		Enumeration<Enumerations.AdministrativeGender> male = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		male.setValue(Enumerations.AdministrativeGender.MALE);

		Enumeration<Enumerations.AdministrativeGender> female = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		female.setValue(Enumerations.AdministrativeGender.FEMALE);

		assertTrue(EmpiMetricEnum.STRING.match(ourFhirContext, male, male, true));

		assertFalse(EmpiMetricEnum.STRING.match(ourFhirContext, male, female, true));
	}

	@Test
	public void testSoundex() {
		assertTrue(match(EmpiMetricEnum.SOUNDEX, new StringType("Gail"), new StringType("Gale")));
		assertTrue(match(EmpiMetricEnum.SOUNDEX, new StringType("John"), new StringType("Jon")));
		assertTrue(match(EmpiMetricEnum.SOUNDEX, new StringType("Thom"), new StringType("Tom")));

		assertFalse(match(EmpiMetricEnum.SOUNDEX, new StringType("Fred"), new StringType("Frank")));
		assertFalse(match(EmpiMetricEnum.SOUNDEX, new StringType("Thomas"), new StringType("Tom")));
	}


	@Test
	public void testCaverphone1() {
		assertTrue(match(EmpiMetricEnum.CAVERPHONE1, new StringType("Gail"), new StringType("Gael")));
		assertTrue(match(EmpiMetricEnum.CAVERPHONE1, new StringType("John"), new StringType("Jon")));

		assertFalse(match(EmpiMetricEnum.CAVERPHONE1, new StringType("Gail"), new StringType("Gale")));
		assertFalse(match(EmpiMetricEnum.CAVERPHONE1, new StringType("Fred"), new StringType("Frank")));
		assertFalse(match(EmpiMetricEnum.CAVERPHONE1, new StringType("Thomas"), new StringType("Tom")));
	}

	@Test
	public void testCaverphone2() {
		assertTrue(match(EmpiMetricEnum.CAVERPHONE2, new StringType("Gail"), new StringType("Gael")));
		assertTrue(match(EmpiMetricEnum.CAVERPHONE2, new StringType("John"), new StringType("Jon")));
		assertTrue(match(EmpiMetricEnum.CAVERPHONE2, new StringType("Gail"), new StringType("Gale")));

		assertFalse(match(EmpiMetricEnum.CAVERPHONE2, new StringType("Fred"), new StringType("Frank")));
		assertFalse(match(EmpiMetricEnum.CAVERPHONE2, new StringType("Thomas"), new StringType("Tom")));
	}

	@Test
	public void testNormalizeSubstring() {
		assertTrue(match(EmpiMetricEnum.SUBSTRING, new StringType("BILLY"), new StringType("Bill")));
		assertTrue(match(EmpiMetricEnum.SUBSTRING, new StringType("Bill"), new StringType("Billy")));
		assertTrue(match(EmpiMetricEnum.SUBSTRING, new StringType("FRED"), new StringType("Frederik")));

		assertFalse(match(EmpiMetricEnum.SUBSTRING, new StringType("Fred"), new StringType("Friederik")));
	}

	private boolean match(EmpiMetricEnum theMetric, StringType theLeft, StringType theRight) {
		return theMetric.match(ourFhirContext, theLeft, theRight, false);
	}
}
