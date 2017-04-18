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

public class V3EntityClassEnumFactory implements EnumFactory<V3EntityClass> {

  public V3EntityClass fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ENT".equals(codeString))
      return V3EntityClass.ENT;
    if ("HCE".equals(codeString))
      return V3EntityClass.HCE;
    if ("LIV".equals(codeString))
      return V3EntityClass.LIV;
    if ("NLIV".equals(codeString))
      return V3EntityClass.NLIV;
    if ("ANM".equals(codeString))
      return V3EntityClass.ANM;
    if ("MIC".equals(codeString))
      return V3EntityClass.MIC;
    if ("PLNT".equals(codeString))
      return V3EntityClass.PLNT;
    if ("PSN".equals(codeString))
      return V3EntityClass.PSN;
    if ("MAT".equals(codeString))
      return V3EntityClass.MAT;
    if ("CHEM".equals(codeString))
      return V3EntityClass.CHEM;
    if ("FOOD".equals(codeString))
      return V3EntityClass.FOOD;
    if ("MMAT".equals(codeString))
      return V3EntityClass.MMAT;
    if ("CONT".equals(codeString))
      return V3EntityClass.CONT;
    if ("HOLD".equals(codeString))
      return V3EntityClass.HOLD;
    if ("DEV".equals(codeString))
      return V3EntityClass.DEV;
    if ("CER".equals(codeString))
      return V3EntityClass.CER;
    if ("MODDV".equals(codeString))
      return V3EntityClass.MODDV;
    if ("ORG".equals(codeString))
      return V3EntityClass.ORG;
    if ("PUB".equals(codeString))
      return V3EntityClass.PUB;
    if ("STATE".equals(codeString))
      return V3EntityClass.STATE;
    if ("NAT".equals(codeString))
      return V3EntityClass.NAT;
    if ("PLC".equals(codeString))
      return V3EntityClass.PLC;
    if ("CITY".equals(codeString))
      return V3EntityClass.CITY;
    if ("COUNTRY".equals(codeString))
      return V3EntityClass.COUNTRY;
    if ("COUNTY".equals(codeString))
      return V3EntityClass.COUNTY;
    if ("PROVINCE".equals(codeString))
      return V3EntityClass.PROVINCE;
    if ("RGRP".equals(codeString))
      return V3EntityClass.RGRP;
    throw new IllegalArgumentException("Unknown V3EntityClass code '"+codeString+"'");
  }

  public String toCode(V3EntityClass code) {
    if (code == V3EntityClass.ENT)
      return "ENT";
    if (code == V3EntityClass.HCE)
      return "HCE";
    if (code == V3EntityClass.LIV)
      return "LIV";
    if (code == V3EntityClass.NLIV)
      return "NLIV";
    if (code == V3EntityClass.ANM)
      return "ANM";
    if (code == V3EntityClass.MIC)
      return "MIC";
    if (code == V3EntityClass.PLNT)
      return "PLNT";
    if (code == V3EntityClass.PSN)
      return "PSN";
    if (code == V3EntityClass.MAT)
      return "MAT";
    if (code == V3EntityClass.CHEM)
      return "CHEM";
    if (code == V3EntityClass.FOOD)
      return "FOOD";
    if (code == V3EntityClass.MMAT)
      return "MMAT";
    if (code == V3EntityClass.CONT)
      return "CONT";
    if (code == V3EntityClass.HOLD)
      return "HOLD";
    if (code == V3EntityClass.DEV)
      return "DEV";
    if (code == V3EntityClass.CER)
      return "CER";
    if (code == V3EntityClass.MODDV)
      return "MODDV";
    if (code == V3EntityClass.ORG)
      return "ORG";
    if (code == V3EntityClass.PUB)
      return "PUB";
    if (code == V3EntityClass.STATE)
      return "STATE";
    if (code == V3EntityClass.NAT)
      return "NAT";
    if (code == V3EntityClass.PLC)
      return "PLC";
    if (code == V3EntityClass.CITY)
      return "CITY";
    if (code == V3EntityClass.COUNTRY)
      return "COUNTRY";
    if (code == V3EntityClass.COUNTY)
      return "COUNTY";
    if (code == V3EntityClass.PROVINCE)
      return "PROVINCE";
    if (code == V3EntityClass.RGRP)
      return "RGRP";
    return "?";
  }

    public String toSystem(V3EntityClass code) {
      return code.getSystem();
      }

}

