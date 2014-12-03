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

/**
 * Primitive type "id" in FHIR: a string from 1 to 36 characters, only containing letters, digits, "-" and "."
 */
public class IdType extends PrimitiveType {

  private static final long serialVersionUID = 8363122970864385593L;

  public static final int MAX_LENGTH = 64;
  
	/**
	 * The value of the id
	 */
	protected String value;

	public IdType(String value) {
    this.value = value;  
  }

  public IdType() {
  }

  /**
	 * @return the id
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the id
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
  protected Type typedCopy() {
		return copy();
	}
	
	@Override
  public IdType copy() {
		IdType dst = new IdType();
		dst.value = value;
		return dst;
	}

  @Override
  public String asStringValue() {
    return value;
  }
	public boolean isEmpty() {
		return super.isEmpty() && value == null;
	}

	public boolean hasValue() {
		return value != null;
	}
	
}
