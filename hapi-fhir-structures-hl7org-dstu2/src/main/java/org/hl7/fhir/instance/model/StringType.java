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

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;


/**
 * Primitive type "string" in FHIR - any sequence of unicode characters less than 1MB in length
 */
@DatatypeDef(name = "string")
public class StringType extends PrimitiveType<String> {

	private static final long serialVersionUID = 3L;

	/**
	 * Create a new String
	 */
	public StringType() {
		super();
	}

	/**
	 * Create a new String
	 */
	public StringType(String theValue) {
		setValue(theValue);
	}

	/**
	 * Returns the value of this StringType, or an empty string ("") if the
	 * value is <code>null</code>. This method is provided as a convenience to
	 * users of this API.
	 */
	public String getValueNotNull() {
		return StringUtils.defaultString(getValue());
	}

	/**
	 * Returns the value of this string, or <code>null</code> if no value
	 * is present
	 */
	@Override
	public String toString() {
		return getValue();
	}

	@Override
	protected String parse(String theValue) {
		return theValue;
	}

	@Override
	protected String encode(String theValue) {
		return theValue;
	}

	@Override
	public StringType copy() {
		return new StringType(getValue());
	}

}
