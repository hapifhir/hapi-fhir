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

// Generated on Sat, Nov 5, 2016 17:49-0400 for FHIR v1.7.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class AllergyIntoleranceStatusEnumFactory implements EnumFactory<AllergyIntoleranceStatus> {

  public AllergyIntoleranceStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return AllergyIntoleranceStatus.ACTIVE;
    if ("active-confirmed".equals(codeString))
      return AllergyIntoleranceStatus.ACTIVECONFIRMED;
    if ("inactive".equals(codeString))
      return AllergyIntoleranceStatus.INACTIVE;
    if ("resolved".equals(codeString))
      return AllergyIntoleranceStatus.RESOLVED;
    if ("refuted".equals(codeString))
      return AllergyIntoleranceStatus.REFUTED;
    if ("entered-in-error".equals(codeString))
      return AllergyIntoleranceStatus.ENTEREDINERROR;
    throw new IllegalArgumentException("Unknown AllergyIntoleranceStatus code '"+codeString+"'");
  }

  public String toCode(AllergyIntoleranceStatus code) {
    if (code == AllergyIntoleranceStatus.ACTIVE)
      return "active";
    if (code == AllergyIntoleranceStatus.ACTIVECONFIRMED)
      return "active-confirmed";
    if (code == AllergyIntoleranceStatus.INACTIVE)
      return "inactive";
    if (code == AllergyIntoleranceStatus.RESOLVED)
      return "resolved";
    if (code == AllergyIntoleranceStatus.REFUTED)
      return "refuted";
    if (code == AllergyIntoleranceStatus.ENTEREDINERROR)
      return "entered-in-error";
    return "?";
  }

    public String toSystem(AllergyIntoleranceStatus code) {
      return code.getSystem();
      }

}

