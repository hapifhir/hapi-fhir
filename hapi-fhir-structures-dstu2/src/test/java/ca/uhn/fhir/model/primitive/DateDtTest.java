package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.TreeSet;

import org.junit.Test;

public class DateDtTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DateDtTest.class);

	@Test
	public void testPrecision() {

//		ourLog.info(""+ new TreeSet<String>(Arrays.asList(TimeZone.getAvailableIDs())));
		
		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.set(1990, Calendar.JANUARY, 1, 0, 0, 0);
		ourLog.info("Time: {}", cal); // 631152000775

		DateDt dateDt = new DateDt(cal.getTime());
		long time = dateDt.getValue().getTime();
		ourLog.info("Time: {}", time); // 631152000775
		ourLog.info("Time: {}", dateDt.getValue()); // 631152000775

		dateDt.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		time = dateDt.getValue().getTime();
		ourLog.info("Time: {}", time); // 631152000775
		ourLog.info("Time: {}", dateDt.getValue()); // 631152000775

		String valueAsString = dateDt.getValueAsString();
		ourLog.info(valueAsString);
		// is 631152000030

	}

	@Test
	public void testConstructors() {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		cal.set(1990, Calendar.JANUARY, 5, 0, 0, 0);
		DateDt dateDt = new DateDt(cal);
		assertEquals("1990-01-05", dateDt.getValueAsString());

		dateDt = new DateDt(1990, 0, 5);
		assertEquals("1990-01-05", dateDt.getValueAsString());
	}
	
	
	
}
