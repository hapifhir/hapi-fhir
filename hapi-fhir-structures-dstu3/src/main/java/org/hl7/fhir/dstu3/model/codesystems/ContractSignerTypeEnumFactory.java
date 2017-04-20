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

public class ContractSignerTypeEnumFactory implements EnumFactory<ContractSignerType> {

  public ContractSignerType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AMENDER".equals(codeString))
      return ContractSignerType.AMENDER;
    if ("AUTHN".equals(codeString))
      return ContractSignerType.AUTHN;
    if ("AUT".equals(codeString))
      return ContractSignerType.AUT;
    if ("AFFL".equals(codeString))
      return ContractSignerType.AFFL;
    if ("AGNT".equals(codeString))
      return ContractSignerType.AGNT;
    if ("ASSIGNED".equals(codeString))
      return ContractSignerType.ASSIGNED;
    if ("CIT".equals(codeString))
      return ContractSignerType.CIT;
    if ("CLAIMANT".equals(codeString))
      return ContractSignerType.CLAIMANT;
    if ("COAUTH".equals(codeString))
      return ContractSignerType.COAUTH;
    if ("CONSENTER".equals(codeString))
      return ContractSignerType.CONSENTER;
    if ("CONSWIT".equals(codeString))
      return ContractSignerType.CONSWIT;
    if ("CONT".equals(codeString))
      return ContractSignerType.CONT;
    if ("COPART".equals(codeString))
      return ContractSignerType.COPART;
    if ("COVPTY".equals(codeString))
      return ContractSignerType.COVPTY;
    if ("DELEGATEE".equals(codeString))
      return ContractSignerType.DELEGATEE;
    if ("delegator".equals(codeString))
      return ContractSignerType.DELEGATOR;
    if ("DEPEND".equals(codeString))
      return ContractSignerType.DEPEND;
    if ("DPOWATT".equals(codeString))
      return ContractSignerType.DPOWATT;
    if ("EMGCON".equals(codeString))
      return ContractSignerType.EMGCON;
    if ("EVTWIT".equals(codeString))
      return ContractSignerType.EVTWIT;
    if ("EXCEST".equals(codeString))
      return ContractSignerType.EXCEST;
    if ("GRANTEE".equals(codeString))
      return ContractSignerType.GRANTEE;
    if ("GRANTOR".equals(codeString))
      return ContractSignerType.GRANTOR;
    if ("GUAR".equals(codeString))
      return ContractSignerType.GUAR;
    if ("GUARD".equals(codeString))
      return ContractSignerType.GUARD;
    if ("GUADLTM".equals(codeString))
      return ContractSignerType.GUADLTM;
    if ("INF".equals(codeString))
      return ContractSignerType.INF;
    if ("INTPRT".equals(codeString))
      return ContractSignerType.INTPRT;
    if ("INSBJ".equals(codeString))
      return ContractSignerType.INSBJ;
    if ("HPOWATT".equals(codeString))
      return ContractSignerType.HPOWATT;
    if ("HPROV".equals(codeString))
      return ContractSignerType.HPROV;
    if ("LEGAUTHN".equals(codeString))
      return ContractSignerType.LEGAUTHN;
    if ("NMDINS".equals(codeString))
      return ContractSignerType.NMDINS;
    if ("NOK".equals(codeString))
      return ContractSignerType.NOK;
    if ("NOTARY".equals(codeString))
      return ContractSignerType.NOTARY;
    if ("PAT".equals(codeString))
      return ContractSignerType.PAT;
    if ("POWATT".equals(codeString))
      return ContractSignerType.POWATT;
    if ("PRIMAUTH".equals(codeString))
      return ContractSignerType.PRIMAUTH;
    if ("PRIRECIP".equals(codeString))
      return ContractSignerType.PRIRECIP;
    if ("RECIP".equals(codeString))
      return ContractSignerType.RECIP;
    if ("RESPRSN".equals(codeString))
      return ContractSignerType.RESPRSN;
    if ("REVIEWER".equals(codeString))
      return ContractSignerType.REVIEWER;
    if ("TRANS".equals(codeString))
      return ContractSignerType.TRANS;
    if ("SOURCE".equals(codeString))
      return ContractSignerType.SOURCE;
    if ("SPOWATT".equals(codeString))
      return ContractSignerType.SPOWATT;
    if ("VALID".equals(codeString))
      return ContractSignerType.VALID;
    if ("VERF".equals(codeString))
      return ContractSignerType.VERF;
    if ("WIT".equals(codeString))
      return ContractSignerType.WIT;
    throw new IllegalArgumentException("Unknown ContractSignerType code '"+codeString+"'");
  }

  public String toCode(ContractSignerType code) {
    if (code == ContractSignerType.AMENDER)
      return "AMENDER";
    if (code == ContractSignerType.AUTHN)
      return "AUTHN";
    if (code == ContractSignerType.AUT)
      return "AUT";
    if (code == ContractSignerType.AFFL)
      return "AFFL";
    if (code == ContractSignerType.AGNT)
      return "AGNT";
    if (code == ContractSignerType.ASSIGNED)
      return "ASSIGNED";
    if (code == ContractSignerType.CIT)
      return "CIT";
    if (code == ContractSignerType.CLAIMANT)
      return "CLAIMANT";
    if (code == ContractSignerType.COAUTH)
      return "COAUTH";
    if (code == ContractSignerType.CONSENTER)
      return "CONSENTER";
    if (code == ContractSignerType.CONSWIT)
      return "CONSWIT";
    if (code == ContractSignerType.CONT)
      return "CONT";
    if (code == ContractSignerType.COPART)
      return "COPART";
    if (code == ContractSignerType.COVPTY)
      return "COVPTY";
    if (code == ContractSignerType.DELEGATEE)
      return "DELEGATEE";
    if (code == ContractSignerType.DELEGATOR)
      return "delegator";
    if (code == ContractSignerType.DEPEND)
      return "DEPEND";
    if (code == ContractSignerType.DPOWATT)
      return "DPOWATT";
    if (code == ContractSignerType.EMGCON)
      return "EMGCON";
    if (code == ContractSignerType.EVTWIT)
      return "EVTWIT";
    if (code == ContractSignerType.EXCEST)
      return "EXCEST";
    if (code == ContractSignerType.GRANTEE)
      return "GRANTEE";
    if (code == ContractSignerType.GRANTOR)
      return "GRANTOR";
    if (code == ContractSignerType.GUAR)
      return "GUAR";
    if (code == ContractSignerType.GUARD)
      return "GUARD";
    if (code == ContractSignerType.GUADLTM)
      return "GUADLTM";
    if (code == ContractSignerType.INF)
      return "INF";
    if (code == ContractSignerType.INTPRT)
      return "INTPRT";
    if (code == ContractSignerType.INSBJ)
      return "INSBJ";
    if (code == ContractSignerType.HPOWATT)
      return "HPOWATT";
    if (code == ContractSignerType.HPROV)
      return "HPROV";
    if (code == ContractSignerType.LEGAUTHN)
      return "LEGAUTHN";
    if (code == ContractSignerType.NMDINS)
      return "NMDINS";
    if (code == ContractSignerType.NOK)
      return "NOK";
    if (code == ContractSignerType.NOTARY)
      return "NOTARY";
    if (code == ContractSignerType.PAT)
      return "PAT";
    if (code == ContractSignerType.POWATT)
      return "POWATT";
    if (code == ContractSignerType.PRIMAUTH)
      return "PRIMAUTH";
    if (code == ContractSignerType.PRIRECIP)
      return "PRIRECIP";
    if (code == ContractSignerType.RECIP)
      return "RECIP";
    if (code == ContractSignerType.RESPRSN)
      return "RESPRSN";
    if (code == ContractSignerType.REVIEWER)
      return "REVIEWER";
    if (code == ContractSignerType.TRANS)
      return "TRANS";
    if (code == ContractSignerType.SOURCE)
      return "SOURCE";
    if (code == ContractSignerType.SPOWATT)
      return "SPOWATT";
    if (code == ContractSignerType.VALID)
      return "VALID";
    if (code == ContractSignerType.VERF)
      return "VERF";
    if (code == ContractSignerType.WIT)
      return "WIT";
    return "?";
  }

    public String toSystem(ContractSignerType code) {
      return code.getSystem();
      }

}

