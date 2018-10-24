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

public class GoalStatusEnumFactory implements EnumFactory<GoalStatus> {

  public GoalStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("proposed".equals(codeString))
      return GoalStatus.PROPOSED;
    if ("accepted".equals(codeString))
      return GoalStatus.ACCEPTED;
    if ("planned".equals(codeString))
      return GoalStatus.PLANNED;
    if ("in-progress".equals(codeString))
      return GoalStatus.INPROGRESS;
    if ("on-target".equals(codeString))
      return GoalStatus.ONTARGET;
    if ("ahead-of-target".equals(codeString))
      return GoalStatus.AHEADOFTARGET;
    if ("behind-target".equals(codeString))
      return GoalStatus.BEHINDTARGET;
    if ("sustaining".equals(codeString))
      return GoalStatus.SUSTAINING;
    if ("achieved".equals(codeString))
      return GoalStatus.ACHIEVED;
    if ("on-hold".equals(codeString))
      return GoalStatus.ONHOLD;
    if ("cancelled".equals(codeString))
      return GoalStatus.CANCELLED;
    if ("entered-in-error".equals(codeString))
      return GoalStatus.ENTEREDINERROR;
    if ("rejected".equals(codeString))
      return GoalStatus.REJECTED;
    throw new IllegalArgumentException("Unknown GoalStatus code '"+codeString+"'");
  }

  public String toCode(GoalStatus code) {
    if (code == GoalStatus.PROPOSED)
      return "proposed";
    if (code == GoalStatus.ACCEPTED)
      return "accepted";
    if (code == GoalStatus.PLANNED)
      return "planned";
    if (code == GoalStatus.INPROGRESS)
      return "in-progress";
    if (code == GoalStatus.ONTARGET)
      return "on-target";
    if (code == GoalStatus.AHEADOFTARGET)
      return "ahead-of-target";
    if (code == GoalStatus.BEHINDTARGET)
      return "behind-target";
    if (code == GoalStatus.SUSTAINING)
      return "sustaining";
    if (code == GoalStatus.ACHIEVED)
      return "achieved";
    if (code == GoalStatus.ONHOLD)
      return "on-hold";
    if (code == GoalStatus.CANCELLED)
      return "cancelled";
    if (code == GoalStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == GoalStatus.REJECTED)
      return "rejected";
    return "?";
  }

    public String toSystem(GoalStatus code) {
      return code.getSystem();
      }

}

