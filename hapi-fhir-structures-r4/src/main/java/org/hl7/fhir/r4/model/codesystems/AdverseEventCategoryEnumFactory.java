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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.r4.model.EnumFactory;

public class AdverseEventCategoryEnumFactory implements EnumFactory<AdverseEventCategory> {

  public AdverseEventCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("product-problem".equals(codeString))
      return AdverseEventCategory.PRODUCTPROBLEM;
    if ("product-quality".equals(codeString))
      return AdverseEventCategory.PRODUCTQUALITY;
    if ("product-use-error".equals(codeString))
      return AdverseEventCategory.PRODUCTUSEERROR;
    if ("wrong-dose".equals(codeString))
      return AdverseEventCategory.WRONGDOSE;
    if ("incorrect-prescribing-information".equals(codeString))
      return AdverseEventCategory.INCORRECTPRESCRIBINGINFORMATION;
    if ("wrong-technique".equals(codeString))
      return AdverseEventCategory.WRONGTECHNIQUE;
    if ("wrong-route-of-administration".equals(codeString))
      return AdverseEventCategory.WRONGROUTEOFADMINISTRATION;
    if ("wrong-rate".equals(codeString))
      return AdverseEventCategory.WRONGRATE;
    if ("wrong-duration".equals(codeString))
      return AdverseEventCategory.WRONGDURATION;
    if ("wrong-time".equals(codeString))
      return AdverseEventCategory.WRONGTIME;
    if ("expired-drug".equals(codeString))
      return AdverseEventCategory.EXPIREDDRUG;
    if ("medical-device-use-error".equals(codeString))
      return AdverseEventCategory.MEDICALDEVICEUSEERROR;
    if ("problem-different-manufacturer".equals(codeString))
      return AdverseEventCategory.PROBLEMDIFFERENTMANUFACTURER;
    if ("unsafe-physical-environment".equals(codeString))
      return AdverseEventCategory.UNSAFEPHYSICALENVIRONMENT;
    throw new IllegalArgumentException("Unknown AdverseEventCategory code '"+codeString+"'");
  }

  public String toCode(AdverseEventCategory code) {
    if (code == AdverseEventCategory.PRODUCTPROBLEM)
      return "product-problem";
    if (code == AdverseEventCategory.PRODUCTQUALITY)
      return "product-quality";
    if (code == AdverseEventCategory.PRODUCTUSEERROR)
      return "product-use-error";
    if (code == AdverseEventCategory.WRONGDOSE)
      return "wrong-dose";
    if (code == AdverseEventCategory.INCORRECTPRESCRIBINGINFORMATION)
      return "incorrect-prescribing-information";
    if (code == AdverseEventCategory.WRONGTECHNIQUE)
      return "wrong-technique";
    if (code == AdverseEventCategory.WRONGROUTEOFADMINISTRATION)
      return "wrong-route-of-administration";
    if (code == AdverseEventCategory.WRONGRATE)
      return "wrong-rate";
    if (code == AdverseEventCategory.WRONGDURATION)
      return "wrong-duration";
    if (code == AdverseEventCategory.WRONGTIME)
      return "wrong-time";
    if (code == AdverseEventCategory.EXPIREDDRUG)
      return "expired-drug";
    if (code == AdverseEventCategory.MEDICALDEVICEUSEERROR)
      return "medical-device-use-error";
    if (code == AdverseEventCategory.PROBLEMDIFFERENTMANUFACTURER)
      return "problem-different-manufacturer";
    if (code == AdverseEventCategory.UNSAFEPHYSICALENVIRONMENT)
      return "unsafe-physical-environment";
    return "?";
  }

    public String toSystem(AdverseEventCategory code) {
      return code.getSystem();
      }

}

