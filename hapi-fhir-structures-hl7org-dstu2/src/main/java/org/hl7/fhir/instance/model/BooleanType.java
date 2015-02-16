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
package org.hl7.fhir.instance.model;

import org.hl7.fhir.instance.model.annotations.DatatypeDef;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;

/**
 * Primitive type "boolean" in FHIR "true" or "false"
 */
@DatatypeDef(name="boolean")
public class BooleanType extends PrimitiveType<Boolean> implements IBaseBooleanDatatype {

	private static final long serialVersionUID = 3L;

	public BooleanType() {
		super();
	}

	public BooleanType(boolean theBoolean) {
		super();
		setValue(theBoolean);
	}

	public BooleanType(Boolean theBoolean) {
		super();
		setValue(theBoolean);
	}

	public BooleanType(String value) {
		super();
		setValueAsString(value);  
	}

	protected Boolean parse(String theValue) {
		String value = theValue.trim();
		if ("true".equals(value)) {
			return Boolean.TRUE;
		} else if ("false".equals(value)) {
			return Boolean.FALSE;
		} else {
			throw new IllegalArgumentException("Invalid boolean string: '" + theValue + "'");
		}
	}

	protected String encode(Boolean theValue) {
		if (Boolean.TRUE.equals(theValue)) {
			return "true";
		} else {
			return "false";
		}
	}
	
	public BooleanType copy() {
		return new BooleanType(getValue());
 	}
}
