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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.fail;


public class DateParamTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DateParamTest.class);


	@Test
	public void testBasicDateParse() {
		DateParam input = new DateParam("2020-01-01");

		// too bad value is a j.u.Date instead of a new JSR-310 type
		// DataParam parses using default tz, so go backwards.
		ZonedDateTime zonedDateTime = input.getValue().toInstant().atZone(ZoneId.systemDefault());
		assertThat(zonedDateTime.getYear()).isEqualTo(2020);
		assertThat(zonedDateTime.getMonth()).isEqualTo(Month.JANUARY);
		assertThat(zonedDateTime.getDayOfMonth()).isEqualTo(1);
		assertThat(input.getPrefix()).isNull();
	}

	@Test
	public void testBadDateFormat() {
		try {
			new DateParam("09-30-1960");
			fail("");
		} catch (DataFormatException e) {
			// expected
		}
	}

	@Test
	public void testPrefixParse() {
		DateParam input = new DateParam("gt2020-01-01");

		assertThat(input.getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
	}

	/**
	 * We support legacy prefixes in addition to the standard ParamPrefixEnum values.
	 *
	 * Testing here since BaseParamWithPrefix is abstract.
 	 */
	@Test
	public void testLegacyPrefixParse() {
		assertThat(translateLegacyPrefix(">=")).isEqualTo(ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
		assertThat(translateLegacyPrefix(">")).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(translateLegacyPrefix("<=")).isEqualTo(ParamPrefixEnum.LESSTHAN_OR_EQUALS);
		assertThat(translateLegacyPrefix("<")).isEqualTo(ParamPrefixEnum.LESSTHAN);
		assertThat(translateLegacyPrefix("~")).isEqualTo(ParamPrefixEnum.APPROXIMATE);
		assertThat(translateLegacyPrefix("=")).isEqualTo(ParamPrefixEnum.EQUAL);
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
			fail("");
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

		assertThat(param.getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(param.getValueAsString()).isEqualTo("2016-06-09T20:38:14.591-05:00");

		ourLog.debug("PRE:  " + param.getValue());
		ourLog.debug("PRE:  " + param.getValue().getTime());
		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.debug("POST: " + dt.getValue());
		assertThat(dt.getValueAsString()).isEqualTo("2016-06-09T21:38:14.591-04:00");
	}

	@Test
	public void testParseMinutePrecision() {
		DateParam param = new DateParam();
		param.setValueAsString("2016-06-09T20:38Z");

		assertThat(param.getPrefix()).isNull();
		assertThat(param.getValueAsString()).isEqualTo("2016-06-09T20:38Z");

		ourLog.debug("PRE:  " + param.getValue());
		ourLog.debug("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.debug("POST: " + dt.getValue());
		assertThat(dt.getValueAsString()).isEqualTo("2016-06-09T16:38:00.000-04:00");
	}

	@Test
	public void testParseMinutePrecisionWithoutTimezone() {
		DateParam param = new DateParam();
		param.setValueAsString("2016-06-09T20:38");

		assertThat(param.getPrefix()).isNull();
		assertThat(param.getValueAsString()).isEqualTo("2016-06-09T20:38");

		ourLog.debug("PRE:  " + param.getValue());
		ourLog.debug("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.debug("POST: " + dt.getValue());
		assertThat(dt.getValueAsString()).startsWith("2016-06-09T");
		assertThat(dt.getValueAsString()).endsWith("8:00.000-04:00");
	}

	@Test
	public void testParseMinutePrecisionWithPrefix() {
		DateParam param = new DateParam();
		param.setValueAsString("gt2016-06-09T20:38Z");

		assertThat(param.getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(param.getValueAsString()).isEqualTo("2016-06-09T20:38Z");

		ourLog.debug("PRE:  " + param.getValue());
		ourLog.debug("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.debug("POST: " + dt.getValue());
		assertThat(dt.getValueAsString()).isEqualTo("2016-06-09T16:38:00.000-04:00");
	}

	@Test
	public void testParseLegacyPrefixes() {
		assertThat(new DateParam("ap2012").getPrefix()).isEqualTo(ParamPrefixEnum.APPROXIMATE);
		assertThat(new DateParam("gt2012").getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN);
		assertThat(new DateParam("ge2012").getPrefix()).isEqualTo(ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
		assertThat(new DateParam("lt2012").getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN);
		assertThat(new DateParam("le2012").getPrefix()).isEqualTo(ParamPrefixEnum.LESSTHAN_OR_EQUALS);
	}

	@Test()
	public void testEqualsAndHashCode() {
		Date now = new Date();
		Date later = new Date(now.getTime() + SECONDS.toMillis(666));
		assertThat(new DateParam(null)).isEqualTo(new DateParam());
		assertThat(new DateParam(NOT_EQUAL, now.getTime())).isEqualTo(new DateParam(NOT_EQUAL, now));
		assertThat(new DateParam(EQUAL, now.getTime())).isEqualTo(new DateParam(EQUAL, now));
		assertThat(new DateParam(EQUAL, later.getTime())).isEqualTo(new DateParam(EQUAL, later));
	}
}
