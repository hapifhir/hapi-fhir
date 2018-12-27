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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class ProgramEnumFactory implements EnumFactory<Program> {

  public Program fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return Program._1;
    if ("2".equals(codeString))
      return Program._2;
    if ("3".equals(codeString))
      return Program._3;
    if ("4".equals(codeString))
      return Program._4;
    if ("5".equals(codeString))
      return Program._5;
    if ("6".equals(codeString))
      return Program._6;
    if ("7".equals(codeString))
      return Program._7;
    if ("8".equals(codeString))
      return Program._8;
    if ("9".equals(codeString))
      return Program._9;
    if ("10".equals(codeString))
      return Program._10;
    if ("11".equals(codeString))
      return Program._11;
    if ("12".equals(codeString))
      return Program._12;
    if ("13".equals(codeString))
      return Program._13;
    if ("14".equals(codeString))
      return Program._14;
    if ("15".equals(codeString))
      return Program._15;
    if ("16".equals(codeString))
      return Program._16;
    if ("17".equals(codeString))
      return Program._17;
    if ("18".equals(codeString))
      return Program._18;
    if ("19".equals(codeString))
      return Program._19;
    if ("20".equals(codeString))
      return Program._20;
    if ("21".equals(codeString))
      return Program._21;
    if ("22".equals(codeString))
      return Program._22;
    if ("23".equals(codeString))
      return Program._23;
    if ("24".equals(codeString))
      return Program._24;
    if ("25".equals(codeString))
      return Program._25;
    if ("26".equals(codeString))
      return Program._26;
    if ("27".equals(codeString))
      return Program._27;
    if ("28".equals(codeString))
      return Program._28;
    if ("29".equals(codeString))
      return Program._29;
    if ("30".equals(codeString))
      return Program._30;
    if ("31".equals(codeString))
      return Program._31;
    if ("32".equals(codeString))
      return Program._32;
    if ("33".equals(codeString))
      return Program._33;
    if ("34".equals(codeString))
      return Program._34;
    if ("35".equals(codeString))
      return Program._35;
    if ("36".equals(codeString))
      return Program._36;
    if ("37".equals(codeString))
      return Program._37;
    if ("38".equals(codeString))
      return Program._38;
    if ("39".equals(codeString))
      return Program._39;
    if ("40".equals(codeString))
      return Program._40;
    if ("41".equals(codeString))
      return Program._41;
    if ("42".equals(codeString))
      return Program._42;
    if ("43".equals(codeString))
      return Program._43;
    if ("44".equals(codeString))
      return Program._44;
    if ("45".equals(codeString))
      return Program._45;
    throw new IllegalArgumentException("Unknown Program code '"+codeString+"'");
  }

  public String toCode(Program code) {
    if (code == Program._1)
      return "1";
    if (code == Program._2)
      return "2";
    if (code == Program._3)
      return "3";
    if (code == Program._4)
      return "4";
    if (code == Program._5)
      return "5";
    if (code == Program._6)
      return "6";
    if (code == Program._7)
      return "7";
    if (code == Program._8)
      return "8";
    if (code == Program._9)
      return "9";
    if (code == Program._10)
      return "10";
    if (code == Program._11)
      return "11";
    if (code == Program._12)
      return "12";
    if (code == Program._13)
      return "13";
    if (code == Program._14)
      return "14";
    if (code == Program._15)
      return "15";
    if (code == Program._16)
      return "16";
    if (code == Program._17)
      return "17";
    if (code == Program._18)
      return "18";
    if (code == Program._19)
      return "19";
    if (code == Program._20)
      return "20";
    if (code == Program._21)
      return "21";
    if (code == Program._22)
      return "22";
    if (code == Program._23)
      return "23";
    if (code == Program._24)
      return "24";
    if (code == Program._25)
      return "25";
    if (code == Program._26)
      return "26";
    if (code == Program._27)
      return "27";
    if (code == Program._28)
      return "28";
    if (code == Program._29)
      return "29";
    if (code == Program._30)
      return "30";
    if (code == Program._31)
      return "31";
    if (code == Program._32)
      return "32";
    if (code == Program._33)
      return "33";
    if (code == Program._34)
      return "34";
    if (code == Program._35)
      return "35";
    if (code == Program._36)
      return "36";
    if (code == Program._37)
      return "37";
    if (code == Program._38)
      return "38";
    if (code == Program._39)
      return "39";
    if (code == Program._40)
      return "40";
    if (code == Program._41)
      return "41";
    if (code == Program._42)
      return "42";
    if (code == Program._43)
      return "43";
    if (code == Program._44)
      return "44";
    if (code == Program._45)
      return "45";
    return "?";
  }

    public String toSystem(Program code) {
      return code.getSystem();
      }

}

