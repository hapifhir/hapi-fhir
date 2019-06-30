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

public class MedicationrequestStatusReasonEnumFactory implements EnumFactory<MedicationrequestStatusReason> {

  public MedicationrequestStatusReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("altchoice".equals(codeString))
      return MedicationrequestStatusReason.ALTCHOICE;
    if ("clarif".equals(codeString))
      return MedicationrequestStatusReason.CLARIF;
    if ("drughigh".equals(codeString))
      return MedicationrequestStatusReason.DRUGHIGH;
    if ("hospadm".equals(codeString))
      return MedicationrequestStatusReason.HOSPADM;
    if ("labint".equals(codeString))
      return MedicationrequestStatusReason.LABINT;
    if ("non-avail".equals(codeString))
      return MedicationrequestStatusReason.NONAVAIL;
    if ("preg".equals(codeString))
      return MedicationrequestStatusReason.PREG;
    if ("salg".equals(codeString))
      return MedicationrequestStatusReason.SALG;
    if ("sddi".equals(codeString))
      return MedicationrequestStatusReason.SDDI;
    if ("sdupther".equals(codeString))
      return MedicationrequestStatusReason.SDUPTHER;
    if ("sintol".equals(codeString))
      return MedicationrequestStatusReason.SINTOL;
    if ("surg".equals(codeString))
      return MedicationrequestStatusReason.SURG;
    if ("washout".equals(codeString))
      return MedicationrequestStatusReason.WASHOUT;
    throw new IllegalArgumentException("Unknown MedicationrequestStatusReason code '"+codeString+"'");
  }

  public String toCode(MedicationrequestStatusReason code) {
    if (code == MedicationrequestStatusReason.ALTCHOICE)
      return "altchoice";
    if (code == MedicationrequestStatusReason.CLARIF)
      return "clarif";
    if (code == MedicationrequestStatusReason.DRUGHIGH)
      return "drughigh";
    if (code == MedicationrequestStatusReason.HOSPADM)
      return "hospadm";
    if (code == MedicationrequestStatusReason.LABINT)
      return "labint";
    if (code == MedicationrequestStatusReason.NONAVAIL)
      return "non-avail";
    if (code == MedicationrequestStatusReason.PREG)
      return "preg";
    if (code == MedicationrequestStatusReason.SALG)
      return "salg";
    if (code == MedicationrequestStatusReason.SDDI)
      return "sddi";
    if (code == MedicationrequestStatusReason.SDUPTHER)
      return "sdupther";
    if (code == MedicationrequestStatusReason.SINTOL)
      return "sintol";
    if (code == MedicationrequestStatusReason.SURG)
      return "surg";
    if (code == MedicationrequestStatusReason.WASHOUT)
      return "washout";
    return "?";
  }

    public String toSystem(MedicationrequestStatusReason code) {
      return code.getSystem();
      }

}

