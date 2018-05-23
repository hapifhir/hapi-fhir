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

public class AdverseEventCategoryEnumFactory implements EnumFactory<AdverseEventCategory> {

  public AdverseEventCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ProductProblem".equals(codeString))
      return AdverseEventCategory.PRODUCTPROBLEM;
    if ("ProductQuality".equals(codeString))
      return AdverseEventCategory.PRODUCTQUALITY;
    if ("ProductUseError".equals(codeString))
      return AdverseEventCategory.PRODUCTUSEERROR;
    if ("WrongDose".equals(codeString))
      return AdverseEventCategory.WRONGDOSE;
    if ("IncorrectPrescribingInformation".equals(codeString))
      return AdverseEventCategory.INCORRECTPRESCRIBINGINFORMATION;
    if ("WrongTechnique".equals(codeString))
      return AdverseEventCategory.WRONGTECHNIQUE;
    if ("WrongRouteOfAdministration".equals(codeString))
      return AdverseEventCategory.WRONGROUTEOFADMINISTRATION;
    if ("WrongRate".equals(codeString))
      return AdverseEventCategory.WRONGRATE;
    if ("WrongDuration".equals(codeString))
      return AdverseEventCategory.WRONGDURATION;
    if ("WrongTime".equals(codeString))
      return AdverseEventCategory.WRONGTIME;
    if ("ExpiredDrug".equals(codeString))
      return AdverseEventCategory.EXPIREDDRUG;
    if ("MedicalDeviceUseError".equals(codeString))
      return AdverseEventCategory.MEDICALDEVICEUSEERROR;
    if ("ProblemDifferentManufacturer".equals(codeString))
      return AdverseEventCategory.PROBLEMDIFFERENTMANUFACTURER;
    if ("UnsafePhysicalEnvironment".equals(codeString))
      return AdverseEventCategory.UNSAFEPHYSICALENVIRONMENT;
    throw new IllegalArgumentException("Unknown AdverseEventCategory code '"+codeString+"'");
  }

  public String toCode(AdverseEventCategory code) {
    if (code == AdverseEventCategory.PRODUCTPROBLEM)
      return "ProductProblem";
    if (code == AdverseEventCategory.PRODUCTQUALITY)
      return "ProductQuality";
    if (code == AdverseEventCategory.PRODUCTUSEERROR)
      return "ProductUseError";
    if (code == AdverseEventCategory.WRONGDOSE)
      return "WrongDose";
    if (code == AdverseEventCategory.INCORRECTPRESCRIBINGINFORMATION)
      return "IncorrectPrescribingInformation";
    if (code == AdverseEventCategory.WRONGTECHNIQUE)
      return "WrongTechnique";
    if (code == AdverseEventCategory.WRONGROUTEOFADMINISTRATION)
      return "WrongRouteOfAdministration";
    if (code == AdverseEventCategory.WRONGRATE)
      return "WrongRate";
    if (code == AdverseEventCategory.WRONGDURATION)
      return "WrongDuration";
    if (code == AdverseEventCategory.WRONGTIME)
      return "WrongTime";
    if (code == AdverseEventCategory.EXPIREDDRUG)
      return "ExpiredDrug";
    if (code == AdverseEventCategory.MEDICALDEVICEUSEERROR)
      return "MedicalDeviceUseError";
    if (code == AdverseEventCategory.PROBLEMDIFFERENTMANUFACTURER)
      return "ProblemDifferentManufacturer";
    if (code == AdverseEventCategory.UNSAFEPHYSICALENVIRONMENT)
      return "UnsafePhysicalEnvironment";
    return "?";
  }

    public String toSystem(AdverseEventCategory code) {
      return code.getSystem();
      }

}

