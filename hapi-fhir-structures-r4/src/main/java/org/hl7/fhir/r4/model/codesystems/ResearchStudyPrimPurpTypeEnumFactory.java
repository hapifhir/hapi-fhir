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

public class ResearchStudyPrimPurpTypeEnumFactory implements EnumFactory<ResearchStudyPrimPurpType> {

  public ResearchStudyPrimPurpType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("treatment".equals(codeString))
      return ResearchStudyPrimPurpType.TREATMENT;
    if ("prevention".equals(codeString))
      return ResearchStudyPrimPurpType.PREVENTION;
    if ("diagnostic".equals(codeString))
      return ResearchStudyPrimPurpType.DIAGNOSTIC;
    if ("supportive-care".equals(codeString))
      return ResearchStudyPrimPurpType.SUPPORTIVECARE;
    if ("screening".equals(codeString))
      return ResearchStudyPrimPurpType.SCREENING;
    if ("health-services-research".equals(codeString))
      return ResearchStudyPrimPurpType.HEALTHSERVICESRESEARCH;
    if ("basic-science".equals(codeString))
      return ResearchStudyPrimPurpType.BASICSCIENCE;
    if ("device-feasibility".equals(codeString))
      return ResearchStudyPrimPurpType.DEVICEFEASIBILITY;
    throw new IllegalArgumentException("Unknown ResearchStudyPrimPurpType code '"+codeString+"'");
  }

  public String toCode(ResearchStudyPrimPurpType code) {
    if (code == ResearchStudyPrimPurpType.TREATMENT)
      return "treatment";
    if (code == ResearchStudyPrimPurpType.PREVENTION)
      return "prevention";
    if (code == ResearchStudyPrimPurpType.DIAGNOSTIC)
      return "diagnostic";
    if (code == ResearchStudyPrimPurpType.SUPPORTIVECARE)
      return "supportive-care";
    if (code == ResearchStudyPrimPurpType.SCREENING)
      return "screening";
    if (code == ResearchStudyPrimPurpType.HEALTHSERVICESRESEARCH)
      return "health-services-research";
    if (code == ResearchStudyPrimPurpType.BASICSCIENCE)
      return "basic-science";
    if (code == ResearchStudyPrimPurpType.DEVICEFEASIBILITY)
      return "device-feasibility";
    return "?";
  }

    public String toSystem(ResearchStudyPrimPurpType code) {
      return code.getSystem();
      }

}

