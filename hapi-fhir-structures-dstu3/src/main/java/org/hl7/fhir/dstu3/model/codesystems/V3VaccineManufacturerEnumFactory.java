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

public class V3VaccineManufacturerEnumFactory implements EnumFactory<V3VaccineManufacturer> {

  public V3VaccineManufacturer fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AB".equals(codeString))
      return V3VaccineManufacturer.AB;
    if ("AD".equals(codeString))
      return V3VaccineManufacturer.AD;
    if ("ALP".equals(codeString))
      return V3VaccineManufacturer.ALP;
    if ("AR".equals(codeString))
      return V3VaccineManufacturer.AR;
    if ("AVI".equals(codeString))
      return V3VaccineManufacturer.AVI;
    if ("BA".equals(codeString))
      return V3VaccineManufacturer.BA;
    if ("BAY".equals(codeString))
      return V3VaccineManufacturer.BAY;
    if ("BP".equals(codeString))
      return V3VaccineManufacturer.BP;
    if ("BPC".equals(codeString))
      return V3VaccineManufacturer.BPC;
    if ("CEN".equals(codeString))
      return V3VaccineManufacturer.CEN;
    if ("CHI".equals(codeString))
      return V3VaccineManufacturer.CHI;
    if ("CON".equals(codeString))
      return V3VaccineManufacturer.CON;
    if ("EVN".equals(codeString))
      return V3VaccineManufacturer.EVN;
    if ("GRE".equals(codeString))
      return V3VaccineManufacturer.GRE;
    if ("IAG".equals(codeString))
      return V3VaccineManufacturer.IAG;
    if ("IM".equals(codeString))
      return V3VaccineManufacturer.IM;
    if ("IUS".equals(codeString))
      return V3VaccineManufacturer.IUS;
    if ("JPN".equals(codeString))
      return V3VaccineManufacturer.JPN;
    if ("KGC".equals(codeString))
      return V3VaccineManufacturer.KGC;
    if ("LED".equals(codeString))
      return V3VaccineManufacturer.LED;
    if ("MA".equals(codeString))
      return V3VaccineManufacturer.MA;
    if ("MED".equals(codeString))
      return V3VaccineManufacturer.MED;
    if ("MIL".equals(codeString))
      return V3VaccineManufacturer.MIL;
    if ("MIP".equals(codeString))
      return V3VaccineManufacturer.MIP;
    if ("MSD".equals(codeString))
      return V3VaccineManufacturer.MSD;
    if ("NAB".equals(codeString))
      return V3VaccineManufacturer.NAB;
    if ("NAV".equals(codeString))
      return V3VaccineManufacturer.NAV;
    if ("NOV".equals(codeString))
      return V3VaccineManufacturer.NOV;
    if ("NYB".equals(codeString))
      return V3VaccineManufacturer.NYB;
    if ("ORT".equals(codeString))
      return V3VaccineManufacturer.ORT;
    if ("OTC".equals(codeString))
      return V3VaccineManufacturer.OTC;
    if ("PD".equals(codeString))
      return V3VaccineManufacturer.PD;
    if ("PMC".equals(codeString))
      return V3VaccineManufacturer.PMC;
    if ("PRX".equals(codeString))
      return V3VaccineManufacturer.PRX;
    if ("SCL".equals(codeString))
      return V3VaccineManufacturer.SCL;
    if ("SI".equals(codeString))
      return V3VaccineManufacturer.SI;
    if ("SKB".equals(codeString))
      return V3VaccineManufacturer.SKB;
    if ("USA".equals(codeString))
      return V3VaccineManufacturer.USA;
    if ("WA".equals(codeString))
      return V3VaccineManufacturer.WA;
    if ("WAL".equals(codeString))
      return V3VaccineManufacturer.WAL;
    throw new IllegalArgumentException("Unknown V3VaccineManufacturer code '"+codeString+"'");
  }

  public String toCode(V3VaccineManufacturer code) {
    if (code == V3VaccineManufacturer.AB)
      return "AB";
    if (code == V3VaccineManufacturer.AD)
      return "AD";
    if (code == V3VaccineManufacturer.ALP)
      return "ALP";
    if (code == V3VaccineManufacturer.AR)
      return "AR";
    if (code == V3VaccineManufacturer.AVI)
      return "AVI";
    if (code == V3VaccineManufacturer.BA)
      return "BA";
    if (code == V3VaccineManufacturer.BAY)
      return "BAY";
    if (code == V3VaccineManufacturer.BP)
      return "BP";
    if (code == V3VaccineManufacturer.BPC)
      return "BPC";
    if (code == V3VaccineManufacturer.CEN)
      return "CEN";
    if (code == V3VaccineManufacturer.CHI)
      return "CHI";
    if (code == V3VaccineManufacturer.CON)
      return "CON";
    if (code == V3VaccineManufacturer.EVN)
      return "EVN";
    if (code == V3VaccineManufacturer.GRE)
      return "GRE";
    if (code == V3VaccineManufacturer.IAG)
      return "IAG";
    if (code == V3VaccineManufacturer.IM)
      return "IM";
    if (code == V3VaccineManufacturer.IUS)
      return "IUS";
    if (code == V3VaccineManufacturer.JPN)
      return "JPN";
    if (code == V3VaccineManufacturer.KGC)
      return "KGC";
    if (code == V3VaccineManufacturer.LED)
      return "LED";
    if (code == V3VaccineManufacturer.MA)
      return "MA";
    if (code == V3VaccineManufacturer.MED)
      return "MED";
    if (code == V3VaccineManufacturer.MIL)
      return "MIL";
    if (code == V3VaccineManufacturer.MIP)
      return "MIP";
    if (code == V3VaccineManufacturer.MSD)
      return "MSD";
    if (code == V3VaccineManufacturer.NAB)
      return "NAB";
    if (code == V3VaccineManufacturer.NAV)
      return "NAV";
    if (code == V3VaccineManufacturer.NOV)
      return "NOV";
    if (code == V3VaccineManufacturer.NYB)
      return "NYB";
    if (code == V3VaccineManufacturer.ORT)
      return "ORT";
    if (code == V3VaccineManufacturer.OTC)
      return "OTC";
    if (code == V3VaccineManufacturer.PD)
      return "PD";
    if (code == V3VaccineManufacturer.PMC)
      return "PMC";
    if (code == V3VaccineManufacturer.PRX)
      return "PRX";
    if (code == V3VaccineManufacturer.SCL)
      return "SCL";
    if (code == V3VaccineManufacturer.SI)
      return "SI";
    if (code == V3VaccineManufacturer.SKB)
      return "SKB";
    if (code == V3VaccineManufacturer.USA)
      return "USA";
    if (code == V3VaccineManufacturer.WA)
      return "WA";
    if (code == V3VaccineManufacturer.WAL)
      return "WAL";
    return "?";
  }

    public String toSystem(V3VaccineManufacturer code) {
      return code.getSystem();
      }

}

