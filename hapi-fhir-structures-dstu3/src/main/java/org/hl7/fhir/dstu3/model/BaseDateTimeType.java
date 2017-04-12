package org.hl7.fhir.dstu3.model;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseDateTimeType extends PrimitiveType<Date> {

	static final long NANOS_PER_MILLIS = 1000000L;

	static final long NANOS_PER_SECOND = 1000000000L;
	private static final FastDateFormat ourHumanDateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);

	private static final FastDateFormat ourHumanDateTimeFormat = FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM);
	private static final long serialVersionUID = 1L;

	private String myFractionalSeconds;
	private TemporalPrecisionEnum myPrecision = null;
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
	 *            If the specified precision is not allowed for this type
	 */
	public BaseDateTimeType(Date theDate, TemporalPrecisionEnum thePrecision) {
		setValue(theDate, thePrecision);
		if (isPrecisionAllowed(thePrecision) == false) {
			throw new IllegalArgumentException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support " + thePrecision + " precision): " + theDate);
		}
	}

	/**
	 * Constructor
	 */
	public BaseDateTimeType(Date theDate, TemporalPrecisionEnum thePrecision, TimeZone theTimeZone) {
		this(theDate, thePrecision);
		setTimeZone(theTimeZone);
	}

	/**
	 * Constructor
	 * 
	 * @throws IllegalArgumentException
	 *            If the specified precision is not allowed for this type
	 */
	public BaseDateTimeType(String theString) {
		setValueAsString(theString);
		if (isPrecisionAllowed(getPrecision()) == false) {
			throw new IllegalArgumentException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support " + getPrecision() + " precision): " + theString);
		}
	}

	/**
	 * Adds the given amount to the field specified by theField
	 *
	 * @param theField
	 *           The field, uses constants from {@link Calendar} such as {@link Calendar#YEAR}
	 * @param theValue
	 *           The number to add (or subtract for a negative number)
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
			throw new DataFormatException("Unknown field constant: " + theField);
		}
	}

	/**
	 * Returns <code>true</code> if the given object represents a date/time before <code>this</code> object
	 * 
	 * @throws NullPointerException
	 *            If <code>this.getValue()</code> or <code>theDateTimeType.getValue()</code>
	 *            return <code>null</code>
	 */
	public boolean after(DateTimeType theDateTimeType) {
		validateBeforeOrAfter(theDateTimeType);
		return getValue().after(theDateTimeType.getValue());
	}

	/**
	 * Returns <code>true</code> if the given object represents a date/time before <code>this</code> object
	 * 
	 * @throws NullPointerException
	 *            If <code>this.getValue()</code> or <code>theDateTimeType.getValue()</code>
	 *            return <code>null</code>
	 */
	public boolean before(DateTimeType theDateTimeType) {
		validateBeforeOrAfter(theDateTimeType);
		return getValue().before(theDateTimeType.getValue());
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
			GregorianCalendar cal;
			if (myTimeZoneZulu) {
				cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			} else if (myTimeZone != null) {
				cal = new GregorianCalendar(myTimeZone);
			} else {
				cal = new GregorianCalendar();
			}
			cal.setTime(theValue);

			StringBuilder b = new StringBuilder();
			leftPadWithZeros(cal.get(Calendar.YEAR), 4, b);
			if (myPrecision.ordinal() > TemporalPrecisionEnum.YEAR.ordinal()) {
				b.append('-');
				leftPadWithZeros(cal.get(Calendar.MONTH) + 1, 2, b);
				if (myPrecision.ordinal() > TemporalPrecisionEnum.MONTH.ordinal()) {
					b.append('-');
					leftPadWithZeros(cal.get(Calendar.DATE), 2, b);
					if (myPrecision.ordinal() > TemporalPrecisionEnum.DAY.ordinal()) {
						b.append('T');
						leftPadWithZeros(cal.get(Calendar.HOUR_OF_DAY), 2, b);
						b.append(':');
						leftPadWithZeros(cal.get(Calendar.MINUTE), 2, b);
						if (myPrecision.ordinal() > TemporalPrecisionEnum.MINUTE.ordinal()) {
							b.append(':');
							leftPadWithZeros(cal.get(Calendar.SECOND), 2, b);
							if (myPrecision.ordinal() > TemporalPrecisionEnum.SECOND.ordinal()) {
								b.append('.');
								b.append(myFractionalSeconds);
								for (int i = myFractionalSeconds.length(); i < 3; i++) {
									b.append('0');
								}
							}
						}

						if (myTimeZoneZulu) {
							b.append('Z');
						} else if (myTimeZone != null) {
							int offset = myTimeZone.getOffset(theValue.getTime());
							if (offset >= 0) {
								b.append('+');
							} else {
								b.append('-');
								offset = Math.abs(offset);
							}

							int hoursOffset = (int) (offset / DateUtils.MILLIS_PER_HOUR);
							leftPadWithZeros(hoursOffset, 2, b);
							b.append(':');
							int minutesOffset = (int) (offset % DateUtils.MILLIS_PER_HOUR);
							minutesOffset = (int) (minutesOffset / DateUtils.MILLIS_PER_MINUTE);
							leftPadWithZeros(minutesOffset, 2, b);
						}
					}
				}
			}
			return b.toString();
		}
	}

	/**
	 * Returns the month with 1-index, e.g. 1=the first day of the month
	 */
	public Integer getDay() {
		return getFieldValue(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns the default precision for the given datatype
	 */
	protected abstract TemporalPrecisionEnum getDefaultPrecisionForDatatype();

	private Integer getFieldValue(int theField) {
		if (getValue() == null) {
			return null;
		}
		Calendar cal = getValueAsCalendar();
		return cal.get(theField);
	}

	/**
	 * Returns the hour of the day in a 24h clock, e.g. 13=1pm
	 */
	public Integer getHour() {
		return getFieldValue(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Returns the milliseconds within the current second.
	 * <p>
	 * Note that this method returns the
	 * same value as {@link #getNanos()} but with less precision.
	 * </p>
	 */
	public Integer getMillis() {
		return getFieldValue(Calendar.MILLISECOND);
	}

	/**
	 * Returns the minute of the hour in the range 0-59
	 */
	public Integer getMinute() {
		return getFieldValue(Calendar.MINUTE);
	}

	/**
	 * Returns the month with 0-index, e.g. 0=January
	 */
	public Integer getMonth() {
		return getFieldValue(Calendar.MONTH);
	}

	/**
	 * Returns the nanoseconds within the current second
	 * <p>
	 * Note that this method returns the
	 * same value as {@link #getMillis()} but with more precision.
	 * </p>
	 */
	public Long getNanos() {
		if (isBlank(myFractionalSeconds)) {
			return null;
		}
		String retVal = StringUtils.rightPad(myFractionalSeconds, 9, '0');
		retVal = retVal.substring(0, 9);
		return Long.parseLong(retVal);
	}

	private int getOffsetIndex(String theValueString) {
		int plusIndex = theValueString.indexOf('+', 16);
		int minusIndex = theValueString.indexOf('-', 16);
		int zIndex = theValueString.indexOf('Z', 16);
		int retVal = Math.max(Math.max(plusIndex, minusIndex), zIndex);
		if (retVal == -1) {
			return -1;
		}
		if ((retVal - 2) != (plusIndex + minusIndex + zIndex)) {
			throwBadDateFormat(theValueString);
		}
		return retVal;
	}

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
	 * Returns the second of the minute in the range 0-59
	 */
	public Integer getSecond() {
		return getFieldValue(Calendar.SECOND);
	}

	/**
	 * Returns the TimeZone associated with this dateTime's value. May return <code>null</code> if no timezone was
	 * supplied.
	 */
	public TimeZone getTimeZone() {
		if (myTimeZoneZulu) {
			return TimeZone.getTimeZone("GMT");
		}
		return myTimeZone;
	}

	/**
	 * Returns the value of this object as a {@link GregorianCalendar}
	 */
	public GregorianCalendar getValueAsCalendar() {
		if (getValue() == null) {
			return null;
		}
		GregorianCalendar cal;
		if (getTimeZone() != null) {
			cal = new GregorianCalendar(getTimeZone());
		} else {
			cal = new GregorianCalendar();
		}
		cal.setTime(getValue());
		return cal;
	}

	/**
	 * Returns the year, e.g. 2015
	 */
	public Integer getYear() {
		return getFieldValue(Calendar.YEAR);
	}

	/**
	 * To be implemented by subclasses to indicate whether the given precision is allowed by this type
	 */
	abstract boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision);

	/**
	 * Returns true if the timezone is set to GMT-0:00 (Z)
	 */
	public boolean isTimeZoneZulu() {
		return myTimeZoneZulu;
	}

	/**
	 * Returns <code>true</code> if this object represents a date that is today's date
	 * 
	 * @throws NullPointerException
	 *            if {@link #getValue()} returns <code>null</code>
	 */
	public boolean isToday() {
		Validate.notNull(getValue(), getClass().getSimpleName() + " contains null value");
		return DateUtils.isSameDay(new Date(), getValue());
	}

	private void leftPadWithZeros(int theInteger, int theLength, StringBuilder theTarget) {
		String string = Integer.toString(theInteger);
		for (int i = string.length(); i < theLength; i++) {
			theTarget.append('0');
		}
		theTarget.append(string);
	}

	@Override
	protected Date parse(String theValue) throws DataFormatException {
		Calendar cal = new GregorianCalendar(0, 0, 0);
		cal.setTimeZone(TimeZone.getDefault());
		String value = theValue;
		boolean fractionalSecondsSet = false;

		if (value.length() > 0 && (value.charAt(0) == ' ' || value.charAt(value.length() - 1) == ' ')) {
			value = value.trim();
		}

		int length = value.length();
		if (length == 0) {
			return null;
		}

		if (length < 4) {
			throwBadDateFormat(value);
		}

		TemporalPrecisionEnum precision = null;
		cal.set(Calendar.YEAR, parseInt(value, value.substring(0, 4), 0, 9999));
		precision = TemporalPrecisionEnum.YEAR;
		if (length > 4) {
			validateCharAtIndexIs(value, 4, '-');
			validateLengthIsAtLeast(value, 7);
			int monthVal = parseInt(value, value.substring(5, 7), 1, 12) - 1;
			cal.set(Calendar.MONTH, monthVal);
			precision = TemporalPrecisionEnum.MONTH;
			if (length > 7) {
				validateCharAtIndexIs(value, 7, '-');
				validateLengthIsAtLeast(value, 10);
				cal.set(Calendar.DATE, 1); // for some reason getActualMaximum works incorrectly if date isn't set
				int actualMaximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				cal.set(Calendar.DAY_OF_MONTH, parseInt(value, value.substring(8, 10), 1, actualMaximum));
				precision = TemporalPrecisionEnum.DAY;
				if (length > 10) {
					validateLengthIsAtLeast(value, 17);
					validateCharAtIndexIs(value, 10, 'T'); // yyyy-mm-ddThh:mm:ss
					int offsetIdx = getOffsetIndex(value);
					String time;
					if (offsetIdx == -1) {
						// throwBadDateFormat(theValue);
						// No offset - should this be an error?
						time = value.substring(11);
					} else {
						time = value.substring(11, offsetIdx);
						String offsetString = value.substring(offsetIdx);
						setTimeZone(value, offsetString);
						cal.setTimeZone(getTimeZone());
					}
					int timeLength = time.length();

					validateCharAtIndexIs(value, 13, ':');
					cal.set(Calendar.HOUR_OF_DAY, parseInt(value, value.substring(11, 13), 0, 23));
					cal.set(Calendar.MINUTE, parseInt(value, value.substring(14, 16), 0, 59));
					precision = TemporalPrecisionEnum.MINUTE;
					if (timeLength > 5) {
						validateLengthIsAtLeast(value, 19);
						validateCharAtIndexIs(value, 16, ':'); // yyyy-mm-ddThh:mm:ss
						cal.set(Calendar.SECOND, parseInt(value, value.substring(17, 19), 0, 59));
						precision = TemporalPrecisionEnum.SECOND;
						if (timeLength > 8) {
							validateCharAtIndexIs(value, 19, '.'); // yyyy-mm-ddThh:mm:ss.SSSS
							validateLengthIsAtLeast(value, 20);
							int endIndex = getOffsetIndex(value);
							if (endIndex == -1) {
								endIndex = value.length();
							}
							int millis;
							String millisString;
							if (endIndex > 23) {
								myFractionalSeconds = value.substring(20, endIndex);
								fractionalSecondsSet = true;
								endIndex = 23;
								millisString = value.substring(20, endIndex);
								millis = parseInt(value, millisString, 0, 999);
							} else {
								millisString = value.substring(20, endIndex);
								millis = parseInt(value, millisString, 0, 999);
								myFractionalSeconds = millisString;
								fractionalSecondsSet = true;
							}
							if (millisString.length() == 1) {
								millis = millis * 100;
							} else if (millisString.length() == 2) {
								millis = millis * 10;
							}
							cal.set(Calendar.MILLISECOND, millis);
							precision = TemporalPrecisionEnum.MILLI;
						}
					}
				}
			} else {
				cal.set(Calendar.DATE, 1);
			}
		} else {
			cal.set(Calendar.DATE, 1);
		}

		if (fractionalSecondsSet == false) {
			myFractionalSeconds = "";
		}

		myPrecision = precision;
		return cal.getTime();

	}

	private int parseInt(String theValue, String theSubstring, int theLowerBound, int theUpperBound) {
		int retVal = 0;
		try {
			retVal = Integer.parseInt(theSubstring);
		} catch (NumberFormatException e) {
			throwBadDateFormat(theValue);
		}

		if (retVal < theLowerBound || retVal > theUpperBound) {
			throwBadDateFormat(theValue);
		}

		return retVal;
	}

	/**
	 * Sets the month with 1-index, e.g. 1=the first day of the month
	 */
	public BaseDateTimeType setDay(int theDay) {
		setFieldValue(Calendar.DAY_OF_MONTH, theDay, null, 0, 31);
		return this;
	}

	private void setFieldValue(int theField, int theValue, String theFractionalSeconds, int theMinimum, int theMaximum) {
		validateValueInRange(theValue, theMinimum, theMaximum);
		Calendar cal;
		if (getValue() == null) {
			cal = new GregorianCalendar(0, 0, 0);
		} else {
			cal = getValueAsCalendar();
		}
		if (theField != -1) {
			cal.set(theField, theValue);
		}
		if (theFractionalSeconds != null) {
			myFractionalSeconds = theFractionalSeconds;
		} else if (theField == Calendar.MILLISECOND) {
			myFractionalSeconds = StringUtils.leftPad(Integer.toString(theValue), 3, '0');
		}
		super.setValue(cal.getTime());
	}

	/**
	 * Sets the hour of the day in a 24h clock, e.g. 13=1pm
	 */
	public BaseDateTimeType setHour(int theHour) {
		setFieldValue(Calendar.HOUR_OF_DAY, theHour, null, 0, 23);
		return this;
	}

	/**
	 * Sets the milliseconds within the current second.
	 * <p>
	 * Note that this method sets the
	 * same value as {@link #setNanos(long)} but with less precision.
	 * </p>
	 */
	public BaseDateTimeType setMillis(int theMillis) {
		setFieldValue(Calendar.MILLISECOND, theMillis, null, 0, 999);
		return this;
	}

	/**
	 * Sets the minute of the hour in the range 0-59
	 */
	public BaseDateTimeType setMinute(int theMinute) {
		setFieldValue(Calendar.MINUTE, theMinute, null, 0, 59);
		return this;
	}

	/**
	 * Sets the month with 0-index, e.g. 0=January
	 */
	public BaseDateTimeType setMonth(int theMonth) {
		setFieldValue(Calendar.MONTH, theMonth, null, 0, 11);
		return this;
	}

	/**
	 * Sets the nanoseconds within the current second
	 * <p>
	 * Note that this method sets the
	 * same value as {@link #setMillis(int)} but with more precision.
	 * </p>
	 */
	public BaseDateTimeType setNanos(long theNanos) {
		validateValueInRange(theNanos, 0, NANOS_PER_SECOND - 1);
		String fractionalSeconds = StringUtils.leftPad(Long.toString(theNanos), 9, '0');

		// Strip trailing 0s
		for (int i = fractionalSeconds.length(); i > 0; i--) {
			if (fractionalSeconds.charAt(i - 1) != '0') {
				fractionalSeconds = fractionalSeconds.substring(0, i);
				break;
			}
		}
		int millis = (int) (theNanos / NANOS_PER_MILLIS);
		setFieldValue(Calendar.MILLISECOND, millis, fractionalSeconds, 0, 999);
		return this;
	}

	/**
	 * Sets the precision for this datatype
	 * 
	 * @throws DataFormatException
	 */
	public void setPrecision(TemporalPrecisionEnum thePrecision) throws DataFormatException {
		if (thePrecision == null) {
			throw new NullPointerException("Precision may not be null");
		}
		myPrecision = thePrecision;
		updateStringValue();
	}

	/**
	 * Sets the second of the minute in the range 0-59
	 */
	public BaseDateTimeType setSecond(int theSecond) {
		setFieldValue(Calendar.SECOND, theSecond, null, 0, 59);
		return this;
	}

	private BaseDateTimeType setTimeZone(String theWholeValue, String theValue) {

		if (isBlank(theValue)) {
			throwBadDateFormat(theWholeValue);
		} else if (theValue.charAt(0) == 'Z') {
			myTimeZone = null;
			myTimeZoneZulu = true;
		} else if (theValue.length() != 6) {
			throwBadDateFormat(theWholeValue, "Timezone offset must be in the form \"Z\", \"-HH:mm\", or \"+HH:mm\"");
		} else if (theValue.charAt(3) != ':' || !(theValue.charAt(0) == '+' || theValue.charAt(0) == '-')) {
			throwBadDateFormat(theWholeValue, "Timezone offset must be in the form \"Z\", \"-HH:mm\", or \"+HH:mm\"");
		} else {
			parseInt(theWholeValue, theValue.substring(1, 3), 0, 23);
			parseInt(theWholeValue, theValue.substring(4, 6), 0, 59);
			myTimeZoneZulu = false;
			myTimeZone = TimeZone.getTimeZone("GMT" + theValue);
		}

		return this;
	}

	public BaseDateTimeType setTimeZone(TimeZone theTimeZone) {
		myTimeZone = theTimeZone;
		myTimeZoneZulu = false;
		updateStringValue();
		return this;
	}

	public BaseDateTimeType setTimeZoneZulu(boolean theTimeZoneZulu) {
		myTimeZoneZulu = theTimeZoneZulu;
		myTimeZone = null;
		updateStringValue();
		return this;
	}

	/**
	 * Sets the value for this type using the given Java Date object as the time, and using the default precision for
	 * this datatype (unless the precision is already set), as well as the local timezone as determined by the local operating
	 * system. Both of these properties may be modified in subsequent calls if neccesary.
	 */
	@Override
	public BaseDateTimeType setValue(Date theValue) {
		setValue(theValue, getPrecision());
		return this;
	}

	/**
	 * Sets the value for this type using the given Java Date object as the time, and using the specified precision, as
	 * well as the local timezone as determined by the local operating system. Both of
	 * these properties may be modified in subsequent calls if neccesary.
	 * 
	 * @param theValue
	 *           The date value
	 * @param thePrecision
	 *           The precision
	 * @throws DataFormatException
	 */
	public void setValue(Date theValue, TemporalPrecisionEnum thePrecision) throws DataFormatException {
		if (getTimeZone() == null) {
			setTimeZone(TimeZone.getDefault());
		}
		myPrecision = thePrecision;
		myFractionalSeconds = "";
		if (theValue != null) {
			long millis = theValue.getTime() % 1000;
			if (millis < 0) {
				// This is for times before 1970 (see bug #444)
				millis = 1000 + millis;
			}
			String fractionalSeconds = Integer.toString((int) millis);
			myFractionalSeconds = StringUtils.leftPad(fractionalSeconds, 3, '0');
		}
		super.setValue(theValue);
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		clearTimeZone();
		super.setValueAsString(theValue);
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
				if (timeZone.length() == 5) {
					b.append(timeZone.substring(0, 3));
					b.append(':');
					b.append(timeZone.substring(3));
				} else {
					b.append(timeZone);
				}
			}

			setValueAsString(b.toString());
		}
	}

	/**
	 * Sets the year, e.g. 2015
	 */
	public BaseDateTimeType setYear(int theYear) {
		setFieldValue(Calendar.YEAR, theYear, null, 0, 9999);
		return this;
	}

	private void throwBadDateFormat(String theValue) {
		throw new DataFormatException("Invalid date/time format: \"" + theValue + "\"");
	}

	private void throwBadDateFormat(String theValue, String theMesssage) {
		throw new DataFormatException("Invalid date/time format: \"" + theValue + "\": " + theMesssage);
	}

	/**
	 * Returns a view of this date/time as a Calendar object. Note that the returned
	 * Calendar object is entirely independent from <code>this</code> object. Changes to the
	 * calendar will not affect <code>this</code>.
	 */
	public Calendar toCalendar() {
		Calendar retVal = Calendar.getInstance();
		retVal.setTime(getValue());
		retVal.setTimeZone(getTimeZone());
		return retVal;
	}

	/**
	 * Returns a human readable version of this date/time using the system local format.
	 * <p>
	 * <b>Note on time zones:</b> This method renders the value using the time zone that is contained within the value.
	 * For example, if this date object contains the value "2012-01-05T12:00:00-08:00",
	 * the human display will be rendered as "12:00:00" even if the application is being executed on a system in a
	 * different time zone. If this behaviour is not what you want, use
	 * {@link #toHumanDisplayLocalTimezone()} instead.
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
	 * Returns a human readable version of this date/time using the system local format, converted to the local timezone
	 * if neccesary.
	 * 
	 * @see #toHumanDisplay() for a method which does not convert the time to the local timezone before rendering it.
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

	private void validateBeforeOrAfter(DateTimeType theDateTimeType) {
		if (getValue() == null) {
			throw new NullPointerException("This BaseDateTimeType does not contain a value (getValue() returns null)");
		}
		if (theDateTimeType == null) {
			throw new NullPointerException("theDateTimeType must not be null");
		}
		if (theDateTimeType.getValue() == null) {
			throw new NullPointerException("The given BaseDateTimeType does not contain a value (theDateTimeType.getValue() returns null)");
		}
	}

	private void validateCharAtIndexIs(String theValue, int theIndex, char theChar) {
		if (theValue.charAt(theIndex) != theChar) {
			throwBadDateFormat(theValue, "Expected character '" + theChar + "' at index " + theIndex + " but found " + theValue.charAt(theIndex));
		}
	}

	private void validateLengthIsAtLeast(String theValue, int theLength) {
		if (theValue.length() < theLength) {
			throwBadDateFormat(theValue);
		}
	}

	private void validateValueInRange(long theValue, long theMinimum, long theMaximum) {
		if (theValue < theMinimum || theValue > theMaximum) {
			throw new IllegalArgumentException("Value " + theValue + " is not between allowable range: " + theMinimum + " - " + theMaximum);
		}
	}

}
