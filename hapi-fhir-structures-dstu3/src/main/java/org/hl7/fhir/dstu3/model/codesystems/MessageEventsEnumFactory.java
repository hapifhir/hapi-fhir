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

public class MessageEventsEnumFactory implements EnumFactory<MessageEvents> {

  public MessageEvents fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("CodeSystem-expand".equals(codeString))
      return MessageEvents.CODESYSTEMEXPAND;
    if ("MedicationAdministration-Complete".equals(codeString))
      return MessageEvents.MEDICATIONADMINISTRATIONCOMPLETE;
    if ("MedicationAdministration-Nullification".equals(codeString))
      return MessageEvents.MEDICATIONADMINISTRATIONNULLIFICATION;
    if ("MedicationAdministration-Recording".equals(codeString))
      return MessageEvents.MEDICATIONADMINISTRATIONRECORDING;
    if ("MedicationAdministration-Update".equals(codeString))
      return MessageEvents.MEDICATIONADMINISTRATIONUPDATE;
    if ("admin-notify".equals(codeString))
      return MessageEvents.ADMINNOTIFY;
    if ("communication-request".equals(codeString))
      return MessageEvents.COMMUNICATIONREQUEST;
    if ("diagnosticreport-provide".equals(codeString))
      return MessageEvents.DIAGNOSTICREPORTPROVIDE;
    if ("observation-provide".equals(codeString))
      return MessageEvents.OBSERVATIONPROVIDE;
    if ("patient-link".equals(codeString))
      return MessageEvents.PATIENTLINK;
    if ("patient-unlink".equals(codeString))
      return MessageEvents.PATIENTUNLINK;
    if ("valueset-expand".equals(codeString))
      return MessageEvents.VALUESETEXPAND;
    throw new IllegalArgumentException("Unknown MessageEvents code '"+codeString+"'");
  }

  public String toCode(MessageEvents code) {
    if (code == MessageEvents.CODESYSTEMEXPAND)
      return "CodeSystem-expand";
    if (code == MessageEvents.MEDICATIONADMINISTRATIONCOMPLETE)
      return "MedicationAdministration-Complete";
    if (code == MessageEvents.MEDICATIONADMINISTRATIONNULLIFICATION)
      return "MedicationAdministration-Nullification";
    if (code == MessageEvents.MEDICATIONADMINISTRATIONRECORDING)
      return "MedicationAdministration-Recording";
    if (code == MessageEvents.MEDICATIONADMINISTRATIONUPDATE)
      return "MedicationAdministration-Update";
    if (code == MessageEvents.ADMINNOTIFY)
      return "admin-notify";
    if (code == MessageEvents.COMMUNICATIONREQUEST)
      return "communication-request";
    if (code == MessageEvents.DIAGNOSTICREPORTPROVIDE)
      return "diagnosticreport-provide";
    if (code == MessageEvents.OBSERVATIONPROVIDE)
      return "observation-provide";
    if (code == MessageEvents.PATIENTLINK)
      return "patient-link";
    if (code == MessageEvents.PATIENTUNLINK)
      return "patient-unlink";
    if (code == MessageEvents.VALUESETEXPAND)
      return "valueset-expand";
    return "?";
  }

    public String toSystem(MessageEvents code) {
      return code.getSystem();
      }

}

