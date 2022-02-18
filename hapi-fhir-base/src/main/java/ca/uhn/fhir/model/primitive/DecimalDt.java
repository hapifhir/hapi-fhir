package ca.uhn.fhir.model.primitive;

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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.hl7.fhir.instance.model.api.IBaseDecimalDatatype;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;

@DatatypeDef(name = "decimal")
public class DecimalDt extends BasePrimitive<BigDecimal> implements Comparable<DecimalDt>, IBaseDecimalDatatype {

	/**
	 * Constructor
	 */
	public DecimalDt() {
		super();
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public DecimalDt(@SimpleSetter.Parameter(name = "theValue") BigDecimal theValue) {
		setValue(theValue);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public DecimalDt(@SimpleSetter.Parameter(name = "theValue") double theValue) {
		// Use the valueOf here because the constructor gives crazy precision
		// changes due to the floating point conversion
		setValue(BigDecimal.valueOf(theValue));
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public DecimalDt(@SimpleSetter.Parameter(name = "theValue") long theValue) {
		setValue(new BigDecimal(theValue));
	}

	/**
	 * Constructor
	 */
	public DecimalDt(String theValue) {
		setValue(new BigDecimal(theValue));
	}

	@Override
	public int compareTo(DecimalDt theObj) {
		if (getValue() == null && theObj.getValue() == null) {
			return 0;
		}
		if (getValue() != null && (theObj == null || theObj.getValue() == null)) {
			return 1;
		}
		if (getValue() == null && theObj.getValue() != null) {
			return -1;
		}
		return getValue().compareTo(theObj.getValue());
	}

	@Override
	protected String encode(BigDecimal theValue) {
		return getValue().toPlainString();
	}

	/**
	 * Gets the value as an integer, using {@link BigDecimal#intValue()}
	 */
	public int getValueAsInteger() {
		return getValue().intValue();
	}

	public Number getValueAsNumber() {
		return getValue();
	}

	@Override
	protected BigDecimal parse(String theValue) {
		return new BigDecimal(theValue);
	}

	/**
	 * Rounds the value to the given prevision
	 * 
	 * @see MathContext#getPrecision()
	 */
	public void round(int thePrecision) {
		if (getValue() != null) {
			BigDecimal newValue = getValue().round(new MathContext(thePrecision));
			setValue(newValue);
		}
	}

	/**
	 * Rounds the value to the given prevision
	 * 
	 * @see MathContext#getPrecision()
	 * @see MathContext#getRoundingMode()
	 */
	public void round(int thePrecision, RoundingMode theRoundingMode) {
		if (getValue() != null) {
			BigDecimal newValue = getValue().round(new MathContext(thePrecision, theRoundingMode));
			setValue(newValue);
		}
	}

	/**
	 * Sets a new value using an integer
	 */
	public void setValueAsInteger(int theValue) {
		setValue(new BigDecimal(theValue));
	}

}
