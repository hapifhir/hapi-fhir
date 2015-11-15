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

public class V3EntityNamePartQualifierR2EnumFactory implements EnumFactory<V3EntityNamePartQualifierR2> {

  public V3EntityNamePartQualifierR2 fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AD".equals(codeString))
      return V3EntityNamePartQualifierR2.AD;
    if ("SP".equals(codeString))
      return V3EntityNamePartQualifierR2.SP;
    if ("BR".equals(codeString))
      return V3EntityNamePartQualifierR2.BR;
    if ("CL".equals(codeString))
      return V3EntityNamePartQualifierR2.CL;
    if ("IN".equals(codeString))
      return V3EntityNamePartQualifierR2.IN;
    if ("LS".equals(codeString))
      return V3EntityNamePartQualifierR2.LS;
    if ("MID".equals(codeString))
      return V3EntityNamePartQualifierR2.MID;
    if ("PFX".equals(codeString))
      return V3EntityNamePartQualifierR2.PFX;
    if ("PharmaceuticalEntityNamePartQualifiers".equals(codeString))
      return V3EntityNamePartQualifierR2.PHARMACEUTICALENTITYNAMEPARTQUALIFIERS;
    if ("CON".equals(codeString))
      return V3EntityNamePartQualifierR2.CON;
    if ("DEV".equals(codeString))
      return V3EntityNamePartQualifierR2.DEV;
    if ("FLAV".equals(codeString))
      return V3EntityNamePartQualifierR2.FLAV;
    if ("FORMUL".equals(codeString))
      return V3EntityNamePartQualifierR2.FORMUL;
    if ("FRM".equals(codeString))
      return V3EntityNamePartQualifierR2.FRM;
    if ("INV".equals(codeString))
      return V3EntityNamePartQualifierR2.INV;
    if ("POPUL".equals(codeString))
      return V3EntityNamePartQualifierR2.POPUL;
    if ("SCI".equals(codeString))
      return V3EntityNamePartQualifierR2.SCI;
    if ("STR".equals(codeString))
      return V3EntityNamePartQualifierR2.STR;
    if ("TIME".equals(codeString))
      return V3EntityNamePartQualifierR2.TIME;
    if ("TMK".equals(codeString))
      return V3EntityNamePartQualifierR2.TMK;
    if ("USE".equals(codeString))
      return V3EntityNamePartQualifierR2.USE;
    if ("SFX".equals(codeString))
      return V3EntityNamePartQualifierR2.SFX;
    if ("TitleStyles".equals(codeString))
      return V3EntityNamePartQualifierR2.TITLESTYLES;
    if ("AC".equals(codeString))
      return V3EntityNamePartQualifierR2.AC;
    if ("HON".equals(codeString))
      return V3EntityNamePartQualifierR2.HON;
    if ("NB".equals(codeString))
      return V3EntityNamePartQualifierR2.NB;
    if ("PR".equals(codeString))
      return V3EntityNamePartQualifierR2.PR;
    throw new IllegalArgumentException("Unknown V3EntityNamePartQualifierR2 code '"+codeString+"'");
  }

  public String toCode(V3EntityNamePartQualifierR2 code) {
    if (code == V3EntityNamePartQualifierR2.AD)
      return "AD";
    if (code == V3EntityNamePartQualifierR2.SP)
      return "SP";
    if (code == V3EntityNamePartQualifierR2.BR)
      return "BR";
    if (code == V3EntityNamePartQualifierR2.CL)
      return "CL";
    if (code == V3EntityNamePartQualifierR2.IN)
      return "IN";
    if (code == V3EntityNamePartQualifierR2.LS)
      return "LS";
    if (code == V3EntityNamePartQualifierR2.MID)
      return "MID";
    if (code == V3EntityNamePartQualifierR2.PFX)
      return "PFX";
    if (code == V3EntityNamePartQualifierR2.PHARMACEUTICALENTITYNAMEPARTQUALIFIERS)
      return "PharmaceuticalEntityNamePartQualifiers";
    if (code == V3EntityNamePartQualifierR2.CON)
      return "CON";
    if (code == V3EntityNamePartQualifierR2.DEV)
      return "DEV";
    if (code == V3EntityNamePartQualifierR2.FLAV)
      return "FLAV";
    if (code == V3EntityNamePartQualifierR2.FORMUL)
      return "FORMUL";
    if (code == V3EntityNamePartQualifierR2.FRM)
      return "FRM";
    if (code == V3EntityNamePartQualifierR2.INV)
      return "INV";
    if (code == V3EntityNamePartQualifierR2.POPUL)
      return "POPUL";
    if (code == V3EntityNamePartQualifierR2.SCI)
      return "SCI";
    if (code == V3EntityNamePartQualifierR2.STR)
      return "STR";
    if (code == V3EntityNamePartQualifierR2.TIME)
      return "TIME";
    if (code == V3EntityNamePartQualifierR2.TMK)
      return "TMK";
    if (code == V3EntityNamePartQualifierR2.USE)
      return "USE";
    if (code == V3EntityNamePartQualifierR2.SFX)
      return "SFX";
    if (code == V3EntityNamePartQualifierR2.TITLESTYLES)
      return "TitleStyles";
    if (code == V3EntityNamePartQualifierR2.AC)
      return "AC";
    if (code == V3EntityNamePartQualifierR2.HON)
      return "HON";
    if (code == V3EntityNamePartQualifierR2.NB)
      return "NB";
    if (code == V3EntityNamePartQualifierR2.PR)
      return "PR";
    return "?";
  }


}

