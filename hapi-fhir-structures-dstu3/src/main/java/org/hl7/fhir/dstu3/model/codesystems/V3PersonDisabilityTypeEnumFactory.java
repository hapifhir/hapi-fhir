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

public class V3PersonDisabilityTypeEnumFactory implements EnumFactory<V3PersonDisabilityType> {

  public V3PersonDisabilityType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return V3PersonDisabilityType._1;
    if ("2".equals(codeString))
      return V3PersonDisabilityType._2;
    if ("3".equals(codeString))
      return V3PersonDisabilityType._3;
    if ("4".equals(codeString))
      return V3PersonDisabilityType._4;
    if ("5".equals(codeString))
      return V3PersonDisabilityType._5;
    if ("CB".equals(codeString))
      return V3PersonDisabilityType.CB;
    if ("CR".equals(codeString))
      return V3PersonDisabilityType.CR;
    if ("G".equals(codeString))
      return V3PersonDisabilityType.G;
    if ("WC".equals(codeString))
      return V3PersonDisabilityType.WC;
    if ("WK".equals(codeString))
      return V3PersonDisabilityType.WK;
    throw new IllegalArgumentException("Unknown V3PersonDisabilityType code '"+codeString+"'");
  }

  public String toCode(V3PersonDisabilityType code) {
    if (code == V3PersonDisabilityType._1)
      return "1";
    if (code == V3PersonDisabilityType._2)
      return "2";
    if (code == V3PersonDisabilityType._3)
      return "3";
    if (code == V3PersonDisabilityType._4)
      return "4";
    if (code == V3PersonDisabilityType._5)
      return "5";
    if (code == V3PersonDisabilityType.CB)
      return "CB";
    if (code == V3PersonDisabilityType.CR)
      return "CR";
    if (code == V3PersonDisabilityType.G)
      return "G";
    if (code == V3PersonDisabilityType.WC)
      return "WC";
    if (code == V3PersonDisabilityType.WK)
      return "WK";
    return "?";
  }

    public String toSystem(V3PersonDisabilityType code) {
      return code.getSystem();
      }

}

