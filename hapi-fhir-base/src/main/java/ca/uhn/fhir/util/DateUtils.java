package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

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
			throw new IllegalArgumentException(name + " may not be null");
		}
		return argument;
	}

}
