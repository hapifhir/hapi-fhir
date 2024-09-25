package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZoneOffset;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MeasureReportPeriodRequestProcessingServiceTest {

	private final MeasureReportPeriodRequestProcessingService myTestSubject = new MeasureReportPeriodRequestProcessingService(ZoneOffset.UTC);

	@ParameterizedTest
	@CsvSource( nullValues = {"null"},
		value={
			"null, 				2020, 						2021, 						2020-01-01T00:00:00Z, 		2020-12-31T23:59:59Z",
			"UTC, 				2020, 						2021, 						2020-01-01T00:00:00Z, 		2020-12-31T23:59:59Z",
			"America/St_Johns, 	2020, 						2021, 						2020-01-01T00:00:00-03:30, 	2020-12-31T23:59:59-03:30",
			"America/Toronto, 	2020, 						2021, 						2020-01-01T00:00:00-05:00, 	2020-12-31T23:59:59-05:00",
			"America/Denver, 	2020, 						2021, 						2020-01-01T00:00:00-07:00, 	2020-12-31T23:59:59-07:00",

			"null, 				2022-02, 					2022-08,					2022-02-01T00:00:00Z, 		2022-07-31T23:59:59Z",
			"UTC, 				2022-02, 					2022-08,					2022-02-01T00:00:00Z, 		2022-07-31T23:59:59Z",
			"America/St_Johns, 	2022-02, 					2022-08, 					2022-02-01T00:00:00-03:30, 	2022-07-31T23:59:59-02:30",
			"America/Toronto, 	2022-02, 					2022-08, 					2022-02-01T00:00:00-05:00, 	2022-07-31T23:59:59-04:00",
			"America/Denver, 	2022-02, 					2022-08, 					2022-02-01T00:00:00-07:00, 	2022-07-31T23:59:59-06:00",

			"null, 				2024-02-25, 				2024-02-26, 				2024-02-25T00:00:00Z, 		2024-02-25T23:59:59Z",
			"UTC, 				2024-02-25, 				2024-02-26, 				2024-02-25T00:00:00Z, 		2024-02-25T23:59:59Z",
			"America/St_Johns, 	2024-02-25, 				2024-02-26, 				2024-02-25T00:00:00-03:30, 	2024-02-25T23:59:59-03:30",
			"America/Toronto, 	2024-02-25, 				2024-02-26, 				2024-02-25T00:00:00-05:00, 	2024-02-25T23:59:59-05:00",
			"America/Denver, 	2024-02-25, 				2024-02-26, 				2024-02-25T00:00:00-07:00, 	2024-02-25T23:59:59-07:00",

			"null, 				2024-09-25, 				2024-09-26, 				2024-09-25T00:00:00Z, 		2024-09-25T23:59:59Z",
			"UTC, 				2024-09-25, 				2024-09-26, 				2024-09-25T00:00:00Z, 		2024-09-25T23:59:59Z",
			"America/St_Johns, 	2024-09-25, 				2024-09-26, 				2024-09-25T00:00:00-02:30, 	2024-09-25T23:59:59-02:30",
			"America/Toronto, 	2024-09-25, 				2024-09-26, 				2024-09-25T00:00:00-04:00, 	2024-09-25T23:59:59-04:00",
			"America/Denver, 	2024-09-25, 				2024-09-26, 				2024-09-25T00:00:00-06:00, 	2024-09-25T23:59:59-06:00",

			"null, 				2024-09-25T12:00:00-06:00, 	2024-09-26T12:00:00-06:00, 	2024-09-25T12:00:00-06:00,	2024-09-26T11:59:59-06:00",
			"UTC, 				2024-09-25T12:00:00-06:00, 	2024-09-26T12:00:00-06:00, 	2024-09-25T12:00:00-06:00,	2024-09-26T11:59:59-06:00",
			"America/St_Johns,	2024-09-25T12:00:00-06:00, 	2024-09-26T12:00:00-06:00, 	2024-09-25T12:00:00-02:30,	2024-09-26T11:59:59-02:30",
			"America/Toronto,	2024-09-25T12:00:00-06:00, 	2024-09-26T12:00:00-06:00, 	2024-09-25T12:00:00-04:00,	2024-09-26T11:59:59-04:00",
			"America/Denver,	2024-09-25T12:00:00-06:00, 	2024-09-26T12:00:00-06:00, 	2024-09-25T12:00:00-06:00,	2024-09-26T11:59:59-06:00",
		}
	)
	void validateAndProcessTimezone_happyPath(@Nullable String theTimezone, String theInputPeriodStart, String theInputPeriodEnd, String theOutputPeriodStart, String theOutputPeriodEnd) {

		final MeasurePeriodForEvaluation actualResult =
			myTestSubject.validateAndProcessTimezone(getRequestDetails(theTimezone), theInputPeriodStart, theInputPeriodEnd);

		final MeasurePeriodForEvaluation expectedResult = new MeasurePeriodForEvaluation(theOutputPeriodStart, theOutputPeriodEnd);
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	public static Stream<Arguments> errorParams() {
		return Stream.of(
			Arguments.of(null, null, null, new InvalidRequestException("Either start: [null] or end: [null] or both are blank")),
			Arguments.of(null, "", "", new InvalidRequestException("Either start: [] or end: [] or both are blank")),
			Arguments.of(null, "2024", "2024-01", new InvalidRequestException("Period start: 2024 and end: 2024-01 are not the same date/time formats")),
			Arguments.of(null, "2024-01-01T12", "2024-01-01T12", new InvalidRequestException("Either start: 2024-01-01T12 or end: 2024-01-01T12 or both have an supported date/time format")),
			Arguments.of(null, "2024-01-01T12:00:00-02:30", "2024-01-02T12:00:00-04:00", new InvalidRequestException("Zone offsets do not match for period start: 2024-01-01T12:00:00-02:30 and end: 2024-01-02T12:00:00-04:00")),
			Arguments.of(null, "2024-01-01", "2024-01-01", new InvalidRequestException("Start date: 2024-01-01 is the same as end date: 2024-01-01")),
			Arguments.of(null, "2024-01-02", "2024-01-01", new InvalidRequestException("Start date: 2024-01-02 is after end date: 2024-01-01")),
			Arguments.of("Middle-Earth/Combe", "2024-01-02", "2024-01-03", new InvalidRequestException("Invalid value for Timezone header: Middle-Earth/Combe"))
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
