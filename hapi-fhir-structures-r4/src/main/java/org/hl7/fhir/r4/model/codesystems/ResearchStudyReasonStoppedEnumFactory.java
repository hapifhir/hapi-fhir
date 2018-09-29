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

public class ResearchStudyReasonStoppedEnumFactory implements EnumFactory<ResearchStudyReasonStopped> {

  public ResearchStudyReasonStopped fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("accrual-goal-met".equals(codeString))
      return ResearchStudyReasonStopped.ACCRUALGOALMET;
    if ("closed-due-to-toxicity".equals(codeString))
      return ResearchStudyReasonStopped.CLOSEDDUETOTOXICITY;
    if ("closed-due-to-lack-of-study-progress".equals(codeString))
      return ResearchStudyReasonStopped.CLOSEDDUETOLACKOFSTUDYPROGRESS;
    if ("temporarily-closed-per-study-design".equals(codeString))
      return ResearchStudyReasonStopped.TEMPORARILYCLOSEDPERSTUDYDESIGN;
    throw new IllegalArgumentException("Unknown ResearchStudyReasonStopped code '"+codeString+"'");
  }

  public String toCode(ResearchStudyReasonStopped code) {
    if (code == ResearchStudyReasonStopped.ACCRUALGOALMET)
      return "accrual-goal-met";
    if (code == ResearchStudyReasonStopped.CLOSEDDUETOTOXICITY)
      return "closed-due-to-toxicity";
    if (code == ResearchStudyReasonStopped.CLOSEDDUETOLACKOFSTUDYPROGRESS)
      return "closed-due-to-lack-of-study-progress";
    if (code == ResearchStudyReasonStopped.TEMPORARILYCLOSEDPERSTUDYDESIGN)
      return "temporarily-closed-per-study-design";
    return "?";
  }

    public String toSystem(ResearchStudyReasonStopped code) {
      return code.getSystem();
      }

}

