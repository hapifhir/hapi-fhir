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

public class MedRequestStatusReasonEnumFactory implements EnumFactory<MedRequestStatusReason> {

  public MedRequestStatusReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("altchoice".equals(codeString))
      return MedRequestStatusReason.ALTCHOICE;
    if ("clarif".equals(codeString))
      return MedRequestStatusReason.CLARIF;
    if ("drughigh".equals(codeString))
      return MedRequestStatusReason.DRUGHIGH;
    if ("hospadm".equals(codeString))
      return MedRequestStatusReason.HOSPADM;
    if ("labint".equals(codeString))
      return MedRequestStatusReason.LABINT;
    if ("non-avail".equals(codeString))
      return MedRequestStatusReason.NONAVAIL;
    if ("preg".equals(codeString))
      return MedRequestStatusReason.PREG;
    if ("salg".equals(codeString))
      return MedRequestStatusReason.SALG;
    if ("sddi".equals(codeString))
      return MedRequestStatusReason.SDDI;
    if ("sdupther".equals(codeString))
      return MedRequestStatusReason.SDUPTHER;
    if ("sintol".equals(codeString))
      return MedRequestStatusReason.SINTOL;
    if ("surg".equals(codeString))
      return MedRequestStatusReason.SURG;
    if ("washout".equals(codeString))
      return MedRequestStatusReason.WASHOUT;
    throw new IllegalArgumentException("Unknown MedRequestStatusReason code '"+codeString+"'");
  }

  public String toCode(MedRequestStatusReason code) {
    if (code == MedRequestStatusReason.ALTCHOICE)
      return "altchoice";
    if (code == MedRequestStatusReason.CLARIF)
      return "clarif";
    if (code == MedRequestStatusReason.DRUGHIGH)
      return "drughigh";
    if (code == MedRequestStatusReason.HOSPADM)
      return "hospadm";
    if (code == MedRequestStatusReason.LABINT)
      return "labint";
    if (code == MedRequestStatusReason.NONAVAIL)
      return "non-avail";
    if (code == MedRequestStatusReason.PREG)
      return "preg";
    if (code == MedRequestStatusReason.SALG)
      return "salg";
    if (code == MedRequestStatusReason.SDDI)
      return "sddi";
    if (code == MedRequestStatusReason.SDUPTHER)
      return "sdupther";
    if (code == MedRequestStatusReason.SINTOL)
      return "sintol";
    if (code == MedRequestStatusReason.SURG)
      return "surg";
    if (code == MedRequestStatusReason.WASHOUT)
      return "washout";
    return "?";
  }

    public String toSystem(MedRequestStatusReason code) {
      return code.getSystem();
      }

}

