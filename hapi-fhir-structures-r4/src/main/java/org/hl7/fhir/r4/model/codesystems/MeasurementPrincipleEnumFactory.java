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

// Generated on Mon, Jul 2, 2018 20:32-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class MeasurementPrincipleEnumFactory implements EnumFactory<MeasurementPrinciple> {

  public MeasurementPrinciple fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("other".equals(codeString))
      return MeasurementPrinciple.OTHER;
    if ("chemical".equals(codeString))
      return MeasurementPrinciple.CHEMICAL;
    if ("electrical".equals(codeString))
      return MeasurementPrinciple.ELECTRICAL;
    if ("impedance".equals(codeString))
      return MeasurementPrinciple.IMPEDANCE;
    if ("nuclear".equals(codeString))
      return MeasurementPrinciple.NUCLEAR;
    if ("optical".equals(codeString))
      return MeasurementPrinciple.OPTICAL;
    if ("thermal".equals(codeString))
      return MeasurementPrinciple.THERMAL;
    if ("biological".equals(codeString))
      return MeasurementPrinciple.BIOLOGICAL;
    if ("mechanical".equals(codeString))
      return MeasurementPrinciple.MECHANICAL;
    if ("acoustical".equals(codeString))
      return MeasurementPrinciple.ACOUSTICAL;
    if ("manual".equals(codeString))
      return MeasurementPrinciple.MANUAL;
    throw new IllegalArgumentException("Unknown MeasurementPrinciple code '"+codeString+"'");
  }

  public String toCode(MeasurementPrinciple code) {
    if (code == MeasurementPrinciple.OTHER)
      return "other";
    if (code == MeasurementPrinciple.CHEMICAL)
      return "chemical";
    if (code == MeasurementPrinciple.ELECTRICAL)
      return "electrical";
    if (code == MeasurementPrinciple.IMPEDANCE)
      return "impedance";
    if (code == MeasurementPrinciple.NUCLEAR)
      return "nuclear";
    if (code == MeasurementPrinciple.OPTICAL)
      return "optical";
    if (code == MeasurementPrinciple.THERMAL)
      return "thermal";
    if (code == MeasurementPrinciple.BIOLOGICAL)
      return "biological";
    if (code == MeasurementPrinciple.MECHANICAL)
      return "mechanical";
    if (code == MeasurementPrinciple.ACOUSTICAL)
      return "acoustical";
    if (code == MeasurementPrinciple.MANUAL)
      return "manual";
    return "?";
  }

    public String toSystem(MeasurementPrinciple code) {
      return code.getSystem();
      }

}

