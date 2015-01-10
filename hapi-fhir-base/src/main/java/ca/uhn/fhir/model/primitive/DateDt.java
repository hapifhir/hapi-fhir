package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
	 * Constructor which accepts a date value and uses the {@link #DEFAULT_PRECISION} for this type
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
	 * 
	 * @throws DataFormatException
	 *             If the specified precision is not allowed for this type
	 */
	@SimpleSetter
	public DateDt(@SimpleSetter.Parameter(name = "theDate") Date theDate, @SimpleSetter.Parameter(name = "thePrecision") TemporalPrecisionEnum thePrecision) {
		super(theDate, thePrecision);
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

	@Override
	boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision) {
		switch (thePrecision) {
		case YEAR:
		case MONTH:
		case DAY:
			return true;
		default:
			return false;
		}
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
