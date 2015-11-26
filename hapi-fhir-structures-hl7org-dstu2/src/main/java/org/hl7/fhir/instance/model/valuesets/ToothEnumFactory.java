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

public class ToothEnumFactory implements EnumFactory<Tooth> {

  public Tooth fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("0".equals(codeString))
      return Tooth._0;
    if ("1".equals(codeString))
      return Tooth._1;
    if ("2".equals(codeString))
      return Tooth._2;
    if ("3".equals(codeString))
      return Tooth._3;
    if ("4".equals(codeString))
      return Tooth._4;
    if ("5".equals(codeString))
      return Tooth._5;
    if ("6".equals(codeString))
      return Tooth._6;
    if ("7".equals(codeString))
      return Tooth._7;
    if ("8".equals(codeString))
      return Tooth._8;
    if ("11".equals(codeString))
      return Tooth._11;
    if ("12".equals(codeString))
      return Tooth._12;
    if ("13".equals(codeString))
      return Tooth._13;
    if ("14".equals(codeString))
      return Tooth._14;
    if ("15".equals(codeString))
      return Tooth._15;
    if ("16".equals(codeString))
      return Tooth._16;
    if ("17".equals(codeString))
      return Tooth._17;
    if ("18".equals(codeString))
      return Tooth._18;
    if ("21".equals(codeString))
      return Tooth._21;
    if ("22".equals(codeString))
      return Tooth._22;
    if ("23".equals(codeString))
      return Tooth._23;
    if ("24".equals(codeString))
      return Tooth._24;
    if ("25".equals(codeString))
      return Tooth._25;
    if ("26".equals(codeString))
      return Tooth._26;
    if ("27".equals(codeString))
      return Tooth._27;
    if ("28".equals(codeString))
      return Tooth._28;
    if ("31".equals(codeString))
      return Tooth._31;
    if ("32".equals(codeString))
      return Tooth._32;
    if ("33".equals(codeString))
      return Tooth._33;
    if ("34".equals(codeString))
      return Tooth._34;
    if ("35".equals(codeString))
      return Tooth._35;
    if ("36".equals(codeString))
      return Tooth._36;
    if ("37".equals(codeString))
      return Tooth._37;
    if ("38".equals(codeString))
      return Tooth._38;
    if ("41".equals(codeString))
      return Tooth._41;
    if ("42".equals(codeString))
      return Tooth._42;
    if ("43".equals(codeString))
      return Tooth._43;
    if ("44".equals(codeString))
      return Tooth._44;
    if ("45".equals(codeString))
      return Tooth._45;
    if ("46".equals(codeString))
      return Tooth._46;
    if ("47".equals(codeString))
      return Tooth._47;
    if ("48".equals(codeString))
      return Tooth._48;
    throw new IllegalArgumentException("Unknown Tooth code '"+codeString+"'");
  }

  public String toCode(Tooth code) {
    if (code == Tooth._0)
      return "0";
    if (code == Tooth._1)
      return "1";
    if (code == Tooth._2)
      return "2";
    if (code == Tooth._3)
      return "3";
    if (code == Tooth._4)
      return "4";
    if (code == Tooth._5)
      return "5";
    if (code == Tooth._6)
      return "6";
    if (code == Tooth._7)
      return "7";
    if (code == Tooth._8)
      return "8";
    if (code == Tooth._11)
      return "11";
    if (code == Tooth._12)
      return "12";
    if (code == Tooth._13)
      return "13";
    if (code == Tooth._14)
      return "14";
    if (code == Tooth._15)
      return "15";
    if (code == Tooth._16)
      return "16";
    if (code == Tooth._17)
      return "17";
    if (code == Tooth._18)
      return "18";
    if (code == Tooth._21)
      return "21";
    if (code == Tooth._22)
      return "22";
    if (code == Tooth._23)
      return "23";
    if (code == Tooth._24)
      return "24";
    if (code == Tooth._25)
      return "25";
    if (code == Tooth._26)
      return "26";
    if (code == Tooth._27)
      return "27";
    if (code == Tooth._28)
      return "28";
    if (code == Tooth._31)
      return "31";
    if (code == Tooth._32)
      return "32";
    if (code == Tooth._33)
      return "33";
    if (code == Tooth._34)
      return "34";
    if (code == Tooth._35)
      return "35";
    if (code == Tooth._36)
      return "36";
    if (code == Tooth._37)
      return "37";
    if (code == Tooth._38)
      return "38";
    if (code == Tooth._41)
      return "41";
    if (code == Tooth._42)
      return "42";
    if (code == Tooth._43)
      return "43";
    if (code == Tooth._44)
      return "44";
    if (code == Tooth._45)
      return "45";
    if (code == Tooth._46)
      return "46";
    if (code == Tooth._47)
      return "47";
    if (code == Tooth._48)
      return "48";
    return "?";
  }


}

