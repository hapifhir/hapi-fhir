package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class PatientContactRelationshipEnumFactory implements EnumFactory<PatientContactRelationship> {

  public PatientContactRelationship fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("emergency".equals(codeString))
      return PatientContactRelationship.EMERGENCY;
    if ("family".equals(codeString))
      return PatientContactRelationship.FAMILY;
    if ("guardian".equals(codeString))
      return PatientContactRelationship.GUARDIAN;
    if ("friend".equals(codeString))
      return PatientContactRelationship.FRIEND;
    if ("partner".equals(codeString))
      return PatientContactRelationship.PARTNER;
    if ("work".equals(codeString))
      return PatientContactRelationship.WORK;
    if ("caregiver".equals(codeString))
      return PatientContactRelationship.CAREGIVER;
    if ("agent".equals(codeString))
      return PatientContactRelationship.AGENT;
    if ("guarantor".equals(codeString))
      return PatientContactRelationship.GUARANTOR;
    if ("owner".equals(codeString))
      return PatientContactRelationship.OWNER;
    if ("parent".equals(codeString))
      return PatientContactRelationship.PARENT;
    throw new IllegalArgumentException("Unknown PatientContactRelationship code '"+codeString+"'");
  }

  public String toCode(PatientContactRelationship code) {
    if (code == PatientContactRelationship.EMERGENCY)
      return "emergency";
    if (code == PatientContactRelationship.FAMILY)
      return "family";
    if (code == PatientContactRelationship.GUARDIAN)
      return "guardian";
    if (code == PatientContactRelationship.FRIEND)
      return "friend";
    if (code == PatientContactRelationship.PARTNER)
      return "partner";
    if (code == PatientContactRelationship.WORK)
      return "work";
    if (code == PatientContactRelationship.CAREGIVER)
      return "caregiver";
    if (code == PatientContactRelationship.AGENT)
      return "agent";
    if (code == PatientContactRelationship.GUARANTOR)
      return "guarantor";
    if (code == PatientContactRelationship.OWNER)
      return "owner";
    if (code == PatientContactRelationship.PARENT)
      return "parent";
    return "?";
  }


}

