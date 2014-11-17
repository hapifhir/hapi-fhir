package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.DataFormatException;

public class BaseDateTimeDtTest {
	private SimpleDateFormat myDateInstantParser;
	private FastDateFormat myDateInstantZoneParser;

	@Before
	public void before() {
		myDateInstantParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		myDateInstantZoneParser = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSSZ", TimeZone.getTimeZone("GMT-02:00"));
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

	@Test()
	public void testParseMalformatted() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt("20120102");
		assertEquals("20120102", dt.getValueAsString());
		assertEquals("2012-01-02", new SimpleDateFormat("yyyy-MM-dd").format(dt.getValue()));
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
		assertEquals("2013-02", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertNull(dt.getTimeZone());
		assertEquals(TemporalPrecisionEnum.MONTH, dt.getPrecision());
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
	public void testParseSecondZone() throws DataFormatException {
		DateTimeDt dt = new DateTimeDt();
		dt.setValueAsString("2013-02-03T11:22:33-02:00");

		assertEquals("2013-02-03T11:22:33-02:00", dt.getValueAsString());
		assertEquals(false, dt.isTimeZoneZulu());
		assertEquals(TimeZone.getTimeZone("GMT-02:00"), dt.getTimeZone());
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
}
