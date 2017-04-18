package org.hl7.fhir.dstu3.model.codesystems;

/*
  Copyright (c) 2011+, HL7, Inc.
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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.dstu3.model.EnumFactory;

public class SpecialValuesEnumFactory implements EnumFactory<SpecialValues> {

  public SpecialValues fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("true".equals(codeString))
      return SpecialValues.TRUE;
    if ("false".equals(codeString))
      return SpecialValues.FALSE;
    if ("trace".equals(codeString))
      return SpecialValues.TRACE;
    if ("sufficient".equals(codeString))
      return SpecialValues.SUFFICIENT;
    if ("withdrawn".equals(codeString))
      return SpecialValues.WITHDRAWN;
    if ("nil-known".equals(codeString))
      return SpecialValues.NILKNOWN;
    throw new IllegalArgumentException("Unknown SpecialValues code '"+codeString+"'");
  }

  public String toCode(SpecialValues code) {
    if (code == SpecialValues.TRUE)
      return "true";
    if (code == SpecialValues.FALSE)
      return "false";
    if (code == SpecialValues.TRACE)
      return "trace";
    if (code == SpecialValues.SUFFICIENT)
      return "sufficient";
    if (code == SpecialValues.WITHDRAWN)
      return "withdrawn";
    if (code == SpecialValues.NILKNOWN)
      return "nil-known";
    return "?";
  }

    public String toSystem(SpecialValues code) {
      return code.getSystem();
      }

}

