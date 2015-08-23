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

public class V3Hl7RealmEnumFactory implements EnumFactory<V3Hl7Realm> {

  public V3Hl7Realm fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("BindingRealms".equals(codeString))
      return V3Hl7Realm.BINDINGREALMS;
    if ("AffiliateRealms".equals(codeString))
      return V3Hl7Realm.AFFILIATEREALMS;
    if ("AR".equals(codeString))
      return V3Hl7Realm.AR;
    if ("AT".equals(codeString))
      return V3Hl7Realm.AT;
    if ("AU".equals(codeString))
      return V3Hl7Realm.AU;
    if ("BR".equals(codeString))
      return V3Hl7Realm.BR;
    if ("CA".equals(codeString))
      return V3Hl7Realm.CA;
    if ("CH".equals(codeString))
      return V3Hl7Realm.CH;
    if ("CL".equals(codeString))
      return V3Hl7Realm.CL;
    if ("CN".equals(codeString))
      return V3Hl7Realm.CN;
    if ("CO".equals(codeString))
      return V3Hl7Realm.CO;
    if ("CZ".equals(codeString))
      return V3Hl7Realm.CZ;
    if ("DE".equals(codeString))
      return V3Hl7Realm.DE;
    if ("DK".equals(codeString))
      return V3Hl7Realm.DK;
    if ("ES".equals(codeString))
      return V3Hl7Realm.ES;
    if ("FI".equals(codeString))
      return V3Hl7Realm.FI;
    if ("FR".equals(codeString))
      return V3Hl7Realm.FR;
    if ("GR".equals(codeString))
      return V3Hl7Realm.GR;
    if ("HR".equals(codeString))
      return V3Hl7Realm.HR;
    if ("IE".equals(codeString))
      return V3Hl7Realm.IE;
    if ("IN".equals(codeString))
      return V3Hl7Realm.IN;
    if ("IT".equals(codeString))
      return V3Hl7Realm.IT;
    if ("JP".equals(codeString))
      return V3Hl7Realm.JP;
    if ("KR".equals(codeString))
      return V3Hl7Realm.KR;
    if ("LT".equals(codeString))
      return V3Hl7Realm.LT;
    if ("MX".equals(codeString))
      return V3Hl7Realm.MX;
    if ("NL".equals(codeString))
      return V3Hl7Realm.NL;
    if ("NZ".equals(codeString))
      return V3Hl7Realm.NZ;
    if ("RO".equals(codeString))
      return V3Hl7Realm.RO;
    if ("RU".equals(codeString))
      return V3Hl7Realm.RU;
    if ("SE".equals(codeString))
      return V3Hl7Realm.SE;
    if ("SG".equals(codeString))
      return V3Hl7Realm.SG;
    if ("SOA".equals(codeString))
      return V3Hl7Realm.SOA;
    if ("TR".equals(codeString))
      return V3Hl7Realm.TR;
    if ("TW".equals(codeString))
      return V3Hl7Realm.TW;
    if ("UK".equals(codeString))
      return V3Hl7Realm.UK;
    if ("US".equals(codeString))
      return V3Hl7Realm.US;
    if ("UV".equals(codeString))
      return V3Hl7Realm.UV;
    if ("UY".equals(codeString))
      return V3Hl7Realm.UY;
    if ("C1".equals(codeString))
      return V3Hl7Realm.C1;
    if ("GB".equals(codeString))
      return V3Hl7Realm.GB;
    if ("R1".equals(codeString))
      return V3Hl7Realm.R1;
    if ("X1".equals(codeString))
      return V3Hl7Realm.X1;
    if ("NamespaceRealms".equals(codeString))
      return V3Hl7Realm.NAMESPACEREALMS;
    if ("ZZ".equals(codeString))
      return V3Hl7Realm.ZZ;
    throw new IllegalArgumentException("Unknown V3Hl7Realm code '"+codeString+"'");
  }

  public String toCode(V3Hl7Realm code) {
    if (code == V3Hl7Realm.BINDINGREALMS)
      return "BindingRealms";
    if (code == V3Hl7Realm.AFFILIATEREALMS)
      return "AffiliateRealms";
    if (code == V3Hl7Realm.AR)
      return "AR";
    if (code == V3Hl7Realm.AT)
      return "AT";
    if (code == V3Hl7Realm.AU)
      return "AU";
    if (code == V3Hl7Realm.BR)
      return "BR";
    if (code == V3Hl7Realm.CA)
      return "CA";
    if (code == V3Hl7Realm.CH)
      return "CH";
    if (code == V3Hl7Realm.CL)
      return "CL";
    if (code == V3Hl7Realm.CN)
      return "CN";
    if (code == V3Hl7Realm.CO)
      return "CO";
    if (code == V3Hl7Realm.CZ)
      return "CZ";
    if (code == V3Hl7Realm.DE)
      return "DE";
    if (code == V3Hl7Realm.DK)
      return "DK";
    if (code == V3Hl7Realm.ES)
      return "ES";
    if (code == V3Hl7Realm.FI)
      return "FI";
    if (code == V3Hl7Realm.FR)
      return "FR";
    if (code == V3Hl7Realm.GR)
      return "GR";
    if (code == V3Hl7Realm.HR)
      return "HR";
    if (code == V3Hl7Realm.IE)
      return "IE";
    if (code == V3Hl7Realm.IN)
      return "IN";
    if (code == V3Hl7Realm.IT)
      return "IT";
    if (code == V3Hl7Realm.JP)
      return "JP";
    if (code == V3Hl7Realm.KR)
      return "KR";
    if (code == V3Hl7Realm.LT)
      return "LT";
    if (code == V3Hl7Realm.MX)
      return "MX";
    if (code == V3Hl7Realm.NL)
      return "NL";
    if (code == V3Hl7Realm.NZ)
      return "NZ";
    if (code == V3Hl7Realm.RO)
      return "RO";
    if (code == V3Hl7Realm.RU)
      return "RU";
    if (code == V3Hl7Realm.SE)
      return "SE";
    if (code == V3Hl7Realm.SG)
      return "SG";
    if (code == V3Hl7Realm.SOA)
      return "SOA";
    if (code == V3Hl7Realm.TR)
      return "TR";
    if (code == V3Hl7Realm.TW)
      return "TW";
    if (code == V3Hl7Realm.UK)
      return "UK";
    if (code == V3Hl7Realm.US)
      return "US";
    if (code == V3Hl7Realm.UV)
      return "UV";
    if (code == V3Hl7Realm.UY)
      return "UY";
    if (code == V3Hl7Realm.C1)
      return "C1";
    if (code == V3Hl7Realm.GB)
      return "GB";
    if (code == V3Hl7Realm.R1)
      return "R1";
    if (code == V3Hl7Realm.X1)
      return "X1";
    if (code == V3Hl7Realm.NAMESPACEREALMS)
      return "NamespaceRealms";
    if (code == V3Hl7Realm.ZZ)
      return "ZZ";
    return "?";
  }


}

