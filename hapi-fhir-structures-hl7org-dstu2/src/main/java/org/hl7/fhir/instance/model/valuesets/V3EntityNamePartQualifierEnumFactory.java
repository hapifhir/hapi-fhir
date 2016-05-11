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

public class V3EntityNamePartQualifierEnumFactory implements EnumFactory<V3EntityNamePartQualifier> {

  public V3EntityNamePartQualifier fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_OrganizationNamePartQualifier".equals(codeString))
      return V3EntityNamePartQualifier._ORGANIZATIONNAMEPARTQUALIFIER;
    if ("AC".equals(codeString))
      return V3EntityNamePartQualifier.AC;
    if ("AD".equals(codeString))
      return V3EntityNamePartQualifier.AD;
    if ("BR".equals(codeString))
      return V3EntityNamePartQualifier.BR;
    if ("CL".equals(codeString))
      return V3EntityNamePartQualifier.CL;
    if ("IN".equals(codeString))
      return V3EntityNamePartQualifier.IN;
    if ("LS".equals(codeString))
      return V3EntityNamePartQualifier.LS;
    if ("NB".equals(codeString))
      return V3EntityNamePartQualifier.NB;
    if ("PR".equals(codeString))
      return V3EntityNamePartQualifier.PR;
    if ("SP".equals(codeString))
      return V3EntityNamePartQualifier.SP;
    if ("TITLE".equals(codeString))
      return V3EntityNamePartQualifier.TITLE;
    if ("VV".equals(codeString))
      return V3EntityNamePartQualifier.VV;
    if ("PharmaceuticalEntityNamePartQualifiers".equals(codeString))
      return V3EntityNamePartQualifier.PHARMACEUTICALENTITYNAMEPARTQUALIFIERS;
    if ("CON".equals(codeString))
      return V3EntityNamePartQualifier.CON;
    if ("DEV".equals(codeString))
      return V3EntityNamePartQualifier.DEV;
    if ("FLAV".equals(codeString))
      return V3EntityNamePartQualifier.FLAV;
    if ("FORMUL".equals(codeString))
      return V3EntityNamePartQualifier.FORMUL;
    if ("FRM".equals(codeString))
      return V3EntityNamePartQualifier.FRM;
    if ("INV".equals(codeString))
      return V3EntityNamePartQualifier.INV;
    if ("POPUL".equals(codeString))
      return V3EntityNamePartQualifier.POPUL;
    if ("SCI".equals(codeString))
      return V3EntityNamePartQualifier.SCI;
    if ("STR".equals(codeString))
      return V3EntityNamePartQualifier.STR;
    if ("TIME".equals(codeString))
      return V3EntityNamePartQualifier.TIME;
    if ("TMK".equals(codeString))
      return V3EntityNamePartQualifier.TMK;
    if ("USE".equals(codeString))
      return V3EntityNamePartQualifier.USE;
    if ("_PersonNamePartQualifier".equals(codeString))
      return V3EntityNamePartQualifier._PERSONNAMEPARTQUALIFIER;
    if ("_PersonNamePartAffixTypes".equals(codeString))
      return V3EntityNamePartQualifier._PERSONNAMEPARTAFFIXTYPES;
    if ("_PersonNamePartChangeQualifier".equals(codeString))
      return V3EntityNamePartQualifier._PERSONNAMEPARTCHANGEQUALIFIER;
    if ("_PersonNamePartMiscQualifier".equals(codeString))
      return V3EntityNamePartQualifier._PERSONNAMEPARTMISCQUALIFIER;
    throw new IllegalArgumentException("Unknown V3EntityNamePartQualifier code '"+codeString+"'");
  }

  public String toCode(V3EntityNamePartQualifier code) {
    if (code == V3EntityNamePartQualifier._ORGANIZATIONNAMEPARTQUALIFIER)
      return "_OrganizationNamePartQualifier";
    if (code == V3EntityNamePartQualifier.AC)
      return "AC";
    if (code == V3EntityNamePartQualifier.AD)
      return "AD";
    if (code == V3EntityNamePartQualifier.BR)
      return "BR";
    if (code == V3EntityNamePartQualifier.CL)
      return "CL";
    if (code == V3EntityNamePartQualifier.IN)
      return "IN";
    if (code == V3EntityNamePartQualifier.LS)
      return "LS";
    if (code == V3EntityNamePartQualifier.NB)
      return "NB";
    if (code == V3EntityNamePartQualifier.PR)
      return "PR";
    if (code == V3EntityNamePartQualifier.SP)
      return "SP";
    if (code == V3EntityNamePartQualifier.TITLE)
      return "TITLE";
    if (code == V3EntityNamePartQualifier.VV)
      return "VV";
    if (code == V3EntityNamePartQualifier.PHARMACEUTICALENTITYNAMEPARTQUALIFIERS)
      return "PharmaceuticalEntityNamePartQualifiers";
    if (code == V3EntityNamePartQualifier.CON)
      return "CON";
    if (code == V3EntityNamePartQualifier.DEV)
      return "DEV";
    if (code == V3EntityNamePartQualifier.FLAV)
      return "FLAV";
    if (code == V3EntityNamePartQualifier.FORMUL)
      return "FORMUL";
    if (code == V3EntityNamePartQualifier.FRM)
      return "FRM";
    if (code == V3EntityNamePartQualifier.INV)
      return "INV";
    if (code == V3EntityNamePartQualifier.POPUL)
      return "POPUL";
    if (code == V3EntityNamePartQualifier.SCI)
      return "SCI";
    if (code == V3EntityNamePartQualifier.STR)
      return "STR";
    if (code == V3EntityNamePartQualifier.TIME)
      return "TIME";
    if (code == V3EntityNamePartQualifier.TMK)
      return "TMK";
    if (code == V3EntityNamePartQualifier.USE)
      return "USE";
    if (code == V3EntityNamePartQualifier._PERSONNAMEPARTQUALIFIER)
      return "_PersonNamePartQualifier";
    if (code == V3EntityNamePartQualifier._PERSONNAMEPARTAFFIXTYPES)
      return "_PersonNamePartAffixTypes";
    if (code == V3EntityNamePartQualifier._PERSONNAMEPARTCHANGEQUALIFIER)
      return "_PersonNamePartChangeQualifier";
    if (code == V3EntityNamePartQualifier._PERSONNAMEPARTMISCQUALIFIER)
      return "_PersonNamePartMiscQualifier";
    return "?";
  }


}

