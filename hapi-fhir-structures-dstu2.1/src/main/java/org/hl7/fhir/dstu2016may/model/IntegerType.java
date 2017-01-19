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
package org.hl7.fhir.dstu2016may.model;

import org.hl7.fhir.instance.model.api.IBaseIntegerDatatype;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;

/**
 * Primitive type "integer" in FHIR: A signed 32-bit integer
 */
@DatatypeDef(name = "integer")
public class IntegerType extends PrimitiveType<Integer> implements IBaseIntegerDatatype {

	private static final long serialVersionUID = 3L;

	/**
	 * Constructor
	 */
	public IntegerType() {
		// nothing
	}

	/**
	 * Constructor
	 */
	public IntegerType(int theInteger) {
		setValue(theInteger);
	}

	/**
	 * Constructor
	 * 
	 * @param theIntegerAsString
	 *            A string representation of an integer
	 * @throws IllegalArgumentException
	 *             If the string is not a valid integer representation
	 */
	public IntegerType(String theIntegerAsString) {
		setValueAsString(theIntegerAsString);
	}

	/**
	 * Constructor
	 * 
	 * @param theValue The value
	 * @throws IllegalArgumentException If the value is too large to fit in a signed integer
	 */
	public IntegerType(Long theValue) {
	    if (theValue < java.lang.Integer.MIN_VALUE || theValue > java.lang.Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (theValue + " cannot be cast to int without changing its value.");
	    }
	    if(theValue!=null) {
	    	setValue((int)theValue.longValue());
	    }
	}

	@Override
	protected Integer parse(String theValue) {
		try {
			return Integer.parseInt(theValue);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	protected String encode(Integer theValue) {
		return Integer.toString(theValue);
	}

	@Override
	public IntegerType copy() {
		return new IntegerType(getValue());
	}

	public String fhirType() {
		return "integer";		
	}
}
