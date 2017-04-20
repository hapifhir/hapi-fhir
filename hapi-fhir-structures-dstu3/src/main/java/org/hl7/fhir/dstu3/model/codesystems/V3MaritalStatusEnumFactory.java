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

public class V3MaritalStatusEnumFactory implements EnumFactory<V3MaritalStatus> {

  public V3MaritalStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A".equals(codeString))
      return V3MaritalStatus.A;
    if ("D".equals(codeString))
      return V3MaritalStatus.D;
    if ("I".equals(codeString))
      return V3MaritalStatus.I;
    if ("L".equals(codeString))
      return V3MaritalStatus.L;
    if ("M".equals(codeString))
      return V3MaritalStatus.M;
    if ("P".equals(codeString))
      return V3MaritalStatus.P;
    if ("S".equals(codeString))
      return V3MaritalStatus.S;
    if ("T".equals(codeString))
      return V3MaritalStatus.T;
    if ("U".equals(codeString))
      return V3MaritalStatus.U;
    if ("W".equals(codeString))
      return V3MaritalStatus.W;
    throw new IllegalArgumentException("Unknown V3MaritalStatus code '"+codeString+"'");
  }

  public String toCode(V3MaritalStatus code) {
    if (code == V3MaritalStatus.A)
      return "A";
    if (code == V3MaritalStatus.D)
      return "D";
    if (code == V3MaritalStatus.I)
      return "I";
    if (code == V3MaritalStatus.L)
      return "L";
    if (code == V3MaritalStatus.M)
      return "M";
    if (code == V3MaritalStatus.P)
      return "P";
    if (code == V3MaritalStatus.S)
      return "S";
    if (code == V3MaritalStatus.T)
      return "T";
    if (code == V3MaritalStatus.U)
      return "U";
    if (code == V3MaritalStatus.W)
      return "W";
    return "?";
  }

    public String toSystem(V3MaritalStatus code) {
      return code.getSystem();
      }

}

