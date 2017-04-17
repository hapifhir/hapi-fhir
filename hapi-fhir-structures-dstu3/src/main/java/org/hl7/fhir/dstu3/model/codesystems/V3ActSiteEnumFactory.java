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

public class V3ActSiteEnumFactory implements EnumFactory<V3ActSite> {

  public V3ActSite fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_HumanActSite".equals(codeString))
      return V3ActSite._HUMANACTSITE;
    if ("_HumanSubstanceAdministrationSite".equals(codeString))
      return V3ActSite._HUMANSUBSTANCEADMINISTRATIONSITE;
    if ("BE".equals(codeString))
      return V3ActSite.BE;
    if ("BN".equals(codeString))
      return V3ActSite.BN;
    if ("BU".equals(codeString))
      return V3ActSite.BU;
    if ("LA".equals(codeString))
      return V3ActSite.LA;
    if ("LAC".equals(codeString))
      return V3ActSite.LAC;
    if ("LACF".equals(codeString))
      return V3ActSite.LACF;
    if ("LD".equals(codeString))
      return V3ActSite.LD;
    if ("LE".equals(codeString))
      return V3ActSite.LE;
    if ("LEJ".equals(codeString))
      return V3ActSite.LEJ;
    if ("LF".equals(codeString))
      return V3ActSite.LF;
    if ("LG".equals(codeString))
      return V3ActSite.LG;
    if ("LH".equals(codeString))
      return V3ActSite.LH;
    if ("LIJ".equals(codeString))
      return V3ActSite.LIJ;
    if ("LLAQ".equals(codeString))
      return V3ActSite.LLAQ;
    if ("LLFA".equals(codeString))
      return V3ActSite.LLFA;
    if ("LMFA".equals(codeString))
      return V3ActSite.LMFA;
    if ("LN".equals(codeString))
      return V3ActSite.LN;
    if ("LPC".equals(codeString))
      return V3ActSite.LPC;
    if ("LSC".equals(codeString))
      return V3ActSite.LSC;
    if ("LT".equals(codeString))
      return V3ActSite.LT;
    if ("LUA".equals(codeString))
      return V3ActSite.LUA;
    if ("LUAQ".equals(codeString))
      return V3ActSite.LUAQ;
    if ("LUFA".equals(codeString))
      return V3ActSite.LUFA;
    if ("LVG".equals(codeString))
      return V3ActSite.LVG;
    if ("LVL".equals(codeString))
      return V3ActSite.LVL;
    if ("OD".equals(codeString))
      return V3ActSite.OD;
    if ("OS".equals(codeString))
      return V3ActSite.OS;
    if ("OU".equals(codeString))
      return V3ActSite.OU;
    if ("PA".equals(codeString))
      return V3ActSite.PA;
    if ("PERIN".equals(codeString))
      return V3ActSite.PERIN;
    if ("RA".equals(codeString))
      return V3ActSite.RA;
    if ("RAC".equals(codeString))
      return V3ActSite.RAC;
    if ("RACF".equals(codeString))
      return V3ActSite.RACF;
    if ("RD".equals(codeString))
      return V3ActSite.RD;
    if ("RE".equals(codeString))
      return V3ActSite.RE;
    if ("REJ".equals(codeString))
      return V3ActSite.REJ;
    if ("RF".equals(codeString))
      return V3ActSite.RF;
    if ("RG".equals(codeString))
      return V3ActSite.RG;
    if ("RH".equals(codeString))
      return V3ActSite.RH;
    if ("RIJ".equals(codeString))
      return V3ActSite.RIJ;
    if ("RLAQ".equals(codeString))
      return V3ActSite.RLAQ;
    if ("RLFA".equals(codeString))
      return V3ActSite.RLFA;
    if ("RMFA".equals(codeString))
      return V3ActSite.RMFA;
    if ("RN".equals(codeString))
      return V3ActSite.RN;
    if ("RPC".equals(codeString))
      return V3ActSite.RPC;
    if ("RSC".equals(codeString))
      return V3ActSite.RSC;
    if ("RT".equals(codeString))
      return V3ActSite.RT;
    if ("RUA".equals(codeString))
      return V3ActSite.RUA;
    if ("RUAQ".equals(codeString))
      return V3ActSite.RUAQ;
    if ("RUFA".equals(codeString))
      return V3ActSite.RUFA;
    if ("RVG".equals(codeString))
      return V3ActSite.RVG;
    if ("RVL".equals(codeString))
      return V3ActSite.RVL;
    throw new IllegalArgumentException("Unknown V3ActSite code '"+codeString+"'");
  }

  public String toCode(V3ActSite code) {
    if (code == V3ActSite._HUMANACTSITE)
      return "_HumanActSite";
    if (code == V3ActSite._HUMANSUBSTANCEADMINISTRATIONSITE)
      return "_HumanSubstanceAdministrationSite";
    if (code == V3ActSite.BE)
      return "BE";
    if (code == V3ActSite.BN)
      return "BN";
    if (code == V3ActSite.BU)
      return "BU";
    if (code == V3ActSite.LA)
      return "LA";
    if (code == V3ActSite.LAC)
      return "LAC";
    if (code == V3ActSite.LACF)
      return "LACF";
    if (code == V3ActSite.LD)
      return "LD";
    if (code == V3ActSite.LE)
      return "LE";
    if (code == V3ActSite.LEJ)
      return "LEJ";
    if (code == V3ActSite.LF)
      return "LF";
    if (code == V3ActSite.LG)
      return "LG";
    if (code == V3ActSite.LH)
      return "LH";
    if (code == V3ActSite.LIJ)
      return "LIJ";
    if (code == V3ActSite.LLAQ)
      return "LLAQ";
    if (code == V3ActSite.LLFA)
      return "LLFA";
    if (code == V3ActSite.LMFA)
      return "LMFA";
    if (code == V3ActSite.LN)
      return "LN";
    if (code == V3ActSite.LPC)
      return "LPC";
    if (code == V3ActSite.LSC)
      return "LSC";
    if (code == V3ActSite.LT)
      return "LT";
    if (code == V3ActSite.LUA)
      return "LUA";
    if (code == V3ActSite.LUAQ)
      return "LUAQ";
    if (code == V3ActSite.LUFA)
      return "LUFA";
    if (code == V3ActSite.LVG)
      return "LVG";
    if (code == V3ActSite.LVL)
      return "LVL";
    if (code == V3ActSite.OD)
      return "OD";
    if (code == V3ActSite.OS)
      return "OS";
    if (code == V3ActSite.OU)
      return "OU";
    if (code == V3ActSite.PA)
      return "PA";
    if (code == V3ActSite.PERIN)
      return "PERIN";
    if (code == V3ActSite.RA)
      return "RA";
    if (code == V3ActSite.RAC)
      return "RAC";
    if (code == V3ActSite.RACF)
      return "RACF";
    if (code == V3ActSite.RD)
      return "RD";
    if (code == V3ActSite.RE)
      return "RE";
    if (code == V3ActSite.REJ)
      return "REJ";
    if (code == V3ActSite.RF)
      return "RF";
    if (code == V3ActSite.RG)
      return "RG";
    if (code == V3ActSite.RH)
      return "RH";
    if (code == V3ActSite.RIJ)
      return "RIJ";
    if (code == V3ActSite.RLAQ)
      return "RLAQ";
    if (code == V3ActSite.RLFA)
      return "RLFA";
    if (code == V3ActSite.RMFA)
      return "RMFA";
    if (code == V3ActSite.RN)
      return "RN";
    if (code == V3ActSite.RPC)
      return "RPC";
    if (code == V3ActSite.RSC)
      return "RSC";
    if (code == V3ActSite.RT)
      return "RT";
    if (code == V3ActSite.RUA)
      return "RUA";
    if (code == V3ActSite.RUAQ)
      return "RUAQ";
    if (code == V3ActSite.RUFA)
      return "RUFA";
    if (code == V3ActSite.RVG)
      return "RVG";
    if (code == V3ActSite.RVL)
      return "RVL";
    return "?";
  }

    public String toSystem(V3ActSite code) {
      return code.getSystem();
      }

}

