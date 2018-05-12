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

public class MedDispenseStatusReasonEnumFactory implements EnumFactory<MedDispenseStatusReason> {

  public MedDispenseStatusReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("frr01".equals(codeString))
      return MedDispenseStatusReason.FRR01;
    if ("frr02".equals(codeString))
      return MedDispenseStatusReason.FRR02;
    if ("frr03".equals(codeString))
      return MedDispenseStatusReason.FRR03;
    if ("frr04".equals(codeString))
      return MedDispenseStatusReason.FRR04;
    if ("frr05".equals(codeString))
      return MedDispenseStatusReason.FRR05;
    if ("frr06".equals(codeString))
      return MedDispenseStatusReason.FRR06;
    if ("altchoice".equals(codeString))
      return MedDispenseStatusReason.ALTCHOICE;
    if ("clarif".equals(codeString))
      return MedDispenseStatusReason.CLARIF;
    if ("drughigh".equals(codeString))
      return MedDispenseStatusReason.DRUGHIGH;
    if ("hospadm".equals(codeString))
      return MedDispenseStatusReason.HOSPADM;
    if ("labint".equals(codeString))
      return MedDispenseStatusReason.LABINT;
    if ("non-avail".equals(codeString))
      return MedDispenseStatusReason.NONAVAIL;
    if ("preg".equals(codeString))
      return MedDispenseStatusReason.PREG;
    if ("salg".equals(codeString))
      return MedDispenseStatusReason.SALG;
    if ("sddi".equals(codeString))
      return MedDispenseStatusReason.SDDI;
    if ("sdupther".equals(codeString))
      return MedDispenseStatusReason.SDUPTHER;
    if ("sintol".equals(codeString))
      return MedDispenseStatusReason.SINTOL;
    if ("surg".equals(codeString))
      return MedDispenseStatusReason.SURG;
    if ("washout".equals(codeString))
      return MedDispenseStatusReason.WASHOUT;
    throw new IllegalArgumentException("Unknown MedDispenseStatusReason code '"+codeString+"'");
  }

  public String toCode(MedDispenseStatusReason code) {
    if (code == MedDispenseStatusReason.FRR01)
      return "frr01";
    if (code == MedDispenseStatusReason.FRR02)
      return "frr02";
    if (code == MedDispenseStatusReason.FRR03)
      return "frr03";
    if (code == MedDispenseStatusReason.FRR04)
      return "frr04";
    if (code == MedDispenseStatusReason.FRR05)
      return "frr05";
    if (code == MedDispenseStatusReason.FRR06)
      return "frr06";
    if (code == MedDispenseStatusReason.ALTCHOICE)
      return "altchoice";
    if (code == MedDispenseStatusReason.CLARIF)
      return "clarif";
    if (code == MedDispenseStatusReason.DRUGHIGH)
      return "drughigh";
    if (code == MedDispenseStatusReason.HOSPADM)
      return "hospadm";
    if (code == MedDispenseStatusReason.LABINT)
      return "labint";
    if (code == MedDispenseStatusReason.NONAVAIL)
      return "non-avail";
    if (code == MedDispenseStatusReason.PREG)
      return "preg";
    if (code == MedDispenseStatusReason.SALG)
      return "salg";
    if (code == MedDispenseStatusReason.SDDI)
      return "sddi";
    if (code == MedDispenseStatusReason.SDUPTHER)
      return "sdupther";
    if (code == MedDispenseStatusReason.SINTOL)
      return "sintol";
    if (code == MedDispenseStatusReason.SURG)
      return "surg";
    if (code == MedDispenseStatusReason.WASHOUT)
      return "washout";
    return "?";
  }

    public String toSystem(MedDispenseStatusReason code) {
      return code.getSystem();
      }

}

