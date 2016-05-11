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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.hl7.fhir.instance.model.api.IBaseDecimalDatatype;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;

/**
 * Primitive type "decimal" in FHIR: A rational number
 */
@DatatypeDef(name = "decimal")
public class DecimalType extends PrimitiveType<BigDecimal> implements Comparable<DecimalType>, IBaseDecimalDatatype {

	private static final long serialVersionUID = 3L;

	/**
	 * Constructor
	 */
	public DecimalType() {
		super();
	}

	/**
	 * Constructor
	 */
	public DecimalType(BigDecimal theValue) {
		setValue(theValue);
	}

	/**
	 * Constructor
	 */
	public DecimalType(double theValue) {
		// Use the valueOf here because the constructor gives wacky precision
		// changes due to the floating point conversion
		setValue(BigDecimal.valueOf(theValue));
	}

	/**
	 * Constructor
	 */
	public DecimalType(long theValue) {
		setValue(new BigDecimal(theValue));
	}

	/**
	 * Constructor
	 */
	public DecimalType(String theValue) {
		setValue(new BigDecimal(theValue));
	}

	@Override
	public int compareTo(DecimalType theObj) {
		if (getValue() == null && theObj.getValue() == null) {
			return 0;
		}
		if (getValue() != null && theObj.getValue() == null) {
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

	/**
	 * Sets a new value using a long
	 */
	public void setValue(long theValue) {
		setValue(new BigDecimal(theValue));
	}

	/**
	 * Sets a new value using a double
	 */
	public void setValue(double theValue) {
		setValue(new BigDecimal(theValue));
	}

	@Override
	public DecimalType copy() {
		return new DecimalType(getValue());
	}

	public String fhirType() {
		return "decimal";		
	}

}
