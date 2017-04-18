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

public class V3LivingArrangementEnumFactory implements EnumFactory<V3LivingArrangement> {

  public V3LivingArrangement fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("HL".equals(codeString))
      return V3LivingArrangement.HL;
    if ("M".equals(codeString))
      return V3LivingArrangement.M;
    if ("T".equals(codeString))
      return V3LivingArrangement.T;
    if ("I".equals(codeString))
      return V3LivingArrangement.I;
    if ("CS".equals(codeString))
      return V3LivingArrangement.CS;
    if ("G".equals(codeString))
      return V3LivingArrangement.G;
    if ("N".equals(codeString))
      return V3LivingArrangement.N;
    if ("X".equals(codeString))
      return V3LivingArrangement.X;
    if ("PR".equals(codeString))
      return V3LivingArrangement.PR;
    if ("H".equals(codeString))
      return V3LivingArrangement.H;
    if ("R".equals(codeString))
      return V3LivingArrangement.R;
    if ("SL".equals(codeString))
      return V3LivingArrangement.SL;
    throw new IllegalArgumentException("Unknown V3LivingArrangement code '"+codeString+"'");
  }

  public String toCode(V3LivingArrangement code) {
    if (code == V3LivingArrangement.HL)
      return "HL";
    if (code == V3LivingArrangement.M)
      return "M";
    if (code == V3LivingArrangement.T)
      return "T";
    if (code == V3LivingArrangement.I)
      return "I";
    if (code == V3LivingArrangement.CS)
      return "CS";
    if (code == V3LivingArrangement.G)
      return "G";
    if (code == V3LivingArrangement.N)
      return "N";
    if (code == V3LivingArrangement.X)
      return "X";
    if (code == V3LivingArrangement.PR)
      return "PR";
    if (code == V3LivingArrangement.H)
      return "H";
    if (code == V3LivingArrangement.R)
      return "R";
    if (code == V3LivingArrangement.SL)
      return "SL";
    return "?";
  }

    public String toSystem(V3LivingArrangement code) {
      return code.getSystem();
      }

}

