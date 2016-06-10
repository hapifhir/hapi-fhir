package ca.uhn.fhir.model.primitive;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.ValidationResult;

public class BaseDateTimeDtDstu2Test {
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static Locale ourDefaultLocale;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseDateTimeDtDstu2Test.class);



	private SimpleDateFormat myDateInstantParser;

	
	private FastDateFormat myDateInstantZoneParser;
	
	@Before
	public void before() {
		myDateInstantParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		myDateInstantZoneParser = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ", TimeZone.getTimeZone("GMT-02:00"));
	}
	
	
	@Test
	public void setTimezoneToZulu() {
		DateTimeDt dt = new DateTimeDt(new Date(816411488000L));
		// assertEquals("1995-11-14T23:58:08", dt.getValueAsString());
		dt.setTimeZoneZulu(true);
		assertEquals("1995-11-15T04:58:08Z", dt.getValueAsString());
	}

	/**
	 * Test for #57
	 */
	@Test
	public void testConstructorRejectsInvalidPrecision() {
		try {
			new DateDt("2001-01-02T11:13:33");
			fail();
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), containsString("precision"));
		}
		try {
			new InstantDt("2001-01-02");
			fail();
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), containsString("precision"));
		}
	}

	/**
	 * Test for #57
	 */
	@Test
	public void testDateParsesWithInvalidPrecision() {
		Condition c = new Condition();
		c.setDateRecorded(new DateDt());
		c.getDateRecordedElement().setValueAsString("2001-01-02T11:13:33");
		assertEquals(TemporalPrecisionEnum.SECOND, c.getDateRecordedElement().getPrecision());

		String encoded = ourCtx.newXmlParser().encodeResourceToString(c);
		Assert.assertThat(encoded, Matchers.containsString("value=\"2001-01-02T11:13:33\""));

		c = ourCtx.newXmlParser().parseResource(Condition.class, encoded);

		assertEquals("2001-01-02T11:13:33", c.getDateRecordedElement().getValueAsString());
		assertEquals(TemporalPrecisionEnum.SECOND, c.getDateRecordedElement().getPrecision());

		ValidationResult outcome = ourCtx.newValidator().validateWithResult(c);
		String outcomeStr = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome());
		ourLog.info(outcomeStr);

		assertThat(outcomeStr, containsString("date-primitive"));
	}

	@Test
	public void testDateTimeInLocalTimezone() {
		DateTimeDt dt = DateTimeDt.withCurrentTime();
		String str = dt.getValueAsString();
		char offset = str.charAt(19);
		if (offset != '+' && offset != '-' && offset != 'Z') {
			fail("No timezone provided: " + str);
		}
	}

	@Test
	public void testEncodeOffset() throws Exception {
		String offset = InstantDt.withCurrentTime().setTimeZone(TimeZone.getTimeZone("America/Toronto")).getValueAsString();
		assertThat(offset, either(endsWith("-05:00")).or(endsWith("-04:00")));
	}

	
	@Test
	public void testInstantInLocalTimezone() {
		InstantDt dt = InstantDt.withCurrentTime();
		String str = dt.getValueAsString();
		char offset = str.charAt(23);
		if (offset != '+' && offset != '-' && offset != 'Z') {
			fail("No timezone provided: " + str);
		}
	}
	
	@Test
	public void testLargePrecision() {
		DateTimeDt dt = new DateTimeDt("2014-03-06T22:09:58.9121174+04:30");
		
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("Z"));
		assertEquals("2014-03-06 17:39:58.912", myDateInstantParser.format(dt.getValue()));
	}

	@Test
	public void testMinutePrecisionEncode() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		cal.set(1990, Calendar.JANUARY, 3, 3, 22, 11);
		
		DateTimeDt date = new DateTimeDt();
		date.setValue(cal.getTime(), TemporalPrecisionEnum.MINUTE);
		date.setTimeZone(TimeZone.getTimeZone("EST"));
		assertEquals("1990-01-02T21:22-05:00", date.getValueAsString());

		date.setTimeZoneZulu(true);
		assertEquals("1990-01-03T02:22Z", date.getValueAsString());
	}

	@Test
	public void testParseDate() {
		new DateDt("2012-03-31");
	}

	@Test
	public void testParseDay() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02-03");

		assertEquals("2013-02-03", myDateInstantParser.format(dt.getValue()).substring(0, 10));
		assertEquals("2013-02-03", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.DAY, dt.getPrecision());
	}

	/**
	 * See #381
	 */
	@Test
	public void testParseFailsForInvalidDate() {
		try {
			DateTimeDt dt = new DateTimeDt("9999-13-01");
			fail(dt.getValue().toString());
		} catch (DataFormatException e) {
			// good
		}

	}

	@Test
	public void testParseHandlesMillis() {
		InstantDt dt = new InstantDt();
		dt.setValueAsString("2015-06-22T15:44:32.831-04:00");
		Date date = dt.getValue();

		InstantDt dt2 = new InstantDt();
		dt2.setValue(date);
		dt2.setTimeZoneZulu(true);
		String string = dt2.getValueAsString();

		assertEquals("2015-06-22T19:44:32.831Z", string);
	}

	@Test
	public void testDateTimeFormatsInvalid() {
		// Bad timezone
		verifyFails("1974-12-01T00:00:00A");
		verifyFails("1974-12-01T00:00:00=00:00");
		verifyFails("1974-12-01T00:00:00+");
		verifyFails("1974-12-01T00:00:00+25:00");
		verifyFails("1974-12-01T00:00:00+00:61");
		verifyFails("1974-12-01T00:00:00+00 401");
		verifyFails("1974-12-01T00:00:00+0");
		verifyFails("1974-12-01T00:00:00+01");
		verifyFails("1974-12-01T00:00:00+011");
		verifyFails("1974-12-01T00:00:00+0110");
		
		// Out of range
		verifyFails("1974-12-25T25:00:00Z");
		verifyFails("1974-12-25T24:00:00Z");
		verifyFails("1974-12-25T23:60:00Z");
		verifyFails("1974-12-25T23:59:60Z");
		
		// Invalid Separators
		verifyFails("1974-12-25T23 59:00Z");
		verifyFails("1974-12-25T23:59 00Z");
		
		// Invalid length
		verifyFails("1974-12-25T2Z");
		verifyFails("1974-12-25T22:Z");
		verifyFails("1974-12-25T22:1Z");
		verifyFails("1974-12-25T22:11:Z");
		verifyFails("1974-12-25T22:11:1Z");
	}
	
	
	@Test
	public void testDateFormatsInvalid() {
		// No spaces in dates
		verifyFails("1974 12-25");
		verifyFails("1974-12 25");

		// No letters
		verifyFails("A974-12-25");
		verifyFails("1974-A2-25");
		verifyFails("1974-12-A5");

		// Date shouldn't have a time zone
		verifyFails("1974-12-25Z");
		verifyFails("1974-12-25+10:00");
		
		// Out of range
		verifyFails("1974-13-25");
		verifyFails("1974-12-32");
		verifyFails("2015-02-29");
		verifyFails("-016-02-01");
		verifyFails("2016--2-01");
		verifyFails("2016-02--1");

		// Invalid length
		verifyFails("2");
		verifyFails("20");
		verifyFails("201");
		verifyFails("2016-0");
		verifyFails("2016-02-0");
	}


	private void verifyFails(String input) {
		try {
			DateTimeDt dt = new DateTimeDt();
			dt.setValueAsString(input);
			fail();
		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertThat(e.getMessage(), containsString("Invalid date/time format: \"" + input + "\""));
		}
	}

	@Test
	public void testParseInvalidZoneOffset() {
		try {
			new DateTimeDt("2010-01-01T00:00:00.1234-09:00Z");
			fail();
		} catch (DataFormatException e) {
			assertEquals("Invalid FHIR date/time string: 2010-01-01T00:00:00.1234-09:00Z", e.getMessage());
		}
	}

	
	@Test(expected = DataFormatException.class)
	public void testParseMalformatted() throws DataFormatException {
		new DateTimeDt("20120102");
	}


	@Test
	public void testParseMilli() throws DataFormatException {
		InstantDt dt = new InstantDt();
		dt.setValueAsString("2013-02-03T11:22:33.234");

		assertEquals("2013-02-03 11:22:33.234", myDateInstantParser.format(dt.getValue()).substring(0, 23));
		assertEquals("2013-02-03T11:22:33.234", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MILLI, dt.getPrecision());
	}

	@Test
	public void testParseMilliZone() throws DataFormatException {
		InstantDt dt = new InstantDt();
		dt.setValueAsString("2013-02-03T11:22:33.234-02:00");

		assertEquals("2013-02-03 11:22:33.234-0200", myDateInstantZoneParser.format(dt.getValue()));
		assertEquals("2013-02-03T11:22:33.234-02:00", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertEquals(TimeZone.getTimeZone("GMT-02:00"), dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MILLI, dt.getPrecision());
	}
	
	@Test
	public void testParseMilliZulu() throws DataFormatException {
		InstantDt dt = new InstantDt();
		dt.setValueAsString("2013-02-03T11:22:33.234Z");

		assertEquals("2013-02-03 09:22:33.234-0200", myDateInstantZoneParser.format(dt.getValue()));
		assertEquals("2013-02-03T11:22:33.234Z", dt.getValueAsString());
		assertEquals(true, dt.isTimeZoneZulu());
		assertEquals("GMT", dt.getTimeZone().getID());
		assertEquals(TemporalPrecisionEnum.MILLI, dt.getPrecision());
	}

	@Test
	public void testParseMonth() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02");

		ourLog.info("Date: {}", dt.getValue());
		assertEquals("2013-02", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MONTH, dt.getPrecision());

		assertEquals("2013-02", myDateInstantParser.format(dt.getValue()).substring(0, 7));
	}

	@Test(expected = DataFormatException.class)
	public void testParseMonthNoDashes() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("201302");
	}

	@Test
	public void testParseSecond() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02-03T11:22:33");

		assertEquals("2013-02-03 11:22:33", myDateInstantParser.format(dt.getValue()).substring(0, 19));
		assertEquals("2013-02-03T11:22:33", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.SECOND, dt.getPrecision());
	}

	@Test
	public void testParseSecondulu() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02-03T11:22:33Z");

		assertEquals("2013-02-03T11:22:33Z", dt.getValueAsString());
		assertEquals(true, dt.isTimeZoneZulu());
		assertEquals("GMT", dt.getTimeZone().getID());
		assertEquals(TemporalPrecisionEnum.SECOND, dt.getPrecision());
	}

	@Test
	public void testParseSecondZone() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02-03T11:22:33-02:00");

		assertEquals("2013-02-03T11:22:33-02:00", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertEquals(TimeZone.getTimeZone("GMT-02:00"), dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.SECOND, dt.getPrecision());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly0millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		
		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00-09:00");
		
		assertEquals("2010-01-01T00:00:00-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.000", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());
		
		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00Z", dt.getValueAsString());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly1millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		
		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.1-09:00");
		
		assertEquals("2010-01-01T00:00:00.1-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.001", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());
		
		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00.001Z", dt.getValueAsString());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly2millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		
		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.12-09:00");
		
		assertEquals("2010-01-01T00:00:00.12-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.012", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());
		
		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00.012Z", dt.getValueAsString());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly3millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		
		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.123-09:00");
		
		assertEquals("2010-01-01T00:00:00.123-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.123", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());
		
		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00.123Z", dt.getValueAsString());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly4millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		
		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.1234-09:00");
		
		assertEquals("2010-01-01T00:00:00.1234-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.123", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());
		
		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00.1234Z", dt.getValueAsString());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly5millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		
		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.12345-09:00");
		
		assertEquals("2010-01-01T00:00:00.12345-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.123", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());
		
		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00.12345Z", dt.getValueAsString());
	}

	@Test
	public void testParseYear() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013");

		assertEquals("2013", myDateInstantParser.format(dt.getValue()).substring(0, 4));
		assertEquals("2013", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.YEAR, dt.getPrecision());
	}

	/**
	 * See #101
	 */
	@Test
	public void testPrecision() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDateInstantParser.parse("2012-01-02 22:31:02.333"));
		cal.setTimeZone(TimeZone.getTimeZone("EST"));

		Patient patient = new Patient();
		patient.setBirthDate(cal.getTime(), TemporalPrecisionEnum.DAY);
		String out = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(out, containsString("<birthDate value=\"2012-01-02\"/>"));
	}

	/**
	 * See HAPI #101 - https://github.com/jamesagnew/hapi-fhir/issues/101
	 */
	@Test
	public void testPrecisionRespectedForSetValue() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDateInstantParser.parse("2012-01-02 22:31:02.333"));
		cal.setTimeZone(TimeZone.getTimeZone("EST"));

		Date time = cal.getTime();

		DateDt date = new DateDt();
		date.setValue(time);
		assertEquals("2012-01-02", date.getValueAsString());
	}

	/**
	 * See HAPI #101 - https://github.com/jamesagnew/hapi-fhir/issues/101
	 */
	@Test
	public void testPrecisionRespectedForSetValueWithPrecision() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDateInstantParser.parse("2012-01-02 22:31:02.333"));
		cal.setTimeZone(TimeZone.getTimeZone("EST"));

		Date time = cal.getTime();

		DateDt date = new DateDt();
		date.setValue(time, TemporalPrecisionEnum.DAY);
		assertEquals("2012-01-02", date.getValueAsString());
	}

	@Test
	public void testSetValueByString() {
		InstantDt i = new InstantDt();
		i.setValueAsString("2014-06-20T20:22:09Z");

		assertNotNull(i.getValue());
		assertNotNull(i.getValueAsString());

		assertEquals(1403295729000L, i.getValue().getTime());
		assertEquals("2014-06-20T20:22:09Z", i.getValueAsString());
	}

	@Test
	public void testToHumanDisplay() {
		DateTimeDt dt = new DateTimeDt("2012-01-05T12:00:00-08:00");
		String human = dt.toHumanDisplay();
		ourLog.info(human);
		assertThat(human, containsString("2012"));
		assertThat(human, containsString("12"));
	}

	public static void afterClass() {
		Locale.setDefault(ourDefaultLocale);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		/*
		 * We cache the default locale, but temporarily set it to a random value during this test. This helps ensure
		 * that there are no language specific dependencies in the test.
		 */
		ourDefaultLocale = Locale.getDefault();

		Locale[] available = { Locale.CANADA, Locale.GERMANY, Locale.TAIWAN };
		Locale newLocale = available[(int) (Math.random() * available.length)];
		Locale.setDefault(newLocale);

		ourLog.info("Tests are running in locale: " + newLocale.getDisplayName());
	}

}
