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

public class V3AddressPartTypeEnumFactory implements EnumFactory<V3AddressPartType> {

  public V3AddressPartType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ADL".equals(codeString))
      return V3AddressPartType.ADL;
    if ("AL".equals(codeString))
      return V3AddressPartType.AL;
    if ("DAL".equals(codeString))
      return V3AddressPartType.DAL;
    if ("SAL".equals(codeString))
      return V3AddressPartType.SAL;
    if ("BNN".equals(codeString))
      return V3AddressPartType.BNN;
    if ("BNR".equals(codeString))
      return V3AddressPartType.BNR;
    if ("BNS".equals(codeString))
      return V3AddressPartType.BNS;
    if ("CAR".equals(codeString))
      return V3AddressPartType.CAR;
    if ("CEN".equals(codeString))
      return V3AddressPartType.CEN;
    if ("CNT".equals(codeString))
      return V3AddressPartType.CNT;
    if ("CPA".equals(codeString))
      return V3AddressPartType.CPA;
    if ("CTY".equals(codeString))
      return V3AddressPartType.CTY;
    if ("DEL".equals(codeString))
      return V3AddressPartType.DEL;
    if ("DINST".equals(codeString))
      return V3AddressPartType.DINST;
    if ("DINSTA".equals(codeString))
      return V3AddressPartType.DINSTA;
    if ("DINSTQ".equals(codeString))
      return V3AddressPartType.DINSTQ;
    if ("DIR".equals(codeString))
      return V3AddressPartType.DIR;
    if ("DMOD".equals(codeString))
      return V3AddressPartType.DMOD;
    if ("DMODID".equals(codeString))
      return V3AddressPartType.DMODID;
    if ("DPID".equals(codeString))
      return V3AddressPartType.DPID;
    if ("INT".equals(codeString))
      return V3AddressPartType.INT;
    if ("POB".equals(codeString))
      return V3AddressPartType.POB;
    if ("PRE".equals(codeString))
      return V3AddressPartType.PRE;
    if ("STA".equals(codeString))
      return V3AddressPartType.STA;
    if ("STB".equals(codeString))
      return V3AddressPartType.STB;
    if ("STR".equals(codeString))
      return V3AddressPartType.STR;
    if ("STTYP".equals(codeString))
      return V3AddressPartType.STTYP;
    if ("UNID".equals(codeString))
      return V3AddressPartType.UNID;
    if ("UNIT".equals(codeString))
      return V3AddressPartType.UNIT;
    if ("ZIP".equals(codeString))
      return V3AddressPartType.ZIP;
    throw new IllegalArgumentException("Unknown V3AddressPartType code '"+codeString+"'");
  }

  public String toCode(V3AddressPartType code) {
    if (code == V3AddressPartType.ADL)
      return "ADL";
    if (code == V3AddressPartType.AL)
      return "AL";
    if (code == V3AddressPartType.DAL)
      return "DAL";
    if (code == V3AddressPartType.SAL)
      return "SAL";
    if (code == V3AddressPartType.BNN)
      return "BNN";
    if (code == V3AddressPartType.BNR)
      return "BNR";
    if (code == V3AddressPartType.BNS)
      return "BNS";
    if (code == V3AddressPartType.CAR)
      return "CAR";
    if (code == V3AddressPartType.CEN)
      return "CEN";
    if (code == V3AddressPartType.CNT)
      return "CNT";
    if (code == V3AddressPartType.CPA)
      return "CPA";
    if (code == V3AddressPartType.CTY)
      return "CTY";
    if (code == V3AddressPartType.DEL)
      return "DEL";
    if (code == V3AddressPartType.DINST)
      return "DINST";
    if (code == V3AddressPartType.DINSTA)
      return "DINSTA";
    if (code == V3AddressPartType.DINSTQ)
      return "DINSTQ";
    if (code == V3AddressPartType.DIR)
      return "DIR";
    if (code == V3AddressPartType.DMOD)
      return "DMOD";
    if (code == V3AddressPartType.DMODID)
      return "DMODID";
    if (code == V3AddressPartType.DPID)
      return "DPID";
    if (code == V3AddressPartType.INT)
      return "INT";
    if (code == V3AddressPartType.POB)
      return "POB";
    if (code == V3AddressPartType.PRE)
      return "PRE";
    if (code == V3AddressPartType.STA)
      return "STA";
    if (code == V3AddressPartType.STB)
      return "STB";
    if (code == V3AddressPartType.STR)
      return "STR";
    if (code == V3AddressPartType.STTYP)
      return "STTYP";
    if (code == V3AddressPartType.UNID)
      return "UNID";
    if (code == V3AddressPartType.UNIT)
      return "UNIT";
    if (code == V3AddressPartType.ZIP)
      return "ZIP";
    return "?";
  }

    public String toSystem(V3AddressPartType code) {
      return code.getSystem();
      }

}

