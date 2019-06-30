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

public class MedicationdispenseStatusReasonEnumFactory implements EnumFactory<MedicationdispenseStatusReason> {

  public MedicationdispenseStatusReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("frr01".equals(codeString))
      return MedicationdispenseStatusReason.FRR01;
    if ("frr02".equals(codeString))
      return MedicationdispenseStatusReason.FRR02;
    if ("frr03".equals(codeString))
      return MedicationdispenseStatusReason.FRR03;
    if ("frr04".equals(codeString))
      return MedicationdispenseStatusReason.FRR04;
    if ("frr05".equals(codeString))
      return MedicationdispenseStatusReason.FRR05;
    if ("frr06".equals(codeString))
      return MedicationdispenseStatusReason.FRR06;
    if ("altchoice".equals(codeString))
      return MedicationdispenseStatusReason.ALTCHOICE;
    if ("clarif".equals(codeString))
      return MedicationdispenseStatusReason.CLARIF;
    if ("drughigh".equals(codeString))
      return MedicationdispenseStatusReason.DRUGHIGH;
    if ("hospadm".equals(codeString))
      return MedicationdispenseStatusReason.HOSPADM;
    if ("labint".equals(codeString))
      return MedicationdispenseStatusReason.LABINT;
    if ("non-avail".equals(codeString))
      return MedicationdispenseStatusReason.NONAVAIL;
    if ("preg".equals(codeString))
      return MedicationdispenseStatusReason.PREG;
    if ("saig".equals(codeString))
      return MedicationdispenseStatusReason.SAIG;
    if ("sddi".equals(codeString))
      return MedicationdispenseStatusReason.SDDI;
    if ("sdupther".equals(codeString))
      return MedicationdispenseStatusReason.SDUPTHER;
    if ("sintol".equals(codeString))
      return MedicationdispenseStatusReason.SINTOL;
    if ("surg".equals(codeString))
      return MedicationdispenseStatusReason.SURG;
    if ("washout".equals(codeString))
      return MedicationdispenseStatusReason.WASHOUT;
    if ("outofstock".equals(codeString))
      return MedicationdispenseStatusReason.OUTOFSTOCK;
    if ("offmarket".equals(codeString))
      return MedicationdispenseStatusReason.OFFMARKET;
    throw new IllegalArgumentException("Unknown MedicationdispenseStatusReason code '"+codeString+"'");
  }

  public String toCode(MedicationdispenseStatusReason code) {
    if (code == MedicationdispenseStatusReason.FRR01)
      return "frr01";
    if (code == MedicationdispenseStatusReason.FRR02)
      return "frr02";
    if (code == MedicationdispenseStatusReason.FRR03)
      return "frr03";
    if (code == MedicationdispenseStatusReason.FRR04)
      return "frr04";
    if (code == MedicationdispenseStatusReason.FRR05)
      return "frr05";
    if (code == MedicationdispenseStatusReason.FRR06)
      return "frr06";
    if (code == MedicationdispenseStatusReason.ALTCHOICE)
      return "altchoice";
    if (code == MedicationdispenseStatusReason.CLARIF)
      return "clarif";
    if (code == MedicationdispenseStatusReason.DRUGHIGH)
      return "drughigh";
    if (code == MedicationdispenseStatusReason.HOSPADM)
      return "hospadm";
    if (code == MedicationdispenseStatusReason.LABINT)
      return "labint";
    if (code == MedicationdispenseStatusReason.NONAVAIL)
      return "non-avail";
    if (code == MedicationdispenseStatusReason.PREG)
      return "preg";
    if (code == MedicationdispenseStatusReason.SAIG)
      return "saig";
    if (code == MedicationdispenseStatusReason.SDDI)
      return "sddi";
    if (code == MedicationdispenseStatusReason.SDUPTHER)
      return "sdupther";
    if (code == MedicationdispenseStatusReason.SINTOL)
      return "sintol";
    if (code == MedicationdispenseStatusReason.SURG)
      return "surg";
    if (code == MedicationdispenseStatusReason.WASHOUT)
      return "washout";
    if (code == MedicationdispenseStatusReason.OUTOFSTOCK)
      return "outofstock";
    if (code == MedicationdispenseStatusReason.OFFMARKET)
      return "offmarket";
    return "?";
  }

    public String toSystem(MedicationdispenseStatusReason code) {
      return code.getSystem();
      }

}

