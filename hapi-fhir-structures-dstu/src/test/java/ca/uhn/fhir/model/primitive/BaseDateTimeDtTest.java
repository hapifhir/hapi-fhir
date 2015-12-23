package ca.uhn.fhir.model.primitive;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.resource.Condition;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.validation.ValidationResult;

public class BaseDateTimeDtTest {
	private SimpleDateFormat myDateInstantParser;
	private FastDateFormat myDateInstantZoneParser;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseDateTimeDtTest.class);

	private static FhirContext ourCtx = FhirContext.forDstu1();

	@Before
	public void before() {
		myDateInstantParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		myDateInstantZoneParser = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ", TimeZone.getTimeZone("GMT-02:00"));
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
	
	
	@Test
	public void setTimezoneToZulu() {
		DateTimeDt dt = new DateTimeDt(new Date(816411488000L));
		// assertEquals("1995-11-14T23:58:08", dt.getValueAsString());
		dt.setTimeZoneZulu(true);
		assertEquals("1995-11-15T04:58:08Z", dt.getValueAsString());
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
	public void testInstantInLocalTimezone() {
		InstantDt dt = InstantDt.withCurrentTime();
		String str = dt.getValueAsString();
		char offset = str.charAt(23);
		if (offset != '+' && offset != '-' && offset != 'Z') {
			fail("No timezone provided: " + str);
		}
	}

	/**
	 * Test for #57
	 */
	@Test
	public void testDateParsesWithInvalidPrecision() {
		Condition c = new Condition();
		c.setDateAsserted(new DateDt());
		c.getDateAsserted().setValueAsString("2001-01-02T11:13:33");
		assertEquals(TemporalPrecisionEnum.SECOND, c.getDateAsserted().getPrecision());

		String encoded = ourCtx.newXmlParser().encodeResourceToString(c);
		Assert.assertThat(encoded, Matchers.containsString("value=\"2001-01-02T11:13:33\""));

		c = ourCtx.newXmlParser().parseResource(Condition.class, encoded);

		assertEquals("2001-01-02T11:13:33", c.getDateAsserted().getValueAsString());
		assertEquals(TemporalPrecisionEnum.SECOND, c.getDateAsserted().getPrecision());

		ValidationResult outcome = ourCtx.newValidator().validateWithResult(c);
		String outcomeStr = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome());
		ourLog.info(outcomeStr);

		assertThat(outcomeStr, containsString("date-primitive"));
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

	@Test
	public void testFormats() throws Exception {
		Date instant = myDateInstantParser.parse("2001-02-03 13:01:02.555");
		for (FastDateFormat next : BaseDateTimeDt.getFormatters()) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(instant);
			String value = next.format(cal);
			ourLog.info("String: {}", value);

			DateTimeDt dt = new DateTimeDt(value);
			String reEncoded = next.format(dt.getValue());

			assertEquals(value, reEncoded);

		}
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

	@Test()
	public void testParseMalformatted() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt("20120102");
		assertEquals("20120102", dt.getValueAsString());
		assertEquals("2012-01-02", new SimpleDateFormat("yyyy-MM-dd").format(dt.getValue()));
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
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MILLI, dt.getPrecision());
	}

	@Test
	public void testParseMonth() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02");

		assertEquals("2013-02", myDateInstantParser.format(dt.getValue()).substring(0, 7));
		assertEquals("2013-02", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MONTH, dt.getPrecision());
	}

	@Test
	public void testParseMonthNoDashes() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("201302");

		assertEquals("2013-02", myDateInstantParser.format(dt.getValue()).substring(0, 7));
		assertEquals("201302", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MONTH, dt.getPrecision());
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
		assertEquals(null, dt.getTimeZone());
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
	public void testParseYear() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013");

		assertEquals("2013", myDateInstantParser.format(dt.getValue()).substring(0, 4));
		assertEquals("2013", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.YEAR, dt.getPrecision());
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

}
