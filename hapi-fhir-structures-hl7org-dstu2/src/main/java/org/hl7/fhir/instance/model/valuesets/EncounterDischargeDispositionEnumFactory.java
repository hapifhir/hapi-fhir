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

public class EncounterDischargeDispositionEnumFactory implements EnumFactory<EncounterDischargeDisposition> {

  public EncounterDischargeDisposition fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("home".equals(codeString))
      return EncounterDischargeDisposition.HOME;
    if ("other-hcf".equals(codeString))
      return EncounterDischargeDisposition.OTHERHCF;
    if ("hosp".equals(codeString))
      return EncounterDischargeDisposition.HOSP;
    if ("long".equals(codeString))
      return EncounterDischargeDisposition.LONG;
    if ("aadvice".equals(codeString))
      return EncounterDischargeDisposition.AADVICE;
    if ("exp".equals(codeString))
      return EncounterDischargeDisposition.EXP;
    if ("psy".equals(codeString))
      return EncounterDischargeDisposition.PSY;
    if ("rehab".equals(codeString))
      return EncounterDischargeDisposition.REHAB;
    if ("oth".equals(codeString))
      return EncounterDischargeDisposition.OTH;
    throw new IllegalArgumentException("Unknown EncounterDischargeDisposition code '"+codeString+"'");
  }

  public String toCode(EncounterDischargeDisposition code) {
    if (code == EncounterDischargeDisposition.HOME)
      return "home";
    if (code == EncounterDischargeDisposition.OTHERHCF)
      return "other-hcf";
    if (code == EncounterDischargeDisposition.HOSP)
      return "hosp";
    if (code == EncounterDischargeDisposition.LONG)
      return "long";
    if (code == EncounterDischargeDisposition.AADVICE)
      return "aadvice";
    if (code == EncounterDischargeDisposition.EXP)
      return "exp";
    if (code == EncounterDischargeDisposition.PSY)
      return "psy";
    if (code == EncounterDischargeDisposition.REHAB)
      return "rehab";
    if (code == EncounterDischargeDisposition.OTH)
      return "oth";
    return "?";
  }


}

