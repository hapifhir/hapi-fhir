package ca.uhn.fhir.model.primitive;

import java.util.Calendar;

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

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

/**
 * Represents a FHIR date datatype. Valid precisions values for this type are:
 * <ul>
 * <li>{@link TemporalPrecisionEnum#YEAR}
 * <li>{@link TemporalPrecisionEnum#MONTH}
 * <li>{@link TemporalPrecisionEnum#DAY}
 * </ul>
 * 
 * <p>
 * <b>Note on using Java Date objects:</b> This type stores the date as a Java Date. Note that
 * the Java Date has more precision (millisecond precision), and does not store a timezone. As such,
 * it could potentially cause issues. For example, if a Date contains the number of milliseconds at
 * midnight in a timezone across the date line from your location, it might refer to a different date than
 * intended.
 * </p>
 * <p>
 * As such, it is recommended to use the <code>Calendar<code> or <code>int,int,int</code> constructors  
 * </p>
 */
@DatatypeDef(name = "date")
public class DateDt extends BaseDateTimeDt {

	/**
	 * The default precision for this type
	 */
	public static final TemporalPrecisionEnum DEFAULT_PRECISION = TemporalPrecisionEnum.DAY;

	/**
	 * Constructor
	 */
	public DateDt() {
		super();
	}

	/**
	 * Constructor which accepts a date value and uses the {@link #DEFAULT_PRECISION} for this type.
	 */
	public DateDt(Calendar theCalendar) {
		super(theCalendar.getTime(), DEFAULT_PRECISION);
		setTimeZone(theCalendar.getTimeZone());
	}

	/**
	 * Constructor which accepts a date value and uses the {@link #DEFAULT_PRECISION} for this type.
	 * <b>Please see the note on timezones</b> on the {@link DateDt class documentation} for considerations
	 * when using this constructor!
	 */
	@SimpleSetter(suffix = "WithDayPrecision")
	public DateDt(@SimpleSetter.Parameter(name = "theDate") Date theDate) {
		super(theDate, DEFAULT_PRECISION);
	}

	/**
	 * Constructor which accepts a date value and a precision value. Valid precisions values for this type are:
	 * <ul>
	 * <li>{@link TemporalPrecisionEnum#YEAR}
	 * <li>{@link TemporalPrecisionEnum#MONTH}
	 * <li>{@link TemporalPrecisionEnum#DAY}
	 * </ul>
	 * <b>Please see the note on timezones</b> on the {@link DateDt class documentation} for considerations
	 * when using this constructor!
	 * 
	 * @throws DataFormatException
	 *             If the specified precision is not allowed for this type
	 */
	@SimpleSetter
	public DateDt(@SimpleSetter.Parameter(name = "theDate") Date theDate, @SimpleSetter.Parameter(name = "thePrecision") TemporalPrecisionEnum thePrecision) {
		super(theDate, thePrecision);
	}

	/**
	 * Constructor which accepts a date value and uses the {@link #DEFAULT_PRECISION} for this type.
	 * 
	 * @param theYear The year, e.g. 2015
	 * @param theMonth The month, e.g. 0 for January
	 * @param theDay The day (1 indexed) e.g. 1 for the first day of the month
	 */
	public DateDt(int theYear, int theMonth, int theDay) {
		this(toCalendarZulu(theYear, theMonth, theDay));
	}

	/**
	 * Constructor which accepts a date as a string in FHIR format
	 * 
	 * @throws DataFormatException
	 *             If the precision in the date string is not allowed for this type
	 */
	public DateDt(String theDate) {
		super(theDate);
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
	protected boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision) {
		switch (thePrecision) {
		case YEAR:
		case MONTH:
		case DAY:
			return true;
		default:
			return false;
		}
	}

	private static GregorianCalendar toCalendarZulu(int theYear, int theMonth, int theDay) {
		GregorianCalendar retVal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		retVal.set(Calendar.YEAR, theYear);
		retVal.set(Calendar.MONTH, theMonth);
		retVal.set(Calendar.DATE, theDay);
		return retVal;
	}

}
