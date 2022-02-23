package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * A utility class for parsing and formatting HTTP dates as used in cookies and
 * other headers.  This class handles dates as defined by RFC 2616 section
 * 3.3.1 as well as some other common non-standard formats.
 * <p>
 * This class is basically intended to be a high-performance workaround
 * for the fact that Java SimpleDateFormat is kind of expensive to
 * create and yet isn't thread safe.
 * </p>
 * <p>
 * This class was adapted from the class with the same name from the Jetty
 * project, licensed under the terms of the Apache Software License 2.0.
 * </p>
 */
public final class DateUtils {

	/**
	 * GMT TimeZone
	 */
	public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	/**
	 * Date format pattern used to parse HTTP date headers in RFC 1123 format.
	 */
	@SuppressWarnings("WeakerAccess")
	public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

	/**
	 * Date format pattern used to parse HTTP date headers in RFC 1036 format.
	 */
	@SuppressWarnings("WeakerAccess")
	public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";

	/**
	 * Date format pattern used to parse HTTP date headers in ANSI C
	 * {@code asctime()} format.
	 */
	@SuppressWarnings("WeakerAccess")
	public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";

	private static final String PATTERN_INTEGER_DATE = "yyyyMMdd";

	private static final String[] DEFAULT_PATTERNS = new String[]{
		PATTERN_RFC1123,
		PATTERN_RFC1036,
		PATTERN_ASCTIME
	};
	private static final Date DEFAULT_TWO_DIGIT_YEAR_START;

	static {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(GMT);
		calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
	}

	/**
	 * This class should not be instantiated.
	 */
	private DateUtils() {
	}

	/**
	 * A factory for {@link SimpleDateFormat}s. The instances are stored in a
	 * threadlocal way because SimpleDateFormat is not thread safe as noted in
	 * {@link SimpleDateFormat its javadoc}.
	 */
	final static class DateFormatHolder {

		private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = ThreadLocal.withInitial(() -> new SoftReference<>(new HashMap<>()));

		/**
		 * creates a {@link SimpleDateFormat} for the requested format string.
		 *
		 * @param pattern a non-{@code null} format String according to
		 *                {@link SimpleDateFormat}. The format is not checked against
		 *                {@code null} since all paths go through
		 *                {@link DateUtils}.
		 * @return the requested format. This simple DateFormat should not be used
		 * to {@link SimpleDateFormat#applyPattern(String) apply} to a
		 * different pattern.
		 */
		static SimpleDateFormat formatFor(final String pattern) {
			final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
			Map<String, SimpleDateFormat> formats = ref.get();
			if (formats == null) {
				formats = new HashMap<>();
				THREADLOCAL_FORMATS.set(
					new SoftReference<>(formats));
			}

			SimpleDateFormat format = formats.get(pattern);
			if (format == null) {
				format = new SimpleDateFormat(pattern, Locale.US);
				format.setTimeZone(TimeZone.getTimeZone("GMT"));
				formats.put(pattern, format);
			}

			return format;
		}

	}

	/**
	 * Parses a date value.  The formats used for parsing the date value are retrieved from
	 * the default http params.
	 *
	 * @param theDateValue the date value to parse
	 * @return the parsed date or null if input could not be parsed
	 */
	public static Date parseDate(final String theDateValue) {
		notNull(theDateValue, "Date value");
		String v = theDateValue;
		if (v.length() > 1 && v.startsWith("'") && v.endsWith("'")) {
			v = v.substring(1, v.length() - 1);
		}

		for (final String dateFormat : DEFAULT_PATTERNS) {
			final SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
			dateParser.set2DigitYearStart(DEFAULT_TWO_DIGIT_YEAR_START);
			final ParsePosition pos = new ParsePosition(0);
			final Date result = dateParser.parse(v, pos);
			if (pos.getIndex() != 0) {
				return result;
			}
		}
		return null;
	}

	public static Date getHighestInstantFromDate(Date theDateValue) {
		Calendar sourceCal = Calendar.getInstance();
		sourceCal.setTime(theDateValue);

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-12:00"));
		copyDateAndTrundateTime(sourceCal, cal);
		return cal.getTime();
	}

	public static Date getLowestInstantFromDate(Date theDateValue) {
		Calendar sourceCal = Calendar.getInstance();
		sourceCal.setTime(theDateValue);

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+14:00"));
		copyDateAndTrundateTime(sourceCal, cal);
		return cal.getTime();
	}

	private static void copyDateAndTrundateTime(Calendar theSourceCal, Calendar theCal) {
		theCal.set(Calendar.YEAR, theSourceCal.get(Calendar.YEAR));
		theCal.set(Calendar.MONTH, theSourceCal.get(Calendar.MONTH));
		theCal.set(Calendar.DAY_OF_MONTH, theSourceCal.get(Calendar.DAY_OF_MONTH));
		theCal.set(Calendar.HOUR_OF_DAY, 0);
		theCal.set(Calendar.MINUTE, 0);
		theCal.set(Calendar.SECOND, 0);
		theCal.set(Calendar.MILLISECOND, 0);
	}

	public static int convertDateToDayInteger(final Date theDateValue) {
		notNull(theDateValue, "Date value");
		SimpleDateFormat format = new SimpleDateFormat(PATTERN_INTEGER_DATE);
		String theDateString = format.format(theDateValue);
		return Integer.parseInt(theDateString);
	}

	public static String convertDateToIso8601String(final Date theDateValue) {
		notNull(theDateValue, "Date value");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		return format.format(theDateValue);
	}

	/**
	 * Formats the given date according to the RFC 1123 pattern.
	 *
	 * @param date The date to format.
	 * @return An RFC 1123 formatted date string.
	 * @see #PATTERN_RFC1123
	 */
	public static String formatDate(final Date date) {
		notNull(date, "Date");
		notNull(PATTERN_RFC1123, "Pattern");
		final SimpleDateFormat formatter = DateFormatHolder.formatFor(PATTERN_RFC1123);
		return formatter.format(date);
	}

	public static <T> T notNull(final T argument, final String name) {
		if (argument == null) {
			throw new IllegalArgumentException(Msg.code(1783) + name + " may not be null");
		}
		return argument;
	}
		
	/**
	 * Convert an incomplete date e.g. 2020 or 2020-01 to a complete date with lower
	 * bound to the first day of the year/month, and upper bound to the last day of
	 * the year/month
	 * 
	 *  e.g. 2020 to 2020-01-01 (left), 2020-12-31 (right)
	 *  2020-02 to 2020-02-01 (left), 2020-02-29 (right)
	 *
	 * @param theIncompleteDateStr 2020 or 2020-01
	 * @return a pair of complete date, left is lower bound, and right is upper bound
	 */
	public static Pair<String, String> getCompletedDate(String theIncompleteDateStr) {
		
		if (StringUtils.isBlank(theIncompleteDateStr)) 
			return new ImmutablePair<>(null, null);
			
		String lbStr, upStr;
		// YYYY only, return the last day of the year
		if (theIncompleteDateStr.length() == 4)  {
			lbStr = theIncompleteDateStr + "-01-01"; // first day of the year
			upStr = theIncompleteDateStr + "-12-31"; // last day of the year
			return new ImmutablePair<>(lbStr, upStr);
		}
		
		// Not YYYY-MM, no change
		if (theIncompleteDateStr.length() != 7)
			return new ImmutablePair<>(theIncompleteDateStr, theIncompleteDateStr);
		
		// YYYY-MM Only
		Date lb;
		try {
			// first day of the month
			lb = new SimpleDateFormat("yyyy-MM-dd").parse(theIncompleteDateStr+"-01");   
		} catch (ParseException e) {
			return new ImmutablePair<>(theIncompleteDateStr, theIncompleteDateStr);
		}
		
		// last day of the month
	    Calendar calendar = Calendar.getInstance();  
	    calendar.setTime(lb);  

	    calendar.add(Calendar.MONTH, 1);  
	    calendar.set(Calendar.DAY_OF_MONTH, 1);  
	    calendar.add(Calendar.DATE, -1);  

	    Date ub = calendar.getTime();  

	    lbStr = new SimpleDateFormat("yyyy-MM-dd").format(lb);  
	    upStr = new SimpleDateFormat("yyyy-MM-dd").format(ub);  
		
		return new ImmutablePair<>(lbStr, upStr);
	}
	
	public static Date getEndOfDay(Date theDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(theDate);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return cal.getTime();
	}
}
