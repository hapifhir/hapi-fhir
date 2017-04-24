package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.dstu3.model.EnumFactory;

public class MedicationRequestStatusEnumFactory implements EnumFactory<MedicationRequestStatus> {

  public MedicationRequestStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return MedicationRequestStatus.ACTIVE;
    if ("on-hold".equals(codeString))
      return MedicationRequestStatus.ONHOLD;
    if ("cancelled".equals(codeString))
      return MedicationRequestStatus.CANCELLED;
    if ("completed".equals(codeString))
      return MedicationRequestStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return MedicationRequestStatus.ENTEREDINERROR;
    if ("stopped".equals(codeString))
      return MedicationRequestStatus.STOPPED;
    if ("draft".equals(codeString))
      return MedicationRequestStatus.DRAFT;
    if ("unknown".equals(codeString))
      return MedicationRequestStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown MedicationRequestStatus code '"+codeString+"'");
  }

  public String toCode(MedicationRequestStatus code) {
    if (code == MedicationRequestStatus.ACTIVE)
      return "active";
    if (code == MedicationRequestStatus.ONHOLD)
      return "on-hold";
    if (code == MedicationRequestStatus.CANCELLED)
      return "cancelled";
    if (code == MedicationRequestStatus.COMPLETED)
      return "completed";
    if (code == MedicationRequestStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == MedicationRequestStatus.STOPPED)
      return "stopped";
    if (code == MedicationRequestStatus.DRAFT)
      return "draft";
    if (code == MedicationRequestStatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(MedicationRequestStatus code) {
      return code.getSystem();
      }

}

