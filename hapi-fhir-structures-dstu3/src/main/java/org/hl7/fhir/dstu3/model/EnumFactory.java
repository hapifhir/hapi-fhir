package org.hl7.fhir.dstu3.model;

import org.hl7.fhir.instance.model.api.IBaseEnumFactory;


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
 * Helper class to help manage generic enumerated types
 */
public interface EnumFactory<T extends Enum<?>> extends IBaseEnumFactory<T> {

	/**
	 * Read an enumeration value from the string that represents it on the XML or JSON
	 * @param codeString the value found in the XML or JSON
	 * @return the enumeration value
	 * @throws Exception is the value is not known
	 */
  public T fromCode(String codeString) throws IllegalArgumentException;
  
  /**
   * Get the XML/JSON representation for an enumerated value
   * @param code - the enumeration value
   * @return the XML/JSON representation
   */
  public String toCode(T code);

  /**
   * Get the XML/JSON representation for an enumerated value
   * @param code - the enumeration value
   * @return the XML/JSON representation
   */
  public String toSystem(T code);

}
