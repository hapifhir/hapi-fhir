package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class ValidationProcessEnumFactory implements EnumFactory<ValidationProcess> {

  public ValidationProcess fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("edit-check".equals(codeString))
      return ValidationProcess.EDITCHECK;
    if ("valueset".equals(codeString))
      return ValidationProcess.VALUESET;
    if ("primary".equals(codeString))
      return ValidationProcess.PRIMARY;
    if ("multi".equals(codeString))
      return ValidationProcess.MULTI;
    if ("standalone".equals(codeString))
      return ValidationProcess.STANDALONE;
    if ("in-context".equals(codeString))
      return ValidationProcess.INCONTEXT;
    throw new IllegalArgumentException("Unknown ValidationProcess code '"+codeString+"'");
  }

  public String toCode(ValidationProcess code) {
    if (code == ValidationProcess.EDITCHECK)
      return "edit-check";
    if (code == ValidationProcess.VALUESET)
      return "valueset";
    if (code == ValidationProcess.PRIMARY)
      return "primary";
    if (code == ValidationProcess.MULTI)
      return "multi";
    if (code == ValidationProcess.STANDALONE)
      return "standalone";
    if (code == ValidationProcess.INCONTEXT)
      return "in-context";
    return "?";
  }

    public String toSystem(ValidationProcess code) {
      return code.getSystem();
      }

}

