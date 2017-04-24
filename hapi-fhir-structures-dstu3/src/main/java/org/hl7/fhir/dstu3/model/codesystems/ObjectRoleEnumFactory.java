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

public class ObjectRoleEnumFactory implements EnumFactory<ObjectRole> {

  public ObjectRole fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return ObjectRole._1;
    if ("2".equals(codeString))
      return ObjectRole._2;
    if ("3".equals(codeString))
      return ObjectRole._3;
    if ("4".equals(codeString))
      return ObjectRole._4;
    if ("5".equals(codeString))
      return ObjectRole._5;
    if ("6".equals(codeString))
      return ObjectRole._6;
    if ("7".equals(codeString))
      return ObjectRole._7;
    if ("8".equals(codeString))
      return ObjectRole._8;
    if ("9".equals(codeString))
      return ObjectRole._9;
    if ("10".equals(codeString))
      return ObjectRole._10;
    if ("11".equals(codeString))
      return ObjectRole._11;
    if ("12".equals(codeString))
      return ObjectRole._12;
    if ("13".equals(codeString))
      return ObjectRole._13;
    if ("14".equals(codeString))
      return ObjectRole._14;
    if ("15".equals(codeString))
      return ObjectRole._15;
    if ("16".equals(codeString))
      return ObjectRole._16;
    if ("17".equals(codeString))
      return ObjectRole._17;
    if ("18".equals(codeString))
      return ObjectRole._18;
    if ("19".equals(codeString))
      return ObjectRole._19;
    if ("20".equals(codeString))
      return ObjectRole._20;
    if ("21".equals(codeString))
      return ObjectRole._21;
    if ("22".equals(codeString))
      return ObjectRole._22;
    if ("23".equals(codeString))
      return ObjectRole._23;
    if ("24".equals(codeString))
      return ObjectRole._24;
    throw new IllegalArgumentException("Unknown ObjectRole code '"+codeString+"'");
  }

  public String toCode(ObjectRole code) {
    if (code == ObjectRole._1)
      return "1";
    if (code == ObjectRole._2)
      return "2";
    if (code == ObjectRole._3)
      return "3";
    if (code == ObjectRole._4)
      return "4";
    if (code == ObjectRole._5)
      return "5";
    if (code == ObjectRole._6)
      return "6";
    if (code == ObjectRole._7)
      return "7";
    if (code == ObjectRole._8)
      return "8";
    if (code == ObjectRole._9)
      return "9";
    if (code == ObjectRole._10)
      return "10";
    if (code == ObjectRole._11)
      return "11";
    if (code == ObjectRole._12)
      return "12";
    if (code == ObjectRole._13)
      return "13";
    if (code == ObjectRole._14)
      return "14";
    if (code == ObjectRole._15)
      return "15";
    if (code == ObjectRole._16)
      return "16";
    if (code == ObjectRole._17)
      return "17";
    if (code == ObjectRole._18)
      return "18";
    if (code == ObjectRole._19)
      return "19";
    if (code == ObjectRole._20)
      return "20";
    if (code == ObjectRole._21)
      return "21";
    if (code == ObjectRole._22)
      return "22";
    if (code == ObjectRole._23)
      return "23";
    if (code == ObjectRole._24)
      return "24";
    return "?";
  }

    public String toSystem(ObjectRole code) {
      return code.getSystem();
      }

}

