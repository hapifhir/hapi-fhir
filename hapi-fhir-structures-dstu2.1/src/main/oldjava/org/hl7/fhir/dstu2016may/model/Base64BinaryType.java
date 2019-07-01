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
package org.hl7.fhir.dstu2016may.model;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;

/**
 * Primitive type "base64Binary" in FHIR: a sequence of bytes represented in base64
 */
@DatatypeDef(name="base64binary")
public class Base64BinaryType extends PrimitiveType<byte[]> {

	private static final long serialVersionUID = 3L;

	/**
	 * Constructor
	 */
	public Base64BinaryType() {
		super();
	}

	public Base64BinaryType(byte[] theBytes) {
		super();
		setValue(theBytes);
	}

	public Base64BinaryType(String theValue) {
		super();
		setValueAsString(theValue);
	}

	protected byte[] parse(String theValue) {
		return Base64.decodeBase64(theValue);
	}

	protected String encode(byte[] theValue) {
		return Base64.encodeBase64String(theValue);
	}

	@Override
	public Base64BinaryType copy() {
		return new Base64BinaryType(getValue());
	}

	public String fhirType() {
		return "base64Binary";
	}
}
