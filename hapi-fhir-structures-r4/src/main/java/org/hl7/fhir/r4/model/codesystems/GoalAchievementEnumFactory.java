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

public class GoalAchievementEnumFactory implements EnumFactory<GoalAchievement> {

  public GoalAchievement fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("in-progress".equals(codeString))
      return GoalAchievement.INPROGRESS;
    if ("improving".equals(codeString))
      return GoalAchievement.IMPROVING;
    if ("worsening".equals(codeString))
      return GoalAchievement.WORSENING;
    if ("no-change".equals(codeString))
      return GoalAchievement.NOCHANGE;
    if ("achieved".equals(codeString))
      return GoalAchievement.ACHIEVED;
    if ("sustaining".equals(codeString))
      return GoalAchievement.SUSTAINING;
    if ("not-achieved".equals(codeString))
      return GoalAchievement.NOTACHIEVED;
    if ("no-progress".equals(codeString))
      return GoalAchievement.NOPROGRESS;
    if ("not-attainable".equals(codeString))
      return GoalAchievement.NOTATTAINABLE;
    throw new IllegalArgumentException("Unknown GoalAchievement code '"+codeString+"'");
  }

  public String toCode(GoalAchievement code) {
    if (code == GoalAchievement.INPROGRESS)
      return "in-progress";
    if (code == GoalAchievement.IMPROVING)
      return "improving";
    if (code == GoalAchievement.WORSENING)
      return "worsening";
    if (code == GoalAchievement.NOCHANGE)
      return "no-change";
    if (code == GoalAchievement.ACHIEVED)
      return "achieved";
    if (code == GoalAchievement.SUSTAINING)
      return "sustaining";
    if (code == GoalAchievement.NOTACHIEVED)
      return "not-achieved";
    if (code == GoalAchievement.NOPROGRESS)
      return "no-progress";
    if (code == GoalAchievement.NOTATTAINABLE)
      return "not-attainable";
    return "?";
  }

    public String toSystem(GoalAchievement code) {
      return code.getSystem();
      }

}

