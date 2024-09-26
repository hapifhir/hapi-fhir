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
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

// LUKETODO: changelog
// LUKETODO: javadoc
public class MeasureReportPeriodRequestProcessingService {
	private static final Logger ourLog = LoggerFactory.getLogger(MeasureReportPeriodRequestProcessingService.class);

	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_INPUT = DateTimeFormatter.ofPattern("yyyy");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_INPUT = DateTimeFormatter.ofPattern("yyyy-MM");
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_INPUT = DateTimeFormatter.ISO_DATE;
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_INPUT =
			DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_Z_OUTPUT =
			DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	private static final Map<Integer, DateTimeFormatter> VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH = Map.of(
			4, DATE_TIME_FORMATTER_YYYY_INPUT,
			7, DATE_TIME_FORMATTER_YYYY_MM_INPUT,
			10, DATE_TIME_FORMATTER_YYYY_MM_DD_INPUT,
			19, DATE_TIME_FORMATTER_YYYY_MM_DD_HH_MM_SS_INPUT);

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
			return new MeasurePeriodForEvaluation(null, null);
		}

		if (thePeriodStart.length() != thePeriodEnd.length()) {
			throw new InvalidRequestException(String.format(
					"Period start: %s and end: %s are not the same date/time formats", thePeriodStart, thePeriodEnd));
		}

		final DateTimeFormatter dateTimeFormatterStart =
				VALID_DATE_TIME_FORMATTERS_BY_FORMAT_LENGTH.get(thePeriodStart.length());

		if (dateTimeFormatterStart == null) {
			throw new InvalidRequestException(String.format(
					"Unsupported Date/Time format for period start: %s or end: %s", thePeriodStart, thePeriodEnd));
		}

		final Optional<LocalDateTime> optLocalDateTimeStart = DateUtils.parseDateTimeStringIfValid(
						thePeriodStart, dateTimeFormatterStart)
				.flatMap(DateUtils::extractLocalDateTimeForRangeStartOrEmpty);

		final Optional<LocalDateTime> optLocalDateTimeEnd = DateUtils.parseDateTimeStringIfValid(
						thePeriodEnd, dateTimeFormatterStart)
				.flatMap(DateUtils::extractLocalDateTimeForRangeEndOrEmpty);

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

		return new MeasurePeriodForEvaluation(
				formatWithTimezone(localDateTimeStart, theZoneId), formatWithTimezone(localDateTimeEnd, theZoneId));
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
				throw new InvalidRequestException("Invalid value for Timezone header: " + clientTimezoneString);
			}
		}

		return myFallbackTimezone;
	}
}
