package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.APPROXIMATE;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.EQUAL;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.NOT_EQUAL;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DateParamTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DateParamTest.class);

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
		Date date = new Date();

		DateParam param = new DateParam();
		param.setValueAsString("gt2016-06-09T20:38:14.591-05:00");

		assertEquals(ParamPrefixEnum.GREATERTHAN, param.getPrefix());
		assertEquals("2016-06-09T20:38:14.591-05:00", param.getValueAsString());

		ourLog.info("PRE:  " + param.getValue());
		ourLog.info("PRE:  " + param.getValue().getTime());
		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.info("POST: " + dt.getValue());
		assertEquals("2016-06-09T21:38:14.591-04:00", dt.getValueAsString());
	}

	@Test
	public void testParseMinutePrecision() {
		DateParam param = new DateParam();
		param.setValueAsString("2016-06-09T20:38Z");

		assertEquals(null, param.getPrefix());
		assertEquals("2016-06-09T20:38Z", param.getValueAsString());

		ourLog.info("PRE:  " + param.getValue());
		ourLog.info("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.info("POST: " + dt.getValue());
		assertEquals("2016-06-09T16:38:00.000-04:00", dt.getValueAsString());
	}

	@Test
	public void testParseMinutePrecisionWithoutTimezone() {
		DateParam param = new DateParam();
		param.setValueAsString("2016-06-09T20:38");

		assertEquals(null, param.getPrefix());
		assertEquals("2016-06-09T20:38", param.getValueAsString());

		ourLog.info("PRE:  " + param.getValue());
		ourLog.info("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.info("POST: " + dt.getValue());
		assertThat(dt.getValueAsString(), startsWith("2016-06-09T"));
		assertThat(dt.getValueAsString(), endsWith("8:00.000-04:00"));
	}

	@Test
	public void testParseMinutePrecisionWithPrefix() {
		DateParam param = new DateParam();
		param.setValueAsString("gt2016-06-09T20:38Z");

		assertEquals(ParamPrefixEnum.GREATERTHAN, param.getPrefix());
		assertEquals("2016-06-09T20:38Z", param.getValueAsString());

		ourLog.info("PRE:  " + param.getValue());
		ourLog.info("PRE:  " + param.getValue().getTime());

		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.info("POST: " + dt.getValue());
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
		List<DateParam> uniqueSamples = newArrayList(
			new DateParam(),
			new DateParam(NOT_EQUAL, new Date()),
			new DateParam(EQUAL, new Date().getTime()),
			new DateParam(APPROXIMATE, "2017-10-17T11:11:11.111-11:11"));
		DateParam previous = null;
		for (DateParam current : uniqueSamples) {
			ourLog.info("Equals test samples: [previous=" + previous + ", current=" + current + "]");
			assertThat(current, is(equalTo(copyOf(current))));
			assertThat(current.hashCode(), is(equalTo(copyOf(current).hashCode())));

			assertThat(current, is(not(equalTo(previous))));
			assertThat(current.hashCode(), is(not(equalTo(previous != null ? previous.hashCode() : null))));
			previous = current;
		}
	}

	private DateParam copyOf(DateParam param) {
		return new DateParam(param.getPrefix(), param.getValue());
	}
}
