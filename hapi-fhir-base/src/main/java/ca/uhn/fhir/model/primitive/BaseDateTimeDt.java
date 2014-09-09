package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import static ca.uhn.fhir.model.api.TemporalPrecisionEnum.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseDateTimeDt extends BasePrimitive<Date> {

	private static final Pattern ourYearDashMonthDashDayPattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
	private static final Pattern ourYearDashMonthPattern = Pattern.compile("[0-9]{4}-[0-9]{2}");
	private static final FastDateFormat ourYearFormat = FastDateFormat.getInstance("yyyy");
	private static final FastDateFormat ourYearMonthDayFormat = FastDateFormat.getInstance("yyyy-MM-dd");
	private static final FastDateFormat ourYearMonthDayNoDashesFormat = FastDateFormat.getInstance("yyyyMMdd");
	private static final Pattern ourYearMonthDayPattern = Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}");
	private static final FastDateFormat ourYearMonthDayTimeFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
	private static final FastDateFormat ourYearMonthDayTimeMilliFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static final FastDateFormat ourYearMonthDayTimeMilliZoneFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
	private static final FastDateFormat ourYearMonthDayTimeZoneFormat = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
	private static final FastDateFormat ourYearMonthFormat = FastDateFormat.getInstance("yyyy-MM");
	private static final FastDateFormat ourYearMonthNoDashesFormat = FastDateFormat.getInstance("yyyyMM");
	private static final Pattern ourYearMonthPattern = Pattern.compile("[0-9]{4}[0-9]{2}");
	private static final Pattern ourYearPattern = Pattern.compile("[0-9]{4}");

	private TemporalPrecisionEnum myPrecision = TemporalPrecisionEnum.SECOND;
	private TimeZone myTimeZone;
	private boolean myTimeZoneZulu = false;
	private Date myValue;

	/**
	 * Gets the precision for this datatype using field values from {@link Calendar}, such as {@link Calendar#MONTH}.
	 * Default is {@link Calendar#DAY_OF_MONTH}
	 * 
	 * @see #setPrecision(int)
	 */
	public TemporalPrecisionEnum getPrecision() {
		return myPrecision;
	}

	public TimeZone getTimeZone() {
		return myTimeZone;
	}

	@Override
	public Date getValue() {
		return myValue;
	}

	@Override
	public String getValueAsString() {
		if (myValue == null) {
			return null;
		} else {
			switch (myPrecision) {
			case DAY:
				return ourYearMonthDayFormat.format(myValue);
			case MONTH:
				return ourYearMonthFormat.format(myValue);
			case YEAR:
				return ourYearFormat.format(myValue);
			case SECOND:
				if (myTimeZoneZulu) {
					GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
					cal.setTime(myValue);
					return ourYearMonthDayTimeFormat.format(cal) + "Z";
				} else if (myTimeZone != null) {
					GregorianCalendar cal = new GregorianCalendar(myTimeZone);
					cal.setTime(myValue);
					return ourYearMonthDayTimeZoneFormat.format(cal);
				} else {
					return ourYearMonthDayTimeFormat.format(myValue);
				}
			case MILLI:
				if (myTimeZoneZulu) {
					GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
					cal.setTime(myValue);
					return ourYearMonthDayTimeMilliFormat.format(cal) + "Z";
				} else if (myTimeZone != null) {
					GregorianCalendar cal = new GregorianCalendar(myTimeZone);
					cal.setTime(myValue);
					return ourYearMonthDayTimeMilliZoneFormat.format(cal);
				} else {
					return ourYearMonthDayTimeMilliFormat.format(myValue);
				}
			}
			throw new IllegalStateException("Invalid precision (this is a HAPI bug, shouldn't happen): " + myPrecision);
		}
	}

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
		Validate.notNull(myValue, getClass().getSimpleName() + " contains null value");
		return DateUtils.isSameDay(new Date(), myValue);
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
	 * @throws DataFormatException
	 */
	public void setPrecision(TemporalPrecisionEnum thePrecision) throws DataFormatException {
		if (thePrecision == null) {
			throw new NullPointerException("Precision may not be null");
		}
		myPrecision = thePrecision;
	}

	public void setTimeZone(TimeZone theTimeZone) {
		myTimeZone = theTimeZone;
	}

	public void setTimeZoneZulu(boolean theTimeZoneZulu) {
		myTimeZoneZulu = theTimeZoneZulu;
	}

	@Override
	public void setValue(Date theValue) throws DataFormatException {
		myValue = theValue;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		try {
			if (theValue == null) {
				myValue = null;
				clearTimeZone();
			} else if (theValue.length() == 4 && ourYearPattern.matcher(theValue).matches()) {
				if (isPrecisionAllowed(YEAR)) {
					setValue((ourYearFormat).parse(theValue));
					setPrecision(YEAR);
					clearTimeZone();
				} else {
					throw new DataFormatException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support YEAR precision): " + theValue);
				}
			} else if (theValue.length() == 6 && ourYearMonthPattern.matcher(theValue).matches()) {
				// Eg. 198401 (allow this just to be lenient)
				if (isPrecisionAllowed(MONTH)) {
					setValue((ourYearMonthNoDashesFormat).parse(theValue));
					setPrecision(MONTH);
					clearTimeZone();
				} else {
					throw new DataFormatException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support DAY precision): " + theValue);
				}
			} else if (theValue.length() == 7 && ourYearDashMonthPattern.matcher(theValue).matches()) {
				// E.g. 1984-01 (this is valid according to the spec)
				if (isPrecisionAllowed(MONTH)) {
					setValue((ourYearMonthFormat).parse(theValue));
					setPrecision(MONTH);
					clearTimeZone();
				} else {
					throw new DataFormatException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support MONTH precision): " + theValue);
				}
			} else if (theValue.length() == 8 && ourYearMonthDayPattern.matcher(theValue).matches()) {
				// Eg. 19840101 (allow this just to be lenient)
				if (isPrecisionAllowed(DAY)) {
					setValue((ourYearMonthDayNoDashesFormat).parse(theValue));
					setPrecision(DAY);
					clearTimeZone();
				} else {
					throw new DataFormatException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support DAY precision): " + theValue);
				}
			} else if (theValue.length() == 10 && ourYearDashMonthDashDayPattern.matcher(theValue).matches()) {
				// E.g. 1984-01-01 (this is valid according to the spec)
				if (isPrecisionAllowed(DAY)) {
					setValue((ourYearMonthDayFormat).parse(theValue));
					setPrecision(DAY);
					clearTimeZone();
				} else {
					throw new DataFormatException("Invalid date/time string (datatype " + getClass().getSimpleName() + " does not support DAY precision): " + theValue);
				}
			} else if (theValue.length() >= 18) {
				int dotIndex = theValue.indexOf('.', 18);
				if (dotIndex == -1 && !isPrecisionAllowed(SECOND)) {
					throw new DataFormatException("Invalid date/time string (data type does not support SECONDS precision): " + theValue);
				} else if (dotIndex > -1 && !isPrecisionAllowed(MILLI)) {
					throw new DataFormatException("Invalid date/time string (data type " + getClass().getSimpleName() + " does not support MILLIS precision):" + theValue);
				}

				Calendar cal;
				try {
					cal = DatatypeConverter.parseDateTime(theValue);
				} catch (IllegalArgumentException e) {
					throw new DataFormatException("Invalid data/time string (" + e.getMessage() + "): " + theValue);
				}
				myValue = cal.getTime();
				if (dotIndex == -1) {
					setPrecision(TemporalPrecisionEnum.SECOND);
				} else {
					setPrecision(TemporalPrecisionEnum.MILLI);
				}

				clearTimeZone();
				if (theValue.endsWith("Z")) {
					myTimeZoneZulu = true;
				} else if (theValue.indexOf('+', 19) != -1 || theValue.indexOf('-', 19) != -1) {
					myTimeZone = cal.getTimeZone();
				}

			} else {
				throw new DataFormatException("Invalid date/time string (invalid length): " + theValue);
			}
		} catch (ParseException e) {
			throw new DataFormatException("Invalid date string (" + e.getMessage() + "): " + theValue);
		}
	}

	private void clearTimeZone() {
		myTimeZone = null;
		myTimeZoneZulu = false;
	}

	/**
	 * To be implemented by subclasses to indicate whether the given precision is allowed by this type
	 */
	abstract boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision);

}
