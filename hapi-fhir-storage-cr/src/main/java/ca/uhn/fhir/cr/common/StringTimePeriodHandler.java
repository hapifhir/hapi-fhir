/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.DateUtils;
import jakarta.annotation.Nullable;
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
	 * @param theRequestDetails RequestDetails that may or may not contain a Timezone header
	 * @return the parsed start date/time with zone info
	 */
	@Nullable
	public ZonedDateTime getStartZonedDateTime(
			@Nullable String theInputDateTimeString, RequestDetails theRequestDetails) {
		ourLog.debug("transforming String start date: {} to ZonedDateTime", theInputDateTimeString);
		return getStartZonedDateTime(theInputDateTimeString, getClientTimezoneOrInvalidRequest(theRequestDetails));
	}

	/**
	 * Get the start period as a parsed ZoneDateTime (ex 2024 to 2024-01-01T00:00:00-07:00).
	 *
	 * @param theInputDateTimeString A String representation of the period start date in yyyy, yyyy-MM, YYYY-MM-dd, or yyyy-MM-ddTHH:mm:ss
	 * @param theTimezone A ZoneId with which to convert the timestamp
	 * @return the parsed start date/time with zone info
	 */
	@Nullable
	public ZonedDateTime getStartZonedDateTime(@Nullable String theInputDateTimeString, ZoneId theTimezone) {
		return getZonedDateTime(
				theInputDateTimeString,
				theTimezone,
				true,
				// start date/time
				DateUtils::extractLocalDateTimeForRangeStartOrEmpty);
	}

	/**
	 * Get the end period as a parsed ZoneDateTime (ex 2024 to 2024-12-31T23:59:59-07:00).
	 *
	 * @param theInputDateTimeString A String representation of the period start date in yyyy, yyyy-MM, YYYY-MM-dd, or yyyy-MM-ddTHH:mm:ss
	 * @param theRequestDetails RequestDetails that may or may not contain a Timezone header
	 * @return the parsed end date/time with zone info
	 */
	@Nullable
	public ZonedDateTime getEndZonedDateTime(
			@Nullable String theInputDateTimeString, RequestDetails theRequestDetails) {
		ourLog.debug("transforming String end date: {} to ZonedDateTime", theInputDateTimeString);
		return getEndZonedDateTime(theInputDateTimeString, getClientTimezoneOrInvalidRequest(theRequestDetails));
	}

	/**
	 * Get the end period as a parsed ZoneDateTime (ex 2024 to 2024-12-31T23:59:59-07:00).
	 *
	 * @param theInputDateTimeString A String representation of the period start date in yyyy, yyyy-MM, YYYY-MM-dd, or yyyy-MM-ddTHH:mm:ss
	 * @param theTimezone A ZoneId with which to convert the timestamp
	 * @return the parsed end date/time with zone info
	 */
	@Nullable
	public ZonedDateTime getEndZonedDateTime(@Nullable String theInputDateTimeString, ZoneId theTimezone) {
		return getZonedDateTime(
				theInputDateTimeString,
				theTimezone,
				false,
				// end date/time
				DateUtils::extractLocalDateTimeForRangeEndOrEmpty);
	}

	private ZonedDateTime getZonedDateTime(
			String theInputDateTimeString,
			ZoneId theTimezone,
			boolean theIsStart,
			Function<TemporalAccessor, Optional<LocalDateTime>> theStartOrEndExtractFunction) {

		// We may pass null periods to clinical-reasoning
		if (theInputDateTimeString == null) {
			return null;
		}

		final DateTimeFormatter dateTimeFormat = validateAndGetDateTimeFormat(theInputDateTimeString);

		final LocalDateTime localDateTime = validateAndGetLocalDateTime(
				theInputDateTimeString, dateTimeFormat, theStartOrEndExtractFunction, theIsStart);

		final ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, theTimezone);

		ourLog.debug(
				"successfully transformed String date: {} to ZonedDateTime: {}", theInputDateTimeString, zonedDateTime);

		return zonedDateTime;
	}

	private LocalDateTime validateAndGetLocalDateTime(
			String thePeriod,
			DateTimeFormatter theDateTimeFormatter,
			Function<TemporalAccessor, Optional<LocalDateTime>> theTemporalAccessorToLocalDateTimeConverter,
			boolean isStart) {
		return DateUtils.parseDateTimeStringIfValid(thePeriod, theDateTimeFormatter)
				.flatMap(theTemporalAccessorToLocalDateTimeConverter)
				.orElseThrow(() -> {
					ourLog.warn(
							"{}Period {}: {} has an unsupported format",
							Msg.code(2558),
							isStart ? "start" : "end",
							thePeriod);

					return new InvalidRequestException(String.format(
							"%sPeriod %s: %s has an unsupported format",
							Msg.code(2558), isStart ? "start" : "end", thePeriod));
				});
	}

	private DateTimeFormatter validateAndGetDateTimeFormat(String theInputDateTimeString) {
		final DateTimeFormatter dateTimeFormatter =
				VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH.get(theInputDateTimeString.length());

		if (dateTimeFormatter == null) {
			ourLog.warn("{}Unsupported Date/Time format for input: {}", Msg.code(2559), theInputDateTimeString);

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
				ourLog.warn("{}Invalid value for Timezone header: {}", Msg.code(2561), clientTimezoneString);
				throw new InvalidRequestException(
						String.format("%sInvalid value for Timezone header: %s", Msg.code(2561), clientTimezoneString));
			}
		}

		return myFallbackTimezone;
	}
}
