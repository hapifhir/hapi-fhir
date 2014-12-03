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

import org.hl7.fhir.instance.formats.FormatUtilities;

/**
 * Primitive type "base64Binary" in FHIR: a sequence of bytes represented in base64
 */
public class Base64BinaryType extends PrimitiveType {

  private static final long serialVersionUID = 6170198697716842056L;
	/**
	 * The byte content in the base64Binary
	 */
	private byte[] value;

	public Base64BinaryType(byte[] value) {
    this.value = value;  
  }

  public Base64BinaryType() {
  }

  /**
	 * @return The byte content in the base64Binary
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * @param value The byte content in the base64Binary
	 */
	public void setValue(byte[] value) {
		this.value = value;
	}
	
	@Override
  public Base64BinaryType copy() {
		Base64BinaryType dst = new Base64BinaryType();
		dst.value = value;
		return dst;
	}
	
	@Override
  protected Type typedCopy() {
		return copy();
	}

	public String getStringValue() {
	  return FormatUtilities.toString(value);
  }

  @Override
  public String asStringValue() {
    return new String(value);
  }
  
	public boolean isEmpty() {
		return super.isEmpty() && value == null;
	}

	public boolean hasValue() {
		return value != null;
	}
	
}
