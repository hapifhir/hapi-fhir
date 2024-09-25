package ca.uhn.fhir.util;

import jakarta.annotation.Nullable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateUtilsTest {

	private static final Logger ourLog = LoggerFactory.getLogger(DateUtilsTest.class);

	private static final ZoneId TIMEZONE_NEWFOUNDLAND = ZoneId.of("America/St_Johns");

	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD = DateTimeFormatter.ISO_LOCAL_DATE;
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
	private static final Duration MINUS_ONE_SECOND = Duration.of(-1, ChronoUnit.SECONDS);


	private static Stream<Arguments> extractLocalDateTimeIfValidParams() {
		return Stream.of(
			Arguments.of(
				getTemporalAccessor(2024),
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor(2023, 2),
				LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor(2022, 9),
				LocalDateTime.of(2022, Month.SEPTEMBER, 1, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor(2021, 3, 24),
				LocalDateTime.of(2021, Month.MARCH, 24, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor(2024, 10, 23),
				LocalDateTime.of(2024, Month.OCTOBER, 23, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor(2024, 8, 24, 12),
				LocalDateTime.of(2024, Month.AUGUST, 24, 12, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor(2024, 11, 24, 12, 35),
				LocalDateTime.of(2024, Month.NOVEMBER, 24, 12, 35, 0)
			),
			Arguments.of(
				getTemporalAccessor(2024, 9, 24, 12, 35, 47),
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47)
			)
		);
	}

	private static Stream<Arguments> formatWithZoneAndOptionalDeltaParams() {
		return Stream.of(
			Arguments.of(
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-01-01T00:00:00Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-02-01T00:00:00Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.AUGUST, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-08-01T00:00:00Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.MARCH, 24, 12, 35, 47),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-03-24T12:35:47Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-09-24T12:35:47Z"
			),

			Arguments.of(
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2023-12-31T23:59:59Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2024-01-31T23:59:59Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.AUGUST, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2024-07-31T23:59:59Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.MARCH, 24, 12, 35, 47),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2024-03-24T12:35:46Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2024-09-24T12:35:46Z"
			),

			Arguments.of(
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-01-01T00:00:00-03:30"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-02-01T00:00:00-03:30"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.AUGUST, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-08-01T00:00:00-02:30"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.MARCH, 24, 12, 35, 47),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-03-24T12:35:47-02:30"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-09-24T12:35:47-02:30"
			),

			Arguments.of(
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2023-12-31T23:59:59-03:30"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2024-01-31T23:59:59-03:30"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.AUGUST, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2024-07-31T23:59:59-02:30"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.MARCH, 24, 12, 35, 47),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2024-03-24T12:35:46-02:30"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				MINUS_ONE_SECOND,
				"2024-09-24T12:35:46-02:30"
			),

			Arguments.of(
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-01-01T00:00:00Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-02-01T00:00:00Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.AUGUST, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-08-01T00:00:00Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.MARCH, 24, 12, 35, 47),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-03-24T12:35:47Z"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z,
				null,
				"2024-09-24T12:35:47Z"
			),

			Arguments.of(
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2023-12-31"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2024-01-31"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.AUGUST, 1, 0, 0, 0),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2024-07-31"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.MARCH, 24, 12, 35, 47),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2024-03-24"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47),
				ZoneOffset.UTC,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2024-09-24"
			),

			Arguments.of(
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				null,
				"2024-01-01"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				null,
				"2024-02-01"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.AUGUST, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				null,
				"2024-08-01"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.MARCH, 24, 12, 35, 47),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				null,
				"2024-03-24"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				null,
				"2024-09-24"
			),

			Arguments.of(
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2023-12-31"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2024-01-31"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.AUGUST, 1, 0, 0, 0),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2024-07-31"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.MARCH, 24, 12, 35, 47),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2024-03-24"
			),
			Arguments.of(
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47),
				TIMEZONE_NEWFOUNDLAND,
				DATE_TIME_FORMATTER_YYYY_MM_DD,
				MINUS_ONE_SECOND,
				"2024-09-24"
			)
		);
	}

	@ParameterizedTest
	@MethodSource("extractLocalDateTimeIfValidParams")
	void extractLocalDateTimeIfValid (
			TemporalAccessor theTemporalAccessor,
			@Nullable LocalDateTime theExpectedResult) {
		assertThat(DateUtils.extractLocalDateTimeIfValid(theTemporalAccessor))
			.isEqualTo(Optional.ofNullable(theExpectedResult));
	}

	@ParameterizedTest
	@MethodSource("formatWithZoneAndOptionalDeltaParams")
	void formatWithZoneAndOptionalDelta (
			LocalDateTime theLocalDateTime,
			ZoneId theZoneId,
			DateTimeFormatter theDateTimeFormatter,
			@Nullable TemporalAmount theDelta,
			String theExpectedResult) {

		assertThat(DateUtils.formatWithZoneAndOptionalDelta(theLocalDateTime, theZoneId, theDateTimeFormatter, theDelta))
			.isEqualTo(theExpectedResult);
	}

	private static TemporalAccessor getTemporalAccessor(int theYear) {
		return getTemporalAccessor(theYear, null, null, null, null, null);
	}

	private static TemporalAccessor getTemporalAccessor(int theYear, int theMonth) {
		return getTemporalAccessor(theYear, theMonth, null, null, null, null);
	}

	private static TemporalAccessor getTemporalAccessor(int theYear, int theMonth, int theDay) {
		return getTemporalAccessor(theYear, theMonth, theDay, null, null, null);
	}

	private static TemporalAccessor getTemporalAccessor(int theYear, int theMonth, int theDay, int theHour) {
		return getTemporalAccessor(theYear, theMonth, theDay, theHour, null, null);
	}

	private static TemporalAccessor getTemporalAccessor(int theYear, int theMonth, int theDay, int theHour, int theMinute) {
		return getTemporalAccessor(theYear, theMonth, theDay, theHour, theMinute, null);
	}

	private static TemporalAccessor getTemporalAccessor(
			int year,
			@Nullable Integer month,
			@Nullable Integer day,
			@Nullable Integer hour,
			@Nullable Integer minute,
			@Nullable Integer second) {
		final TemporalAccessor temporalAccessor = mock(TemporalAccessor.class);

		mockAccessor(temporalAccessor, ChronoField.YEAR, year);
		mockAccessor(temporalAccessor, ChronoField.MONTH_OF_YEAR, month);
		mockAccessor(temporalAccessor, ChronoField.DAY_OF_MONTH, day);
		mockAccessor(temporalAccessor, ChronoField.HOUR_OF_DAY, hour);
		mockAccessor(temporalAccessor, ChronoField.MINUTE_OF_HOUR, minute);
		mockAccessor(temporalAccessor, ChronoField.SECOND_OF_MINUTE, second);

		return temporalAccessor;
	}

	private static void mockAccessor(TemporalAccessor theTemporalAccessor, TemporalField theTemporalField, @Nullable Integer theValue) {
		if (theValue != null) {
			when(theTemporalAccessor.isSupported(theTemporalField)).thenReturn(true);
			when(theTemporalAccessor.get(theTemporalField)).thenReturn(theValue);
		}
	}
}
