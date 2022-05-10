package org.hl7.fhir.dstu3.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.lang3.time.FastDateFormat;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BaseDateTimeTypeDstu3Test {
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static Locale ourDefaultLocale;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseDateTimeTypeDstu3Test.class);
	private SimpleDateFormat myDateInstantParser;
	private FastDateFormat myDateInstantZoneParser;

	@BeforeEach
	public void before() {
		myDateInstantParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		myDateInstantZoneParser = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ", TimeZone.getTimeZone("GMT-02:00"));
	}

	@Test
	public void setTimezoneToZulu() {
		DateTimeType dt = new DateTimeType(new Date(816411488000L));
		// assertEquals("1995-11-14T23:58:08", dt.getValueAsString());
		dt.setTimeZoneZulu(true);
		assertEquals("1995-11-15T04:58:08Z", dt.getValueAsString());
	}

	@Test
	public void testAfter() {
		assertTrue(new DateTimeType("2011-01-01T12:12:12Z").after(new DateTimeType("2011-01-01T12:12:11Z")));
		assertFalse(new DateTimeType("2011-01-01T12:12:11Z").after(new DateTimeType("2011-01-01T12:12:12Z")));
		assertFalse(new DateTimeType("2011-01-01T12:12:12Z").after(new DateTimeType("2011-01-01T12:12:12Z")));
	}

	@Test
	public void testBefore() {
		assertFalse(new DateTimeType("2011-01-01T12:12:12Z").before(new DateTimeType("2011-01-01T12:12:11Z")));
		assertTrue(new DateTimeType("2011-01-01T12:12:11Z").before(new DateTimeType("2011-01-01T12:12:12Z")));
		assertFalse(new DateTimeType("2011-01-01T12:12:12Z").before(new DateTimeType("2011-01-01T12:12:12Z")));
	}

	@Test
	public void testParseMinuteShouldFail() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
			dt.setValueAsString("2013-02-03T11:22");
			fail();
		} catch (DataFormatException e) {
			assertEquals(e.getMessage(), "Invalid date/time string (datatype DateTimeType does not support MINUTE precision): 2013-02-03T11:22");
		}
	}

	@Test
	public void testParseMinuteZuluShouldFail() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
			dt.setValueAsString("2013-02-03T11:22Z");
			fail();
		} catch (DataFormatException e) {
			assertEquals(e.getMessage(), "Invalid date/time string (datatype DateTimeType does not support MINUTE precision): 2013-02-03T11:22Z");
		}
	}

	@Test()
	public void testAfterNull() {
		try {
			assertTrue(new DateTimeType().after(new DateTimeType("2011-01-01T12:12:11Z")));
			fail();
		} catch (NullPointerException e) {
			assertEquals("This BaseDateTimeType does not contain a value (getValue() returns null)", e.getMessage());
		}
		try {
			assertTrue(new DateTimeType("2011-01-01T12:12:11Z").after(new DateTimeType()));
			fail();
		} catch (NullPointerException e) {
			assertEquals("The given BaseDateTimeType does not contain a value (theDateTimeType.getValue() returns null)", e.getMessage());
		}
		try {
			assertTrue(new DateTimeType("2011-01-01T12:12:11Z").after(null));
			fail();
		} catch (NullPointerException e) {
			assertEquals("theDateTimeType must not be null", e.getMessage());
		}
	}

	@Test()
	public void testBeforeNull1() {
		try {
			assertTrue(new DateTimeType().before(new DateTimeType("2011-01-01T12:12:11Z")));
			fail();
		} catch (NullPointerException e) {
			assertEquals("This BaseDateTimeType does not contain a value (getValue() returns null)", e.getMessage());
		}
		try {
			assertTrue(new DateTimeType("2011-01-01T12:12:11Z").before(new DateTimeType()));
			fail();
		} catch (NullPointerException e) {
			assertEquals("The given BaseDateTimeType does not contain a value (theDateTimeType.getValue() returns null)", e.getMessage());
		}
		try {
			assertTrue(new DateTimeType("2011-01-01T12:12:11Z").before(null));
			fail();
		} catch (NullPointerException e) {
			assertEquals("theDateTimeType must not be null", e.getMessage());
		}
	}

	/**
	 * Test for #57
	 */
	@Test
	public void testConstructorRejectsInvalidPrecision() {
		try {
			new DateType("2001-01-02T11:13:33");
			fail();
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), containsString("precision"));
		}
		try {
			new InstantType("2001-01-02");
			fail();
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), containsString("precision"));
		}
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
		verifyFails("2015-02-30");
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

	/**
	 * Test for #57
	 */
	@Test
	public void testDateParsesWithInvalidPrecision() {
		Goal c = new Goal();
		c.setStatusDateElement(new DateType());
		c.getStatusDateElement().setValueAsString("2001-01-02T11:13:33");
		assertEquals(TemporalPrecisionEnum.SECOND, c.getStatusDateElement().getPrecision());

		String encoded = ourCtx.newXmlParser().encodeResourceToString(c);
		assertThat(encoded, Matchers.containsString("value=\"2001-01-02T11:13:33\""));

		c = ourCtx.newXmlParser().parseResource(Goal.class, encoded);

		assertEquals("2001-01-02T11:13:33", c.getStatusDateElement().getValueAsString());
		assertEquals(TemporalPrecisionEnum.SECOND, c.getStatusDateElement().getPrecision());

		ValidationResult outcome = ourCtx.newValidator().validateWithResult(c);
		String outcomeStr = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome());
		ourLog.info(outcomeStr);

		assertThat(outcomeStr, containsString("date-primitive"));
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
	public void testDateTimeFormatsInvalidMillis() {
		verifyFails("1974-12-01T00:00:00.AZ");
		verifyFails("1974-12-01T00:00:00.-Z");
		verifyFails("1974-12-01T00:00:00.-1Z");
		verifyFails("1974-12-01T00:00:00..1111Z");
	}

	@Test
	public void testDateTimeInLocalTimezone() {
		DateTimeType dt = DateTimeType.now();
		String str = dt.getValueAsString();
		char offset = str.charAt(19);
		if (offset != '+' && offset != '-' && offset != 'Z') {
			fail("No timezone provided: " + str);
		}
	}

	@Test
	public void testEncodeOffset() throws Exception {
		String offset = InstantType.withCurrentTime().setTimeZone(TimeZone.getTimeZone("America/Toronto")).getValueAsString();
		assertThat(offset, either(endsWith("-05:00")).or(endsWith("-04:00")));
	}

	@Test
	public void testEncodeZeroOffset() {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2011-01-01T12:00:00-04:00");
		dt.setTimeZone(TimeZone.getTimeZone("GMT-0:00"));

		String val = dt.getValueAsString();
		assertEquals("2011-01-01T16:00:00+00:00", val);
	}

	@Test
	public void testFromTime() {
		long millis;

		millis = 1466022208001L;
		String expected = "2016-06-15T20:23:28.001Z";
		validate(millis, expected);

		millis = 1466022208123L;
		expected = "2016-06-15T20:23:28.123Z";
		validate(millis, expected);

		millis = 1466022208100L;
		expected = "2016-06-15T20:23:28.100Z";
		validate(millis, expected);

		millis = 1466022208000L;
		expected = "2016-06-15T20:23:28.000Z";
		validate(millis, expected);

	}

	@Test
	public void testGetPartials() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		assertEquals(2011, dt.getYear().intValue());
		assertEquals(2, dt.getMonth().intValue());
		assertEquals(11, dt.getDay().intValue());
		assertEquals(15, dt.getHour().intValue());
		assertEquals(44, dt.getMinute().intValue());
		assertEquals(13, dt.getSecond().intValue());
		assertEquals(275, dt.getMillis().intValue());
		assertEquals(275647578L, dt.getNanos().longValue());

		dt = new InstantType();
		assertEquals(null, dt.getYear());
		assertEquals(null, dt.getMonth());
		assertEquals(null, dt.getDay());
		assertEquals(null, dt.getHour());
		assertEquals(null, dt.getMinute());
		assertEquals(null, dt.getSecond());
		assertEquals(null, dt.getMillis());
		assertEquals(null, dt.getNanos());
	}

	@Test
	public void testGetValueAsCalendar() {
		assertNull(new InstantType().getValueAsCalendar());

		InstantType dt = new InstantType("2011-01-03T07:11:22.002-08:00");
		GregorianCalendar cal = dt.getValueAsCalendar();

		assertEquals(2011, cal.get(Calendar.YEAR));
		assertEquals(7, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(2, cal.get(Calendar.MILLISECOND));
		assertEquals("GMT-08:00", cal.getTimeZone().getID());
	}

	@Test
	public void testInstantInLocalTimezone() {
		InstantType dt = InstantType.withCurrentTime();
		String str = dt.getValueAsString();
		char offset = str.charAt(23);
		if (offset != '+' && offset != '-' && offset != 'Z') {
			fail("No timezone provided: " + str);
		}
	}

	@Test
	public void testLargePrecision() {
		DateTimeType dt = new DateTimeType("2014-03-06T22:09:58.9121174+04:30");

		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("GMT"));
		assertEquals("2014-03-06 17:39:58.912", myDateInstantParser.format(dt.getValue()));
	}

	@Test
	public void testMinutePrecisionEncode() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		cal.set(1990, Calendar.JANUARY, 3, 3, 22, 11);

		DateTimeType date = new DateTimeType();
		date.setValue(cal.getTime(), ca.uhn.fhir.model.api.TemporalPrecisionEnum.MINUTE);
		date.setTimeZone(TimeZone.getTimeZone("EST"));
		assertEquals("1990-01-02T21:22-05:00", date.getValueAsString());

		date.setTimeZoneZulu(true);
		assertEquals("1990-01-03T02:22Z", date.getValueAsString());
	}

	@Test
	public void testNewInstance() throws InterruptedException {
		InstantType now = InstantType.withCurrentTime();
		Thread.sleep(100);
		InstantType then = InstantType.withCurrentTime();
		assertTrue(now.getValue().before(then.getValue()));
	}

	/**
	 * See #444
	 */
	@Test
	public void testParseAndEncodeDateBefore1970() {
		LocalDateTime ldt = LocalDateTime.of(1960, 9, 7, 0, 44, 25, 12387401);
		Date from = Date.from(ldt.toInstant(ZoneOffset.UTC));
		InstantType type = (InstantType) new InstantType(from).setTimeZoneZulu(true);
		String encoded = type.asStringValue();

		ourLog.info("LDT:      " + ldt.toString());
		ourLog.info("Expected: " + "1960-09-07T00:44:25.012");
		ourLog.info("Actual:   " + encoded);

		assertEquals("1960-09-07T00:44:25.012Z", encoded);

		type = new InstantType(encoded);
		assertEquals(1960, type.getYear().intValue());
		assertEquals(8, type.getMonth().intValue()); // 0-indexed unlike LocalDateTime.of
		assertEquals(7, type.getDay().intValue());
		assertEquals(0, type.getHour().intValue());
		assertEquals(44, type.getMinute().intValue());
		assertEquals(25, type.getSecond().intValue());
		assertEquals(12, type.getMillis().intValue());

	}

	@Test
	public void testParseDate() {
		new DateType("2012-03-31");
	}

	@Test
	public void testParseDay() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
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
			DateTimeType dt = new DateTimeType("9999-13-01");
			fail(dt.getValue().toString());
		} catch (DataFormatException e) {
			// good
		}

	}

	@Test
	public void testParseHandlesMillis() {
		InstantType dt = new InstantType();
		dt.setValueAsString("2015-06-22T15:44:32.831-04:00");
		Date date = dt.getValue();

		InstantType dt2 = new InstantType();
		dt2.setValue(date);
		dt2.setTimeZoneZulu(true);
		String string = dt2.getValueAsString();

		assertEquals("2015-06-22T19:44:32.831Z", string);
	}

	@Test
	public void testParseHandlesMillisPartial() {
		// .12 should be 120ms
		validateMillisPartial("2015-06-22T00:00:00.1Z", 100);
		validateMillisPartial("2015-06-22T00:00:00.12Z", 120);
		validateMillisPartial("2015-06-22T00:00:00.123Z", 123);
		validateMillisPartial("2015-06-22T00:00:00.1234Z", 123);
		validateMillisPartial("2015-06-22T00:00:00.01Z", 10);
		validateMillisPartial("2015-06-22T00:00:00.012Z", 12);
		validateMillisPartial("2015-06-22T00:00:00.0123Z", 12);
		validateMillisPartial("2015-06-22T00:00:00.001Z", 1);
		validateMillisPartial("2015-06-22T00:00:00.0012Z", 1);
		validateMillisPartial("2015-06-22T00:00:00.00123Z", 1);
	}

	/*
	 * Just to be lenient
	 */
	@Test
	public void testParseIgnoresLeadingAndTrailingSpace() {
		DateTimeType dt = new DateTimeType("  2014-10-11T12:11:00Z      ");
		assertEquals("2014-10-11 10:11:00.000-0200", myDateInstantZoneParser.format(dt.getValue()));
	}

	@Test
	public void testParseInvalid() {
		try {
			DateTimeType dt = new DateTimeType();
			dt.setValueAsString("1974-12-25+10:00");
			fail();
		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertEquals("Invalid date/time format: \"1974-12-25+10:00\": Expected character 'T' at index 10 but found +", e.getMessage());
		}
		try {
			DateTimeType dt = new DateTimeType();
			dt.setValueAsString("1974-12-25Z");
			fail();
		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertEquals("Invalid date/time format: \"1974-12-25Z\"", e.getMessage());
		}
	}

	@Test
	public void testParseInvalidZoneOffset() {
		try {
			new DateTimeType("2010-01-01T00:00:00.1234-09:00Z");
			fail();
		} catch (DataFormatException e) {
			assertEquals("Invalid date/time format: \"2010-01-01T00:00:00.1234-09:00Z\"", e.getMessage());
		}
	}

	@Test
	public void testParseMalformatted() throws DataFormatException {
		try {
			new DateTimeType("20120102");
			fail();
		} catch (DataFormatException e) {
			assertEquals("Invalid date/time format: \"20120102\": Expected character '-' at index 4 but found 0", e.getMessage());
		}
	}

	@Test
	public void testParseMilli() throws DataFormatException {
		InstantType dt = new InstantType();
		dt.setValueAsString("2013-02-03T11:22:33.234");

		assertEquals("2013-02-03 11:22:33.234", myDateInstantParser.format(dt.getValue()).substring(0, 23));
		assertEquals("2013-02-03T11:22:33.234", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MILLI, dt.getPrecision());
	}

	@Test
	public void testParseMilliZone() throws DataFormatException {
		InstantType dt = new InstantType();
		dt.setValueAsString("2013-02-03T11:22:33.234-02:00");

		assertEquals("2013-02-03 11:22:33.234-0200", myDateInstantZoneParser.format(dt.getValue()));
		assertEquals("2013-02-03T11:22:33.234-02:00", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertEquals(TimeZone.getTimeZone("GMT-02:00"), dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MILLI, dt.getPrecision());
	}

	@Test
	public void testParseMilliZulu() throws DataFormatException {
		InstantType dt = new InstantType();
		dt.setValueAsString("2013-02-03T11:22:33.234Z");

		assertEquals("2013-02-03 09:22:33.234-0200", myDateInstantZoneParser.format(dt.getValue()));
		assertEquals("2013-02-03T11:22:33.234Z", dt.getValueAsString());
		assertEquals(true, dt.isTimeZoneZulu());
		assertEquals("GMT", dt.getTimeZone().getID());
		assertEquals(TemporalPrecisionEnum.MILLI, dt.getPrecision());
	}

	@Test
	public void testParseMonth() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02");

		ourLog.info("Date: {}", dt.getValue());
		assertEquals("2013-02", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MONTH, dt.getPrecision());

		assertEquals("2013-02", myDateInstantParser.format(dt.getValue()).substring(0, 7));
	}

	@Test
	public void testParseMonthNoDashes() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
		dt.setValueAsString("201302");
			fail();
		} catch (DataFormatException e) {
			assertEquals("Invalid date/time format: \"201302\": Expected character '-' at index 4 but found 0", e.getMessage());
		}
	}

	@Test
	public void testParseMinute() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
			dt.setValueAsString("2013-02-03T11:22");
		} catch (DataFormatException e) {
			assertEquals("Invalid date/time string (datatype DateTimeType does not support MINUTE precision): 2013-02-03T11:22", e.getMessage());
		}
	}

	@Test
	public void testParseMinuteZulu() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
			dt.setValueAsString("2013-02-03T11:22Z");
		} catch (Exception e) {
			assertEquals("Invalid date/time string (datatype DateTimeType does not support MINUTE precision): 2013-02-03T11:22Z", e.getMessage());
		}
	}

	@Test
	public void testParseSecond() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02-03T11:22:33");

		assertEquals("2013-02-03 11:22:33", myDateInstantParser.format(dt.getValue()).substring(0, 19));
		assertEquals("2013-02-03T11:22:33", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.SECOND, dt.getPrecision());
	}

	@Test
	public void testParseSecondZulu() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02-03T11:22:33Z");

		assertEquals("2013-02-03T11:22:33Z", dt.getValueAsString());
		assertEquals(true, dt.isTimeZoneZulu());
		assertEquals("GMT", dt.getTimeZone().getID());
		assertEquals(TemporalPrecisionEnum.SECOND, dt.getPrecision());
	}

	@Test
	public void testParseSecondZone() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02-03T11:22:33-02:00");

		assertEquals("2013-02-03T11:22:33-02:00", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertEquals(TimeZone.getTimeZone("GMT-02:00"), dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.SECOND, dt.getPrecision());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly0millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00-09:00");

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

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.1-09:00");

		assertEquals("2010-01-01T00:00:00.1-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.100", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());

		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00.100Z", dt.getValueAsString());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly2millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.12-09:00");

		assertEquals("2010-01-01T00:00:00.12-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.120", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());

		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00.120Z", dt.getValueAsString());
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly3millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.123-09:00");

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

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.1234-09:00");

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

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.12345-09:00");

		assertEquals("2010-01-01T00:00:00.12345-09:00", dt.getValueAsString());
		assertEquals("2010-01-01 04:00:00.123", myDateInstantParser.format(dt.getValue()));
		assertEquals("GMT-09:00", dt.getTimeZone().getID());
		assertEquals(-32400000L, dt.getTimeZone().getRawOffset());

		dt.setTimeZoneZulu(true);
		assertEquals("2010-01-01T09:00:00.12345Z", dt.getValueAsString());
	}

	@Test
	public void testParseYear() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
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
		patient.setBirthDateElement(new DateType(cal.getTime(), TemporalPrecisionEnum.DAY));
		String out = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(out, containsString("<birthDate value=\"2012-01-02\"/>"));
	}

	/**
	 * See HAPI #101 - https://github.com/hapifhir/hapi-fhir/issues/101
	 */
	@Test
	public void testPrecisionRespectedForSetValue() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDateInstantParser.parse("2012-01-02 22:31:02.333"));
		cal.setTimeZone(TimeZone.getTimeZone("EST"));

		Date time = cal.getTime();

		DateType date = new DateType();
		date.setValue(time);
		assertEquals("2012-01-02", date.getValueAsString());
	}

	/**
	 * See HAPI #101 - https://github.com/hapifhir/hapi-fhir/issues/101
	 */
	@Test
	public void testPrecisionRespectedForSetValueWithPrecision() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDateInstantParser.parse("2012-01-02 22:31:02.333"));
		cal.setTimeZone(TimeZone.getTimeZone("EST"));

		Date time = cal.getTime();

		DateType date = new DateType();
		date.setValue(time, ca.uhn.fhir.model.api.TemporalPrecisionEnum.DAY);
		assertEquals("2012-01-02", date.getValueAsString());
	}

	@Test
	public void testSetPartialsDayFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setDay(15);
		assertEquals(15, dt.getDay().intValue());
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertEquals("2011-03-15T15:44:13.27564757855254768473697463986328969635-08:00", valueAsString);
	}

	@Test
	public void testSetPartialsHourFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setHour(23);
		assertEquals(23, dt.getHour().intValue());
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertEquals("2011-03-11T23:44:13.27564757855254768473697463986328969635-08:00", valueAsString);
	}

	@Test
	public void testSetPartialsInvalid() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setNanos(0);
		dt.setNanos(BaseDateTimeType.NANOS_PER_SECOND - 1);
		try {
			dt.setNanos(BaseDateTimeType.NANOS_PER_SECOND);
		} catch (IllegalArgumentException e) {
			assertEquals("Value 1000000000 is not between allowable range: 0 - 999999999", e.getMessage());
		}
	}

	@Test
	public void testSetPartialsMillisFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMillis(12);
		assertEquals(12, dt.getMillis().intValue());
		assertEquals(12 * BaseDateTimeType.NANOS_PER_MILLIS, dt.getNanos().longValue());
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertEquals("2011-03-11T15:44:13.012-08:00", valueAsString);
	}

	@Test
	public void testSetPartialsMinuteFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMinute(54);
		assertEquals(54, dt.getMinute().intValue());
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertEquals("2011-03-11T15:54:13.27564757855254768473697463986328969635-08:00", valueAsString);
	}

	@Test
	public void testSetPartialsMonthFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMonth(3);
		assertEquals(3, dt.getMonth().intValue());
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertEquals("2011-04-11T15:44:13.27564757855254768473697463986328969635-08:00", valueAsString);
	}

	@Test
	public void testSetPartialsNanosFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setNanos(100000000L);
		assertEquals(100000000L, dt.getNanos().longValue());
		assertEquals(100, dt.getMillis().intValue());
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertEquals("2011-03-11T15:44:13.100-08:00", valueAsString);
	}

	@Test
	public void testSetPartialsSecondFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setSecond(1);
		assertEquals(1, dt.getSecond().intValue());
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertEquals("2011-03-11T15:44:01.27564757855254768473697463986328969635-08:00", valueAsString);
	}

	@Test
	public void testSetPartialsYearFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setYear(2016);
		assertEquals(2016, dt.getYear().intValue());
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertEquals("2016-03-11T15:44:13.27564757855254768473697463986328969635-08:00", valueAsString);
	}

	@Test
	public void testSetValueByString() {
		InstantType i = new InstantType();
		i.setValueAsString("2014-06-20T20:22:09Z");

		assertNotNull(i.getValue());
		assertNotNull(i.getValueAsString());

		assertEquals(1403295729000L, i.getValue().getTime());
		assertEquals("2014-06-20T20:22:09Z", i.getValueAsString());
	}

	@Test
	public void testToHumanDisplay() {
		DateTimeType dt = new DateTimeType("2012-01-05T12:00:00-08:00");
		String human = dt.toHumanDisplay();
		ourLog.info(human);
		assertThat(human, containsString("2012"));
		assertThat(human, containsString("12"));
	}

	private void validate(long millis, String expected) {
		InstantType dt;
		dt = new InstantType(new Date(millis));
		dt.setTimeZoneZulu(true);
		assertEquals(expected, dt.getValueAsString());

		assertEquals(millis % 1000, dt.getMillis().longValue());
		assertEquals((millis % 1000) * BaseDateTimeType.NANOS_PER_MILLIS, dt.getNanos().longValue());

		dt = new InstantType();
		dt.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		dt.setValue(new Date(millis));
		assertEquals(expected.replace("Z", "+00:00"), dt.getValueAsString());
	}

	private void validateMillisPartial(String input, int expected) {
		InstantType dt = new InstantType();
		dt.setValueAsString(input);
		Date date = dt.getValue();

		assertEquals(expected, date.getTime() % 1000);
	}

	private void verifyFails(String input) {
		try {
			DateTimeType dt = new DateTimeType();
			dt.setValueAsString(input);
			fail();
		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertThat(e.getMessage(), containsString("Invalid date/time format: \"" + input + "\""));
		}
	}

	public static void afterClass() {
		Locale.setDefault(ourDefaultLocale);
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() {
		/*
		 * We cache the default locale, but temporarily set it to a random value during this test. This helps ensure that
		 * there are no language specific dependencies in the test.
		 */
		ourDefaultLocale = Locale.getDefault();

		Locale[] available = { Locale.CANADA, Locale.GERMANY, Locale.TAIWAN };
		Locale newLocale = available[(int) (Math.random() * available.length)];
		Locale.setDefault(newLocale);

		ourLog.info("Tests are running in locale: " + newLocale.getDisplayName());
	}

}
