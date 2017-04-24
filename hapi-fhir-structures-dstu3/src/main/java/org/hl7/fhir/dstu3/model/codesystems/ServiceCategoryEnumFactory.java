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

public class ServiceCategoryEnumFactory implements EnumFactory<ServiceCategory> {

  public ServiceCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return ServiceCategory._1;
    if ("2".equals(codeString))
      return ServiceCategory._2;
    if ("34".equals(codeString))
      return ServiceCategory._34;
    if ("3".equals(codeString))
      return ServiceCategory._3;
    if ("4".equals(codeString))
      return ServiceCategory._4;
    if ("5".equals(codeString))
      return ServiceCategory._5;
    if ("6".equals(codeString))
      return ServiceCategory._6;
    if ("7".equals(codeString))
      return ServiceCategory._7;
    if ("8".equals(codeString))
      return ServiceCategory._8;
    if ("36".equals(codeString))
      return ServiceCategory._36;
    if ("9".equals(codeString))
      return ServiceCategory._9;
    if ("10".equals(codeString))
      return ServiceCategory._10;
    if ("11".equals(codeString))
      return ServiceCategory._11;
    if ("12".equals(codeString))
      return ServiceCategory._12;
    if ("13".equals(codeString))
      return ServiceCategory._13;
    if ("14".equals(codeString))
      return ServiceCategory._14;
    if ("15".equals(codeString))
      return ServiceCategory._15;
    if ("16".equals(codeString))
      return ServiceCategory._16;
    if ("17".equals(codeString))
      return ServiceCategory._17;
    if ("35".equals(codeString))
      return ServiceCategory._35;
    if ("18".equals(codeString))
      return ServiceCategory._18;
    if ("19".equals(codeString))
      return ServiceCategory._19;
    if ("20".equals(codeString))
      return ServiceCategory._20;
    if ("21".equals(codeString))
      return ServiceCategory._21;
    if ("22".equals(codeString))
      return ServiceCategory._22;
    if ("38".equals(codeString))
      return ServiceCategory._38;
    if ("23".equals(codeString))
      return ServiceCategory._23;
    if ("24".equals(codeString))
      return ServiceCategory._24;
    if ("25".equals(codeString))
      return ServiceCategory._25;
    if ("26".equals(codeString))
      return ServiceCategory._26;
    if ("27".equals(codeString))
      return ServiceCategory._27;
    if ("28".equals(codeString))
      return ServiceCategory._28;
    if ("29".equals(codeString))
      return ServiceCategory._29;
    if ("30".equals(codeString))
      return ServiceCategory._30;
    if ("31".equals(codeString))
      return ServiceCategory._31;
    if ("32".equals(codeString))
      return ServiceCategory._32;
    if ("37".equals(codeString))
      return ServiceCategory._37;
    if ("33".equals(codeString))
      return ServiceCategory._33;
    throw new IllegalArgumentException("Unknown ServiceCategory code '"+codeString+"'");
  }

  public String toCode(ServiceCategory code) {
    if (code == ServiceCategory._1)
      return "1";
    if (code == ServiceCategory._2)
      return "2";
    if (code == ServiceCategory._34)
      return "34";
    if (code == ServiceCategory._3)
      return "3";
    if (code == ServiceCategory._4)
      return "4";
    if (code == ServiceCategory._5)
      return "5";
    if (code == ServiceCategory._6)
      return "6";
    if (code == ServiceCategory._7)
      return "7";
    if (code == ServiceCategory._8)
      return "8";
    if (code == ServiceCategory._36)
      return "36";
    if (code == ServiceCategory._9)
      return "9";
    if (code == ServiceCategory._10)
      return "10";
    if (code == ServiceCategory._11)
      return "11";
    if (code == ServiceCategory._12)
      return "12";
    if (code == ServiceCategory._13)
      return "13";
    if (code == ServiceCategory._14)
      return "14";
    if (code == ServiceCategory._15)
      return "15";
    if (code == ServiceCategory._16)
      return "16";
    if (code == ServiceCategory._17)
      return "17";
    if (code == ServiceCategory._35)
      return "35";
    if (code == ServiceCategory._18)
      return "18";
    if (code == ServiceCategory._19)
      return "19";
    if (code == ServiceCategory._20)
      return "20";
    if (code == ServiceCategory._21)
      return "21";
    if (code == ServiceCategory._22)
      return "22";
    if (code == ServiceCategory._38)
      return "38";
    if (code == ServiceCategory._23)
      return "23";
    if (code == ServiceCategory._24)
      return "24";
    if (code == ServiceCategory._25)
      return "25";
    if (code == ServiceCategory._26)
      return "26";
    if (code == ServiceCategory._27)
      return "27";
    if (code == ServiceCategory._28)
      return "28";
    if (code == ServiceCategory._29)
      return "29";
    if (code == ServiceCategory._30)
      return "30";
    if (code == ServiceCategory._31)
      return "31";
    if (code == ServiceCategory._32)
      return "32";
    if (code == ServiceCategory._37)
      return "37";
    if (code == ServiceCategory._33)
      return "33";
    return "?";
  }

    public String toSystem(ServiceCategory code) {
      return code.getSystem();
      }

}

