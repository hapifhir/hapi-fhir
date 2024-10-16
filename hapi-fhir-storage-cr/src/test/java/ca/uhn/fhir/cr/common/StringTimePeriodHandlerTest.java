package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringTimePeriodHandlerTest {

	private static final String ZONE_ID_Z = "Z";
	private static final String TIMEZONE_UTC = ZoneOffset.UTC.getId();

	private static final ZoneId TIMEZONE_AMERICA_ST_JOHNS = ZoneId.of("America/St_Johns");
	private static final ZoneId TIMEZONE_AMERICA_TORONTO = ZoneId.of("America/Toronto");
	private static final ZoneId TIMEZONE_AMERICA_DENVER = ZoneId.of("America/Denver");

	private static final String TIMEZONE_AMERICA_ST_JOHNS_ID = TIMEZONE_AMERICA_ST_JOHNS.getId();
	private static final String TIMEZONE_AMERICA_TORONTO_ID = TIMEZONE_AMERICA_TORONTO.getId();
	private static final String TIMEZONE_AMERICA_DENVER_ID = TIMEZONE_AMERICA_DENVER.getId();

	private static final LocalDate LOCAL_DATE_2020_01_01 = LocalDate.of(2020, Month.JANUARY, 1);
	private static final LocalDate LOCAL_DATE_2021_12_31 = LocalDate.of(2021, Month.DECEMBER, 31);

	private static final LocalDate LOCAL_DATE_2022_02_01 = LocalDate.of(2022, Month.FEBRUARY, 1);
	private static final LocalDate LOCAL_DATE_2022_02_28 = LocalDate.of(2022, Month.FEBRUARY, 28);
	private static final LocalDate LOCAL_DATE_2022_08_31 = LocalDate.of(2022, Month.AUGUST, 31);

	private static final LocalDate LOCAL_DATE_2024_01_01 = LocalDate.of(2024, Month.JANUARY, 1);
	private static final LocalDate LOCAL_DATE_2024_01_02 = LocalDate.of(2024, Month.JANUARY, 2);
	private static final LocalDate LOCAL_DATE_2024_02_01 = LocalDate.of(2024, Month.FEBRUARY, 1);
	private static final LocalDate LOCAL_DATE_2024_02_25 = LocalDate.of(2024, Month.FEBRUARY, 25);
	private static final LocalDate LOCAL_DATE_2024_02_26 = LocalDate.of(2024, Month.FEBRUARY, 26);
	private static final LocalDate LOCAL_DATE_2024_02_29 = LocalDate.of(2024, Month.FEBRUARY, 29);
	private static final LocalDate LOCAL_DATE_2024_09_25 = LocalDate.of(2024, Month.SEPTEMBER, 25);
	private static final LocalDate LOCAL_DATE_2024_09_26 = LocalDate.of(2024, Month.SEPTEMBER, 26);

	private static final LocalTime LOCAL_TIME_00_00_00 = LocalTime.of(0,0,0);
	private static final LocalTime LOCAL_TIME_12_00_00 = LocalTime.of(12,0,0);
	private static final LocalTime LOCAL_TIME_23_59_59 = LocalTime.of(23,59,59);

	private static final LocalDateTime _2020_01_01_00_00_00 = LOCAL_DATE_2020_01_01.atTime(LOCAL_TIME_00_00_00);

	private static final LocalDateTime _2021_12_31_23_59_59 = LOCAL_DATE_2021_12_31.atTime(LOCAL_TIME_23_59_59);

	private static final LocalDateTime _2022_02_01_00_00_00 = LOCAL_DATE_2022_02_01.atTime(LOCAL_TIME_00_00_00);
	private static final LocalDateTime _2022_02_28_23_59_59 = LOCAL_DATE_2022_02_28.atTime(LOCAL_TIME_23_59_59);
	private static final LocalDateTime _2022_08_31_23_59_59 = LOCAL_DATE_2022_08_31.atTime(LOCAL_TIME_23_59_59);

	private static final LocalDateTime _2024_01_01_12_00_00 = LOCAL_DATE_2024_01_01.atTime(LOCAL_TIME_12_00_00);
	private static final LocalDateTime _2024_01_02_12_00_00 = LOCAL_DATE_2024_01_02.atTime(LOCAL_TIME_12_00_00);
	private static final LocalDateTime _2024_02_01_00_00_00 = LOCAL_DATE_2024_02_01.atTime(LOCAL_TIME_00_00_00);

	private static final LocalDateTime _2024_02_25_00_00_00 = LOCAL_DATE_2024_02_25.atTime(LOCAL_TIME_00_00_00);
	private static final LocalDateTime _2024_02_26_23_59_59 = LOCAL_DATE_2024_02_26.atTime(LOCAL_TIME_23_59_59);

	private static final LocalDateTime _2024_02_29_23_59_59 = LOCAL_DATE_2024_02_29.atTime(LOCAL_TIME_23_59_59);
	private static final LocalDateTime _2024_09_25_00_00_00 = LOCAL_DATE_2024_09_25.atTime(LOCAL_TIME_00_00_00);
	private static final LocalDateTime _2024_09_25_12_00_00 = LOCAL_DATE_2024_09_25.atTime(LOCAL_TIME_12_00_00);
	private static final LocalDateTime _2024_09_26_12_00_00 = LOCAL_DATE_2024_09_26.atTime(LOCAL_TIME_12_00_00);
	private static final LocalDateTime _2024_09_26_23_59_59 = LOCAL_DATE_2024_09_26.atTime(LOCAL_TIME_23_59_59);

	private final StringTimePeriodHandler myTestSubject = new StringTimePeriodHandler(ZoneOffset.UTC);

	private static Stream<Arguments> getStartZonedDateTime_happyPath_params() {
		return Stream.of(
			Arguments.of(null, null, null),
			Arguments.of(ZONE_ID_Z, null, null),
			Arguments.of(TIMEZONE_UTC, null, null),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, null, null),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, null, null),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, null, null),

			Arguments.of(null, 								"2020", _2020_01_01_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2020", _2020_01_01_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2020", _2020_01_01_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2020", _2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2020", _2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2020", _2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2022-02", _2022_02_01_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2022-02", _2022_02_01_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2022-02", _2022_02_01_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2022-02", _2022_02_01_00_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2022-02", _2022_02_01_00_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2022-02", _2022_02_01_00_00_00.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-02-25", _2024_02_25_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-02-25", _2024_02_25_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-02-25", _2024_02_25_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-02-25", _2024_02_25_00_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-02-25", _2024_02_25_00_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-02-25", _2024_02_25_00_00_00.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-09-25", _2024_09_25_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-09-25", _2024_09_25_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-09-25", _2024_09_25_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-09-25", _2024_09_25_00_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-09-25", _2024_09_25_00_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-09-25", _2024_09_25_00_00_00.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-01-01T12:00:00", _2024_01_01_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-01-01T12:00:00", _2024_01_01_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-01-01T12:00:00", _2024_01_01_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-01-01T12:00:00", _2024_01_01_12_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-01-01T12:00:00", _2024_01_01_12_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-01-01T12:00:00", _2024_01_01_12_00_00.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-09-25T12:00:00", _2024_09_25_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-09-25T12:00:00", _2024_09_25_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-09-25T12:00:00", _2024_09_25_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-09-25T12:00:00", _2024_09_25_12_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-09-25T12:00:00", _2024_09_25_12_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-09-25T12:00:00", _2024_09_25_12_00_00.atZone(TIMEZONE_AMERICA_DENVER))
		);
	}

	private static Stream<Arguments> getEndZonedDateTime_happyPath_params() {
		return Stream.of(
			Arguments.of(null, null, null),
			Arguments.of(ZONE_ID_Z, null, null),
			Arguments.of(TIMEZONE_UTC, null, null),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, null, null),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, null, null),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, null, null),

			Arguments.of(null, 								"2021", _2021_12_31_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2021", _2021_12_31_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2021", _2021_12_31_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2021", _2021_12_31_23_59_59.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2021", _2021_12_31_23_59_59.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2021", _2021_12_31_23_59_59.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2022-08", _2022_08_31_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2022-08", _2022_08_31_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2022-08", _2022_08_31_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2022-08", _2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2022-08", _2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2022-08", _2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2022-02", _2022_02_28_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2022-02", _2022_02_28_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2022-02", _2022_02_28_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2022-02", _2022_02_28_23_59_59.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2022-02", _2022_02_28_23_59_59.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2022-02", _2022_02_28_23_59_59.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-02", _2024_02_29_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-02", _2024_02_29_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-02", _2024_02_29_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-02", _2024_02_29_23_59_59.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-02", _2024_02_29_23_59_59.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-02", _2024_02_29_23_59_59.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-02-26", _2024_02_26_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-02-26", _2024_02_26_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-02-26", _2024_02_26_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-02-26", _2024_02_26_23_59_59.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-02-26", _2024_02_26_23_59_59.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-02-26", _2024_02_26_23_59_59.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-09-26", _2024_09_26_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-09-26", _2024_09_26_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-09-26", _2024_09_26_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-09-26", _2024_09_26_23_59_59.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-09-26", _2024_09_26_23_59_59.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-09-26", _2024_09_26_23_59_59.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-01-02T12:00:00", _2024_01_02_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-01-02T12:00:00", _2024_01_02_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-01-02T12:00:00", _2024_01_02_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-01-02T12:00:00", _2024_01_02_12_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-01-02T12:00:00", _2024_01_02_12_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-01-02T12:00:00", _2024_01_02_12_00_00.atZone(TIMEZONE_AMERICA_DENVER)),

			Arguments.of(null, 								"2024-09-26T12:00:00", _2024_09_26_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(ZONE_ID_Z, 							"2024-09-26T12:00:00", _2024_09_26_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_UTC, 						"2024-09-26T12:00:00", _2024_09_26_12_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, 		"2024-09-26T12:00:00", _2024_09_26_12_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, 		"2024-09-26T12:00:00", _2024_09_26_12_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, 		"2024-09-26T12:00:00", _2024_09_26_12_00_00.atZone(TIMEZONE_AMERICA_DENVER))
		);
	}

	@ParameterizedTest
	@MethodSource("getStartZonedDateTime_happyPath_params")
	void getStartZonedDateTime_happyPath(@Nullable String theTimezone, String theInputPeriodStart, ZonedDateTime expectedResult) {

		final ZonedDateTime actualResult =
			myTestSubject.getStartZonedDateTime(theInputPeriodStart, getRequestDetails(theTimezone));

		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@ParameterizedTest
	@MethodSource("getEndZonedDateTime_happyPath_params")
	void getEndZonedDateTime_happyPath(@Nullable String theTimezone, String theInputPeriodEnd, ZonedDateTime expectedResult) {

		final ZonedDateTime actualResult =
			myTestSubject.getEndZonedDateTime(theInputPeriodEnd, getRequestDetails(theTimezone));

		assertThat(actualResult).isEqualTo(expectedResult);
	}

	private static Stream<Arguments> errorParams() {
		return Stream.of(
			Arguments.of(null, "2024-01-01T12", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-01-01T12")),
			Arguments.of("Middle-Earth/Combe", "2024-01-02", new InvalidRequestException("HAPI-2561: Invalid value for Timezone header: Middle-Earth/Combe")),
			Arguments.of(null, "2024-01-01T12:00:00-02:30", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-01-01T12:00:00-02:30")),
			Arguments.of(ZONE_ID_Z, "2024-01-01T12:00:00-02:30", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-01-01T12:00:00-02:30")),
			Arguments.of("UTC", "2024-01-01T12:00:00-02:30", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-01-01T12:00:00-02:30")),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, "2024-01-01T12:00:00-02:30", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-01-01T12:00:00-02:30")),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, "2024-01-01T12:00:00-02:30", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-01-01T12:00:00-02:30")),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, "2024-01-01T12:00:00-02:30", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-01-01T12:00:00-02:30")),
			Arguments.of(null, "2024-09-25T12:00:00-06:00", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-09-25T12:00:00-06:00")),
			Arguments.of(ZONE_ID_Z, "2024-09-25T12:00:00-06:00", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-09-25T12:00:00-06:00")),
			Arguments.of("UTC", "2024-09-25T12:00:00-06:00", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-09-25T12:00:00-06:00")),
			Arguments.of(TIMEZONE_AMERICA_ST_JOHNS_ID, "2024-09-25T12:00:00-06:00", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-09-25T12:00:00-06:00")),
			Arguments.of(TIMEZONE_AMERICA_TORONTO_ID, "2024-09-25T12:00:00-06:00", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-09-25T12:00:00-06:00")),
			Arguments.of(TIMEZONE_AMERICA_DENVER_ID, "2024-09-25T12:00:00-06:00", new InvalidRequestException("HAPI-2559: Unsupported Date/Time format for input: 2024-09-25T12:00:00-06:00"))
		);
	}

	@ParameterizedTest
	@MethodSource("errorParams")
	void getStartZonedDateTime_errorPaths(@Nullable String theTimezone, @Nullable String theInputPeriodStart, InvalidRequestException theExpectedResult) {
		assertThatThrownBy(() -> myTestSubject.getStartZonedDateTime(theInputPeriodStart, getRequestDetails(theTimezone)))
			.hasMessage(theExpectedResult.getMessage())
			.isInstanceOf(theExpectedResult.getClass());
	}

	@ParameterizedTest
	@MethodSource("errorParams")
	void getEndZonedDateTime_errorPaths(@Nullable String theTimezone, @Nullable String theInputPeriodEnd, InvalidRequestException theExpectedResult) {
		assertThatThrownBy(() -> myTestSubject.getEndZonedDateTime(theInputPeriodEnd, getRequestDetails(theTimezone)))
			.hasMessage(theExpectedResult.getMessage())
			.isInstanceOf(theExpectedResult.getClass());
	}

	private static Stream<Arguments> serializeDeserializeRoundTripParams() {
		return Stream.of(
			Arguments.of(_2020_01_01_00_00_00.atZone(ZoneOffset.UTC), "2020-01-01T00:00:00Z"),
			Arguments.of(_2022_08_31_23_59_59.atZone(ZoneOffset.UTC), "2022-08-31T23:59:59Z"),
			Arguments.of(_2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS), "2020-01-01T00:00:00-03:30"),
			Arguments.of(_2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_ST_JOHNS), "2022-08-31T23:59:59-02:30"),
			Arguments.of(_2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_TORONTO), "2020-01-01T00:00:00-05:00"),
			Arguments.of(_2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_TORONTO), "2022-08-31T23:59:59-04:00"),
			Arguments.of(_2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_DENVER), "2020-01-01T00:00:00-07:00"),
			Arguments.of(_2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_DENVER), "2022-08-31T23:59:59-06:00")
		);
	}

	@ParameterizedTest
	@MethodSource("serializeDeserializeRoundTripParams")
	void serializeDeserializeRoundTrip(ZonedDateTime theInputDateTime, String theExpectedResult) {
		final String actualResult = myTestSubject.serialize(theInputDateTime);

		assertThat(actualResult).isEqualTo(theExpectedResult);

		final ZonedDateTime deSerialized = myTestSubject.deSerialize(actualResult);

		assertThat(deSerialized).isEqualTo(theInputDateTime);
	}

	private static Stream<Arguments> deSerializeRoundTripParams() {
		return Stream.of(
			Arguments.of("2020-01-01T00:00:00Z", _2020_01_01_00_00_00.atZone(ZoneOffset.UTC)),
			Arguments.of("2022-08-31T23:59:59Z", _2022_08_31_23_59_59.atZone(ZoneOffset.UTC)),
			Arguments.of("2020-01-01T00:00:00-03:30", _2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of("2022-08-31T23:59:59-02:30", _2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_ST_JOHNS)),
			Arguments.of("2020-01-01T00:00:00-05:00", _2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of("2022-08-31T23:59:59-04:00", _2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_TORONTO)),
			Arguments.of("2020-01-01T00:00:00-07:00", _2020_01_01_00_00_00.atZone(TIMEZONE_AMERICA_DENVER)),
			Arguments.of("2022-08-31T23:59:59-06:00", _2022_08_31_23_59_59.atZone(TIMEZONE_AMERICA_DENVER))
		);
	}

	@ParameterizedTest
	@MethodSource("deSerializeRoundTripParams")
	void deSerializeRoundTrip(String theInputString, ZonedDateTime theExpectedResult) {
		final ZonedDateTime actualResult = myTestSubject.deSerialize(theInputString);

		assertThat(actualResult).isEqualTo(theExpectedResult);

		final String serialized = myTestSubject.serialize(actualResult);

		assertThat(serialized).isEqualTo(theInputString);
	}

	private static RequestDetails getRequestDetails(@Nullable String theTimezone) {
		final SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		Optional.ofNullable(theTimezone)
			.ifPresent(nonNullTimezone -> systemRequestDetails .addHeader(Constants.HEADER_CLIENT_TIMEZONE, nonNullTimezone));
		return systemRequestDetails;
	}
}
