package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


import org.hl7.fhir.instance.model.EnumFactory;

public class V3LanguageAbilityModeEnumFactory implements EnumFactory<V3LanguageAbilityMode> {

  public V3LanguageAbilityMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ESGN".equals(codeString))
      return V3LanguageAbilityMode.ESGN;
    if ("ESP".equals(codeString))
      return V3LanguageAbilityMode.ESP;
    if ("EWR".equals(codeString))
      return V3LanguageAbilityMode.EWR;
    if ("RSGN".equals(codeString))
      return V3LanguageAbilityMode.RSGN;
    if ("RSP".equals(codeString))
      return V3LanguageAbilityMode.RSP;
    if ("RWR".equals(codeString))
      return V3LanguageAbilityMode.RWR;
    throw new IllegalArgumentException("Unknown V3LanguageAbilityMode code '"+codeString+"'");
  }

  public String toCode(V3LanguageAbilityMode code) {
    if (code == V3LanguageAbilityMode.ESGN)
      return "ESGN";
    if (code == V3LanguageAbilityMode.ESP)
      return "ESP";
    if (code == V3LanguageAbilityMode.EWR)
      return "EWR";
    if (code == V3LanguageAbilityMode.RSGN)
      return "RSGN";
    if (code == V3LanguageAbilityMode.RSP)
      return "RSP";
    if (code == V3LanguageAbilityMode.RWR)
      return "RWR";
    return "?";
  }


}

