package org.hl7.fhir.dstu3.model;

import org.hl7.fhir.instance.model.api.IBaseEnumeration;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;

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
 * Primitive type "code" in FHIR, where the code is tied to an enumerated list of possible values
 * 
 */
@DatatypeDef(name = "code", isSpecialization = true)
public class Enumeration<T extends Enum<?>> extends PrimitiveType<T> implements IBaseEnumeration<T> {

	private static final long serialVersionUID = 1L;
	private final EnumFactory<T> myEnumFactory;

	/**
	 * Constructor
	 */
	public Enumeration(EnumFactory<T> theEnumFactory) {
		if (theEnumFactory == null)
			throw new IllegalArgumentException("An enumeration factory must be provided");
		myEnumFactory = theEnumFactory;
	}

	/**
	 * Constructor
	 */
	public Enumeration(EnumFactory<T> theEnumFactory, String theValue) {
		if (theEnumFactory == null)
			throw new IllegalArgumentException("An enumeration factory must be provided");
		myEnumFactory = theEnumFactory;
		setValueAsString(theValue);
	}

	/**
	 * Constructor
	 */
	public Enumeration(EnumFactory<T> theEnumFactory, T theValue) {
		if (theEnumFactory == null)
			throw new IllegalArgumentException("An enumeration factory must be provided");
		myEnumFactory = theEnumFactory;
		setValue(theValue);
	}

	@Override
	public Enumeration<T> copy() {
		return new Enumeration<T>(myEnumFactory, getValue());
	}

	@Override
	protected String encode(T theValue) {
		return myEnumFactory.toCode(theValue);
	}

	public String fhirType() {
		return "code";
	}

	/**
	 * Provides the enum factory which binds this enumeration to a specific ValueSet
	 */
	public EnumFactory<T> getEnumFactory() {
		return myEnumFactory;
	}

	@Override
	protected T parse(String theValue) {
		if (myEnumFactory != null) {
			return myEnumFactory.fromCode(theValue);
		}
		return null;
	}
}
