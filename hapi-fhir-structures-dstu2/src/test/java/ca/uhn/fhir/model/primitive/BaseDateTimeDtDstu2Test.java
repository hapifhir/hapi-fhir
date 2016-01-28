package ca.uhn.fhir.model.primitive;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;

public class BaseDateTimeDtDstu2Test {
	private static Locale ourDefaultLocale;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseDateTimeDtDstu2Test.class);
	private SimpleDateFormat myDateInstantParser;

	@Before
	public void before() {
		myDateInstantParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
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
