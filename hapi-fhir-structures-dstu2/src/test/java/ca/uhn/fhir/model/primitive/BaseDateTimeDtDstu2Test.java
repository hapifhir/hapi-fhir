package ca.uhn.fhir.model.primitive;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.TestUtil;

public class BaseDateTimeDtDstu2Test {
	private static Locale ourDefaultLocale;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseDateTimeDtDstu2Test.class);
	private SimpleDateFormat myDateInstantParser;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Before
	public void before() {
		myDateInstantParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}

	
	@Test
	public void testLargePrecision() {
		DateTimeDt dt = new DateTimeDt("2014-03-06T22:09:58.9121174+04:30");
		
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("Z"));
		assertEquals("2014-03-06 17:39:58.912", myDateInstantParser.format(dt.getValue()));
	}
	
	@Test
	public void testEncodeOffset() throws Exception {
		myDateInstantParser.parse("2011-01-01 11:11:11.0");
		myDateInstantParser.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		String offset = InstantDt.withCurrentTime().setTimeZone(TimeZone.getTimeZone("America/Toronto")).getValueAsString();
		assertThat(offset, endsWith("-05:00"));
	}
	
	
	@Test
	public void testParseInvalid() {
		try {
			DateTimeDt dt = new DateTimeDt();
			dt.setValueAsString("1974-12-25+10:00");
			fail();
		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertEquals("Invalid date/time format: \"1974-12-25+10:00\"", e.getMessage());
		}
		try {
			DateTimeDt dt = new DateTimeDt();
			dt.setValueAsString("1974-12-25Z");
			fail();
		} catch (ca.uhn.fhir.parser.DataFormatException e) {
			assertEquals("Invalid date/time format: \"1974-12-25Z\"", e.getMessage());
		}
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
	public void testParseInvalidZoneOffset() {
		try {
			new DateTimeDt("2010-01-01T00:00:00.1234-09:00Z");
			fail();
		} catch (DataFormatException e) {
			assertEquals("Invalid FHIR date/time string: 2010-01-01T00:00:00.1234-09:00Z", e.getMessage());
		}
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
