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

/**
 * Primitive type "integer" in FHIR: A signed 32-bit integer 
 * @author Grahame
 *
 */
public class IntegerType extends PrimitiveType {

  private static final long serialVersionUID = -553171308047944356L;
	/**
	 * the actual value of the number
	 */
	private java.lang.Integer value;
	/**
	 * The exact representation of the number on the wire. i.e. does it have leading zeros. 
	 * This SHOULD not be used, but is provided in case it's absolutely needed
	 */
	private String original;
	
	public IntegerType(Integer value) {
    if (value != null) {
      this.value = value;
      original = value.toString();
    }
  }

  public IntegerType() {
  }

  /**
	 * @return the integer value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the integer value
	 */
	public void setValue(int value) {
		this.value = value;
		this.original = null;
	}

	/**
	 * @return The exact representation of the number on the wire
	 */
  public String getOriginal() {
    return original;
  }

  /**
   * @param original The exact representation of the number on the wire
   */
  public void setOriginal(String original) {
    this.original = original;
  } 
	
	@Override
  public IntegerType copy() {
		IntegerType dst = new IntegerType();
		dst.value = value;
		dst.original = original;
		return dst;
	}
	
	@Override
  protected Type typedCopy() {
		return copy();
	}

	public String getStringValue() {
	  return java.lang.Integer.toString(value);
  }

  @Override
  public String asStringValue() {
    return original != null ? original : java.lang.Integer.toString(value);
  }
	public boolean isEmpty() {
		return super.isEmpty() && value == null;
	}

  public boolean hasValue() {
    return value != null;
  }

}
