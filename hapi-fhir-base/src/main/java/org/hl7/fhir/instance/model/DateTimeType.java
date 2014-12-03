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
 * Primitive type "dateTime" in FHIR: any day in a gregorian calendar, along with the time in a 24hour clock, and possibly a timezone
 */
public class DateTimeType extends PrimitiveType {

  private static final long serialVersionUID = 7592289953733558372L;
	/**
	 * The value of the dateTime
	 */
	private DateAndTime value;

	public DateTimeType(DateAndTime value) {
    this.value = value;  
  }

  public DateTimeType() {
  }

  /**
	 * @return The value of the dateTime
	 */
	public DateAndTime getValue() {
		return value;
	}

	/**
	 * @param value The value of the dateTime
	 */
	public void setValue(DateAndTime value) {
		this.value = value;
	}
	
	@Override
  public DateTimeType copy() {
		DateTimeType dst = new DateTimeType();
		dst.value = value;
		return dst;
	}
	
	@Override
  protected Type typedCopy() {
		return copy();
	}

  @Override
  public String asStringValue() {
    return value.toString();  
  }

	public boolean isEmpty() {
		return super.isEmpty() && value == null;
	}

	public boolean hasValue() {
		return value != null;
	}
	
}
