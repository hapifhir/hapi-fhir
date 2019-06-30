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

public class MedicationrequestStatusEnumFactory implements EnumFactory<MedicationrequestStatus> {

  public MedicationrequestStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return MedicationrequestStatus.ACTIVE;
    if ("on-hold".equals(codeString))
      return MedicationrequestStatus.ONHOLD;
    if ("cancelled".equals(codeString))
      return MedicationrequestStatus.CANCELLED;
    if ("completed".equals(codeString))
      return MedicationrequestStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return MedicationrequestStatus.ENTEREDINERROR;
    if ("stopped".equals(codeString))
      return MedicationrequestStatus.STOPPED;
    if ("draft".equals(codeString))
      return MedicationrequestStatus.DRAFT;
    if ("unknown".equals(codeString))
      return MedicationrequestStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown MedicationrequestStatus code '"+codeString+"'");
  }

  public String toCode(MedicationrequestStatus code) {
    if (code == MedicationrequestStatus.ACTIVE)
      return "active";
    if (code == MedicationrequestStatus.ONHOLD)
      return "on-hold";
    if (code == MedicationrequestStatus.CANCELLED)
      return "cancelled";
    if (code == MedicationrequestStatus.COMPLETED)
      return "completed";
    if (code == MedicationrequestStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == MedicationrequestStatus.STOPPED)
      return "stopped";
    if (code == MedicationrequestStatus.DRAFT)
      return "draft";
    if (code == MedicationrequestStatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(MedicationrequestStatus code) {
      return code.getSystem();
      }

}

