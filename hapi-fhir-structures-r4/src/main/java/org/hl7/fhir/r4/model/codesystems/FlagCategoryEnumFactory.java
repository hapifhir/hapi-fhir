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

public class FlagCategoryEnumFactory implements EnumFactory<FlagCategory> {

  public FlagCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("diet".equals(codeString))
      return FlagCategory.DIET;
    if ("drug".equals(codeString))
      return FlagCategory.DRUG;
    if ("lab".equals(codeString))
      return FlagCategory.LAB;
    if ("admin".equals(codeString))
      return FlagCategory.ADMIN;
    if ("contact".equals(codeString))
      return FlagCategory.CONTACT;
    if ("clinical".equals(codeString))
      return FlagCategory.CLINICAL;
    if ("behavioral".equals(codeString))
      return FlagCategory.BEHAVIORAL;
    if ("research".equals(codeString))
      return FlagCategory.RESEARCH;
    if ("advance-directive".equals(codeString))
      return FlagCategory.ADVANCEDIRECTIVE;
    if ("safety".equals(codeString))
      return FlagCategory.SAFETY;
    throw new IllegalArgumentException("Unknown FlagCategory code '"+codeString+"'");
  }

  public String toCode(FlagCategory code) {
    if (code == FlagCategory.DIET)
      return "diet";
    if (code == FlagCategory.DRUG)
      return "drug";
    if (code == FlagCategory.LAB)
      return "lab";
    if (code == FlagCategory.ADMIN)
      return "admin";
    if (code == FlagCategory.CONTACT)
      return "contact";
    if (code == FlagCategory.CLINICAL)
      return "clinical";
    if (code == FlagCategory.BEHAVIORAL)
      return "behavioral";
    if (code == FlagCategory.RESEARCH)
      return "research";
    if (code == FlagCategory.ADVANCEDIRECTIVE)
      return "advance-directive";
    if (code == FlagCategory.SAFETY)
      return "safety";
    return "?";
  }

    public String toSystem(FlagCategory code) {
      return code.getSystem();
      }

}

