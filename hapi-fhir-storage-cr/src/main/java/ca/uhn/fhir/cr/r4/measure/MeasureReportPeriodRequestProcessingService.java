/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.DateUtils;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.Map;
import java.util.Optional;

// LUKETODO: copyright header
// LUKETODO: javadoc
// LUKETODO: unit test
/*
according to the docs:  periodStart and periodEnd support Dates (YYYY, YYYY-MM, or YYYY-MM-DD) and DateTimes (YYYY-MM-DDThh:mm:ss+zz:zz)
if the user passes in:
Header:  America/Toronto
periodStart “2024-09-25T12:00:00-06:00”
which timezone do we respect, or do we convert the periodStart to EDT?
also, what happens if they pass in start and end in different formats?
periodStart “2024-09-25T12:00:00-06:00"
periodEnd: “2024-10-01”
also, if we don’t already, I’ll return a 400 if they pass a start that’s after an end
 */

/*
| request input | request timezone | delta  | converted input to CR     |
| --------------| ---------------- | ------ | ------------------------- |
| 2024-09-24    |  null            | null   | 2024-09-24T00:00:00Z      |
| 2024-09-24    |  UTC             | null   | 2024-09-24T00:00:00Z      |
| 2024-09-24    |  Newfoundland    | null   | 2024-09-24T00:00:00-02:30 |
| 2024-09-24    |  Eastern         | null   | 2024-09-24T00:00:00-04:00 |
| 2024-09-24    |  Mountain        | null   | 2024-09-24T00:00:00-06:00 |
| 2024-02-24    |  null            | null   | 2024-02-24T00:00:00Z      |
| 2024-02-24    |  UTC             | null   | 2024-02-24T00:00:00Z      |
| 2024-02-24    |  Newfoundland    | null   | 2024-02-24T00:00:00-03:30 |
| 2024-02-24    |  Eastern         | null   | 2024-02-24T00:00:00-05:00 |
| 2024-02-24    |  Mountain        | null   | 2024-02-24T00:00:00-07:00 |
| 2024-09-24    |  null            | -1 sec | 2024-09-24T23:00:59Z      |
| 2024-09-24    |  UTC             | -1 sec | 2024-09-24T23:59:59Z      |
| 2024-09-24    |  Newfoundland    | -1 sec | 2024-09-24T23:59:59-02:30 |
| 2024-09-24    |  Eastern         | -1 sec | 2024-09-24T23:59:59-04:00 |
| 2024-09-24    |  Mountain        | -1 sec | 2024-09-24T23:59:59-06:00 |
| 2024-02-24    |  null            | -1 sec | 2024-02-24T23:59:59Z      |
| 2024-02-24    |  UTC             | -1 sec | 2024-02-24T23:59:59Z      |
| 2024-02-24    |  Newfoundland    | -1 sec | 2024-02-24T23:59:59-03:30 |
| 2024-02-24    |  Eastern         | -1 sec | 2024-02-24T23:59:59-05:00 |
| 2024-02-24    |  Mountain        | -1 sec | 2024-02-24T23:59:59-07:00 |
 */
public class MeasureReportPeriodRequestProcessingService {
	private static final Logger ourLog = LoggerFactory.getLogger(MeasureReportPeriodRequestProcessingService.class);

	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_INPUT = DateTimeFormatter.ofPattern("yyyy");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_INPUT = DateTimeFormatter.ofPattern("yyyy-MM");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_INPUT = DateTimeFormatter.ISO_DATE;
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z_INPUT_OR_OUTPUT =
			DateTimeFormatter.ISO_OFFSET_DATE_TIME;
	private static final Duration MINUS_ONE_SECOND = Duration.of(-1, ChronoUnit.SECONDS);

	private static final Map<Integer, DateTimeFormatter> VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH = Map.of(
			4, DATE_TIME_FORMATTER_YYYY_INPUT,
			7, DATE_TIME_FORMATTER_YYYY_MM_INPUT,
			10, DATE_TIME_FORMATTER_YYYY_MM_DD_INPUT,
			20, DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z_INPUT_OR_OUTPUT,
			25, DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z_INPUT_OR_OUTPUT);

	private final ZoneId myFallbackTimezone;

	public MeasureReportPeriodRequestProcessingService(ZoneId theFallbackTimezone) {
		myFallbackTimezone = theFallbackTimezone;
	}

	public MeasurePeriodForEvaluation validateAndProcessTimezone(
			RequestDetails theRequestDetails, String thePeriodStart, String thePeriodEnd) {
		final ZoneId clientTimezone = getClientTimezoneOrInvalidRequest(theRequestDetails);

		return validateInputDates(thePeriodStart, thePeriodEnd, clientTimezone);
	}

	private MeasurePeriodForEvaluation validateInputDates(
			String thePeriodStart, String thePeriodEnd, ZoneId theZoneId) {
		if (Strings.isBlank(thePeriodStart) || Strings.isBlank(thePeriodEnd)) {
			throw new InvalidRequestException(
					String.format("Either start: [%s] or end: [%s] or both are blank", thePeriodStart, thePeriodEnd));
		}

		if (thePeriodStart.length() != thePeriodEnd.length()) {
			throw new InvalidRequestException(String.format(
					"Period start: %s and end: %s are not the same date/time formats", thePeriodStart, thePeriodEnd));
		}

		final DateTimeFormatter dateTimeFormatterStart =
				VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH.get(thePeriodStart.length());

		if (dateTimeFormatterStart == null) {
			throw new InvalidRequestException(String.format(
					"Either start: %s or end: %s or both have an supported date/time format",
					thePeriodStart, thePeriodEnd));
		}

		final Optional<TemporalAccessor> optTemporalAccessorStart =
				DateUtils.parseDateTimeStringIfValid(thePeriodStart, dateTimeFormatterStart);
		final Optional<TemporalAccessor> optTemporalAccessorEnd =
				DateUtils.parseDateTimeStringIfValid(thePeriodEnd, dateTimeFormatterStart);

		if (optTemporalAccessorStart.isEmpty() || optTemporalAccessorEnd.isEmpty()) {
			throw new InvalidRequestException("Either start or end period have an unsupported format");
		}

		final Optional<LocalDateTime> optLocalDateTimeStart =
				DateUtils.extractLocalDateTimeIfValid(optTemporalAccessorStart.get());
		final Optional<LocalDateTime> optLocalDateTimeEnd =
				DateUtils.extractLocalDateTimeIfValid(optTemporalAccessorEnd.get());

		final Optional<ZoneOffset> optZoneOffsetStart =
				DateUtils.getZoneOffsetIfSupported(optTemporalAccessorStart.get());
		final Optional<ZoneOffset> optZoneOffsetEnd = DateUtils.getZoneOffsetIfSupported(optTemporalAccessorEnd.get());

		if (optLocalDateTimeStart.isEmpty() || optLocalDateTimeEnd.isEmpty()) {
			throw new InvalidRequestException("Either start or end period have an unsupported format");
		}

		final LocalDateTime localDateTimeStart = optLocalDateTimeStart.get();
		final LocalDateTime localDateTimeEnd = optLocalDateTimeEnd.get();

		if (localDateTimeStart.isEqual(localDateTimeEnd)) {
			throw new InvalidRequestException(
					String.format("Start date: %s is the same as end date: %s", thePeriodStart, thePeriodEnd));
		}

		if (localDateTimeStart.isAfter(localDateTimeEnd)) {
			throw new InvalidRequestException(
					String.format("Start date: %s is after end date: %s", thePeriodStart, thePeriodEnd));
		}

		if (!optZoneOffsetStart.equals(optZoneOffsetEnd)) {
			throw new InvalidRequestException(String.format(
					"Zone offsets do not match for period start: %s and end: %s", thePeriodStart, thePeriodEnd));
		}

		// Either the input Strings have no offset

		// Or the request timezone is different from the default
		if (optZoneOffsetStart.isEmpty() || !myFallbackTimezone.getRules().equals(theZoneId.getRules())) {

			// Preserve backwards compatibility
			if (optZoneOffsetStart.isPresent()) {
				ourLog.warn(
						"Start offset is not the same as the timezone header.  Ignoring both start and stop offsets for start: {} amd: {}",
						thePeriodStart,
						thePeriodEnd);
			}

			return new MeasurePeriodForEvaluation(
					formatWithZonePeriodStart(localDateTimeStart, theZoneId),
					formatWithZonePeriodEnd(localDateTimeEnd, theZoneId));
		}

		return new MeasurePeriodForEvaluation(
				formatWithZonePeriodStart(localDateTimeStart, optZoneOffsetStart.get()),
				formatWithZonePeriodEnd(localDateTimeEnd, optZoneOffsetEnd.get()));
	}

	private String formatWithZonePeriodStart(LocalDateTime theLocalDateTime, ZoneId theZoneId) {
		return formatWithZoneAndOptionalDelta(theLocalDateTime, theZoneId, null);
	}

	private String formatWithZonePeriodEnd(LocalDateTime theLocalDateTime, ZoneId theZoneId) {
		return formatWithZoneAndOptionalDelta(theLocalDateTime, theZoneId, MINUS_ONE_SECOND);
	}

	private String formatWithZoneAndOptionalDelta(
			LocalDateTime theLocalDateTime, ZoneId theZoneId, @Nullable TemporalAmount theDelta) {
		return DateUtils.formatWithZoneAndOptionalDelta(
				theLocalDateTime, theZoneId, DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z_INPUT_OR_OUTPUT, theDelta);
	}

	private ZoneId getClientTimezoneOrInvalidRequest(RequestDetails theRequestDetails) {
		final String clientTimezoneString = theRequestDetails.getHeader(Constants.HEADER_CLIENT_TIMEZONE);

		if (Strings.isNotBlank(clientTimezoneString)) {
			try {
				return ZoneId.of(clientTimezoneString);
			} catch (Exception exception) {
				throw new InvalidRequestException("Invalid value for Timezone header: " + clientTimezoneString);
			}
		}

		return myFallbackTimezone;
	}
}
