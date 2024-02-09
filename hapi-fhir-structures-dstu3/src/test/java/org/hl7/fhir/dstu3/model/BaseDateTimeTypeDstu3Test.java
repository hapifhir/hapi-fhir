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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.endsWith;
import static org.assertj.core.api.Assertions.fail;


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
		assertThat(dt.getValueAsString()).isEqualTo("1995-11-15T04:58:08Z");
	}

	@Test
	public void testAfter() {
		assertThat(new DateTimeType("2011-01-01T12:12:12Z").after(new DateTimeType("2011-01-01T12:12:11Z"))).isTrue();
		assertThat(new DateTimeType("2011-01-01T12:12:11Z").after(new DateTimeType("2011-01-01T12:12:12Z"))).isFalse();
		assertThat(new DateTimeType("2011-01-01T12:12:12Z").after(new DateTimeType("2011-01-01T12:12:12Z"))).isFalse();
	}

	@Test
	public void testBefore() {
		assertThat(new DateTimeType("2011-01-01T12:12:12Z").before(new DateTimeType("2011-01-01T12:12:11Z"))).isFalse();
		assertThat(new DateTimeType("2011-01-01T12:12:11Z").before(new DateTimeType("2011-01-01T12:12:12Z"))).isTrue();
		assertThat(new DateTimeType("2011-01-01T12:12:12Z").before(new DateTimeType("2011-01-01T12:12:12Z"))).isFalse();
	}

	@Disabled
	@Test
	public void testParseMinuteShouldFail() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
			dt.setValueAsString("2013-02-03T11:22");
			fail("");		} catch (DataFormatException e) {
			assertThat("Invalid date/time string (datatype DateTimeType does not support MINUTE precision): 2013-02-03T11:22").isEqualTo(e.getMessage());
		}
	}

	@Disabled
	@Test
	public void testParseMinuteZuluShouldFail() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
			dt.setValueAsString("2013-02-03T11:22Z");
			fail("");		} catch (DataFormatException e) {
			assertThat("Invalid date/time string (datatype DateTimeType does not support MINUTE precision): 2013-02-03T11:22Z").isEqualTo(e.getMessage());
		}
	}

	@Test()
	public void testAfterNull() {
		try {
			assertThat(new DateTimeType().after(new DateTimeType("2011-01-01T12:12:11Z"))).isTrue();
			fail("");		} catch (NullPointerException e) {
			assertThat(e.getMessage()).isEqualTo("This BaseDateTimeType does not contain a value (getValue() returns null)");
		}
		try {
			assertThat(new DateTimeType("2011-01-01T12:12:11Z").after(new DateTimeType())).isTrue();
			fail("");		} catch (NullPointerException e) {
			assertThat(e.getMessage()).isEqualTo("The given BaseDateTimeType does not contain a value (theDateTimeType.getValue() returns null)");
		}
		try {
			assertThat(new DateTimeType("2011-01-01T12:12:11Z").after(null)).isTrue();
			fail("");		} catch (NullPointerException e) {
			assertThat(e.getMessage()).isEqualTo("theDateTimeType must not be null");
		}
	}

	@Test()
	public void testBeforeNull1() {
		try {
			assertThat(new DateTimeType().before(new DateTimeType("2011-01-01T12:12:11Z"))).isTrue();
			fail("");		} catch (NullPointerException e) {
			assertThat(e.getMessage()).isEqualTo("This BaseDateTimeType does not contain a value (getValue() returns null)");
		}
		try {
			assertThat(new DateTimeType("2011-01-01T12:12:11Z").before(new DateTimeType())).isTrue();
			fail("");		} catch (NullPointerException e) {
			assertThat(e.getMessage()).isEqualTo("The given BaseDateTimeType does not contain a value (theDateTimeType.getValue() returns null)");
		}
		try {
			assertThat(new DateTimeType("2011-01-01T12:12:11Z").before(null)).isTrue();
			fail("");		} catch (NullPointerException e) {
			assertThat(e.getMessage()).isEqualTo("theDateTimeType must not be null");
		}
	}

	/**
	 * Test for #57
	 */
	@Test
	public void testConstructorRejectsInvalidPrecision() {
		try {
			new DateType("2001-01-02T11:13:33");
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).contains("precision");
		}
		try {
			new InstantType("2001-01-02");
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).contains("precision");
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
		assertThat(c.getStatusDateElement().getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);

		String encoded = ourCtx.newXmlParser().encodeResourceToString(c);
		assertThat(encoded).contains("value=\"2001-01-02T11:13:33\"");

		c = ourCtx.newXmlParser().parseResource(Goal.class, encoded);

		assertThat(c.getStatusDateElement().getValueAsString()).isEqualTo("2001-01-02T11:13:33");
		assertThat(c.getStatusDateElement().getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);

		ValidationResult outcome = ourCtx.newValidator().validateWithResult(c);
		String outcomeStr = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome());
		ourLog.info(outcomeStr);

		assertThat(outcomeStr).contains("date-primitive");
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
		assertThat(val).isEqualTo("2011-01-01T16:00:00+00:00");
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
		assertThat(dt.getYear().intValue()).isEqualTo(2011);
		assertThat(dt.getMonth().intValue()).isEqualTo(2);
		assertThat(dt.getDay().intValue()).isEqualTo(11);
		assertThat(dt.getHour().intValue()).isEqualTo(15);
		assertThat(dt.getMinute().intValue()).isEqualTo(44);
		assertThat(dt.getSecond().intValue()).isEqualTo(13);
		assertThat(dt.getMillis().intValue()).isEqualTo(275);
		assertThat(dt.getNanos().longValue()).isEqualTo(275647578L);

		dt = new InstantType();
		assertThat(dt.getYear()).isNull();
		assertThat(dt.getMonth()).isNull();
		assertThat(dt.getDay()).isNull();
		assertThat(dt.getHour()).isNull();
		assertThat(dt.getMinute()).isNull();
		assertThat(dt.getSecond()).isNull();
		assertThat(dt.getMillis()).isNull();
		assertThat(dt.getNanos()).isNull();
	}

	@Test
	public void testGetValueAsCalendar() {
		assertThat(new InstantType().getValueAsCalendar()).isNull();

		InstantType dt = new InstantType("2011-01-03T07:11:22.002-08:00");
		GregorianCalendar cal = dt.getValueAsCalendar();

		assertThat(cal.get(Calendar.YEAR)).isEqualTo(2011);
		assertThat(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(7);
		assertThat(cal.get(Calendar.MILLISECOND)).isEqualTo(2);
		assertThat(cal.getTimeZone().getID()).isEqualTo("GMT-08:00");
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
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2014-03-06 17:39:58.912");
	}

	@Test
	public void testMinutePrecisionEncode() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		cal.set(1990, Calendar.JANUARY, 3, 3, 22, 11);

		DateTimeType date = new DateTimeType();
		date.setValue(cal.getTime(), ca.uhn.fhir.model.api.TemporalPrecisionEnum.MINUTE);
		date.setTimeZone(TimeZone.getTimeZone("EST"));
		assertThat(date.getValueAsString()).isEqualTo("1990-01-02T21:22-05:00");

		date.setTimeZoneZulu(true);
		assertThat(date.getValueAsString()).isEqualTo("1990-01-03T02:22Z");
	}

	@Test
	public void testNewInstance() throws InterruptedException {
		InstantType now = InstantType.withCurrentTime();
		Thread.sleep(100);
		InstantType then = InstantType.withCurrentTime();
		assertThat(now.getValue().before(then.getValue())).isTrue();
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

		assertThat(encoded).isEqualTo("1960-09-07T00:44:25.012Z");

		type = new InstantType(encoded);
		assertThat(type.getYear().intValue()).isEqualTo(1960);
		assertThat(type.getMonth().intValue()).isEqualTo(8); // 0-indexed unlike LocalDateTime.of
		assertThat(type.getDay().intValue()).isEqualTo(7);
		assertThat(type.getHour().intValue()).isEqualTo(0);
		assertThat(type.getMinute().intValue()).isEqualTo(44);
		assertThat(type.getSecond().intValue()).isEqualTo(25);
		assertThat(type.getMillis().intValue()).isEqualTo(12);

	}

	@Test
	public void testParseDate() {
		new DateType("2012-03-31");
	}

	@Test
	public void testParseDay() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02-03");

		assertThat(myDateInstantParser.format(dt.getValue()).substring(0, 10)).isEqualTo("2013-02-03");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isNull();
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.DAY);
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

		assertThat(string).isEqualTo("2015-06-22T19:44:32.831Z");
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
		assertThat(myDateInstantZoneParser.format(dt.getValue())).isEqualTo("2014-10-11 10:11:00.000-0200");
	}

	@Test
	public void testParseInvalid() {
		try {
			DateTimeType dt = new DateTimeType();
			dt.setValueAsString("1974-12-25+10:00");
			fail("");		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo("Invalid date/time format: \"1974-12-25+10:00\": Expected character 'T' at index 10 but found +");
		}
		try {
			DateTimeType dt = new DateTimeType();
			dt.setValueAsString("1974-12-25Z");
			fail("");		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo("Invalid date/time format: \"1974-12-25Z\"");
		}
	}

	@Test
	public void testParseInvalidZoneOffset() {
		try {
			new DateTimeType("2010-01-01T00:00:00.1234-09:00Z");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo("Invalid date/time format: \"2010-01-01T00:00:00.1234-09:00Z\"");
		}
	}

	@Test
	public void testParseMalformatted() throws DataFormatException {
		try {
			new DateTimeType("20120102");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo("Invalid date/time format: \"20120102\": Expected character '-' at index 4 but found 0");
		}
	}

	@Test
	public void testParseMilli() throws DataFormatException {
		InstantType dt = new InstantType();
		dt.setValueAsString("2013-02-03T11:22:33.234");

		assertThat(myDateInstantParser.format(dt.getValue()).substring(0, 23)).isEqualTo("2013-02-03 11:22:33.234");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33.234");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isNull();
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.MILLI);
	}

	@Test
	public void testParseMilliZone() throws DataFormatException {
		InstantType dt = new InstantType();
		dt.setValueAsString("2013-02-03T11:22:33.234-02:00");

		assertThat(myDateInstantZoneParser.format(dt.getValue())).isEqualTo("2013-02-03 11:22:33.234-0200");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33.234-02:00");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isEqualTo(TimeZone.getTimeZone("GMT-02:00"));
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.MILLI);
	}

	@Test
	public void testParseMilliZulu() throws DataFormatException {
		InstantType dt = new InstantType();
		dt.setValueAsString("2013-02-03T11:22:33.234Z");

		assertThat(myDateInstantZoneParser.format(dt.getValue())).isEqualTo("2013-02-03 09:22:33.234-0200");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33.234Z");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(true);
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT");
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.MILLI);
	}

	@Test
	public void testParseMonth() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02");

		ourLog.info("Date: {}", dt.getValue());
		assertThat(dt.getValueAsString()).isEqualTo("2013-02");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isNull();
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.MONTH);

		assertThat(myDateInstantParser.format(dt.getValue()).substring(0, 7)).isEqualTo("2013-02");
	}

	@Test
	public void testParseMonthNoDashes() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
		dt.setValueAsString("201302");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo("Invalid date/time format: \"201302\": Expected character '-' at index 4 but found 0");
		}
	}

	@Test
	public void testParseMinute() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
			dt.setValueAsString("2013-02-03T11:22");
		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo("Invalid date/time string (datatype DateTimeType does not support MINUTE precision): 2013-02-03T11:22");
		}
	}

	@Test
	public void testParseMinuteZulu() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		try {
			dt.setValueAsString("2013-02-03T11:22Z");
		} catch (Exception e) {
			assertThat(e.getMessage()).isEqualTo("Invalid date/time string (datatype DateTimeType does not support MINUTE precision): 2013-02-03T11:22Z");
		}
	}

	@Test
	public void testParseSecond() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02-03T11:22:33");

		assertThat(myDateInstantParser.format(dt.getValue()).substring(0, 19)).isEqualTo("2013-02-03 11:22:33");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isNull();
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);
	}

	@Test
	public void testParseSecondZulu() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02-03T11:22:33Z");

		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33Z");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(true);
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT");
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);
	}

	@Test
	public void testParseSecondZone() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013-02-03T11:22:33-02:00");

		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33-02:00");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isEqualTo(TimeZone.getTimeZone("GMT-02:00"));
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly0millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00-09:00");

		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T00:00:00-09:00");
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2010-01-01 04:00:00.000");
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT-09:00");
		assertThat(dt.getTimeZone().getRawOffset()).isEqualTo(-32400000L);

		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T09:00:00Z");
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly1millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.1-09:00");

		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T00:00:00.1-09:00");
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2010-01-01 04:00:00.100");
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT-09:00");
		assertThat(dt.getTimeZone().getRawOffset()).isEqualTo(-32400000L);

		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T09:00:00.100Z");
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly2millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.12-09:00");

		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T00:00:00.12-09:00");
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2010-01-01 04:00:00.120");
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT-09:00");
		assertThat(dt.getTimeZone().getRawOffset()).isEqualTo(-32400000L);

		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T09:00:00.120Z");
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly3millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.123-09:00");

		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T00:00:00.123-09:00");
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2010-01-01 04:00:00.123");
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT-09:00");
		assertThat(dt.getTimeZone().getRawOffset()).isEqualTo(-32400000L);

		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T09:00:00.123Z");
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly4millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.1234-09:00");

		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T00:00:00.1234-09:00");
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2010-01-01 04:00:00.123");
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT-09:00");
		assertThat(dt.getTimeZone().getRawOffset()).isEqualTo(-32400000L);

		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T09:00:00.1234Z");
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly5millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeType dt = new DateTimeType("2010-01-01T00:00:00.12345-09:00");

		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T00:00:00.12345-09:00");
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2010-01-01 04:00:00.123");
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT-09:00");
		assertThat(dt.getTimeZone().getRawOffset()).isEqualTo(-32400000L);

		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T09:00:00.12345Z");
	}

	@Test
	public void testParseYear() throws DataFormatException {
		DateTimeType dt = new DateTimeType();
		dt.setValueAsString("2013");

		assertThat(myDateInstantParser.format(dt.getValue()).substring(0, 4)).isEqualTo("2013");
		assertThat(dt.getValueAsString()).isEqualTo("2013");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isNull();
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.YEAR);
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
		assertThat(out).contains("<birthDate value=\"2012-01-02\"/>");
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
		assertThat(date.getValueAsString()).isEqualTo("2012-01-02");
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
		assertThat(date.getValueAsString()).isEqualTo("2012-01-02");
	}

	@Test
	public void testSetPartialsDayFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setDay(15);
		assertThat(dt.getDay().intValue()).isEqualTo(15);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-15T15:44:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsHourFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setHour(23);
		assertThat(dt.getHour().intValue()).isEqualTo(23);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T23:44:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsInvalid() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setNanos(0);
		dt.setNanos(BaseDateTimeType.NANOS_PER_SECOND - 1);
		try {
			dt.setNanos(BaseDateTimeType.NANOS_PER_SECOND);
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("Value 1000000000 is not between allowable range: 0 - 999999999");
		}
	}

	@Test
	public void testSetPartialsMillisFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMillis(12);
		assertThat(dt.getMillis().intValue()).isEqualTo(12);
		assertThat(dt.getNanos().longValue()).isEqualTo(12 * BaseDateTimeType.NANOS_PER_MILLIS);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T15:44:13.012-08:00");
	}

	@Test
	public void testSetPartialsMinuteFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMinute(54);
		assertThat(dt.getMinute().intValue()).isEqualTo(54);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T15:54:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsMonthFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMonth(3);
		assertThat(dt.getMonth().intValue()).isEqualTo(3);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-04-11T15:44:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsNanosFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setNanos(100000000L);
		assertThat(dt.getNanos().longValue()).isEqualTo(100000000L);
		assertThat(dt.getMillis().intValue()).isEqualTo(100);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T15:44:13.100-08:00");
	}

	@Test
	public void testSetPartialsSecondFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setSecond(1);
		assertThat(dt.getSecond().intValue()).isEqualTo(1);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T15:44:01.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsYearFromExisting() {
		InstantType dt = new InstantType("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setYear(2016);
		assertThat(dt.getYear().intValue()).isEqualTo(2016);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2016-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetValueByString() {
		InstantType i = new InstantType();
		i.setValueAsString("2014-06-20T20:22:09Z");

		assertThat(i.getValue()).isNotNull();
		assertThat(i.getValueAsString()).isNotNull();

		assertThat(i.getValue().getTime()).isEqualTo(1403295729000L);
		assertThat(i.getValueAsString()).isEqualTo("2014-06-20T20:22:09Z");
	}

	@Test
	public void testToHumanDisplay() {
		DateTimeType dt = new DateTimeType("2012-01-05T12:00:00-08:00");
		String human = dt.toHumanDisplay();
		ourLog.info(human);
		assertThat(human).contains("2012");
		assertThat(human).contains("12");
	}

	private void validate(long millis, String expected) {
		InstantType dt;
		dt = new InstantType(new Date(millis));
		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo(expected);

		assertThat(dt.getMillis().longValue()).isEqualTo(millis % 1000);
		assertThat(dt.getNanos().longValue()).isEqualTo((millis % 1000) * BaseDateTimeType.NANOS_PER_MILLIS);

		dt = new InstantType();
		dt.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		dt.setValue(new Date(millis));
		assertThat(dt.getValueAsString()).isEqualTo(expected.replace("Z", "+00:00"));
	}

	private void validateMillisPartial(String input, int expected) {
		InstantType dt = new InstantType();
		dt.setValueAsString(input);
		Date date = dt.getValue();

		assertThat(date.getTime() % 1000).isEqualTo(expected);
	}

	private void verifyFails(String input) {
		try {
			DateTimeType dt = new DateTimeType();
			dt.setValueAsString(input);
			fail("");		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertThat(e.getMessage()).contains("Invalid date/time format: \"" + input + "\"");
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
