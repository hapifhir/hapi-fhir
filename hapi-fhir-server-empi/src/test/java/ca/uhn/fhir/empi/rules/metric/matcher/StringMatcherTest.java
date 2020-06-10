package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringMatcherTest extends BaseMatcherTest {
	@Test
	public void testMetaphone() {
		assertTrue(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Durie"), new StringType("dury")));
		assertTrue(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Balo"), new StringType("ballo")));
		assertTrue(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Hans Peter"), new StringType("Hanspeter")));
		assertTrue(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Lawson"), new StringType("Law son")));

		assertFalse(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Allsop"), new StringType("Allsob")));
		assertFalse(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Gevne"), new StringType("Geve")));
		assertFalse(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Bruce"), new StringType("Bruch")));
		assertFalse(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Smith"), new StringType("Schmidt")));
		assertFalse(EmpiMetricEnum.METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Jyothi"), new StringType("Jyoti")));
	}

	@Test
	public void testDoubleMetaphone() {
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Durie"), new StringType("dury")));
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Balo"), new StringType("ballo")));
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Hans Peter"), new StringType("Hanspeter")));
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Lawson"), new StringType("Law son")));
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Allsop"), new StringType("Allsob")));

		assertFalse(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Gevne"), new StringType("Geve")));
		assertFalse(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Bruce"), new StringType("Bruch")));
		assertFalse(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Smith"), new StringType("Schmidt")));
		assertFalse(EmpiMetricEnum.DOUBLE_METAPHONE.matchForUnitTest(ourFhirContext, new StringType("Jyothi"), new StringType("Jyoti")));
	}

	@Test
	public void testNormalizeCase() {
		assertTrue(EmpiMetricEnum.STRING.matchForUnitTest(ourFhirContext, new StringType("joe"), new StringType("JoE")));
		assertTrue(EmpiMetricEnum.STRING.matchForUnitTest(ourFhirContext, new StringType("MCTAVISH"), new StringType("McTavish")));

		assertFalse(EmpiMetricEnum.STRING.matchForUnitTest(ourFhirContext, new StringType("joey"), new StringType("joe")));
		assertFalse(EmpiMetricEnum.STRING.matchForUnitTest(ourFhirContext, new StringType("joe"), new StringType("joey")));
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
		assertTrue(EmpiMetricEnum.SOUNDEX.matchForUnitTest(ourFhirContext, new StringType("Gail"), new StringType("Gail")));

		assertFalse(EmpiMetricEnum.SOUNDEX.matchForUnitTest(ourFhirContext, new StringType("Fred"), new StringType("Frank")));
		assertFalse(EmpiMetricEnum.SOUNDEX.matchForUnitTest(ourFhirContext, new StringType("Thomas"), new StringType("Tom")));
	}

	@Test
	public void testCaverphone2() {
		assertTrue(EmpiMetricEnum.CAVERPHONE2.matchForUnitTest(ourFhirContext, new StringType("Gail"), new StringType("Gail")));

		assertFalse(EmpiMetricEnum.CAVERPHONE2.matchForUnitTest(ourFhirContext, new StringType("Fred"), new StringType("Frank")));
		assertFalse(EmpiMetricEnum.CAVERPHONE2.matchForUnitTest(ourFhirContext, new StringType("Thomas"), new StringType("Tom")));
	}

	@Test
	public void testNormalizeSubstring() {
		assertTrue(EmpiMetricEnum.SUBSTRING.matchForUnitTest(ourFhirContext, new StringType("BILLY"), new StringType("Bill")));
		assertTrue(EmpiMetricEnum.SUBSTRING.matchForUnitTest(ourFhirContext, new StringType("Bill"), new StringType("Billy")));
		assertTrue(EmpiMetricEnum.SUBSTRING.matchForUnitTest(ourFhirContext, new StringType("FRED"), new StringType("Frederik")));

		assertFalse(EmpiMetricEnum.SUBSTRING.matchForUnitTest(ourFhirContext, new StringType("Fred"), new StringType("Friederik")));
	}

}
