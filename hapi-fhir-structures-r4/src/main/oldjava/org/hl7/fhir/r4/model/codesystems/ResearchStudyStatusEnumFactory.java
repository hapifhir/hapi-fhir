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

public class ResearchStudyStatusEnumFactory implements EnumFactory<ResearchStudyStatus> {

  public ResearchStudyStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return ResearchStudyStatus.ACTIVE;
    if ("administratively-completed".equals(codeString))
      return ResearchStudyStatus.ADMINISTRATIVELYCOMPLETED;
    if ("approved".equals(codeString))
      return ResearchStudyStatus.APPROVED;
    if ("closed-to-accrual".equals(codeString))
      return ResearchStudyStatus.CLOSEDTOACCRUAL;
    if ("closed-to-accrual-and-intervention".equals(codeString))
      return ResearchStudyStatus.CLOSEDTOACCRUALANDINTERVENTION;
    if ("completed".equals(codeString))
      return ResearchStudyStatus.COMPLETED;
    if ("disapproved".equals(codeString))
      return ResearchStudyStatus.DISAPPROVED;
    if ("in-review".equals(codeString))
      return ResearchStudyStatus.INREVIEW;
    if ("temporarily-closed-to-accrual".equals(codeString))
      return ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUAL;
    if ("temporarily-closed-to-accrual-and-intervention".equals(codeString))
      return ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION;
    if ("withdrawn".equals(codeString))
      return ResearchStudyStatus.WITHDRAWN;
    throw new IllegalArgumentException("Unknown ResearchStudyStatus code '"+codeString+"'");
  }

  public String toCode(ResearchStudyStatus code) {
    if (code == ResearchStudyStatus.ACTIVE)
      return "active";
    if (code == ResearchStudyStatus.ADMINISTRATIVELYCOMPLETED)
      return "administratively-completed";
    if (code == ResearchStudyStatus.APPROVED)
      return "approved";
    if (code == ResearchStudyStatus.CLOSEDTOACCRUAL)
      return "closed-to-accrual";
    if (code == ResearchStudyStatus.CLOSEDTOACCRUALANDINTERVENTION)
      return "closed-to-accrual-and-intervention";
    if (code == ResearchStudyStatus.COMPLETED)
      return "completed";
    if (code == ResearchStudyStatus.DISAPPROVED)
      return "disapproved";
    if (code == ResearchStudyStatus.INREVIEW)
      return "in-review";
    if (code == ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUAL)
      return "temporarily-closed-to-accrual";
    if (code == ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION)
      return "temporarily-closed-to-accrual-and-intervention";
    if (code == ResearchStudyStatus.WITHDRAWN)
      return "withdrawn";
    return "?";
  }

    public String toSystem(ResearchStudyStatus code) {
      return code.getSystem();
      }

}

