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

import java.util.Date;
import java.util.TimeZone;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;

@DatatypeDef(name = "dateTime")
public class DateTimeDt extends BaseDateTimeDt {

	/**
	 * The default precision for this type
	 */
	public static final TemporalPrecisionEnum DEFAULT_PRECISION = TemporalPrecisionEnum.SECOND;

	/**
	 * Constructor
	 */
	public DateTimeDt() {
		super();
	}

	/**
	 * Create a new DateTimeDt
	 */
	@SimpleSetter(suffix = "WithSecondsPrecision")
	public DateTimeDt(@SimpleSetter.Parameter(name = "theDate") Date theDate) {
		setValue(theDate);
		setPrecision(DEFAULT_PRECISION);
		setTimeZone(TimeZone.getDefault());
	}

	/**
	 * Constructor which accepts a date value and a precision value. Valid
	 * precisions values for this type are:
	 * <ul>
	 * <li>{@link TemporalPrecisionEnum#YEAR}
	 * <li>{@link TemporalPrecisionEnum#MONTH}
	 * <li>{@link TemporalPrecisionEnum#DAY}
	 * <li>{@link TemporalPrecisionEnum#SECOND}
	 * <li>{@link TemporalPrecisionEnum#MILLI}
	 * </ul>
	 */
	@SimpleSetter
	public DateTimeDt(@SimpleSetter.Parameter(name = "theDate") Date theDate, @SimpleSetter.Parameter(name = "thePrecision") TemporalPrecisionEnum thePrecision) {
		setValue(theDate);
		setPrecision(thePrecision);
		setTimeZone(TimeZone.getDefault());
	}

	/**
	 * Create a new instance using a string date/time
	 */
	public DateTimeDt(String theValue) {
		setValueAsString(theValue);
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
	 * Returns a new instance of DateTimeDt with the current system time and SECOND precision
	 */
	public static DateTimeDt withCurrentTime() {
		return new DateTimeDt(new Date(), TemporalPrecisionEnum.SECOND);
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


}
