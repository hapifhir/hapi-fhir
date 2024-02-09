package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Patient;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.endsWith;
import static org.assertj.core.api.Assertions.fail;


public class BaseDateTimeDtDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseDateTimeDtDstu2Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static Locale ourDefaultLocale;
	private SimpleDateFormat myDateInstantParser;
	private FastDateFormat myDateInstantZoneParser;

	/**
	 * See #444
	 */
	@Test
	public void testParseAndEncodeDateBefore1970() {
		LocalDateTime ldt = LocalDateTime.of(1960, 9, 7, 0, 44, 25, 12387401);
		Date from = Date.from(ldt.toInstant(ZoneOffset.UTC));
		InstantDt type = (InstantDt) new InstantDt(from).setTimeZoneZulu(true);
		String encoded = type.getValueAsString();

		ourLog.info("LDT:      " + ldt.toString());
		ourLog.info("Expected: " + "1960-09-07T00:44:25.012");
		ourLog.info("Actual:   " + encoded);

		assertThat(encoded).isEqualTo("1960-09-07T00:44:25.012Z");

		type = new InstantDt(encoded);
		assertThat(type.getYear().intValue()).isEqualTo(1960);
		assertThat(type.getMonth().intValue()).isEqualTo(8); // 0-indexed unlike LocalDateTime.of
		assertThat(type.getDay().intValue()).isEqualTo(7);
		assertThat(type.getHour().intValue()).isEqualTo(0);
		assertThat(type.getMinute().intValue()).isEqualTo(44);
		assertThat(type.getSecond().intValue()).isEqualTo(25);
		assertThat(type.getMillis().intValue()).isEqualTo(12);

	}

	@Test
	public void testFromTime() {
		long millis;
		InstantDt dt;

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


	private void validate(long millis, String expected) {
		InstantDt dt;
		dt = new InstantDt(new Date(millis));
		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo(expected);

		assertThat(dt.getMillis().longValue()).isEqualTo(millis % 1000);
		assertThat(dt.getNanos().longValue()).isEqualTo((millis % 1000) * BaseDateTimeDt.NANOS_PER_MILLIS);

		dt = new InstantDt();
		dt.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
		dt.setValue(new Date(millis));
		assertThat(dt.getValueAsString()).isEqualTo(expected.replace("Z", "+00:00"));
	}


	@Test
	public void testSetPartialsYearFromExisting() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setYear(2016);
		assertThat(dt.getYear().intValue()).isEqualTo(2016);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2016-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsMonthFromExisting() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMonth(3);
		assertThat(dt.getMonth().intValue()).isEqualTo(3);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-04-11T15:44:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsDayFromExisting() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setDay(15);
		assertThat(dt.getDay().intValue()).isEqualTo(15);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-15T15:44:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsHourFromExisting() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setHour(23);
		assertThat(dt.getHour().intValue()).isEqualTo(23);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T23:44:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsMinuteFromExisting() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMinute(54);
		assertThat(dt.getMinute().intValue()).isEqualTo(54);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T15:54:13.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsSecondFromExisting() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setSecond(1);
		assertThat(dt.getSecond().intValue()).isEqualTo(1);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T15:44:01.27564757855254768473697463986328969635-08:00");
	}

	@Test
	public void testSetPartialsMillisFromExisting() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setMillis(12);
		assertThat(dt.getMillis().intValue()).isEqualTo(12);
		assertThat(dt.getNanos().longValue()).isEqualTo(12 * BaseDateTimeDt.NANOS_PER_MILLIS);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T15:44:13.012-08:00");
	}

	@Test
	public void testSetPartialsNanosFromExisting() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setNanos(100000000L);
		assertThat(dt.getNanos().longValue()).isEqualTo(100000000L);
		assertThat(dt.getMillis().intValue()).isEqualTo(100);
		String valueAsString = dt.getValueAsString();
		ourLog.info(valueAsString);
		assertThat(valueAsString).isEqualTo("2011-03-11T15:44:13.100-08:00");
	}

	@Test
	public void testSetPartialsInvalid() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		dt.setNanos(0);
		dt.setNanos(BaseDateTimeDt.NANOS_PER_SECOND - 1);
		try {
			dt.setNanos(BaseDateTimeDt.NANOS_PER_SECOND);
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1884) + "Value 1000000000 is not between allowable range: 0 - 999999999");
		}
	}

	@Test
	public void testGetPartials() {
		InstantDt dt = new InstantDt("2011-03-11T15:44:13.27564757855254768473697463986328969635-08:00");
		assertThat(dt.getYear().intValue()).isEqualTo(2011);
		assertThat(dt.getMonth().intValue()).isEqualTo(2);
		assertThat(dt.getDay().intValue()).isEqualTo(11);
		assertThat(dt.getHour().intValue()).isEqualTo(15);
		assertThat(dt.getMinute().intValue()).isEqualTo(44);
		assertThat(dt.getSecond().intValue()).isEqualTo(13);
		assertThat(dt.getMillis().intValue()).isEqualTo(275);
		assertThat(dt.getNanos().longValue()).isEqualTo(275647578L);

		dt = new InstantDt();
		assertThat(dt.getYear()).isEqualTo(null);
		assertThat(dt.getMonth()).isEqualTo(null);
		assertThat(dt.getDay()).isEqualTo(null);
		assertThat(dt.getHour()).isEqualTo(null);
		assertThat(dt.getMinute()).isEqualTo(null);
		assertThat(dt.getSecond()).isEqualTo(null);
		assertThat(dt.getMillis()).isEqualTo(null);
		assertThat(dt.getNanos()).isEqualTo(null);
	}

	@BeforeEach
	public void before() {
		myDateInstantParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		myDateInstantZoneParser = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ", TimeZone.getTimeZone("GMT-02:00"));
	}

	@Test
	public void setTimezoneToZulu() {
		DateTimeDt dt = new DateTimeDt(new Date(816411488000L));
		// assertEquals("1995-11-14T23:58:08", dt.getValueAsString());
		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo("1995-11-15T04:58:08Z");
	}

	/**
	 * Test for #57
	 */
	@Test
	public void testConstructorRejectsInvalidPrecision() {
		try {
			new DateDt("2001-01-02T11:13:33");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).contains("precision");
		}
		try {
			new InstantDt("2001-01-02");
			fail("");		} catch (DataFormatException e) {
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
		Condition c = new Condition();
		c.setDateRecorded(new DateDt());
		c.getDateRecordedElement().setValueAsString("2001-01-02T11:13:33");
		assertThat(c.getDateRecordedElement().getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);

		String encoded = ourCtx.newXmlParser().encodeResourceToString(c);
		assertThat(encoded).contains("value=\"2001-01-02T11:13:33\"");

		c = ourCtx.newXmlParser().parseResource(Condition.class, encoded);

		assertThat(c.getDateRecordedElement().getValueAsString()).isEqualTo("2001-01-02T11:13:33");
		assertThat(c.getDateRecordedElement().getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);

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
		DateTimeDt dt = DateTimeDt.withCurrentTime();
		String str = dt.getValueAsString();
		char offset = str.charAt(19);
		if (offset != '+' && offset != '-' && offset != 'Z') {
			fail("No timezone provided: " + str);
		}
	}

	@Test
	public void testEncodeOffset() {
		String offset = InstantDt.withCurrentTime().setTimeZone(TimeZone.getTimeZone("America/Toronto")).getValueAsString();
		assertThat(offset, either(endsWith("-05:00")).or(endsWith("-04:00")));
	}

	@Test
	public void testEncodeZeroOffset() {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2011-01-01T12:00:00-04:00");
		dt.setTimeZone(TimeZone.getTimeZone("GMT-0:00"));

		String val = dt.getValueAsString();
		assertThat(val).isEqualTo("2011-01-01T16:00:00+00:00");
	}

	@Test
	public void testGetValueAsCalendar() {
		assertThat(new InstantDt().getValueAsCalendar()).isNull();

		InstantDt dt = new InstantDt("2011-01-03T07:11:22.002-08:00");
		GregorianCalendar cal = dt.getValueAsCalendar();

		assertThat(cal.get(Calendar.YEAR)).isEqualTo(2011);
		assertThat(cal.get(Calendar.HOUR_OF_DAY)).isEqualTo(7);
		assertThat(cal.get(Calendar.MILLISECOND)).isEqualTo(2);
		assertThat(cal.getTimeZone().getID()).isEqualTo("GMT-08:00");
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

		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("GMT"));
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2014-03-06 17:39:58.912");
	}

	@Test
	public void testMinutePrecisionEncode() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		cal.set(1990, Calendar.JANUARY, 3, 3, 22, 11);

		DateTimeDt date = new DateTimeDt();
		date.setValue(cal.getTime(), TemporalPrecisionEnum.MINUTE);
		date.setTimeZone(TimeZone.getTimeZone("EST"));
		assertThat(date.getValueAsString()).isEqualTo("1990-01-02T21:22-05:00");

		date.setTimeZoneZulu(true);
		assertThat(date.getValueAsString()).isEqualTo("1990-01-03T02:22Z");
	}

	@Test
	public void testNewInstance() throws InterruptedException {
		InstantDt now = InstantDt.withCurrentTime();
		Thread.sleep(100);
		InstantDt then = InstantDt.withCurrentTime();
		assertThat(now.getValue().before(then.getValue())).isTrue();
	}

	@Test
	public void testParseDate() {
		new DateDt("2012-03-31");
	}

	@Test
	public void testParseDay() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
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
		DateTimeDt dt = new DateTimeDt("  2014-10-11T12:11:00Z      ");
		assertThat(myDateInstantZoneParser.format(dt.getValue())).isEqualTo("2014-10-11 10:11:00.000-0200");
	}

	@Test
	public void testParseInvalidZoneOffset() {
		try {
			new DateTimeDt("2010-01-01T00:00:00.1234-09:00Z");
			fail("");		} catch (DataFormatException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1882) + "Invalid date/time format: \"2010-01-01T00:00:00.1234-09:00Z\"");
		}
	}

	@Test
	public void testParseMalformatted() throws DataFormatException {
		assertThatExceptionOfType(DataFormatException.class).isThrownBy(() -> {
			new DateTimeDt("20120102");
		});
	}

	@Test
	public void testParseMilli() throws DataFormatException {
		InstantDt dt = new InstantDt();
		dt.setValueAsString("2013-02-03T11:22:33.234");

		assertThat(myDateInstantParser.format(dt.getValue()).substring(0, 23)).isEqualTo("2013-02-03 11:22:33.234");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33.234");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isNull();
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.MILLI);
	}

	@Test
	public void testParseMilliZone() throws DataFormatException {
		InstantDt dt = new InstantDt();
		dt.setValueAsString("2013-02-03T11:22:33.234-02:00");

		assertThat(myDateInstantZoneParser.format(dt.getValue())).isEqualTo("2013-02-03 11:22:33.234-0200");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33.234-02:00");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isEqualTo(TimeZone.getTimeZone("GMT-02:00"));
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.MILLI);
	}

	@Test
	public void testParseMilliZulu() throws DataFormatException {
		InstantDt dt = new InstantDt();
		dt.setValueAsString("2013-02-03T11:22:33.234Z");

		assertThat(myDateInstantZoneParser.format(dt.getValue())).isEqualTo("2013-02-03 09:22:33.234-0200");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33.234Z");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(true);
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT");
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.MILLI);
	}

	@Test
	public void testParseMonth() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
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
		assertThatExceptionOfType(DataFormatException.class).isThrownBy(() -> {
			DateTimeDt dt = new DateTimeDt();
			dt.setValueAsString("201302");
		});
	}

	@Test
	public void testParseMinuteShouldFail() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		try {
			dt.setValueAsString("2013-02-03T11:22");
			fail("");		} catch (DataFormatException e) {
			assertThat(Msg.code(1885) + "Invalid date/time string (datatype DateTimeDt does not support MINUTE precision): 2013-02-03T11:22").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void testParseMinuteZuluShouldFail() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		try {
			dt.setValueAsString("2013-02-03T11:22Z");
			fail("");		} catch (DataFormatException e) {
			assertThat(Msg.code(1885) + "Invalid date/time string (datatype DateTimeDt does not support MINUTE precision): 2013-02-03T11:22Z").isEqualTo(e.getMessage());
		}
	}

	@Test
	public void testParseSecond() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02-03T11:22:33");

		assertThat(myDateInstantParser.format(dt.getValue()).substring(0, 19)).isEqualTo("2013-02-03 11:22:33");
		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isNull();
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);
	}

	@Test
	public void testParseSecondZulu() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02-03T11:22:33Z");

		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33Z");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(true);
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT");
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);
	}

	@Test
	public void testParseSecondZone() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02-03T11:22:33-02:00");

		assertThat(dt.getValueAsString()).isEqualTo("2013-02-03T11:22:33-02:00");
		assertThat(dt.isTimeZoneZulu()).isEqualTo(false);
		assertThat(dt.getTimeZone()).isEqualTo(TimeZone.getTimeZone("GMT-02:00"));
		assertThat(dt.getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);
	}

	@Test
	public void testParseTimeZoneOffsetCorrectly0millis() {
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));

		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00-09:00");

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

		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.1-09:00");

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

		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.12-09:00");

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

		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.123-09:00");

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

		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.1234-09:00");

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

		DateTimeDt dt = new DateTimeDt("2010-01-01T00:00:00.12345-09:00");

		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T00:00:00.12345-09:00");
		assertThat(myDateInstantParser.format(dt.getValue())).isEqualTo("2010-01-01 04:00:00.123");
		assertThat(dt.getTimeZone().getID()).isEqualTo("GMT-09:00");
		assertThat(dt.getTimeZone().getRawOffset()).isEqualTo(-32400000L);

		dt.setTimeZoneZulu(true);
		assertThat(dt.getValueAsString()).isEqualTo("2010-01-01T09:00:00.12345Z");
	}

	@Test
	public void testParseYear() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
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
		patient.setBirthDate(cal.getTime(), TemporalPrecisionEnum.DAY);
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

		DateDt date = new DateDt();
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

		DateDt date = new DateDt();
		date.setValue(time, TemporalPrecisionEnum.DAY);
		assertThat(date.getValueAsString()).isEqualTo("2012-01-02");
	}

	@Test
	public void testSetValueByString() {
		InstantDt i = new InstantDt();
		i.setValueAsString("2014-06-20T20:22:09Z");

		assertThat(i.getValue()).isNotNull();
		assertThat(i.getValueAsString()).isNotNull();

		assertThat(i.getValue().getTime()).isEqualTo(1403295729000L);
		assertThat(i.getValueAsString()).isEqualTo("2014-06-20T20:22:09Z");
	}

	@Test
	public void testToHumanDisplay() {
		DateTimeDt dt = new DateTimeDt("2012-01-05T12:00:00-08:00");
		String human = dt.toHumanDisplay();
		ourLog.info(human);
		assertThat(human).contains("2012");
		assertThat(human).contains("12");
	}

	private void validateMillisPartial(String input, int expected) {
		InstantDt dt = new InstantDt();
		dt.setValueAsString(input);
		Date date = dt.getValue();

		assertThat(date.getTime() % 1000).isEqualTo(expected);
	}

	private void verifyFails(String input) {
		try {
			DateTimeDt dt = new DateTimeDt();
			dt.setValueAsString(input);
			fail("");		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertThat(e.getMessage()).contains("Invalid date/time format: \"" + input + "\"");
		}
	}

	@AfterAll
	public static void afterClassClearContext() {
		Locale.setDefault(ourDefaultLocale);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() {
		/*
		 * We cache the default locale, but temporarily set it to a random value during this test. This helps ensure that there are no language specific dependencies in the test.
		 */
		ourDefaultLocale = Locale.getDefault();

		Locale[] available = {Locale.CANADA, Locale.GERMANY, Locale.TAIWAN};
		Locale newLocale = available[(int) (Math.random() * available.length)];
		Locale.setDefault(newLocale);

		ourLog.info("Tests are running in locale: " + newLocale.getDisplayName());
	}

}
