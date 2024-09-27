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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.DateUtils;
import jakarta.annotation.Nonnull;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Used immediately after receiving a REST call by $evaluate-measure and any potential variants to validate and convert
 * period start and end inputs to timezones with offsets.  The offset is determined from the request header a value for "Timezone".
 * <p/>
 * This class takes a fallback timezone that's used in case the request header does not contain a value for "Timezone".
 * <p/>
 * The output date/time format is the following to be compatible with clinical-reasoning: yyyy-MM-dd'T'HH:mm:ss.SXXX
 * ex: 2023-01-01T00:00:00.0-07:00
 * <p/>
 * Currently, these are the date/time formats supported:
 * <ol>
 *     <li>yyyy</li>
 *     <li>yyyy-MM</li>
 *     <li>yyyy-MM-dd</li>
 *     <li>yyyy-MM-ddTHH:mm:ss</li>
 * </ol>
 */
public class MeasureReportPeriodRequestValidatorAndConverter {
	private static final Logger ourLog = LoggerFactory.getLogger(MeasureReportPeriodRequestValidatorAndConverter.class);

	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_INPUT = DateTimeFormatter.ofPattern("yyyy");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_INPUT = DateTimeFormatter.ofPattern("yyyy-MM");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_INPUT = DateTimeFormatter.ISO_DATE;
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_INPUT =
			DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	// This specific format is needed because otherwise clinical-reasoning will error out when parsing the output
	// ex:  2023-01-01T00:00:00.0-07:00
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z_OUTPUT =
			DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SXXX");

	private static final Map<Integer, DateTimeFormatter> VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH = Map.of(
			4, DATE_TIME_FORMATTER_YYYY_INPUT,
			7, DATE_TIME_FORMATTER_YYYY_MM_INPUT,
			10, DATE_TIME_FORMATTER_YYYY_MM_DD_INPUT,
			19, DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_INPUT);

	private final ZoneId myFallbackTimezone;

	public MeasureReportPeriodRequestValidatorAndConverter(ZoneId theFallbackTimezone) {
		myFallbackTimezone = theFallbackTimezone;
	}

	public MeasurePeriodForEvaluation validateAndProcessTimezone(
			RequestDetails theRequestDetails, String thePeriodStart, String thePeriodEnd) {
		final ZoneId clientTimezone = getClientTimezoneOrInvalidRequest(theRequestDetails);

		return validateInputDates(thePeriodStart, thePeriodEnd, clientTimezone);
	}

	private MeasurePeriodForEvaluation validateInputDates(
			String thePeriodStart, String thePeriodEnd, ZoneId theZoneId) {

		if ((Strings.isBlank(thePeriodStart) && !Strings.isBlank(thePeriodEnd))
				|| (!Strings.isBlank(thePeriodStart) && Strings.isBlank(thePeriodEnd))) {
			throw new InvalidRequestException(String.format(
					"%sEither both period start: [%s] and end: [%s] must be empty or non empty",
					Msg.code(2554), thePeriodStart, thePeriodEnd));
		}

		if (Strings.isBlank(thePeriodStart) && Strings.isBlank(thePeriodEnd)) {
			return new MeasurePeriodForEvaluation(null, null);
		}

		if (thePeriodStart.length() != thePeriodEnd.length()) {
			throw new InvalidRequestException(String.format(
					"%sPeriod start: %s and end: %s are not the same date/time formats",
					Msg.code(2555), thePeriodStart, thePeriodEnd));
		}

		final DateTimeFormatter dateTimeFormatterStart = validateAndGetDateTimeFormat(thePeriodStart, thePeriodEnd);

		final LocalDateTime localDateTimeStart = validateAndGetLocalDateTime(
				thePeriodStart, dateTimeFormatterStart, DateUtils::extractLocalDateTimeForRangeStartOrEmpty, true);
		final LocalDateTime localDateTimeEnd = validateAndGetLocalDateTime(
				thePeriodEnd, dateTimeFormatterStart, DateUtils::extractLocalDateTimeForRangeEndOrEmpty, false);

		validateParsedPeriodStartAndEnd(thePeriodStart, thePeriodEnd, localDateTimeStart, localDateTimeEnd);

		final String periodStartFormatted = formatWithTimezone(localDateTimeStart, theZoneId);
		final String periodEndFormatted = formatWithTimezone(localDateTimeEnd, theZoneId);

		return new MeasurePeriodForEvaluation(periodStartFormatted, periodEndFormatted);
	}

	private static void validateParsedPeriodStartAndEnd(
			String theThePeriodStart,
			String theThePeriodEnd,
			LocalDateTime theLocalDateTimeStart,
			LocalDateTime theLocalDateTimeEnd) {
		// This should probably never happen
		if (theLocalDateTimeStart.isEqual(theLocalDateTimeEnd)) {
			throw new InvalidRequestException(String.format(
					"%sStart date: %s is the same as end date: %s",
					Msg.code(2556), theThePeriodStart, theThePeriodEnd));
		}

		if (theLocalDateTimeStart.isAfter(theLocalDateTimeEnd)) {
			throw new InvalidRequestException(String.format(
					"%sInvalid Interval - the ending boundary: %s must be greater than or equal to the starting boundary: %s",
					Msg.code(2557), theThePeriodEnd, theThePeriodStart));
		}
	}

	private LocalDateTime validateAndGetLocalDateTime(
			String thePeriod,
			DateTimeFormatter theDateTimeFormatter,
			Function<TemporalAccessor, Optional<LocalDateTime>> theTemporalAccessorToLocalDateTimeConverter,
			boolean isStart) {
		return DateUtils.parseDateTimeStringIfValid(thePeriod, theDateTimeFormatter)
				.flatMap(theTemporalAccessorToLocalDateTimeConverter)
				.orElseThrow(() -> new InvalidRequestException(String.format(
						"%s Period %s: %s has an unsupported format",
						Msg.code(2558), isStart ? "start" : "end", thePeriod)));
	}

	@Nonnull
	private static DateTimeFormatter validateAndGetDateTimeFormat(String theThePeriodStart, String theThePeriodEnd) {
		final DateTimeFormatter dateTimeFormatterStart =
				VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH.get(theThePeriodStart.length());

		if (dateTimeFormatterStart == null) {
			throw new InvalidRequestException(String.format(
					"%s Unsupported Date/Time format for period start: %s or end: %s",
					Msg.code(2559), theThePeriodStart, theThePeriodEnd));
		}
		return dateTimeFormatterStart;
	}

	private String formatWithTimezone(LocalDateTime theLocalDateTime, ZoneId theZoneId) {
		return theLocalDateTime.atZone(theZoneId).format(DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z_OUTPUT);
	}

	private ZoneId getClientTimezoneOrInvalidRequest(RequestDetails theRequestDetails) {
		final String clientTimezoneString = theRequestDetails.getHeader(Constants.HEADER_CLIENT_TIMEZONE);

		if (Strings.isNotBlank(clientTimezoneString)) {
			try {
				return ZoneId.of(clientTimezoneString);
			} catch (Exception exception) {
				throw new InvalidRequestException(
						String.format("%sInvalid value for Timezone header: %s", Msg.code(2560), clientTimezoneString));
			}
		}

		return myFallbackTimezone;
	}
}
