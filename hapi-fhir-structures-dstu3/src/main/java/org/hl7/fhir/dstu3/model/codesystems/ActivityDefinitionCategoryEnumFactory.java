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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class ActivityDefinitionCategoryEnumFactory implements EnumFactory<ActivityDefinitionCategory> {

  public ActivityDefinitionCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("communication".equals(codeString))
      return ActivityDefinitionCategory.COMMUNICATION;
    if ("device".equals(codeString))
      return ActivityDefinitionCategory.DEVICE;
    if ("diagnostic".equals(codeString))
      return ActivityDefinitionCategory.DIAGNOSTIC;
    if ("diet".equals(codeString))
      return ActivityDefinitionCategory.DIET;
    if ("drug".equals(codeString))
      return ActivityDefinitionCategory.DRUG;
    if ("encounter".equals(codeString))
      return ActivityDefinitionCategory.ENCOUNTER;
    if ("immunization".equals(codeString))
      return ActivityDefinitionCategory.IMMUNIZATION;
    if ("observation".equals(codeString))
      return ActivityDefinitionCategory.OBSERVATION;
    if ("procedure".equals(codeString))
      return ActivityDefinitionCategory.PROCEDURE;
    if ("referral".equals(codeString))
      return ActivityDefinitionCategory.REFERRAL;
    if ("supply".equals(codeString))
      return ActivityDefinitionCategory.SUPPLY;
    if ("vision".equals(codeString))
      return ActivityDefinitionCategory.VISION;
    if ("other".equals(codeString))
      return ActivityDefinitionCategory.OTHER;
    throw new IllegalArgumentException("Unknown ActivityDefinitionCategory code '"+codeString+"'");
  }

  public String toCode(ActivityDefinitionCategory code) {
    if (code == ActivityDefinitionCategory.COMMUNICATION)
      return "communication";
    if (code == ActivityDefinitionCategory.DEVICE)
      return "device";
    if (code == ActivityDefinitionCategory.DIAGNOSTIC)
      return "diagnostic";
    if (code == ActivityDefinitionCategory.DIET)
      return "diet";
    if (code == ActivityDefinitionCategory.DRUG)
      return "drug";
    if (code == ActivityDefinitionCategory.ENCOUNTER)
      return "encounter";
    if (code == ActivityDefinitionCategory.IMMUNIZATION)
      return "immunization";
    if (code == ActivityDefinitionCategory.OBSERVATION)
      return "observation";
    if (code == ActivityDefinitionCategory.PROCEDURE)
      return "procedure";
    if (code == ActivityDefinitionCategory.REFERRAL)
      return "referral";
    if (code == ActivityDefinitionCategory.SUPPLY)
      return "supply";
    if (code == ActivityDefinitionCategory.VISION)
      return "vision";
    if (code == ActivityDefinitionCategory.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(ActivityDefinitionCategory code) {
      return code.getSystem();
      }

}

