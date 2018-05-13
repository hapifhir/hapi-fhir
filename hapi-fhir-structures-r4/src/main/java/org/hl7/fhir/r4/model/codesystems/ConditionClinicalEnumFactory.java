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

public class ConditionClinicalEnumFactory implements EnumFactory<ConditionClinical> {

  public ConditionClinical fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return ConditionClinical.ACTIVE;
    if ("recurrence".equals(codeString))
      return ConditionClinical.RECURRENCE;
    if ("relapse".equals(codeString))
      return ConditionClinical.RELAPSE;
    if ("well-controlled".equals(codeString))
      return ConditionClinical.WELLCONTROLLED;
    if ("poorly-controlled".equals(codeString))
      return ConditionClinical.POORLYCONTROLLED;
    if ("inactive".equals(codeString))
      return ConditionClinical.INACTIVE;
    if ("remission".equals(codeString))
      return ConditionClinical.REMISSION;
    if ("resolved".equals(codeString))
      return ConditionClinical.RESOLVED;
    throw new IllegalArgumentException("Unknown ConditionClinical code '"+codeString+"'");
  }

  public String toCode(ConditionClinical code) {
    if (code == ConditionClinical.ACTIVE)
      return "active";
    if (code == ConditionClinical.RECURRENCE)
      return "recurrence";
    if (code == ConditionClinical.RELAPSE)
      return "relapse";
    if (code == ConditionClinical.WELLCONTROLLED)
      return "well-controlled";
    if (code == ConditionClinical.POORLYCONTROLLED)
      return "poorly-controlled";
    if (code == ConditionClinical.INACTIVE)
      return "inactive";
    if (code == ConditionClinical.REMISSION)
      return "remission";
    if (code == ConditionClinical.RESOLVED)
      return "resolved";
    return "?";
  }

    public String toSystem(ConditionClinical code) {
      return code.getSystem();
      }

}

