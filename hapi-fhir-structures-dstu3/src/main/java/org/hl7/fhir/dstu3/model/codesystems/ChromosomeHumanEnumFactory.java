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

public class ChromosomeHumanEnumFactory implements EnumFactory<ChromosomeHuman> {

  public ChromosomeHuman fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return ChromosomeHuman._1;
    if ("2".equals(codeString))
      return ChromosomeHuman._2;
    if ("3".equals(codeString))
      return ChromosomeHuman._3;
    if ("4".equals(codeString))
      return ChromosomeHuman._4;
    if ("5".equals(codeString))
      return ChromosomeHuman._5;
    if ("6".equals(codeString))
      return ChromosomeHuman._6;
    if ("7".equals(codeString))
      return ChromosomeHuman._7;
    if ("8".equals(codeString))
      return ChromosomeHuman._8;
    if ("9".equals(codeString))
      return ChromosomeHuman._9;
    if ("10".equals(codeString))
      return ChromosomeHuman._10;
    if ("11".equals(codeString))
      return ChromosomeHuman._11;
    if ("12".equals(codeString))
      return ChromosomeHuman._12;
    if ("13".equals(codeString))
      return ChromosomeHuman._13;
    if ("14".equals(codeString))
      return ChromosomeHuman._14;
    if ("15".equals(codeString))
      return ChromosomeHuman._15;
    if ("16".equals(codeString))
      return ChromosomeHuman._16;
    if ("17".equals(codeString))
      return ChromosomeHuman._17;
    if ("18".equals(codeString))
      return ChromosomeHuman._18;
    if ("19".equals(codeString))
      return ChromosomeHuman._19;
    if ("20".equals(codeString))
      return ChromosomeHuman._20;
    if ("21".equals(codeString))
      return ChromosomeHuman._21;
    if ("22".equals(codeString))
      return ChromosomeHuman._22;
    if ("X".equals(codeString))
      return ChromosomeHuman.X;
    if ("Y".equals(codeString))
      return ChromosomeHuman.Y;
    throw new IllegalArgumentException("Unknown ChromosomeHuman code '"+codeString+"'");
  }

  public String toCode(ChromosomeHuman code) {
    if (code == ChromosomeHuman._1)
      return "1";
    if (code == ChromosomeHuman._2)
      return "2";
    if (code == ChromosomeHuman._3)
      return "3";
    if (code == ChromosomeHuman._4)
      return "4";
    if (code == ChromosomeHuman._5)
      return "5";
    if (code == ChromosomeHuman._6)
      return "6";
    if (code == ChromosomeHuman._7)
      return "7";
    if (code == ChromosomeHuman._8)
      return "8";
    if (code == ChromosomeHuman._9)
      return "9";
    if (code == ChromosomeHuman._10)
      return "10";
    if (code == ChromosomeHuman._11)
      return "11";
    if (code == ChromosomeHuman._12)
      return "12";
    if (code == ChromosomeHuman._13)
      return "13";
    if (code == ChromosomeHuman._14)
      return "14";
    if (code == ChromosomeHuman._15)
      return "15";
    if (code == ChromosomeHuman._16)
      return "16";
    if (code == ChromosomeHuman._17)
      return "17";
    if (code == ChromosomeHuman._18)
      return "18";
    if (code == ChromosomeHuman._19)
      return "19";
    if (code == ChromosomeHuman._20)
      return "20";
    if (code == ChromosomeHuman._21)
      return "21";
    if (code == ChromosomeHuman._22)
      return "22";
    if (code == ChromosomeHuman.X)
      return "X";
    if (code == ChromosomeHuman.Y)
      return "Y";
    return "?";
  }

    public String toSystem(ChromosomeHuman code) {
      return code.getSystem();
      }

}

