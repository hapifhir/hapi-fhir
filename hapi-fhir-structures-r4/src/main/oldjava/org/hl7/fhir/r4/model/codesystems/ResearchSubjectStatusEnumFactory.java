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

public class ResearchSubjectStatusEnumFactory implements EnumFactory<ResearchSubjectStatus> {

  public ResearchSubjectStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("candidate".equals(codeString))
      return ResearchSubjectStatus.CANDIDATE;
    if ("eligible".equals(codeString))
      return ResearchSubjectStatus.ELIGIBLE;
    if ("follow-up".equals(codeString))
      return ResearchSubjectStatus.FOLLOWUP;
    if ("ineligible".equals(codeString))
      return ResearchSubjectStatus.INELIGIBLE;
    if ("not-registered".equals(codeString))
      return ResearchSubjectStatus.NOTREGISTERED;
    if ("off-study".equals(codeString))
      return ResearchSubjectStatus.OFFSTUDY;
    if ("on-study".equals(codeString))
      return ResearchSubjectStatus.ONSTUDY;
    if ("on-study-intervention".equals(codeString))
      return ResearchSubjectStatus.ONSTUDYINTERVENTION;
    if ("on-study-observation".equals(codeString))
      return ResearchSubjectStatus.ONSTUDYOBSERVATION;
    if ("pending-on-study".equals(codeString))
      return ResearchSubjectStatus.PENDINGONSTUDY;
    if ("potential-candidate".equals(codeString))
      return ResearchSubjectStatus.POTENTIALCANDIDATE;
    if ("screening".equals(codeString))
      return ResearchSubjectStatus.SCREENING;
    if ("withdrawn".equals(codeString))
      return ResearchSubjectStatus.WITHDRAWN;
    throw new IllegalArgumentException("Unknown ResearchSubjectStatus code '"+codeString+"'");
  }

  public String toCode(ResearchSubjectStatus code) {
    if (code == ResearchSubjectStatus.CANDIDATE)
      return "candidate";
    if (code == ResearchSubjectStatus.ELIGIBLE)
      return "eligible";
    if (code == ResearchSubjectStatus.FOLLOWUP)
      return "follow-up";
    if (code == ResearchSubjectStatus.INELIGIBLE)
      return "ineligible";
    if (code == ResearchSubjectStatus.NOTREGISTERED)
      return "not-registered";
    if (code == ResearchSubjectStatus.OFFSTUDY)
      return "off-study";
    if (code == ResearchSubjectStatus.ONSTUDY)
      return "on-study";
    if (code == ResearchSubjectStatus.ONSTUDYINTERVENTION)
      return "on-study-intervention";
    if (code == ResearchSubjectStatus.ONSTUDYOBSERVATION)
      return "on-study-observation";
    if (code == ResearchSubjectStatus.PENDINGONSTUDY)
      return "pending-on-study";
    if (code == ResearchSubjectStatus.POTENTIALCANDIDATE)
      return "potential-candidate";
    if (code == ResearchSubjectStatus.SCREENING)
      return "screening";
    if (code == ResearchSubjectStatus.WITHDRAWN)
      return "withdrawn";
    return "?";
  }

    public String toSystem(ResearchSubjectStatus code) {
      return code.getSystem();
      }

}

