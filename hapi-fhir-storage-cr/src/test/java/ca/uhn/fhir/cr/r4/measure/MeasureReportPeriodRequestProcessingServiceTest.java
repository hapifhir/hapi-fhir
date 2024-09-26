package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MeasureReportPeriodRequestProcessingServiceTest {

	private final MeasureReportPeriodRequestProcessingService myTestSubject = new MeasureReportPeriodRequestProcessingService(ZoneOffset.UTC);

	@Test
	void dammit() {
		final String expectedResult = "2020-01-01T00:00:00.0-05:00";
		final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SXXX");
		final ZonedDateTime input = ZonedDateTime.of(LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0), ZoneId.of("America/Toronto"));

		final String actualResult = input.format(dateTimeFormatter);

		assertThat(actualResult).isEqualTo(expectedResult);
	}

	// LUKETODO:  what happens if only one is null?
	@ParameterizedTest
	@CsvSource( nullValues = {"null"},
		value={
		//  request timezone    start                   end                     expected converted start    	expected converted end
			"null, 				null, 					null, 					null,							null",
			"Z, 				null, 					null, 					null,							null",
			"UTC, 				null, 					null, 					null,							null",
			"America/St_Johns, 	null, 					null, 					null,							null",
			"America/Toronto, 	null, 					null, 					null,							null",
			"America/Denver, 	null, 					null, 					null,							null",

			"null, 				2020, 					2021, 					2020-01-01T00:00:00.0Z, 		2021-12-31T23:59:59.0Z",
			"Z, 				2020, 					2021, 					2020-01-01T00:00:00.0Z, 		2021-12-31T23:59:59.0Z",
			"UTC, 				2020, 					2021, 					2020-01-01T00:00:00.0Z, 		2021-12-31T23:59:59.0Z",
			"America/St_Johns, 	2020, 					2021, 					2020-01-01T00:00:00.0-03:30, 	2021-12-31T23:59:59.0-03:30",
			"America/Toronto, 	2020, 					2021, 					2020-01-01T00:00:00.0-05:00, 	2021-12-31T23:59:59.0-05:00",
			"America/Denver, 	2020, 					2021, 					2020-01-01T00:00:00.0-07:00, 	2021-12-31T23:59:59.0-07:00",

			"null, 				2022-02, 				2022-08,				2022-02-01T00:00:00.0Z, 		2022-08-31T23:59:59.0Z",
			"Z, 				2022-02, 				2022-08,				2022-02-01T00:00:00.0Z, 		2022-08-31T23:59:59.0Z",
			"UTC, 				2022-02, 				2022-08,				2022-02-01T00:00:00.0Z, 		2022-08-31T23:59:59.0Z",
			"America/St_Johns, 	2022-02, 				2022-08, 				2022-02-01T00:00:00.0-03:30, 	2022-08-31T23:59:59.0-02:30",
			"America/Toronto, 	2022-02, 				2022-08, 				2022-02-01T00:00:00.0-05:00, 	2022-08-31T23:59:59.0-04:00",
			"America/Denver, 	2022-02, 				2022-08, 				2022-02-01T00:00:00.0-07:00, 	2022-08-31T23:59:59.0-06:00",

			"null, 				2022-02, 				2022-02,				2022-02-01T00:00:00.0Z, 		2022-02-28T23:59:59.0Z",
			"Z, 				2022-02, 				2022-02,				2022-02-01T00:00:00.0Z, 		2022-02-28T23:59:59.0Z",
			"UTC, 				2022-02, 				2022-02,				2022-02-01T00:00:00.0Z, 		2022-02-28T23:59:59.0Z",
			"America/St_Johns, 	2022-02, 				2022-02, 				2022-02-01T00:00:00.0-03:30, 		2022-02-28T23:59:59.0-03:30",
			"America/Toronto, 	2022-02, 				2022-02, 				2022-02-01T00:00:00.0-05:00, 		2022-02-28T23:59:59.0-05:00",
			"America/Denver, 	2022-02, 				2022-02, 				2022-02-01T00:00:00.0-07:00, 		2022-02-28T23:59:59.0-07:00",

			// Leap year
			"null, 				2024-02, 				2024-02,				2024-02-01T00:00:00.0Z,			2024-02-29T23:59:59.0Z",
			"Z, 				2024-02, 				2024-02,				2024-02-01T00:00:00.0Z,			2024-02-29T23:59:59.0Z",
			"UTC, 				2024-02, 				2024-02,				2024-02-01T00:00:00.0Z,			2024-02-29T23:59:59.0Z",
			"America/St_Johns, 	2024-02, 				2024-02, 				2024-02-01T00:00:00.0-03:30, 	2024-02-29T23:59:59.0-03:30",
			"America/Toronto, 	2024-02, 				2024-02, 				2024-02-01T00:00:00.0-05:00, 	2024-02-29T23:59:59.0-05:00",
			"America/Denver, 	2024-02, 				2024-02, 				2024-02-01T00:00:00.0-07:00, 	2024-02-29T23:59:59.0-07:00",

			"null, 				2024-02-25, 			2024-02-26, 			2024-02-25T00:00:00.0Z,			2024-02-26T23:59:59.0Z",
			"Z, 				2024-02-25, 			2024-02-26, 			2024-02-25T00:00:00.0Z,			2024-02-26T23:59:59.0Z",
			"UTC, 				2024-02-25, 			2024-02-26, 			2024-02-25T00:00:00.0Z,			2024-02-26T23:59:59.0Z",
			"America/St_Johns, 	2024-02-25, 			2024-02-26, 			2024-02-25T00:00:00.0-03:30, 	2024-02-26T23:59:59.0-03:30",
			"America/Toronto, 	2024-02-25, 			2024-02-26, 			2024-02-25T00:00:00.0-05:00, 	2024-02-26T23:59:59.0-05:00",
			"America/Denver, 	2024-02-25, 			2024-02-26, 			2024-02-25T00:00:00.0-07:00, 	2024-02-26T23:59:59.0-07:00",

			"null, 				2024-09-25, 			2024-09-26, 			2024-09-25T00:00:00.0Z, 			2024-09-26T23:59:59.0Z",
			"Z, 				2024-09-25, 			2024-09-26, 			2024-09-25T00:00:00.0Z, 			2024-09-26T23:59:59.0Z",
			"UTC, 				2024-09-25, 			2024-09-26, 			2024-09-25T00:00:00.0Z, 			2024-09-26T23:59:59.0Z",
			"America/St_Johns, 	2024-09-25, 			2024-09-26, 			2024-09-25T00:00:00.0-02:30, 	2024-09-26T23:59:59.0-02:30",
			"America/Toronto, 	2024-09-25, 			2024-09-26, 			2024-09-25T00:00:00.0-04:00, 	2024-09-26T23:59:59.0-04:00",
			"America/Denver, 	2024-09-25, 			2024-09-26, 			2024-09-25T00:00:00.0-06:00, 	2024-09-26T23:59:59.0-06:00",

			"null, 				2024-01-01T12:00:00, 	2024-01-02T12:00:00, 	2024-01-01T12:00:00.0Z,			2024-01-02T12:00:00.0Z",
			"Z, 				2024-01-01T12:00:00, 	2024-01-02T12:00:00, 	2024-01-01T12:00:00.0Z,			2024-01-02T12:00:00.0Z",
			"UTC, 				2024-01-01T12:00:00, 	2024-01-02T12:00:00, 	2024-01-01T12:00:00.0Z,			2024-01-02T12:00:00.0Z",
			"America/St_Johns,	2024-01-01T12:00:00, 	2024-01-02T12:00:00, 	2024-01-01T12:00:00.0-03:30,	2024-01-02T12:00:00.0-03:30",
			"America/Toronto,	2024-01-01T12:00:00, 	2024-01-02T12:00:00, 	2024-01-01T12:00:00.0-05:00,	2024-01-02T12:00:00.0-05:00",
			"America/Denver,	2024-01-01T12:00:00, 	2024-01-02T12:00:00, 	2024-01-01T12:00:00.0-07:00,	2024-01-02T12:00:00.0-07:00",

			"null, 				2024-09-25T12:00:00, 	2024-09-26T12:00:00, 	2024-09-25T12:00:00.0Z,			2024-09-26T12:00:00.0Z",
			"Z, 				2024-09-25T12:00:00, 	2024-09-26T12:00:00, 	2024-09-25T12:00:00.0Z,			2024-09-26T12:00:00.0Z",
			"UTC, 				2024-09-25T12:00:00, 	2024-09-26T12:00:00, 	2024-09-25T12:00:00.0Z,			2024-09-26T12:00:00.0Z",
			"America/St_Johns,	2024-09-25T12:00:00, 	2024-09-26T12:00:00, 	2024-09-25T12:00:00.0-02:30,	2024-09-26T12:00:00.0-02:30",
			"America/Toronto,	2024-09-25T12:00:00, 	2024-09-26T12:00:00, 	2024-09-25T12:00:00.0-04:00,	2024-09-26T12:00:00.0-04:00",
			"America/Denver,	2024-09-25T12:00:00, 	2024-09-26T12:00:00, 	2024-09-25T12:00:00.0-06:00,	2024-09-26T12:00:00.0-06:00",
		}
	)
	void validateAndProcessTimezone_happyPath(@Nullable String theTimezone, String theInputPeriodStart, String theInputPeriodEnd, String theOutputPeriodStart, String theOutputPeriodEnd) {

		final MeasurePeriodForEvaluation actualResult =
			myTestSubject.validateAndProcessTimezone(getRequestDetails(theTimezone), theInputPeriodStart, theInputPeriodEnd);

		final MeasurePeriodForEvaluation expectedResult = new MeasurePeriodForEvaluation(theOutputPeriodStart, theOutputPeriodEnd);
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	private static Stream<Arguments> errorParams() {
		return Stream.of(
			Arguments.of(null, "2024", "2024-01", new InvalidRequestException("Period start: 2024 and end: 2024-01 are not the same date/time formats")),
			Arguments.of(null, "2024-01-01T12", "2024-01-01T12", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-01-01T12 or end: 2024-01-01T12")),
			Arguments.of(null, "2024-01-02", "2024-01-01", new InvalidRequestException("Invalid Interval - the ending boundary: 2024-01-01 must be greater than or equal to the starting boundary: 2024-01-02")),
			Arguments.of("Middle-Earth/Combe", "2024-01-02", "2024-01-03", new InvalidRequestException("Invalid value for Timezone header: Middle-Earth/Combe")),
			Arguments.of(null, "2024-01-01T12:00:00-02:30", "2024-01-02T12:00:00-04:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-01-01T12:00:00-02:30 or end: 2024-01-02T12:00:00-04:00")),
			Arguments.of("Z", "2024-01-01T12:00:00-02:30", "2024-01-02T12:00:00-04:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-01-01T12:00:00-02:30 or end: 2024-01-02T12:00:00-04:00")),
			Arguments.of("UTC", "2024-01-01T12:00:00-02:30", "2024-01-02T12:00:00-04:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-01-01T12:00:00-02:30 or end: 2024-01-02T12:00:00-04:00")),
			Arguments.of("America/St_Johns", "2024-01-01T12:00:00-02:30", "2024-01-02T12:00:00-04:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-01-01T12:00:00-02:30 or end: 2024-01-02T12:00:00-04:00")),
			Arguments.of("America/Toronto", "2024-01-01T12:00:00-02:30", "2024-01-02T12:00:00-04:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-01-01T12:00:00-02:30 or end: 2024-01-02T12:00:00-04:00")),
			Arguments.of("America/Denver", "2024-01-01T12:00:00-02:30", "2024-01-02T12:00:00-04:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-01-01T12:00:00-02:30 or end: 2024-01-02T12:00:00-04:00")),
			Arguments.of(null, "2024-09-25T12:00:00-06:00", "2024-09-26T12:00:00-06:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-09-25T12:00:00-06:00 or end: 2024-09-26T12:00:00-06:00")),
			Arguments.of("Z", "2024-09-25T12:00:00-06:00", "2024-09-26T12:00:00-06:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-09-25T12:00:00-06:00 or end: 2024-09-26T12:00:00-06:00")),
			Arguments.of("UTC", "2024-09-25T12:00:00-06:00", "2024-09-26T12:00:00-06:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-09-25T12:00:00-06:00 or end: 2024-09-26T12:00:00-06:00")),
			Arguments.of("America/St_Johns", "2024-09-25T12:00:00-06:00", "2024-09-26T12:00:00-06:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-09-25T12:00:00-06:00 or end: 2024-09-26T12:00:00-06:00")),
			Arguments.of("America/Toronto", "2024-09-25T12:00:00-06:00", "2024-09-26T12:00:00-06:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-09-25T12:00:00-06:00 or end: 2024-09-26T12:00:00-06:00")),
			Arguments.of("America/Denver", "2024-09-25T12:00:00-06:00", "2024-09-26T12:00:00-06:00", new InvalidRequestException("Unsupported Date/Time format for period start: 2024-09-25T12:00:00-06:00 or end: 2024-09-26T12:00:00-06:00"))
		);
	}

	@ParameterizedTest
	@MethodSource("errorParams")
	void validateAndProcessTimezone_errorPaths(@Nullable String theTimezone, @Nullable String theInputPeriodStart, @Nullable String theInputPeriodEnd, InvalidRequestException theExpectedResult) {
		assertThatThrownBy(() -> myTestSubject.validateAndProcessTimezone(getRequestDetails(theTimezone), theInputPeriodStart, theInputPeriodEnd))
			.hasMessage(theExpectedResult.getMessage())
			.isInstanceOf(theExpectedResult.getClass());
	}

	private static RequestDetails getRequestDetails(@Nullable String theTimezone) {
		final SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		Optional.ofNullable(theTimezone)
			.ifPresent(nonNullTimezone -> systemRequestDetails .addHeader(Constants.HEADER_CLIENT_TIMEZONE, nonNullTimezone));
		return systemRequestDetails;
	}
}
