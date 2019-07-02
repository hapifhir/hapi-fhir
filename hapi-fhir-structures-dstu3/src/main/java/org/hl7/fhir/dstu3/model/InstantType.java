/*
Copyright (c) 2011+, HL7, Inc
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
/**
 * 
 */
package org.hl7.fhir.dstu3.model;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.DataFormatException;

/**
 * Represents a FHIR instant datatype. Valid precisions values for this type are:
 * <ul>
 * <li>{@link TemporalPrecisionEnum#SECOND}
 * <li>{@link TemporalPrecisionEnum#MILLI}
 * </ul>
 */
@DatatypeDef(name="instant")
public class InstantType extends BaseDateTimeType {

	/**
	 * The default precision for this type
	 */
	public static final TemporalPrecisionEnum DEFAULT_PRECISION = TemporalPrecisionEnum.MILLI;
	private static final long serialVersionUID = 3L;

	/**
	 * Constructor which creates an InstantDt with <b>no timne value</b>. Note
	 * that unlike the default constructor for the Java {@link Date} or
	 * {@link Calendar} objects, this constructor does not initialize the object
	 * with the current time.
	 * 
	 * @see #withCurrentTime() to create a new object that has been initialized
	 *      with the current time.
	 */
	public InstantType() {
		super();
	}

	/**
	 * Create a new DateTimeDt
	 */
	public InstantType(Calendar theCalendar) {
		super(theCalendar.getTime(), DEFAULT_PRECISION, theCalendar.getTimeZone());
	}

	/**
	 * Create a new instance using the given date, precision level, and time zone
	 * 
	 * @throws DataFormatException
	 *             If the specified precision is not allowed for this type
	 */
	public InstantType(Date theDate, TemporalPrecisionEnum thePrecision, TimeZone theTimezone) {
		super(theDate, thePrecision, theTimezone);
	}


	/**
	 * Create a new DateTimeDt using an existing value. <b>Use this constructor with caution</b>,
	 * as it may create more precision than warranted (since for example it is possible to pass in
	 * a DateTime with only a year, and this constructor will convert to an InstantDt with 
	 * milliseconds precision).
	 */
	public InstantType(BaseDateTimeType theDateTime) {
		// Do not call super(foo) here, we don't want to trigger a DataFormatException
		setValue(theDateTime.getValue());
		setPrecision(DEFAULT_PRECISION);
		setTimeZone(theDateTime.getTimeZone());
	}

	/**
	 * Create a new DateTimeDt with the given date/time and {@link TemporalPrecisionEnum#MILLI} precision
	 */
	public InstantType(Date theDate) {
		super(theDate, DEFAULT_PRECISION, TimeZone.getDefault());
	}

	/**
	 * Constructor which accepts a date value and a precision value. Valid
	 * precisions values for this type are:
	 * <ul>
	 * <li>{@link TemporalPrecisionEnum#SECOND}
	 * <li>{@link TemporalPrecisionEnum#MILLI}
	 * </ul>
	 */
	public InstantType(Date theDate, TemporalPrecisionEnum thePrecision) {
		setValue(theDate);
		setPrecision(thePrecision);
		setTimeZone(TimeZone.getDefault());
	}

	/**
	 * Create a new InstantDt from a string value
	 * 
	 * @param theString
	 *            The string representation of the string. Must be in a valid
	 *            format according to the FHIR specification
	 * @throws DataFormatException
	 */
	public InstantType(String theString) {
		super(theString);
	}

	/**
	 * Invokes {@link Date#after(Date)} on the contained Date against the given
	 * date
	 * 
	 * @throws NullPointerException
	 *             If the {@link #getValue() contained Date} is null
	 */
	public boolean after(Date theDate) {
		return getValue().after(theDate);
	}

	/**
	 * Invokes {@link Date#before(Date)} on the contained Date against the given
	 * date
	 * 
	 * @throws NullPointerException
	 *             If the {@link #getValue() contained Date} is null
	 */
	public boolean before(Date theDate) {
		return getValue().before(theDate);
	}

	@Override
	public InstantType copy() {
     InstantType retVal = new InstantType();
     retVal.setValueAsString(getValueAsString());
     return retVal;
	}

	public String fhirType() {
		return "instant";
	}

	/**
	 * Returns the default precision for this datatype
	 *
	 * @see #DEFAULT_PRECISION
	 */
	@Override
	protected TemporalPrecisionEnum getDefaultPrecisionForDatatype() {
		return DEFAULT_PRECISION;
	}

	@Override
	boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision) {
		switch (thePrecision) {
		case SECOND:
		case MILLI:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Sets the value of this instant to the current time (from the system
	 * clock) and the local/default timezone (as retrieved using
	 * {@link TimeZone#getDefault()}. This TimeZone is generally obtained from
	 * the underlying OS.
	 */
	public void setToCurrentTimeInLocalTimeZone() {
		setValue(new Date());
		setTimeZone(TimeZone.getDefault());
	}

	/**
	 * Returns a new instance of DateTimeType with the current system time and MILLI precision and the system local time
	 * zone
	 */
	public static InstantType now() {
		return new InstantType(new Date(), TemporalPrecisionEnum.MILLI, TimeZone.getDefault());
	}

	/**
	 * Creates a new instance by parsing an HL7 v3 format date time string
	 */
	public static InstantType parseV3(String theV3String) {
		InstantType retVal = new InstantType();
		retVal.setValueAsV3String(theV3String);
		return retVal;
	}

	/**
	 * Factory method which creates a new InstantDt with millisecond precision and initializes it with the
	 * current time and the system local timezone.
	 */
	public static InstantType withCurrentTime() {
		return new InstantType(new Date(), TemporalPrecisionEnum.MILLI, TimeZone.getDefault());
	}
}
