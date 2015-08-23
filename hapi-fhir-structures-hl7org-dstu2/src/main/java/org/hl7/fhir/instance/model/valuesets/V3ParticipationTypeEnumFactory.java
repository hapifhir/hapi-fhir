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

public class V3ParticipationTypeEnumFactory implements EnumFactory<V3ParticipationType> {

  public V3ParticipationType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("PART".equals(codeString))
      return V3ParticipationType.PART;
    if ("_ParticipationAncillary".equals(codeString))
      return V3ParticipationType._PARTICIPATIONANCILLARY;
    if ("ADM".equals(codeString))
      return V3ParticipationType.ADM;
    if ("ATND".equals(codeString))
      return V3ParticipationType.ATND;
    if ("CALLBCK".equals(codeString))
      return V3ParticipationType.CALLBCK;
    if ("CON".equals(codeString))
      return V3ParticipationType.CON;
    if ("DIS".equals(codeString))
      return V3ParticipationType.DIS;
    if ("ESC".equals(codeString))
      return V3ParticipationType.ESC;
    if ("REF".equals(codeString))
      return V3ParticipationType.REF;
    if ("_ParticipationInformationGenerator".equals(codeString))
      return V3ParticipationType._PARTICIPATIONINFORMATIONGENERATOR;
    if ("AUT".equals(codeString))
      return V3ParticipationType.AUT;
    if ("INF".equals(codeString))
      return V3ParticipationType.INF;
    if ("TRANS".equals(codeString))
      return V3ParticipationType.TRANS;
    if ("ENT".equals(codeString))
      return V3ParticipationType.ENT;
    if ("WIT".equals(codeString))
      return V3ParticipationType.WIT;
    if ("CST".equals(codeString))
      return V3ParticipationType.CST;
    if ("DIR".equals(codeString))
      return V3ParticipationType.DIR;
    if ("ALY".equals(codeString))
      return V3ParticipationType.ALY;
    if ("BBY".equals(codeString))
      return V3ParticipationType.BBY;
    if ("CAT".equals(codeString))
      return V3ParticipationType.CAT;
    if ("CSM".equals(codeString))
      return V3ParticipationType.CSM;
    if ("DEV".equals(codeString))
      return V3ParticipationType.DEV;
    if ("NRD".equals(codeString))
      return V3ParticipationType.NRD;
    if ("RDV".equals(codeString))
      return V3ParticipationType.RDV;
    if ("DON".equals(codeString))
      return V3ParticipationType.DON;
    if ("EXPAGNT".equals(codeString))
      return V3ParticipationType.EXPAGNT;
    if ("EXPART".equals(codeString))
      return V3ParticipationType.EXPART;
    if ("EXPTRGT".equals(codeString))
      return V3ParticipationType.EXPTRGT;
    if ("EXSRC".equals(codeString))
      return V3ParticipationType.EXSRC;
    if ("PRD".equals(codeString))
      return V3ParticipationType.PRD;
    if ("SBJ".equals(codeString))
      return V3ParticipationType.SBJ;
    if ("SPC".equals(codeString))
      return V3ParticipationType.SPC;
    if ("IND".equals(codeString))
      return V3ParticipationType.IND;
    if ("BEN".equals(codeString))
      return V3ParticipationType.BEN;
    if ("CAGNT".equals(codeString))
      return V3ParticipationType.CAGNT;
    if ("COV".equals(codeString))
      return V3ParticipationType.COV;
    if ("GUAR".equals(codeString))
      return V3ParticipationType.GUAR;
    if ("HLD".equals(codeString))
      return V3ParticipationType.HLD;
    if ("RCT".equals(codeString))
      return V3ParticipationType.RCT;
    if ("RCV".equals(codeString))
      return V3ParticipationType.RCV;
    if ("IRCP".equals(codeString))
      return V3ParticipationType.IRCP;
    if ("NOT".equals(codeString))
      return V3ParticipationType.NOT;
    if ("PRCP".equals(codeString))
      return V3ParticipationType.PRCP;
    if ("REFB".equals(codeString))
      return V3ParticipationType.REFB;
    if ("REFT".equals(codeString))
      return V3ParticipationType.REFT;
    if ("TRC".equals(codeString))
      return V3ParticipationType.TRC;
    if ("LOC".equals(codeString))
      return V3ParticipationType.LOC;
    if ("DST".equals(codeString))
      return V3ParticipationType.DST;
    if ("ELOC".equals(codeString))
      return V3ParticipationType.ELOC;
    if ("ORG".equals(codeString))
      return V3ParticipationType.ORG;
    if ("RML".equals(codeString))
      return V3ParticipationType.RML;
    if ("VIA".equals(codeString))
      return V3ParticipationType.VIA;
    if ("PRF".equals(codeString))
      return V3ParticipationType.PRF;
    if ("DIST".equals(codeString))
      return V3ParticipationType.DIST;
    if ("PPRF".equals(codeString))
      return V3ParticipationType.PPRF;
    if ("SPRF".equals(codeString))
      return V3ParticipationType.SPRF;
    if ("RESP".equals(codeString))
      return V3ParticipationType.RESP;
    if ("VRF".equals(codeString))
      return V3ParticipationType.VRF;
    if ("AUTHEN".equals(codeString))
      return V3ParticipationType.AUTHEN;
    if ("LA".equals(codeString))
      return V3ParticipationType.LA;
    throw new IllegalArgumentException("Unknown V3ParticipationType code '"+codeString+"'");
  }

  public String toCode(V3ParticipationType code) {
    if (code == V3ParticipationType.PART)
      return "PART";
    if (code == V3ParticipationType._PARTICIPATIONANCILLARY)
      return "_ParticipationAncillary";
    if (code == V3ParticipationType.ADM)
      return "ADM";
    if (code == V3ParticipationType.ATND)
      return "ATND";
    if (code == V3ParticipationType.CALLBCK)
      return "CALLBCK";
    if (code == V3ParticipationType.CON)
      return "CON";
    if (code == V3ParticipationType.DIS)
      return "DIS";
    if (code == V3ParticipationType.ESC)
      return "ESC";
    if (code == V3ParticipationType.REF)
      return "REF";
    if (code == V3ParticipationType._PARTICIPATIONINFORMATIONGENERATOR)
      return "_ParticipationInformationGenerator";
    if (code == V3ParticipationType.AUT)
      return "AUT";
    if (code == V3ParticipationType.INF)
      return "INF";
    if (code == V3ParticipationType.TRANS)
      return "TRANS";
    if (code == V3ParticipationType.ENT)
      return "ENT";
    if (code == V3ParticipationType.WIT)
      return "WIT";
    if (code == V3ParticipationType.CST)
      return "CST";
    if (code == V3ParticipationType.DIR)
      return "DIR";
    if (code == V3ParticipationType.ALY)
      return "ALY";
    if (code == V3ParticipationType.BBY)
      return "BBY";
    if (code == V3ParticipationType.CAT)
      return "CAT";
    if (code == V3ParticipationType.CSM)
      return "CSM";
    if (code == V3ParticipationType.DEV)
      return "DEV";
    if (code == V3ParticipationType.NRD)
      return "NRD";
    if (code == V3ParticipationType.RDV)
      return "RDV";
    if (code == V3ParticipationType.DON)
      return "DON";
    if (code == V3ParticipationType.EXPAGNT)
      return "EXPAGNT";
    if (code == V3ParticipationType.EXPART)
      return "EXPART";
    if (code == V3ParticipationType.EXPTRGT)
      return "EXPTRGT";
    if (code == V3ParticipationType.EXSRC)
      return "EXSRC";
    if (code == V3ParticipationType.PRD)
      return "PRD";
    if (code == V3ParticipationType.SBJ)
      return "SBJ";
    if (code == V3ParticipationType.SPC)
      return "SPC";
    if (code == V3ParticipationType.IND)
      return "IND";
    if (code == V3ParticipationType.BEN)
      return "BEN";
    if (code == V3ParticipationType.CAGNT)
      return "CAGNT";
    if (code == V3ParticipationType.COV)
      return "COV";
    if (code == V3ParticipationType.GUAR)
      return "GUAR";
    if (code == V3ParticipationType.HLD)
      return "HLD";
    if (code == V3ParticipationType.RCT)
      return "RCT";
    if (code == V3ParticipationType.RCV)
      return "RCV";
    if (code == V3ParticipationType.IRCP)
      return "IRCP";
    if (code == V3ParticipationType.NOT)
      return "NOT";
    if (code == V3ParticipationType.PRCP)
      return "PRCP";
    if (code == V3ParticipationType.REFB)
      return "REFB";
    if (code == V3ParticipationType.REFT)
      return "REFT";
    if (code == V3ParticipationType.TRC)
      return "TRC";
    if (code == V3ParticipationType.LOC)
      return "LOC";
    if (code == V3ParticipationType.DST)
      return "DST";
    if (code == V3ParticipationType.ELOC)
      return "ELOC";
    if (code == V3ParticipationType.ORG)
      return "ORG";
    if (code == V3ParticipationType.RML)
      return "RML";
    if (code == V3ParticipationType.VIA)
      return "VIA";
    if (code == V3ParticipationType.PRF)
      return "PRF";
    if (code == V3ParticipationType.DIST)
      return "DIST";
    if (code == V3ParticipationType.PPRF)
      return "PPRF";
    if (code == V3ParticipationType.SPRF)
      return "SPRF";
    if (code == V3ParticipationType.RESP)
      return "RESP";
    if (code == V3ParticipationType.VRF)
      return "VRF";
    if (code == V3ParticipationType.AUTHEN)
      return "AUTHEN";
    if (code == V3ParticipationType.LA)
      return "LA";
    return "?";
  }


}

