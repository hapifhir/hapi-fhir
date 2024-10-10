package ca.uhn.fhir.util;

import jakarta.annotation.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilsTest {

	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY = DateTimeFormatter.ofPattern("yyyy");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM = DateTimeFormatter.ofPattern("yyyy-MM");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD = DateTimeFormatter.ISO_DATE;
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	private static Stream<Arguments> extractLocalDateTimeStartIfValidParams() {
		return Stream.of(
			Arguments.of(
				getTemporalAccessor("2024"),
				LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor("2023-02"),
				LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor("2022-09"),
				LocalDateTime.of(2022, Month.SEPTEMBER, 1, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor("2021-03-24"),
				LocalDateTime.of(2021, Month.MARCH, 24, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor("2024-10-23"),
				LocalDateTime.of(2024, Month.OCTOBER, 23, 0, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor("2024-08-24T12"),
				LocalDateTime.of(2024, Month.AUGUST, 24, 12, 0, 0)
			),
			Arguments.of(
				getTemporalAccessor("2024-11-24T12:35"),
				LocalDateTime.of(2024, Month.NOVEMBER, 24, 12, 35, 0)
			),
			Arguments.of(
				getTemporalAccessor("2024-09-24T12:35:47"),
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47)
			)
		);
	}

	private static Stream<Arguments> extractLocalDateTimeEndIfValidParams() {
		return Stream.of(
			Arguments.of(
				getTemporalAccessor("2024"),
				LocalDateTime.of(2024, Month.DECEMBER, 31, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-01"),
				LocalDateTime.of(2023, Month.JANUARY, 31, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-02"),
				LocalDateTime.of(2023, Month.FEBRUARY, 28, 23, 59, 59)
			),
			// Leap year
			Arguments.of(
				getTemporalAccessor("2024-02"),
				LocalDateTime.of(2024, Month.FEBRUARY, 29, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-03"),
				LocalDateTime.of(2023, Month.MARCH, 31, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-04"),
				LocalDateTime.of(2023, Month.APRIL, 30, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-05"),
				LocalDateTime.of(2023, Month.MAY, 31, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-06"),
				LocalDateTime.of(2023, Month.JUNE, 30, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-07"),
				LocalDateTime.of(2023, Month.JULY, 31, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-08"),
				LocalDateTime.of(2023, Month.AUGUST, 31, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2023-09"),
				LocalDateTime.of(2023, Month.SEPTEMBER, 30, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2022-10"),
				LocalDateTime.of(2022, Month.OCTOBER, 31, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2022-11"),
				LocalDateTime.of(2022, Month.NOVEMBER, 30, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2022-12"),
				LocalDateTime.of(2022, Month.DECEMBER, 31, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2021-03-24"),
				LocalDateTime.of(2021, Month.MARCH, 24, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2024-10-23"),
				LocalDateTime.of(2024, Month.OCTOBER, 23, 23, 59, 59)
			),
			Arguments.of(
				getTemporalAccessor("2024-09-24T12:35:47"),
				LocalDateTime.of(2024, Month.SEPTEMBER, 24, 12, 35, 47)
			)
		);
	}

	@ParameterizedTest
	@MethodSource("extractLocalDateTimeStartIfValidParams")
	void extractLocalDateTimeStartIfValid (
			TemporalAccessor theTemporalAccessor,
			@Nullable LocalDateTime theExpectedResult) {
		assertThat(DateUtils.extractLocalDateTimeForRangeStartOrEmpty(theTemporalAccessor))
			.isEqualTo(Optional.ofNullable(theExpectedResult));
	}

	@ParameterizedTest
	@MethodSource("extractLocalDateTimeEndIfValidParams")
	void extractLocalDateTimeEndIfValid (
		TemporalAccessor theTemporalAccessor,
		@Nullable LocalDateTime theExpectedResult) {
		assertThat(DateUtils.extractLocalDateTimeForRangeEndOrEmpty(theTemporalAccessor))
			.isEqualTo(Optional.ofNullable(theExpectedResult));
	}

	private static TemporalAccessor getTemporalAccessor(String theDateTimeString) {
		final DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(theDateTimeString);

		assertThat(dateTimeFormatter)
			.withFailMessage("Cannot find DateTimeFormatter for: " + theDateTimeString)
			.isNotNull();

		return DateUtils.parseDateTimeStringIfValid(
			theDateTimeString,
			dateTimeFormatter
		).orElseThrow(() -> new IllegalArgumentException("Unable to parse: " + theDateTimeString));
	}

	private static DateTimeFormatter getDateTimeFormatter(String theDateTimeString) {
		return switch (theDateTimeString.length()) {
			case 4 -> DATE_TIME_FORMATTER_YYYY;
			case 7 -> DATE_TIME_FORMATTER_YYYY_MM;
			case 10 -> DATE_TIME_FORMATTER_YYYY_MM_DD;
			case 13 -> DATE_TIME_FORMATTER_YYYY_MM_DD_HH;
			case 16 -> DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM;
			case 19 -> DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS;
			default -> null;
		};
	}
}
