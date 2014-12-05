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

package org.hl7.fhir.instance.model;

import java.util.Date;
import java.util.TimeZone;

import org.hl7.fhir.instance.model.annotations.DatatypeDef;

import ca.uhn.fhir.parser.DataFormatException;

/**
 * Represents a FHIR dateTime datatype. Valid precisions values for this type are:
 * <ul>
 * <li>{@link TemporalPrecisionEnum#YEAR}
 * <li>{@link TemporalPrecisionEnum#MONTH}
 * <li>{@link TemporalPrecisionEnum#DAY}
 * <li>{@link TemporalPrecisionEnum#SECOND}
 * <li>{@link TemporalPrecisionEnum#MILLI}
 * </ul>
 */
@DatatypeDef(name = "dateTime")
public class DateTimeType extends BaseDateTimeType {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The default precision for this type
	 */
	public static final TemporalPrecisionEnum DEFAULT_PRECISION = TemporalPrecisionEnum.SECOND;

	/**
	 * Constructor
	 */
	public DateTimeType() {
		super();
	}

	/**
	 * Create a new DateTimeDt with seconds precision and the local time zone
	 */
	public DateTimeType(Date theDate) {
		super(theDate, DEFAULT_PRECISION, TimeZone.getDefault());
	}

	/**
	 * Constructor which accepts a date value and a precision value. Valid precisions values for this type are:
	 * <ul>
	 * <li>{@link TemporalPrecisionEnum#YEAR}
	 * <li>{@link TemporalPrecisionEnum#MONTH}
	 * <li>{@link TemporalPrecisionEnum#DAY}
	 * <li>{@link TemporalPrecisionEnum#SECOND}
	 * <li>{@link TemporalPrecisionEnum#MILLI}
	 * </ul>
	 * 
	 * @throws DataFormatException
	 *             If the specified precision is not allowed for this type
	 */
	public DateTimeType(Date theDate, TemporalPrecisionEnum thePrecision) {
		super(theDate, thePrecision, TimeZone.getDefault());
	}

	/**
	 * Create a new instance using a string date/time
	 * 
	 * @throws DataFormatException
	 *             If the specified precision is not allowed for this type
	 */
	public DateTimeType(String theValue) {
		super(theValue);
	}

	/**
	 * Constructor which accepts a date value, precision value, and time zone. Valid precisions values for this type
	 * are:
	 * <ul>
	 * <li>{@link TemporalPrecisionEnum#YEAR}
	 * <li>{@link TemporalPrecisionEnum#MONTH}
	 * <li>{@link TemporalPrecisionEnum#DAY}
	 * <li>{@link TemporalPrecisionEnum#SECOND}
	 * <li>{@link TemporalPrecisionEnum#MILLI}
	 * </ul>
	 */
	public DateTimeType(Date theDate, TemporalPrecisionEnum thePrecision, TimeZone theTimezone) {
		super(theDate, thePrecision, theTimezone);
	}

	@Override
	boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision) {
		switch (thePrecision) {
		case YEAR:
		case MONTH:
		case DAY:
		case SECOND:
		case MILLI:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Returns a new instance of DateTimeDt with the current system time and SECOND precision and the system local time
	 * zone
	 */
	public static DateTimeType withCurrentTime() {
		return new DateTimeType(new Date(), TemporalPrecisionEnum.SECOND, TimeZone.getDefault());
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
	public DateTimeType copy() {
		return new DateTimeType(getValueAsString());
	}

}
