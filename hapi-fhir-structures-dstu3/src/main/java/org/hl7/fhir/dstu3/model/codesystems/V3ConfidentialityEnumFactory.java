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

public class V3ConfidentialityEnumFactory implements EnumFactory<V3Confidentiality> {

  public V3Confidentiality fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_Confidentiality".equals(codeString))
      return V3Confidentiality._CONFIDENTIALITY;
    if ("L".equals(codeString))
      return V3Confidentiality.L;
    if ("M".equals(codeString))
      return V3Confidentiality.M;
    if ("N".equals(codeString))
      return V3Confidentiality.N;
    if ("R".equals(codeString))
      return V3Confidentiality.R;
    if ("U".equals(codeString))
      return V3Confidentiality.U;
    if ("V".equals(codeString))
      return V3Confidentiality.V;
    if ("_ConfidentialityByAccessKind".equals(codeString))
      return V3Confidentiality._CONFIDENTIALITYBYACCESSKIND;
    if ("B".equals(codeString))
      return V3Confidentiality.B;
    if ("D".equals(codeString))
      return V3Confidentiality.D;
    if ("I".equals(codeString))
      return V3Confidentiality.I;
    if ("_ConfidentialityByInfoType".equals(codeString))
      return V3Confidentiality._CONFIDENTIALITYBYINFOTYPE;
    if ("ETH".equals(codeString))
      return V3Confidentiality.ETH;
    if ("HIV".equals(codeString))
      return V3Confidentiality.HIV;
    if ("PSY".equals(codeString))
      return V3Confidentiality.PSY;
    if ("SDV".equals(codeString))
      return V3Confidentiality.SDV;
    if ("_ConfidentialityModifiers".equals(codeString))
      return V3Confidentiality._CONFIDENTIALITYMODIFIERS;
    if ("C".equals(codeString))
      return V3Confidentiality.C;
    if ("S".equals(codeString))
      return V3Confidentiality.S;
    if ("T".equals(codeString))
      return V3Confidentiality.T;
    throw new IllegalArgumentException("Unknown V3Confidentiality code '"+codeString+"'");
  }

  public String toCode(V3Confidentiality code) {
    if (code == V3Confidentiality._CONFIDENTIALITY)
      return "_Confidentiality";
    if (code == V3Confidentiality.L)
      return "L";
    if (code == V3Confidentiality.M)
      return "M";
    if (code == V3Confidentiality.N)
      return "N";
    if (code == V3Confidentiality.R)
      return "R";
    if (code == V3Confidentiality.U)
      return "U";
    if (code == V3Confidentiality.V)
      return "V";
    if (code == V3Confidentiality._CONFIDENTIALITYBYACCESSKIND)
      return "_ConfidentialityByAccessKind";
    if (code == V3Confidentiality.B)
      return "B";
    if (code == V3Confidentiality.D)
      return "D";
    if (code == V3Confidentiality.I)
      return "I";
    if (code == V3Confidentiality._CONFIDENTIALITYBYINFOTYPE)
      return "_ConfidentialityByInfoType";
    if (code == V3Confidentiality.ETH)
      return "ETH";
    if (code == V3Confidentiality.HIV)
      return "HIV";
    if (code == V3Confidentiality.PSY)
      return "PSY";
    if (code == V3Confidentiality.SDV)
      return "SDV";
    if (code == V3Confidentiality._CONFIDENTIALITYMODIFIERS)
      return "_ConfidentialityModifiers";
    if (code == V3Confidentiality.C)
      return "C";
    if (code == V3Confidentiality.S)
      return "S";
    if (code == V3Confidentiality.T)
      return "T";
    return "?";
  }

    public String toSystem(V3Confidentiality code) {
      return code.getSystem();
      }

}

