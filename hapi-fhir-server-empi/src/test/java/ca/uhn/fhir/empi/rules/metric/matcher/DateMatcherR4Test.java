package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateMatcherR4Test extends BaseMatcherR4Test {

	@Test
	public void testExactDatePrecision() {
		Calendar cal = new GregorianCalendar(2020, 6, 15);
		Calendar sameMonthCal = new GregorianCalendar(2020, 6, 22);
		Calendar sameYearCal = new GregorianCalendar(2020, 11, 13);
		Calendar otherYearCal = new GregorianCalendar(1965, 8, 9);

		Date date = cal.getTime();
		Date sameMonth = sameMonthCal.getTime();
		Date sameYear = sameYearCal.getTime();
		Date otherYear = otherYearCal.getTime();

		assertTrue(dateMatch(date, date, TemporalPrecisionEnum.DAY));
		assertFalse(dateMatch(date, sameMonth, TemporalPrecisionEnum.DAY));
		assertFalse(dateMatch(date, sameYear, TemporalPrecisionEnum.DAY));
		assertFalse(dateMatch(date, otherYear, TemporalPrecisionEnum.DAY));

		assertTrue(dateMatch(date, date, TemporalPrecisionEnum.MONTH));
		assertTrue(dateMatch(date, sameMonth, TemporalPrecisionEnum.MONTH));
		assertFalse(dateMatch(date, sameYear, TemporalPrecisionEnum.MONTH));
		assertFalse(dateMatch(date, otherYear, TemporalPrecisionEnum.MONTH));

		assertTrue(dateMatch(date, date, TemporalPrecisionEnum.YEAR));
		assertTrue(dateMatch(date, sameMonth, TemporalPrecisionEnum.YEAR));
		assertTrue(dateMatch(date, sameYear, TemporalPrecisionEnum.YEAR));
		assertFalse(dateMatch(date, otherYear, TemporalPrecisionEnum.YEAR));
	}

	private boolean dateMatch(Date theDate, Date theSameMonth, TemporalPrecisionEnum theTheDay) {
		return EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(theDate, theTheDay), new DateType(theSameMonth, theTheDay), true);
	}

	@Test
	public void testExactDateTimePrecision() {
		Calendar cal = new GregorianCalendar(2020, 6, 15, 11, 12, 13);
		Calendar sameSecondCal = new GregorianCalendar(2020, 6, 15, 11, 12, 13);
		sameSecondCal.add(Calendar.MILLISECOND, 123);

		Calendar sameDayCal = new GregorianCalendar(2020, 6, 15, 12, 34, 56);

		Date date = cal.getTime();
		Date sameSecond = sameSecondCal.getTime();
		Date sameDay = sameDayCal.getTime();

		// Same precision

		assertTrue(dateTimeMatch(date, date, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.DAY));
		assertTrue(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.DAY));
		assertTrue(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.DAY));

		assertTrue(dateTimeMatch(date, date, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.SECOND));
		assertTrue(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.SECOND));
		assertFalse(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.SECOND));

		assertTrue(dateTimeMatch(date, date, TemporalPrecisionEnum.MILLI, TemporalPrecisionEnum.MILLI));
		assertFalse(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.MILLI, TemporalPrecisionEnum.MILLI));
		assertFalse(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.MILLI, TemporalPrecisionEnum.MILLI));

		// Different precision matches by coarser precision
		assertTrue(dateTimeMatch(date, date, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.DAY));
		assertTrue(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.DAY));
		assertFalse(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.DAY));

		assertTrue(dateTimeMatch(date, date, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.SECOND));
		assertTrue(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.SECOND));
		assertFalse(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.SECOND));
	}

	private boolean dateTimeMatch(Date theDate, Date theSameSecond, TemporalPrecisionEnum theTheDay, TemporalPrecisionEnum theTheDay2) {
		return EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(theDate, theTheDay), new DateTimeType(theSameSecond, theTheDay2), true);
	}
}
