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
    return "?";
  }


}

