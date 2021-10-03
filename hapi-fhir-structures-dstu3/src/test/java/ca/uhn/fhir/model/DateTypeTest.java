package ca.uhn.fhir.model;

import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.DateType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTypeTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DateTypeTest.class);

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@Test
	public void testDateType() {
		DateType birthDate = new DateType(1974, 11, 25);
		assertThat(birthDate.getYear(), is(1974));
		assertThat(birthDate.getMonth(), is(11));
		assertThat(birthDate.getDay(), is(25));
	}

	@SuppressWarnings("unused")
	@Test
	public void testDateTypeWithInvalidMonth() {
		try {
			new DateType(1974, 12, 25);
		} catch (IllegalArgumentException e) {
			assertEquals("theMonth must be between 0 and 11", e.getMessage());
		}
		try {
			new DateType(1974, -1, 25);
		} catch (IllegalArgumentException e) {
			assertEquals("theMonth must be between 0 and 11", e.getMessage());
		}
		try {
			new DateType(1974, 2, 0);
		} catch (IllegalArgumentException e) {
			assertEquals("theMonth must be between 0 and 11", e.getMessage());
		}
		try {
			new DateType(1974, 2, 32);
		} catch (IllegalArgumentException e) {
			assertEquals("theMonth must be between 0 and 11", e.getMessage());
		}
		new DateType(1974, 1, 31);
	}

	@Test
	public void testPrecision() {

		// ourLog.info(""+ new TreeSet<String>(Arrays.asList(TimeZone.getAvailableIDs())));

		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.set(1990, Calendar.JANUARY, 1, 0, 0, 0);
		ourLog.info("Time: {}", cal); // 631152000775

		DateType dateDt = new DateType(cal.getTime());
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
		DateType dateDt = new DateType(cal);
		assertEquals("1990-01-05", dateDt.getValueAsString());

		dateDt = new DateType(1990, 0, 5);
		assertEquals("1990-01-05", dateDt.getValueAsString());
	}

}
