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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.r4.model.EnumFactory;

public class V3Hl7PublishingDomainEnumFactory implements EnumFactory<V3Hl7PublishingDomain> {

  public V3Hl7PublishingDomain fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AB".equals(codeString))
      return V3Hl7PublishingDomain.AB;
    if ("AI".equals(codeString))
      return V3Hl7PublishingDomain.AI;
    if ("AL".equals(codeString))
      return V3Hl7PublishingDomain.AL;
    if ("BB".equals(codeString))
      return V3Hl7PublishingDomain.BB;
    if ("CD".equals(codeString))
      return V3Hl7PublishingDomain.CD;
    if ("CG".equals(codeString))
      return V3Hl7PublishingDomain.CG;
    if ("CI".equals(codeString))
      return V3Hl7PublishingDomain.CI;
    if ("CO".equals(codeString))
      return V3Hl7PublishingDomain.CO;
    if ("CP".equals(codeString))
      return V3Hl7PublishingDomain.CP;
    if ("CR".equals(codeString))
      return V3Hl7PublishingDomain.CR;
    if ("CS".equals(codeString))
      return V3Hl7PublishingDomain.CS;
    if ("CT".equals(codeString))
      return V3Hl7PublishingDomain.CT;
    if ("DD".equals(codeString))
      return V3Hl7PublishingDomain.DD;
    if ("DI".equals(codeString))
      return V3Hl7PublishingDomain.DI;
    if ("DS".equals(codeString))
      return V3Hl7PublishingDomain.DS;
    if ("EM".equals(codeString))
      return V3Hl7PublishingDomain.EM;
    if ("II".equals(codeString))
      return V3Hl7PublishingDomain.II;
    if ("IZ".equals(codeString))
      return V3Hl7PublishingDomain.IZ;
    if ("LB".equals(codeString))
      return V3Hl7PublishingDomain.LB;
    if ("ME".equals(codeString))
      return V3Hl7PublishingDomain.ME;
    if ("MI".equals(codeString))
      return V3Hl7PublishingDomain.MI;
    if ("MM".equals(codeString))
      return V3Hl7PublishingDomain.MM;
    if ("MR".equals(codeString))
      return V3Hl7PublishingDomain.MR;
    if ("MT".equals(codeString))
      return V3Hl7PublishingDomain.MT;
    if ("OB".equals(codeString))
      return V3Hl7PublishingDomain.OB;
    if ("OO".equals(codeString))
      return V3Hl7PublishingDomain.OO;
    if ("OR".equals(codeString))
      return V3Hl7PublishingDomain.OR;
    if ("PA".equals(codeString))
      return V3Hl7PublishingDomain.PA;
    if ("PC".equals(codeString))
      return V3Hl7PublishingDomain.PC;
    if ("PH".equals(codeString))
      return V3Hl7PublishingDomain.PH;
    if ("PM".equals(codeString))
      return V3Hl7PublishingDomain.PM;
    if ("QI".equals(codeString))
      return V3Hl7PublishingDomain.QI;
    if ("QM".equals(codeString))
      return V3Hl7PublishingDomain.QM;
    if ("RG".equals(codeString))
      return V3Hl7PublishingDomain.RG;
    if ("RI".equals(codeString))
      return V3Hl7PublishingDomain.RI;
    if ("RP".equals(codeString))
      return V3Hl7PublishingDomain.RP;
    if ("RR".equals(codeString))
      return V3Hl7PublishingDomain.RR;
    if ("RT".equals(codeString))
      return V3Hl7PublishingDomain.RT;
    if ("RX".equals(codeString))
      return V3Hl7PublishingDomain.RX;
    if ("SC".equals(codeString))
      return V3Hl7PublishingDomain.SC;
    if ("SP".equals(codeString))
      return V3Hl7PublishingDomain.SP;
    if ("TD".equals(codeString))
      return V3Hl7PublishingDomain.TD;
    throw new IllegalArgumentException("Unknown V3Hl7PublishingDomain code '"+codeString+"'");
  }

  public String toCode(V3Hl7PublishingDomain code) {
    if (code == V3Hl7PublishingDomain.AB)
      return "AB";
    if (code == V3Hl7PublishingDomain.AI)
      return "AI";
    if (code == V3Hl7PublishingDomain.AL)
      return "AL";
    if (code == V3Hl7PublishingDomain.BB)
      return "BB";
    if (code == V3Hl7PublishingDomain.CD)
      return "CD";
    if (code == V3Hl7PublishingDomain.CG)
      return "CG";
    if (code == V3Hl7PublishingDomain.CI)
      return "CI";
    if (code == V3Hl7PublishingDomain.CO)
      return "CO";
    if (code == V3Hl7PublishingDomain.CP)
      return "CP";
    if (code == V3Hl7PublishingDomain.CR)
      return "CR";
    if (code == V3Hl7PublishingDomain.CS)
      return "CS";
    if (code == V3Hl7PublishingDomain.CT)
      return "CT";
    if (code == V3Hl7PublishingDomain.DD)
      return "DD";
    if (code == V3Hl7PublishingDomain.DI)
      return "DI";
    if (code == V3Hl7PublishingDomain.DS)
      return "DS";
    if (code == V3Hl7PublishingDomain.EM)
      return "EM";
    if (code == V3Hl7PublishingDomain.II)
      return "II";
    if (code == V3Hl7PublishingDomain.IZ)
      return "IZ";
    if (code == V3Hl7PublishingDomain.LB)
      return "LB";
    if (code == V3Hl7PublishingDomain.ME)
      return "ME";
    if (code == V3Hl7PublishingDomain.MI)
      return "MI";
    if (code == V3Hl7PublishingDomain.MM)
      return "MM";
    if (code == V3Hl7PublishingDomain.MR)
      return "MR";
    if (code == V3Hl7PublishingDomain.MT)
      return "MT";
    if (code == V3Hl7PublishingDomain.OB)
      return "OB";
    if (code == V3Hl7PublishingDomain.OO)
      return "OO";
    if (code == V3Hl7PublishingDomain.OR)
      return "OR";
    if (code == V3Hl7PublishingDomain.PA)
      return "PA";
    if (code == V3Hl7PublishingDomain.PC)
      return "PC";
    if (code == V3Hl7PublishingDomain.PH)
      return "PH";
    if (code == V3Hl7PublishingDomain.PM)
      return "PM";
    if (code == V3Hl7PublishingDomain.QI)
      return "QI";
    if (code == V3Hl7PublishingDomain.QM)
      return "QM";
    if (code == V3Hl7PublishingDomain.RG)
      return "RG";
    if (code == V3Hl7PublishingDomain.RI)
      return "RI";
    if (code == V3Hl7PublishingDomain.RP)
      return "RP";
    if (code == V3Hl7PublishingDomain.RR)
      return "RR";
    if (code == V3Hl7PublishingDomain.RT)
      return "RT";
    if (code == V3Hl7PublishingDomain.RX)
      return "RX";
    if (code == V3Hl7PublishingDomain.SC)
      return "SC";
    if (code == V3Hl7PublishingDomain.SP)
      return "SP";
    if (code == V3Hl7PublishingDomain.TD)
      return "TD";
    return "?";
  }

    public String toSystem(V3Hl7PublishingDomain code) {
      return code.getSystem();
      }

}

