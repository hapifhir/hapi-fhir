package org.hl7.fhir.instance.model;

/*
Copyright (c) 2011+, HL7, Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

*/

import static org.hl7.fhir.instance.model.TemporalPrecisionEnum.DAY;
import static org.hl7.fhir.instance.model.TemporalPrecisionEnum.MONTH;
import static org.hl7.fhir.instance.model.TemporalPrecisionEnum.YEAR;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public abstract class BaseDateTimeType extends PrimitiveType<Date> {

	private static final long serialVersionUID = 1L;

	/*
	 * Add any new formatters to the static block below!!
	 */
	private static final List<FastDateFormat> ourFormatters;

	private static final Pattern ourYearDashMonthDashDayPattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
	private static final Pattern ourYearDashMonthPattern = Pattern.compile("[0-9]{4}-[0-9]{2}");
	private static final FastDateFormat ourYearFormat = FastDateFormat.getInstance("yyyy");
	private static final FastDateFormat ourYearMonthDayFormat = FastDateFormat.getInstance("yyyy-MM-dd");
	private static final FastDateFormat ourYearMonthDayNoDashesFormat = FastDateFormat.getInstance("yyyyMMdd");
	private static final Pattern ourYearMonthDayPattern = Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}");
	private static final FastDateFormat ourYearMonthDayTimeFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
	private static final FastDateFormat ourYearMonthDayTimeMilliFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static final FastDateFormat ourYearMonthDayTimeMilliUTCZFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", TimeZone.getTimeZone("UTC"));
	private static final FastDateFormat ourYearMonthDayTimeMilliZoneFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
	private static final FastDateFormat ourYearMonthDayTimeUTCZFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'", TimeZone.getTimeZone("UTC"));
	private static final FastDateFormat ourYearMonthDayTimeZoneFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
	private static final FastDateFormat ourYearMonthFormat = FastDateFormat.getInstance("yyyy-MM");
	private static final FastDateFormat ourYearMonthNoDashesFormat = FastDateFormat.getInstance("yyyyMM");
	private static final Pattern ourYearMonthPattern = Pattern.compile("[0-9]{4}[0-9]{2}");
	private static final Pattern ourYearPattern = Pattern.compile("[0-9]{4}");
	private static final FastDateFormat ourYearMonthDayTimeMinsFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm");
	private static final FastDateFormat ourYearMonthDayTimeMinsUTCZFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm'Z'", TimeZone.getTimeZone("UTC"));
	private static final FastDateFormat ourYearMonthDayTimeMinsZoneFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mmZZ");

	private static final FastDateFormat ourHumanDateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
	private static final FastDateFormat ourHumanDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);

	static {
		ArrayList<FastDateFormat> formatters = new ArrayList<FastDateFormat>();
		formatters.add(ourYearFormat);
		formatters.add(ourYearMonthDayFormat);
		formatters.add(ourYearMonthDayNoDashesFormat);
		formatters.add(ourYearMonthDayTimeFormat);
		formatters.add(ourYearMonthDayTimeUTCZFormat);
		formatters.add(ourYearMonthDayTimeZoneFormat);
		formatters.add(ourYearMonthDayTimeMilliFormat);
		formatters.add(ourYearMonthDayTimeMilliUTCZFormat);
		formatters.add(ourYearMonthDayTimeMilliZoneFormat);
		formatters.add(ourYearMonthDayTimeMinsFormat);
		formatters.add(ourYearMonthDayTimeMinsUTCZFormat);
		formatters.add(ourYearMonthDayTimeMinsZoneFormat);
		formatters.add(ourYearMonthFormat);
		formatters.add(ourYearMonthNoDashesFormat);
		ourFormatters = Collections.unmodifiableList(formatters);
	}

	private TemporalPrecisionEnum myPrecision = TemporalPrecisionEnum.SECOND;

	private TimeZone myTimeZone;
	private boolean myTimeZoneZulu = false;

	/**
	 * Constructor
	 */
	public BaseDateTimeType() {
		// nothing
	}

	/**
	 * Constructor
	 * 
	 * @throws IllegalArgumentException
	 *             If the specified precision is not allowed for this type
	 */
	public BaseDateTimeType(Date theDate, TemporalPrecisionEnum thePrecision) {
		setValue(theDate, thePrecision);
		if (isPrecisionAllowed(thePrecision) == false) {
			throw new IllegalArgumentException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support " + thePrecision + " precision): " + theDate);
		}
	}

	/**
	 * Constructor
	 * 
	 * @throws IllegalArgumentException
	 *             If the specified precision is not allowed for this type
	 */
	public BaseDateTimeType(String theString) {
		setValueAsString(theString);
		if (isPrecisionAllowed(getPrecision()) == false) {
			throw new IllegalArgumentException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support " + getPrecision() + " precision): " + theString);
		}
	}

	/**
	 * Constructor
	 */
	public BaseDateTimeType(Date theDate, TemporalPrecisionEnum thePrecision, TimeZone theTimeZone) {
		this(theDate, thePrecision);
		setTimeZone(theTimeZone);
	}

	private void clearTimeZone() {
		myTimeZone = null;
		myTimeZoneZulu = false;
	}

	@Override
	protected String encode(Date theValue) {
		if (theValue == null) {
			return null;
		} else {
			switch (myPrecision) {
			case DAY:
				return ourYearMonthDayFormat.format(theValue);
			case MONTH:
				return ourYearMonthFormat.format(theValue);
			case YEAR:
				return ourYearFormat.format(theValue);
			case MINUTE:
				if (myTimeZoneZulu) {
					GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
					cal.setTime(theValue);
					return ourYearMonthDayTimeMinsFormat.format(cal) + "Z";
				} else if (myTimeZone != null) {
					GregorianCalendar cal = new GregorianCalendar(myTimeZone);
					cal.setTime(theValue);
					return (ourYearMonthDayTimeMinsZoneFormat.format(cal));
				} else {
					return ourYearMonthDayTimeMinsFormat.format(theValue);
				}
			case SECOND:
				if (myTimeZoneZulu) {
					GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
					cal.setTime(theValue);
					return ourYearMonthDayTimeFormat.format(cal) + "Z";
				} else if (myTimeZone != null) {
					GregorianCalendar cal = new GregorianCalendar(myTimeZone);
					cal.setTime(theValue);
					return (ourYearMonthDayTimeZoneFormat.format(cal));
				} else {
					return ourYearMonthDayTimeFormat.format(theValue);
				}
			case MILLI:
				if (myTimeZoneZulu) {
					GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
					cal.setTime(theValue);
					return ourYearMonthDayTimeMilliFormat.format(cal) + "Z";
				} else if (myTimeZone != null) {
					GregorianCalendar cal = new GregorianCalendar(myTimeZone);
					cal.setTime(theValue);
					return (ourYearMonthDayTimeMilliZoneFormat.format(cal));
				} else {
					return ourYearMonthDayTimeMilliFormat.format(theValue);
				}
			}
			throw new IllegalStateException("Invalid precision (this is a bug, shouldn't happen): " + myPrecision);
		}
	}

	/**
	 * Returns the default precision for the given datatype
	 */
	protected abstract TemporalPrecisionEnum getDefaultPrecisionForDatatype();

	/**
	 * Gets the precision for this datatype (using the default for the given type if not set)
	 * 
	 * @see #setPrecision(TemporalPrecisionEnum)
	 */
	public TemporalPrecisionEnum getPrecision() {
		if (myPrecision == null) {
			return getDefaultPrecisionForDatatype();
		}
		return myPrecision;
	}

	/**
	 * Returns the TimeZone associated with this dateTime's value. May return <code>null</code> if no timezone was
	 * supplied.
	 */
	public TimeZone getTimeZone() {
		return myTimeZone;
	}

	private boolean hasOffset(String theValue) {
		boolean inTime = false;
		for (int i = 0; i < theValue.length(); i++) {
			switch (theValue.charAt(i)) {
			case 'T':
				inTime = true;
				break;
			case '+':
			case '-':
				if (inTime) {
					return true;
				}
				break;
			}
		}
		return false;
	}

	/**
	 * To be implemented by subclasses to indicate whether the given precision is allowed by this type
	 */
	abstract boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision);

	public boolean isTimeZoneZulu() {
		return myTimeZoneZulu;
	}

	/**
	 * Returns <code>true</code> if this object represents a date that is today's date
	 * 
	 * @throws NullPointerException
	 *             if {@link #getValue()} returns <code>null</code>
	 */
	public boolean isToday() {
		Validate.notNull(getValue(), getClass().getSimpleName() + " contains null value");
		return DateUtils.isSameDay(new Date(), getValue());
	}

	@Override
	protected Date parse(String theValue) throws IllegalArgumentException {
		try {
			if (theValue.length() == 4 && ourYearPattern.matcher(theValue).matches()) {
				if (!isPrecisionAllowed(YEAR)) {
					// ourLog.debug("Invalid date/time string (datatype " + getClass().getSimpleName() +
					// " does not support YEAR precision): " + theValue);
				}
				setPrecision(YEAR);
				clearTimeZone();
				return ((ourYearFormat).parse(theValue));
			} else if (theValue.length() == 6 && ourYearMonthPattern.matcher(theValue).matches()) {
				// Eg. 198401 (allow this just to be lenient)
				if (!isPrecisionAllowed(MONTH)) {
					// ourLog.debug("Invalid date/time string (datatype " + getClass().getSimpleName() +
					// " does not support DAY precision): " + theValue);
				}
				setPrecision(MONTH);
				clearTimeZone();
				return ((ourYearMonthNoDashesFormat).parse(theValue));
			} else if (theValue.length() == 7 && ourYearDashMonthPattern.matcher(theValue).matches()) {
				// E.g. 1984-01 (this is valid according to the spec)
				if (!isPrecisionAllowed(MONTH)) {
					// ourLog.debug("Invalid date/time string (datatype " + getClass().getSimpleName() +
					// " does not support MONTH precision): " + theValue);
				}
				setPrecision(MONTH);
				clearTimeZone();
				return ((ourYearMonthFormat).parse(theValue));
			} else if (theValue.length() == 8 && ourYearMonthDayPattern.matcher(theValue).matches()) {
				// Eg. 19840101 (allow this just to be lenient)
				if (!isPrecisionAllowed(DAY)) {
					// ourLog.debug("Invalid date/time string (datatype " + getClass().getSimpleName() +
					// " does not support DAY precision): " + theValue);
				}
				setPrecision(DAY);
				clearTimeZone();
				return ((ourYearMonthDayNoDashesFormat).parse(theValue));
			} else if (theValue.length() == 10 && ourYearDashMonthDashDayPattern.matcher(theValue).matches()) {
				// E.g. 1984-01-01 (this is valid according to the spec)
				if (!isPrecisionAllowed(DAY)) {
					// ourLog.debug("Invalid date/time string (datatype " + getClass().getSimpleName() +
					// " does not support DAY precision): " + theValue);
				}
				setPrecision(DAY);
				clearTimeZone();
				return ((ourYearMonthDayFormat).parse(theValue));
			} else if (theValue.length() >= 16) { // date and time with possible time zone
				int firstColonIndex = theValue.indexOf(':');
				if (firstColonIndex == -1) {
					throw new IllegalArgumentException("Invalid date/time string: " + theValue);
				}
				
				boolean hasSeconds = theValue.length() > firstColonIndex+3 ? theValue.charAt(firstColonIndex+3) == ':' : false; 
				
				int dotIndex = theValue.length() >= 18 ? theValue.indexOf('.', 18): -1;
				boolean hasMillis = dotIndex > -1;

//				if (!hasMillis && !isPrecisionAllowed(SECOND)) {
					// ourLog.debug("Invalid date/time string (data type does not support SECONDS precision): " +
					// theValue);
//				} else if (hasMillis && !isPrecisionAllowed(MILLI)) {
					// ourLog.debug("Invalid date/time string (data type " + getClass().getSimpleName() +
					// " does not support MILLIS precision):" + theValue);
//				}

				Date retVal;
				if (hasMillis) {
					try {
						if (hasOffset(theValue)) {
							retVal = ourYearMonthDayTimeMilliZoneFormat.parse(theValue);
						} else if (theValue.endsWith("Z")) {
							retVal = ourYearMonthDayTimeMilliUTCZFormat.parse(theValue);
						} else {
							retVal = ourYearMonthDayTimeMilliFormat.parse(theValue);
						}
					} catch (ParseException p2) {
						throw new IllegalArgumentException("Invalid data/time string (" + p2.getMessage() + "): " + theValue);
					}
					setTimeZone(theValue, hasMillis);
					setPrecision(TemporalPrecisionEnum.MILLI);
				} else if (hasSeconds) {
					try {
						if (hasOffset(theValue)) {
							retVal = ourYearMonthDayTimeZoneFormat.parse(theValue);
						} else if (theValue.endsWith("Z")) {
							retVal = ourYearMonthDayTimeUTCZFormat.parse(theValue);
						} else {
							retVal = ourYearMonthDayTimeFormat.parse(theValue);
						}
					} catch (ParseException p2) {
						throw new IllegalArgumentException("Invalid data/time string (" + p2.getMessage() + "): " + theValue);
					}

					setTimeZone(theValue, hasMillis);
					setPrecision(TemporalPrecisionEnum.SECOND);
				} else {
					try {
						if (hasOffset(theValue)) {
							retVal = ourYearMonthDayTimeMinsZoneFormat.parse(theValue);
						} else if (theValue.endsWith("Z")) {
							retVal = ourYearMonthDayTimeMinsUTCZFormat.parse(theValue);
						} else {
							retVal = ourYearMonthDayTimeMinsFormat.parse(theValue);
						}
					} catch (ParseException p2) {
						throw new IllegalArgumentException("Invalid data/time string (" + p2.getMessage() + "): " + theValue, p2);
					}

					setTimeZone(theValue, hasMillis);
					setPrecision(TemporalPrecisionEnum.MINUTE);
				}

				return retVal;
			} else {
				throw new IllegalArgumentException("Invalid date/time string (invalid length): " + theValue);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date string (" + e.getMessage() + "): " + theValue);
		}
	}

	/**
	 * Sets the precision for this datatype using field values from {@link Calendar}. Valid values are:
	 * <ul>
	 * <li>{@link Calendar#SECOND}
	 * <li>{@link Calendar#DAY_OF_MONTH}
	 * <li>{@link Calendar#MONTH}
	 * <li>{@link Calendar#YEAR}
	 * </ul>
	 * 
	 * @throws IllegalArgumentException
	 */
	public void setPrecision(TemporalPrecisionEnum thePrecision) throws IllegalArgumentException {
		if (thePrecision == null) {
			throw new NullPointerException("Precision may not be null");
		}
		myPrecision = thePrecision;
		updateStringValue();
	}

	private void setTimeZone(String theValueString, boolean hasMillis) {
		clearTimeZone();
		int timeZoneStart = 19;
		if (hasMillis)
			timeZoneStart += 4;
		if (theValueString.endsWith("Z")) {
			setTimeZoneZulu(true);
		} else if (theValueString.indexOf("GMT", timeZoneStart) != -1) {
			setTimeZone(TimeZone.getTimeZone(theValueString.substring(timeZoneStart)));
		} else if (theValueString.indexOf('+', timeZoneStart) != -1 || theValueString.indexOf('-', timeZoneStart) != -1) {
			setTimeZone(TimeZone.getTimeZone("GMT" + theValueString.substring(timeZoneStart)));
		}
	}

	public void setTimeZone(TimeZone theTimeZone) {
		myTimeZone = theTimeZone;
		updateStringValue();
	}

	public void setTimeZoneZulu(boolean theTimeZoneZulu) {
		myTimeZoneZulu = theTimeZoneZulu;
		updateStringValue();
	}

	/**
	 * Sets the value of this date/time using the default level of precision
	 * for this datatype
	 * using the system local time zone
	 * 
	 * @param theValue
	 *            The date value
	 */
	@Override
	public BaseDateTimeType setValue(Date theValue) {
		if (myTimeZoneZulu == false && myTimeZone == null) {
			myTimeZone = TimeZone.getDefault();
		}
		myPrecision = getDefaultPrecisionForDatatype();
		BaseDateTimeType retVal = (BaseDateTimeType) super.setValue(theValue);
		return retVal;
	}

	/**
	 * Sets the value of this date/time using the specified level of precision
	 * using the system local time zone
	 * 
	 * @param theValue
	 *            The date value
	 * @param thePrecision
	 *            The precision
	 * @throws IllegalArgumentException
	 */
	public void setValue(Date theValue, TemporalPrecisionEnum thePrecision) throws IllegalArgumentException {
		if (myTimeZoneZulu == false && myTimeZone == null) {
			myTimeZone = TimeZone.getDefault();
		}
		myPrecision = thePrecision;
		super.setValue(theValue);
	}

	@Override
	public void setValueAsString(String theValue) throws IllegalArgumentException {
		clearTimeZone();
		super.setValueAsString(theValue);
	}

	/**
	 * For unit tests only
	 */
	static List<FastDateFormat> getFormatters() {
		return ourFormatters;
	}

	public boolean before(DateTimeType theDateTimeType) {
		return getValue().before(theDateTimeType.getValue());
	}

	public boolean after(DateTimeType theDateTimeType) {
		return getValue().after(theDateTimeType.getValue());
	}

    /**
     * Returns a human readable version of this date/time using the system local format.
     * <p>
     * <b>Note on time zones:</b> This method renders the value using the time zone
     * that is contained within the value. For example, if this date object contains the
     * value "2012-01-05T12:00:00-08:00", the human display will be rendered as "12:00:00"
     * even if the application is being executed on a system in a different time zone. If
     * this behaviour is not what you want, use {@link #toHumanDisplayLocalTimezone()}
     * instead.
     * </p>
     */
	public String toHumanDisplay() {
		TimeZone tz = getTimeZone();
		Calendar value = tz != null ? Calendar.getInstance(tz) : Calendar.getInstance();
		value.setTime(getValue());

		switch (getPrecision()) {
		case YEAR:
		case MONTH:
		case DAY:
			return ourHumanDateFormat.format(value);
		case MILLI:
		case SECOND:
		default:
			return ourHumanDateTimeFormat.format(value);
		}
	}

    /**
     * Returns a human readable version of this date/time using the system local format,
     * converted to the local timezone if neccesary.
     * 
     * @see #toHumanDisplay() for a method which does not convert the time to the local
     * timezone before rendering it.
     */
    public String toHumanDisplayLocalTimezone() {
		switch (getPrecision()) {
        case YEAR:
        case MONTH:
        case DAY:
                return ourHumanDateFormat.format(getValue());
        case MILLI:
        case SECOND:
        default:
                return ourHumanDateTimeFormat.format(getValue());
        }
    }


	/**
	 * Returns a view of this date/time as a Calendar object
	 */
	public Calendar toCalendar() {
		Calendar retVal = Calendar.getInstance();
		retVal.setTime(getValue());
		retVal.setTimeZone(getTimeZone());
		return retVal;
	}

	/**
	 * Sets the TimeZone offset in minutes relative to GMT
	 */
	public void setOffsetMinutes(int theZoneOffsetMinutes) {
		int offsetAbs = Math.abs(theZoneOffsetMinutes);

		int mins = offsetAbs % 60;
		int hours = offsetAbs / 60;

		if (theZoneOffsetMinutes < 0) {
			setTimeZone(TimeZone.getTimeZone("GMT-" + hours + ":" + mins));
		} else {
			setTimeZone(TimeZone.getTimeZone("GMT+" + hours + ":" + mins));
		}
	}

	/**
	 * Returns the time in millis as represented by this Date/Time
	 */
	public long getTime() {
		return getValue().getTime();
	}

	/**
	 * Adds the given amount to the field specified by theField
	 * 
	 * @param theField
	 *            The field, uses constants from {@link Calendar} such as {@link Calendar#YEAR}
	 * @param theValue
	 *            The number to add (or subtract for a negative number)
	 */
	public void add(int theField, int theValue) {
		switch (theField) {
		case Calendar.YEAR:
			setValue(DateUtils.addYears(getValue(), theValue), getPrecision());
			break;
		case Calendar.MONTH:
			setValue(DateUtils.addMonths(getValue(), theValue), getPrecision());
			break;
		case Calendar.DATE:
			setValue(DateUtils.addDays(getValue(), theValue), getPrecision());
			break;
		case Calendar.HOUR:
			setValue(DateUtils.addHours(getValue(), theValue), getPrecision());
			break;
		case Calendar.MINUTE:
			setValue(DateUtils.addMinutes(getValue(), theValue), getPrecision());
			break;
		case Calendar.SECOND:
			setValue(DateUtils.addSeconds(getValue(), theValue), getPrecision());
			break;
		case Calendar.MILLISECOND:
			setValue(DateUtils.addMilliseconds(getValue(), theValue), getPrecision());
			break;
		default:
			throw new IllegalArgumentException("Unknown field constant: " + theField);
		}
	}

	protected void setValueAsV3String(String theV3String) {
		if (StringUtils.isBlank(theV3String)) {
			setValue(null);
		} else {
			StringBuilder b = new StringBuilder();
			String timeZone = null;
			for (int i = 0; i < theV3String.length(); i++) {
				char nextChar = theV3String.charAt(i);
				if (nextChar == '+' || nextChar == '-' || nextChar == 'Z') {
					timeZone = (theV3String.substring(i));
					break;
				}
				
				// assertEquals("2013-02-02T20:13:03-05:00", DateAndTime.parseV3("20130202201303-0500").toString());
				if (i == 4 || i == 6) {
					b.append('-');
				} else if (i == 8) {
					b.append('T');
				} else if (i == 10 || i == 12) {
					b.append(':');
				}
				
				b.append(nextChar);
			}

			if (b.length() == 16)
				b.append(":00"); // schema rule, must have seconds
			if (timeZone != null && b.length() > 10) {
				if (timeZone.length() ==5) {
					b.append(timeZone.substring(0, 3));
					b.append(':');
					b.append(timeZone.substring(3));
				}else {
					b.append(timeZone);
				}
			}
			
			setValueAsString(b.toString());
		}
	}

}
