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
		assertTrue(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Durie"), new StringType("dury")));
		assertTrue(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Balo"), new StringType("ballo")));
		assertTrue(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Hans Peter"), new StringType("Hanspeter")));
		assertTrue(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Lawson"), new StringType("Law son")));

		assertFalse(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Allsop"), new StringType("Allsob")));
		assertFalse(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Gevne"), new StringType("Geve")));
		assertFalse(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Bruce"), new StringType("Bruch")));
		assertFalse(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Smith"), new StringType("Schmidt")));
		assertFalse(EmpiMetricEnum.METAPHONE.match(ourFhirContext, new StringType("Jyothi"), new StringType("Jyoti")));
	}

	@Test
	public void testDoubleMetaphone() {
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Durie"), new StringType("dury")));
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Balo"), new StringType("ballo")));
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Hans Peter"), new StringType("Hanspeter")));
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Lawson"), new StringType("Law son")));
		assertTrue(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Allsop"), new StringType("Allsob")));

		assertFalse(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Gevne"), new StringType("Geve")));
		assertFalse(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Bruce"), new StringType("Bruch")));
		assertFalse(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Smith"), new StringType("Schmidt")));
		assertFalse(EmpiMetricEnum.DOUBLE_METAPHONE.match(ourFhirContext, new StringType("Jyothi"), new StringType("Jyoti")));
	}

	@Test
	public void testNormalizeCase() {
		assertTrue(EmpiMetricEnum.NORMALIZE_CASE.match(ourFhirContext, new StringType("joe"), new StringType("JoE")));
		assertTrue(EmpiMetricEnum.NORMALIZE_CASE.match(ourFhirContext, new StringType("MCTAVISH"), new StringType("McTavish")));

		assertFalse(EmpiMetricEnum.NORMALIZE_CASE.match(ourFhirContext, new StringType("joey"), new StringType("joe")));
		assertFalse(EmpiMetricEnum.NORMALIZE_CASE.match(ourFhirContext, new StringType("joe"), new StringType("joey")));
	}

	@Test
	public void testExactString() {
		assertTrue(EmpiMetricEnum.EXACT.match(ourFhirContext, new StringType("Jilly"), new StringType("Jilly")));

		assertFalse(EmpiMetricEnum.EXACT.match(ourFhirContext, new StringType("MCTAVISH"), new StringType("McTavish")));
		assertFalse(EmpiMetricEnum.EXACT.match(ourFhirContext, new StringType("Durie"), new StringType("dury")));
	}

	@Test
	public void testExactBoolean() {
		assertTrue(EmpiMetricEnum.EXACT.match(ourFhirContext, new BooleanType(true), new BooleanType(true)));

		assertFalse(EmpiMetricEnum.EXACT.match(ourFhirContext, new BooleanType(true), new BooleanType(false)));
		assertFalse(EmpiMetricEnum.EXACT.match(ourFhirContext, new BooleanType(false), new BooleanType(true)));
	}

	@Test
	public void testExactDate() {
		assertTrue(EmpiMetricEnum.EXACT.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-08-09")));

		assertFalse(EmpiMetricEnum.EXACT.match(ourFhirContext, new DateType("1965-08-09"), new DateType("1965-09-08")));
	}

	@Test
	public void testExactGender() {
		Enumeration<Enumerations.AdministrativeGender> male = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		male.setValue(Enumerations.AdministrativeGender.MALE);

		Enumeration<Enumerations.AdministrativeGender> female = new Enumeration<Enumerations.AdministrativeGender>(new Enumerations.AdministrativeGenderEnumFactory());
		female.setValue(Enumerations.AdministrativeGender.FEMALE);

		assertTrue(EmpiMetricEnum.EXACT.match(ourFhirContext, male, male));

		assertFalse(EmpiMetricEnum.EXACT.match(ourFhirContext, male, female));
	}

	@Test
	public void testSoundex() {
		assertTrue(EmpiMetricEnum.SOUNDEX.match(ourFhirContext, new StringType("Gail"), new StringType("Gail")));

		assertFalse(EmpiMetricEnum.SOUNDEX.match(ourFhirContext, new StringType("Fred"), new StringType("Frank")));
		assertFalse(EmpiMetricEnum.SOUNDEX.match(ourFhirContext, new StringType("Thomas"), new StringType("Tom")));
	}
}
