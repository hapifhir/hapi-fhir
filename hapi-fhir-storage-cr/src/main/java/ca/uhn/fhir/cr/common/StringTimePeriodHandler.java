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
package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.cr.r4.measure.MeasurePeriodForEvaluation;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.DateUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
 * Currently, these are the date/time formats supported:
 * <ol>
 *     <li>yyyy</li>
 *     <li>yyyy-MM</li>
 *     <li>yyyy-MM-dd</li>
 *     <li>yyyy-MM-ddTHH:mm:ss</li>
 * </ol>
 * <p/>
 * Also used for various operations to serialize/deserialize dates to/from JSON classes.
 */
// TODO: LD:  Note that this is currently dead code and will be integrated once we change clinical-reasoning to accept
//
public class StringTimePeriodHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(StringTimePeriodHandler.class);

	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_INPUT = DateTimeFormatter.ofPattern("yyyy");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_INPUT = DateTimeFormatter.ofPattern("yyyy-MM");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_INPUT = DateTimeFormatter.ISO_DATE;
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_INPUT =
			DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	private static final DateTimeFormatter DATE_TIME_FORMATTER_JSON_SERIALIZE = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	private static final Map<Integer, DateTimeFormatter> VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH = Map.of(
			4, DATE_TIME_FORMATTER_YYYY_INPUT,
			7, DATE_TIME_FORMATTER_YYYY_MM_INPUT,
			10, DATE_TIME_FORMATTER_YYYY_MM_DD_INPUT,
			19, DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_INPUT);

	// The default, in order to signal to clinical-reasoning that none is set
	private final ZoneId myFallbackTimezone;

	public StringTimePeriodHandler(ZoneId theFallbackTimezone) {
		myFallbackTimezone = theFallbackTimezone;
	}

	/**
	 * Meant to serialize a ZonedDateTime into a String to pass to a JSON object.
	 */
	public String serialize(ZonedDateTime theZoneDateTime) {
		return DATE_TIME_FORMATTER_JSON_SERIALIZE.format(theZoneDateTime);
	}

	/**
	 * Meant to deserialize a String from a JSON object back into a ZonedDateTime.
	 */
	public ZonedDateTime deSerialize(String theInputDateString) {
		return ZonedDateTime.parse(theInputDateString, DATE_TIME_FORMATTER_JSON_SERIALIZE);
	}

	/**
	 * Get the start period as a parsed ZoneDateTime (ex 2024 to 2024-01-01T00:00:00-07:00).
	 *
	 * @param theInputDateTimeString A String representation of the period start date in yyyy, yyyy-MM, YYYY-MM-dd, or yyyy-MM-ddTHH:mm:ss
	 * @param theTimezone A 6 with which to convert the timestamp
	 * @return the parsed start date/time with zone info
	 */
	public ZonedDateTime getStartZonedDateTime(String theInputDateTimeString, ZoneId theTimezone) {
		final DateTimeFormatter dateTimeFormat = validateAndGetDateTimeFormat(theInputDateTimeString);

		final LocalDateTime localDateTime = validateAndGetLocalDateTime(
				theInputDateTimeString, dateTimeFormat, DateUtils::extractLocalDateTimeForRangeStartOrEmpty, true);

		return ZonedDateTime.of(localDateTime, theTimezone);
	}

	/**
	 * Get the end period as a parsed ZoneDateTime (ex 2024 to 2024-12-31T23:59:59-07:00).
	 *
	 * @param theInputDateTimeString A String representation of the period start date in yyyy, yyyy-MM, YYYY-MM-dd, or yyyy-MM-ddTHH:mm:ss
	 * @param theTimezone A ZoneId with which to convert the timestamp
	 * @return the parsed end date/time with zone info
	 */
	public ZonedDateTime getEndZonedDateTime(String theInputDateTimeString, ZoneId theTimezone) {
		final DateTimeFormatter dateTimeFormat = validateAndGetDateTimeFormat(theInputDateTimeString);

		final LocalDateTime localDateTime = validateAndGetLocalDateTime(
				theInputDateTimeString, dateTimeFormat, DateUtils::extractLocalDateTimeForRangeEndOrEmpty, true);

		return ZonedDateTime.of(localDateTime, theTimezone);
	}

	/**
	 * Convert the String representations of both period start and end dates to their ZonedDateTime equivalents
	 *
	 * @param theRequestDetails RequestDetails which may or may not contain a Timezone header
	 * @param thePeriodStart A String representation of the period start date in yyyy, yyyy-MM, YYYY-MM-dd, or yyyy-MM-ddTHH:mm:ss
	 * @param thePeriodEnd A String representation of the period start date in yyyy, yyyy-MM, YYYY-MM-dd, or yyyy-MM-ddTHH:mm:ss
	 * @return A MeasurePeriodForEvaluation containing both the period start and end as ZonedDateTimes
	 */
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
			return MeasurePeriodForEvaluation.EMPTY;
		}

		if (thePeriodStart.length() != thePeriodEnd.length()) {
			throw new InvalidRequestException(String.format(
					"%sPeriod start: %s and end: %s are not the same date/time formats",
					Msg.code(2555), thePeriodStart, thePeriodEnd));
		}

		final DateTimeFormatter dateTimeFormatterStart = validateAndGetDateTimeFormat(thePeriodStart);
		validateAndGetDateTimeFormat(thePeriodEnd);

		final LocalDateTime localDateTimeStart = validateAndGetLocalDateTime(
				thePeriodStart, dateTimeFormatterStart, DateUtils::extractLocalDateTimeForRangeStartOrEmpty, true);
		final LocalDateTime localDateTimeEnd = validateAndGetLocalDateTime(
				thePeriodEnd, dateTimeFormatterStart, DateUtils::extractLocalDateTimeForRangeEndOrEmpty, false);

		validateParsedPeriodStartAndEnd(thePeriodStart, thePeriodEnd, localDateTimeStart, localDateTimeEnd);

		return new MeasurePeriodForEvaluation(localDateTimeStart, localDateTimeEnd, theZoneId);
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
						"%sPeriod %s: %s has an unsupported format",
						Msg.code(2558), isStart ? "start" : "end", thePeriod)));
	}

	private DateTimeFormatter validateAndGetDateTimeFormat(String theInputDateTimeString) {
		final DateTimeFormatter dateTimeFormatter =
				VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH.get(theInputDateTimeString.length());

		if (dateTimeFormatter == null) {
			throw new InvalidRequestException(String.format(
					"%sUnsupported Date/Time format for input: %s", Msg.code(2559), theInputDateTimeString));
		}

		return dateTimeFormatter;
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
