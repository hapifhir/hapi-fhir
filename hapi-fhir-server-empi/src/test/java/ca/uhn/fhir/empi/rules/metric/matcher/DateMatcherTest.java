package ca.uhn.fhir.empi.rules.metric.matcher;

import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateMatcherTest extends BaseMatcherTest {

	@Test
	public void testExactDatePrecision() {
		Calendar cal = new GregorianCalendar(2020,6,15);
		Calendar sameMonthCal = new GregorianCalendar(2020,6,22);
		Calendar sameYearCal = new GregorianCalendar(2020,11,13);
		Calendar otherYearCal = new GregorianCalendar(1965,8,9);

		Date date = cal.getTime();
		Date sameMonth = sameMonthCal.getTime();
		Date sameYear = sameYearCal.getTime();
		Date otherYear = otherYearCal.getTime();

		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.DAY), new DateType(date, TemporalPrecisionEnum.DAY)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.DAY), new DateType(sameMonth, TemporalPrecisionEnum.DAY)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.DAY), new DateType(sameYear, TemporalPrecisionEnum.DAY)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.DAY), new DateType(otherYear, TemporalPrecisionEnum.DAY)));

		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.MONTH), new DateType(date, TemporalPrecisionEnum.MONTH)));
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.MONTH), new DateType(sameMonth, TemporalPrecisionEnum.MONTH)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.MONTH), new DateType(sameYear, TemporalPrecisionEnum.MONTH)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.MONTH), new DateType(otherYear, TemporalPrecisionEnum.MONTH)));

		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.YEAR), new DateType(date, TemporalPrecisionEnum.YEAR)));
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.YEAR), new DateType(sameMonth, TemporalPrecisionEnum.YEAR)));
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.YEAR), new DateType(sameYear, TemporalPrecisionEnum.YEAR)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateType(date, TemporalPrecisionEnum.YEAR), new DateType(otherYear, TemporalPrecisionEnum.YEAR)));
	}

	@Test
	public void testExactDateTimePrecision() {
		Calendar cal =           new GregorianCalendar(2020,6,15, 11, 12, 13);
		Calendar sameSecondCal = new GregorianCalendar(2020,6,15, 11, 12, 13);
		sameSecondCal.add(Calendar.MILLISECOND, 123);

		Calendar sameDayCal =    new GregorianCalendar(2020,6,15, 12, 34, 56);

		Date date = cal.getTime();
		Date sameSecond = sameSecondCal.getTime();
		Date sameDay = sameDayCal.getTime();

		// Same precision

		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.DAY), new DateTimeType(date, TemporalPrecisionEnum.DAY)));
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.DAY), new DateTimeType(sameSecond, TemporalPrecisionEnum.DAY)));
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.DAY), new DateTimeType(sameDay, TemporalPrecisionEnum.DAY)));

		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.SECOND), new DateTimeType(date, TemporalPrecisionEnum.SECOND)));
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.SECOND), new DateTimeType(sameSecond, TemporalPrecisionEnum.SECOND)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.SECOND), new DateTimeType(sameDay, TemporalPrecisionEnum.SECOND)));

		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.MILLI), new DateTimeType(date, TemporalPrecisionEnum.MILLI)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.MILLI), new DateTimeType(sameSecond, TemporalPrecisionEnum.MILLI)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.MILLI), new DateTimeType(sameDay, TemporalPrecisionEnum.MILLI)));

		// Different precision matches by coarser precision
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.SECOND), new DateTimeType(date, TemporalPrecisionEnum.DAY)));
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.SECOND), new DateTimeType(sameSecond, TemporalPrecisionEnum.DAY)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.SECOND), new DateTimeType(sameDay, TemporalPrecisionEnum.DAY)));

		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.DAY), new DateTimeType(date, TemporalPrecisionEnum.SECOND)));
		assertTrue(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.DAY), new DateTimeType(sameSecond, TemporalPrecisionEnum.SECOND)));
		assertFalse(EmpiMetricEnum.DATE.match(ourFhirContext, new DateTimeType(date, TemporalPrecisionEnum.DAY), new DateTimeType(sameDay, TemporalPrecisionEnum.SECOND)));
	}
}
