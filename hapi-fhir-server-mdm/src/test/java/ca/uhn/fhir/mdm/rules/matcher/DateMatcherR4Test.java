package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.HapiDateMatcher;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;

public class DateMatcherR4Test extends BaseMatcherR4Test {

	private HapiDateMatcher myDateMatcher;

	@BeforeEach
	public void before() {
		super.before();
		myDateMatcher = new HapiDateMatcher(ourFhirContext);
	}

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

		assertThat(dateMatch(date, date, TemporalPrecisionEnum.DAY)).isTrue();
		assertThat(dateMatch(date, sameMonth, TemporalPrecisionEnum.DAY)).isFalse();
		assertThat(dateMatch(date, sameYear, TemporalPrecisionEnum.DAY)).isFalse();
		assertThat(dateMatch(date, otherYear, TemporalPrecisionEnum.DAY)).isFalse();

		assertThat(dateMatch(date, date, TemporalPrecisionEnum.MONTH)).isTrue();
		assertThat(dateMatch(date, sameMonth, TemporalPrecisionEnum.MONTH)).isTrue();
		assertThat(dateMatch(date, sameYear, TemporalPrecisionEnum.MONTH)).isFalse();
		assertThat(dateMatch(date, otherYear, TemporalPrecisionEnum.MONTH)).isFalse();

		assertThat(dateMatch(date, date, TemporalPrecisionEnum.YEAR)).isTrue();
		assertThat(dateMatch(date, sameMonth, TemporalPrecisionEnum.YEAR)).isTrue();
		assertThat(dateMatch(date, sameYear, TemporalPrecisionEnum.YEAR)).isTrue();
		assertThat(dateMatch(date, otherYear, TemporalPrecisionEnum.YEAR)).isFalse();
	}

	private boolean dateMatch(Date theDate, Date theSameMonth, TemporalPrecisionEnum theTheDay) {
		myMdmMatcherJson.setExact(true);
		return myDateMatcher.matches(new DateType(theDate, theTheDay), new DateType(theSameMonth, theTheDay), myMdmMatcherJson);
	}

	@Test
	public void testExactDateTimePrecision() {
		Calendar cal = new GregorianCalendar(2020, 6, 15, 11, 12, 13);
		Date date = cal.getTime();

		Calendar sameSecondCal = new GregorianCalendar(2020, 6, 15, 11, 12, 13);
		sameSecondCal.add(Calendar.MILLISECOND, 123);
		Date sameSecond = sameSecondCal.getTime();


		Calendar sameDayCal = new GregorianCalendar(2020, 6, 15, 12, 34, 56);
		Date sameDay = sameDayCal.getTime();


		// Same precision

		assertThat(dateTimeMatch(date, date, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.DAY)).isTrue();
		assertThat(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.DAY)).isTrue();
		assertThat(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.DAY)).isTrue();

		assertThat(dateTimeMatch(date, date, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.SECOND)).isTrue();
		assertThat(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.SECOND)).isTrue();
		assertThat(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.SECOND)).isFalse();

		assertThat(dateTimeMatch(date, date, TemporalPrecisionEnum.MILLI, TemporalPrecisionEnum.MILLI)).isTrue();
		assertThat(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.MILLI, TemporalPrecisionEnum.MILLI)).isFalse();
		assertThat(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.MILLI, TemporalPrecisionEnum.MILLI)).isFalse();

		// Different precision matches by coarser precision
		assertThat(dateTimeMatch(date, date, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.DAY)).isTrue();
		assertThat(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.DAY)).isTrue();
		assertThat(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.SECOND, TemporalPrecisionEnum.DAY)).isTrue();

		assertThat(dateTimeMatch(date, date, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.SECOND)).isTrue();
		assertThat(dateTimeMatch(date, sameSecond, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.SECOND)).isTrue();
		assertThat(dateTimeMatch(date, sameDay, TemporalPrecisionEnum.DAY, TemporalPrecisionEnum.SECOND)).isTrue();


	}

	private boolean dateTimeMatch(Date theDate, Date theSecondDate, TemporalPrecisionEnum thePrecision, TemporalPrecisionEnum theSecondPrecision) {
		myMdmMatcherJson.setExact(true);
		return myDateMatcher.matches(
			new DateTimeType(theDate, thePrecision),
			new DateTimeType(theSecondDate, theSecondPrecision),
			myMdmMatcherJson
		);
	}
}
