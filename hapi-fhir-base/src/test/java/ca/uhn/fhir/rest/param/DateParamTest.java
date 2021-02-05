package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.EQUAL;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.NOT_EQUAL;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;

public class DateParamTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DateParamTest.class);


	@Test
	public void testBasicDateParse() {
		DateParam input = new DateParam("2020-01-01");

		// too bad value is a j.u.Date instead of a new JSR-310 type
		// DataParam parses using default tz, so go backwards.
		ZonedDateTime zonedDateTime = input.getValue().toInstant().atZone(ZoneId.systemDefault());
		assertEquals(2020,zonedDateTime.getYear());
		assertEquals(Month.JANUARY,zonedDateTime.getMonth());
		assertEquals(1,zonedDateTime.getDayOfMonth());
		assertNull(input.getPrefix());
	}

	@Test
	public void testBadDateFormat() {
		try {
			new DateParam("09-30-1960");
			fail();
		} catch (DataFormatException e) {
			// expected
		}
	}

	@Test
	public void testPrefixParse() {
		DateParam input = new DateParam("gt2020-01-01");

		assertEquals(ParamPrefixEnum.GREATERTHAN, input.getPrefix());
	}

	/**
	 * We support legacy prefixes in addition to the standard ParamPrefixEnum values.
	 *
	 * Testing here since BaseParamWithPrefix is abstract.
 	 */
	@Test
	public void testLegacyPrefixParse() {
		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, translateLegacyPrefix(">="));
		assertEquals(ParamPrefixEnum.GREATERTHAN, translateLegacyPrefix(">"));
		assertEquals(ParamPrefixEnum.LESSTHAN_OR_EQUALS, translateLegacyPrefix("<="));
		assertEquals(ParamPrefixEnum.LESSTHAN, translateLegacyPrefix("<"));
		assertEquals(ParamPrefixEnum.APPROXIMATE, translateLegacyPrefix("~"));
		assertEquals(ParamPrefixEnum.EQUAL, translateLegacyPrefix("="));
	}

	private ParamPrefixEnum translateLegacyPrefix(String legacyPrefix) {
		DateParam input = new DateParam(legacyPrefix + "2020-01-01");
		return input.getPrefix();
	}

	@Test
	public void testJunkDateIssue2361() {
		// Issue 2361 - the string "junk" wasn't returning 400 as expected.
		// Parsed as a prefix instead, and discarded.
		// https://github.com/hapifhir/hapi-fhir/issues/2361
		try {
			new DateParam("junk");
			fail();
		} catch (DataFormatException e) {
			// expected
		}
	}

	// merged from ca.uhn.fhir.rest.param.DateParamTest
	@Test
	public void testConstructors() {
		new DateParam();
		new DateParam("2011-01-02");
		new DateParam(ParamPrefixEnum.GREATERTHAN, new Date());
		new DateParam(ParamPrefixEnum.GREATERTHAN, new DateTimeDt("2011-01-02"));
		new DateParam(ParamPrefixEnum.GREATERTHAN, InstantDt.withCurrentTime());
		new DateParam(ParamPrefixEnum.GREATERTHAN, "2011-01-02");

	}

	@Test
	public void testParse() {
		DateParam param = new DateParam();
		param.setValueAsString("gt2016-06-09T20:38:14.591-05:00");

		assertEquals(ParamPrefixEnum.GREATERTHAN, param.getPrefix());
		assertEquals("2016-06-09T20:38:14.591-05:00", param.getValueAsString());

		ourLog.debug("PRE:  " + param.getValue());
		ourLog.debug("PRE:  " + param.getValue().getTime());
		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.debug("POST: " + dt.getValue());
		assertEquals("2016-06-09T21:38:14.591-04:00", dt.getValueAsString());
	}

	@Test
	public void testParseMinutePrecision() {
		DateParam param = new DateParam();
		param.setValueAsString("2016-06-09T20:38Z");

		assertNull(param.getPrefix());
		assertEquals("2016-06-09T20:38Z", param.getValueAsString());

		ourLog.debug("PRE:  " + param.getValue());
		ourLog.debug("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.debug("POST: " + dt.getValue());
		assertEquals("2016-06-09T16:38:00.000-04:00", dt.getValueAsString());
	}

	@Test
	public void testParseMinutePrecisionWithoutTimezone() {
		DateParam param = new DateParam();
		param.setValueAsString("2016-06-09T20:38");

		assertNull(param.getPrefix());
		assertEquals("2016-06-09T20:38", param.getValueAsString());

		ourLog.debug("PRE:  " + param.getValue());
		ourLog.debug("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.debug("POST: " + dt.getValue());
		assertThat(dt.getValueAsString(), startsWith("2016-06-09T"));
		assertThat(dt.getValueAsString(), endsWith("8:00.000-04:00"));
	}

	@Test
	public void testParseMinutePrecisionWithPrefix() {
		DateParam param = new DateParam();
		param.setValueAsString("gt2016-06-09T20:38Z");

		assertEquals(ParamPrefixEnum.GREATERTHAN, param.getPrefix());
		assertEquals("2016-06-09T20:38Z", param.getValueAsString());

		ourLog.debug("PRE:  " + param.getValue());
		ourLog.debug("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.debug("POST: " + dt.getValue());
		assertEquals("2016-06-09T16:38:00.000-04:00", dt.getValueAsString());
	}

	@Test
	public void testParseLegacyPrefixes() {
		assertEquals(ParamPrefixEnum.APPROXIMATE, new DateParam("ap2012").getPrefix());
		assertEquals(ParamPrefixEnum.GREATERTHAN, new DateParam("gt2012").getPrefix());
		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new DateParam("ge2012").getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN, new DateParam("lt2012").getPrefix());
		assertEquals(ParamPrefixEnum.LESSTHAN_OR_EQUALS, new DateParam("le2012").getPrefix());
	}

	@Test()
	public void testEqualsAndHashCode() {
		Date now = new Date();
		Date later = new Date(now.getTime() + SECONDS.toMillis(666));
		assertEquals(new DateParam(), new DateParam(null));
		assertEquals(new DateParam(NOT_EQUAL, now), new DateParam(NOT_EQUAL, now.getTime()));
		assertEquals(new DateParam(EQUAL, now), new DateParam(EQUAL, now.getTime()));
		assertEquals(new DateParam(EQUAL, later), new DateParam(EQUAL, later.getTime()));
	}
}
