package ca.uhn.fhir.model.primitive;


import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;

public class BaseDateTimeDtTest {
	private SimpleDateFormat myDateInstantParser;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseDateTimeDtTest.class);
	
	@Before
	public void before() {
		myDateInstantParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}

	@Test
	public void testToHumanDisplay() {
		DateTimeDt dt = new DateTimeDt("2012-01-05T12:00:00-08:00");
		String human = dt.toHumanDisplay();
		ourLog.info(human);
		assertEquals("Jan 5, 2012 12:00:00 PM", human);
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
	

}
