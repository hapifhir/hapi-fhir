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

public class V3EntityRiskEnumFactory implements EnumFactory<V3EntityRisk> {

  public V3EntityRisk fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AGG".equals(codeString))
      return V3EntityRisk.AGG;
    if ("BIO".equals(codeString))
      return V3EntityRisk.BIO;
    if ("COR".equals(codeString))
      return V3EntityRisk.COR;
    if ("ESC".equals(codeString))
      return V3EntityRisk.ESC;
    if ("IFL".equals(codeString))
      return V3EntityRisk.IFL;
    if ("EXP".equals(codeString))
      return V3EntityRisk.EXP;
    if ("INF".equals(codeString))
      return V3EntityRisk.INF;
    if ("BHZ".equals(codeString))
      return V3EntityRisk.BHZ;
    if ("INJ".equals(codeString))
      return V3EntityRisk.INJ;
    if ("POI".equals(codeString))
      return V3EntityRisk.POI;
    if ("RAD".equals(codeString))
      return V3EntityRisk.RAD;
    throw new IllegalArgumentException("Unknown V3EntityRisk code '"+codeString+"'");
  }

  public String toCode(V3EntityRisk code) {
    if (code == V3EntityRisk.AGG)
      return "AGG";
    if (code == V3EntityRisk.BIO)
      return "BIO";
    if (code == V3EntityRisk.COR)
      return "COR";
    if (code == V3EntityRisk.ESC)
      return "ESC";
    if (code == V3EntityRisk.IFL)
      return "IFL";
    if (code == V3EntityRisk.EXP)
      return "EXP";
    if (code == V3EntityRisk.INF)
      return "INF";
    if (code == V3EntityRisk.BHZ)
      return "BHZ";
    if (code == V3EntityRisk.INJ)
      return "INJ";
    if (code == V3EntityRisk.POI)
      return "POI";
    if (code == V3EntityRisk.RAD)
      return "RAD";
    return "?";
  }

    public String toSystem(V3EntityRisk code) {
      return code.getSystem();
      }

}

