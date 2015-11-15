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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


import org.hl7.fhir.instance.model.EnumFactory;

public class LocationPhysicalTypeEnumFactory implements EnumFactory<LocationPhysicalType> {

  public LocationPhysicalType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("bu".equals(codeString))
      return LocationPhysicalType.BU;
    if ("wi".equals(codeString))
      return LocationPhysicalType.WI;
    if ("lvl".equals(codeString))
      return LocationPhysicalType.LVL;
    if ("co".equals(codeString))
      return LocationPhysicalType.CO;
    if ("ro".equals(codeString))
      return LocationPhysicalType.RO;
    if ("bd".equals(codeString))
      return LocationPhysicalType.BD;
    if ("ve".equals(codeString))
      return LocationPhysicalType.VE;
    if ("ho".equals(codeString))
      return LocationPhysicalType.HO;
    if ("ca".equals(codeString))
      return LocationPhysicalType.CA;
    if ("rd".equals(codeString))
      return LocationPhysicalType.RD;
    if ("jdn".equals(codeString))
      return LocationPhysicalType.JDN;
    if ("area".equals(codeString))
      return LocationPhysicalType.AREA;
    throw new IllegalArgumentException("Unknown LocationPhysicalType code '"+codeString+"'");
  }

  public String toCode(LocationPhysicalType code) {
    if (code == LocationPhysicalType.BU)
      return "bu";
    if (code == LocationPhysicalType.WI)
      return "wi";
    if (code == LocationPhysicalType.LVL)
      return "lvl";
    if (code == LocationPhysicalType.CO)
      return "co";
    if (code == LocationPhysicalType.RO)
      return "ro";
    if (code == LocationPhysicalType.BD)
      return "bd";
    if (code == LocationPhysicalType.VE)
      return "ve";
    if (code == LocationPhysicalType.HO)
      return "ho";
    if (code == LocationPhysicalType.CA)
      return "ca";
    if (code == LocationPhysicalType.RD)
      return "rd";
    if (code == LocationPhysicalType.JDN)
      return "jdn";
    if (code == LocationPhysicalType.AREA)
      return "area";
    return "?";
  }


}

