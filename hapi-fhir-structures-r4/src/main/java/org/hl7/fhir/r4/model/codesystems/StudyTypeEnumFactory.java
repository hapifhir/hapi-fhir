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

public class StudyTypeEnumFactory implements EnumFactory<StudyType> {

  public StudyType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("RCT".equals(codeString))
      return StudyType.RCT;
    if ("CCT".equals(codeString))
      return StudyType.CCT;
    if ("cohort".equals(codeString))
      return StudyType.COHORT;
    if ("case-control".equals(codeString))
      return StudyType.CASECONTROL;
    if ("series".equals(codeString))
      return StudyType.SERIES;
    if ("case-report".equals(codeString))
      return StudyType.CASEREPORT;
    if ("mixed".equals(codeString))
      return StudyType.MIXED;
    throw new IllegalArgumentException("Unknown StudyType code '"+codeString+"'");
  }

  public String toCode(StudyType code) {
    if (code == StudyType.RCT)
      return "RCT";
    if (code == StudyType.CCT)
      return "CCT";
    if (code == StudyType.COHORT)
      return "cohort";
    if (code == StudyType.CASECONTROL)
      return "case-control";
    if (code == StudyType.SERIES)
      return "series";
    if (code == StudyType.CASEREPORT)
      return "case-report";
    if (code == StudyType.MIXED)
      return "mixed";
    return "?";
  }

    public String toSystem(StudyType code) {
      return code.getSystem();
      }

}

