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

public class VaccinationProtocolDoseTargetEnumFactory implements EnumFactory<VaccinationProtocolDoseTarget> {

  public VaccinationProtocolDoseTarget fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("crs".equals(codeString))
      return VaccinationProtocolDoseTarget.CRS;
    if ("dip".equals(codeString))
      return VaccinationProtocolDoseTarget.DIP;
    if ("mea".equals(codeString))
      return VaccinationProtocolDoseTarget.MEA;
    if ("mum".equals(codeString))
      return VaccinationProtocolDoseTarget.MUM;
    if ("rub".equals(codeString))
      return VaccinationProtocolDoseTarget.RUB;
    if ("tet".equals(codeString))
      return VaccinationProtocolDoseTarget.TET;
    if ("hib".equals(codeString))
      return VaccinationProtocolDoseTarget.HIB;
    if ("per".equals(codeString))
      return VaccinationProtocolDoseTarget.PER;
    if ("pol".equals(codeString))
      return VaccinationProtocolDoseTarget.POL;
    throw new IllegalArgumentException("Unknown VaccinationProtocolDoseTarget code '"+codeString+"'");
  }

  public String toCode(VaccinationProtocolDoseTarget code) {
    if (code == VaccinationProtocolDoseTarget.CRS)
      return "crs";
    if (code == VaccinationProtocolDoseTarget.DIP)
      return "dip";
    if (code == VaccinationProtocolDoseTarget.MEA)
      return "mea";
    if (code == VaccinationProtocolDoseTarget.MUM)
      return "mum";
    if (code == VaccinationProtocolDoseTarget.RUB)
      return "rub";
    if (code == VaccinationProtocolDoseTarget.TET)
      return "tet";
    if (code == VaccinationProtocolDoseTarget.HIB)
      return "hib";
    if (code == VaccinationProtocolDoseTarget.PER)
      return "per";
    if (code == VaccinationProtocolDoseTarget.POL)
      return "pol";
    return "?";
  }


}

