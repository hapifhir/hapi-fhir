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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class V3CompressionAlgorithmEnumFactory implements EnumFactory<V3CompressionAlgorithm> {

  public V3CompressionAlgorithm fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("BZ".equals(codeString))
      return V3CompressionAlgorithm.BZ;
    if ("DF".equals(codeString))
      return V3CompressionAlgorithm.DF;
    if ("GZ".equals(codeString))
      return V3CompressionAlgorithm.GZ;
    if ("Z".equals(codeString))
      return V3CompressionAlgorithm.Z;
    if ("Z7".equals(codeString))
      return V3CompressionAlgorithm.Z7;
    if ("ZL".equals(codeString))
      return V3CompressionAlgorithm.ZL;
    throw new IllegalArgumentException("Unknown V3CompressionAlgorithm code '"+codeString+"'");
  }

  public String toCode(V3CompressionAlgorithm code) {
    if (code == V3CompressionAlgorithm.BZ)
      return "BZ";
    if (code == V3CompressionAlgorithm.DF)
      return "DF";
    if (code == V3CompressionAlgorithm.GZ)
      return "GZ";
    if (code == V3CompressionAlgorithm.Z)
      return "Z";
    if (code == V3CompressionAlgorithm.Z7)
      return "Z7";
    if (code == V3CompressionAlgorithm.ZL)
      return "ZL";
    return "?";
  }

    public String toSystem(V3CompressionAlgorithm code) {
      return code.getSystem();
      }

}

