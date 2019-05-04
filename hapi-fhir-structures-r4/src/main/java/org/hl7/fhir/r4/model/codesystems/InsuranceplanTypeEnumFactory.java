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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class InsuranceplanTypeEnumFactory implements EnumFactory<InsuranceplanType> {

  public InsuranceplanType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("medical".equals(codeString))
      return InsuranceplanType.MEDICAL;
    if ("dental".equals(codeString))
      return InsuranceplanType.DENTAL;
    if ("mental".equals(codeString))
      return InsuranceplanType.MENTAL;
    if ("subst-ab".equals(codeString))
      return InsuranceplanType.SUBSTAB;
    if ("vision".equals(codeString))
      return InsuranceplanType.VISION;
    if ("Drug".equals(codeString))
      return InsuranceplanType.DRUG;
    if ("short-term".equals(codeString))
      return InsuranceplanType.SHORTTERM;
    if ("long-term".equals(codeString))
      return InsuranceplanType.LONGTERM;
    if ("hospice".equals(codeString))
      return InsuranceplanType.HOSPICE;
    if ("home".equals(codeString))
      return InsuranceplanType.HOME;
    throw new IllegalArgumentException("Unknown InsuranceplanType code '"+codeString+"'");
  }

  public String toCode(InsuranceplanType code) {
    if (code == InsuranceplanType.MEDICAL)
      return "medical";
    if (code == InsuranceplanType.DENTAL)
      return "dental";
    if (code == InsuranceplanType.MENTAL)
      return "mental";
    if (code == InsuranceplanType.SUBSTAB)
      return "subst-ab";
    if (code == InsuranceplanType.VISION)
      return "vision";
    if (code == InsuranceplanType.DRUG)
      return "Drug";
    if (code == InsuranceplanType.SHORTTERM)
      return "short-term";
    if (code == InsuranceplanType.LONGTERM)
      return "long-term";
    if (code == InsuranceplanType.HOSPICE)
      return "hospice";
    if (code == InsuranceplanType.HOME)
      return "home";
    return "?";
  }

    public String toSystem(InsuranceplanType code) {
      return code.getSystem();
      }

}

