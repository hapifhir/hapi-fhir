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

public class Iso21089LifecycleEnumFactory implements EnumFactory<Iso21089Lifecycle> {

  public Iso21089Lifecycle fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("2".equals(codeString))
      return Iso21089Lifecycle._2;
    if ("14".equals(codeString))
      return Iso21089Lifecycle._14;
    if ("4".equals(codeString))
      return Iso21089Lifecycle._4;
    if ("27".equals(codeString))
      return Iso21089Lifecycle._27;
    if ("10".equals(codeString))
      return Iso21089Lifecycle._10;
    if ("17".equals(codeString))
      return Iso21089Lifecycle._17;
    if ("16".equals(codeString))
      return Iso21089Lifecycle._16;
    if ("7".equals(codeString))
      return Iso21089Lifecycle._7;
    if ("26".equals(codeString))
      return Iso21089Lifecycle._26;
    if ("13".equals(codeString))
      return Iso21089Lifecycle._13;
    if ("21".equals(codeString))
      return Iso21089Lifecycle._21;
    if ("19".equals(codeString))
      return Iso21089Lifecycle._19;
    if ("1".equals(codeString))
      return Iso21089Lifecycle._1;
    if ("11".equals(codeString))
      return Iso21089Lifecycle._11;
    if ("18".equals(codeString))
      return Iso21089Lifecycle._18;
    if ("9".equals(codeString))
      return Iso21089Lifecycle._9;
    if ("6".equals(codeString))
      return Iso21089Lifecycle._6;
    if ("12".equals(codeString))
      return Iso21089Lifecycle._12;
    if ("24".equals(codeString))
      return Iso21089Lifecycle._24;
    if ("15".equals(codeString))
      return Iso21089Lifecycle._15;
    if ("3".equals(codeString))
      return Iso21089Lifecycle._3;
    if ("8".equals(codeString))
      return Iso21089Lifecycle._8;
    if ("22".equals(codeString))
      return Iso21089Lifecycle._22;
    if ("20".equals(codeString))
      return Iso21089Lifecycle._20;
    if ("25".equals(codeString))
      return Iso21089Lifecycle._25;
    throw new IllegalArgumentException("Unknown Iso21089Lifecycle code '"+codeString+"'");
  }

  public String toCode(Iso21089Lifecycle code) {
    if (code == Iso21089Lifecycle._2)
      return "2";
    if (code == Iso21089Lifecycle._14)
      return "14";
    if (code == Iso21089Lifecycle._4)
      return "4";
    if (code == Iso21089Lifecycle._27)
      return "27";
    if (code == Iso21089Lifecycle._10)
      return "10";
    if (code == Iso21089Lifecycle._17)
      return "17";
    if (code == Iso21089Lifecycle._16)
      return "16";
    if (code == Iso21089Lifecycle._7)
      return "7";
    if (code == Iso21089Lifecycle._26)
      return "26";
    if (code == Iso21089Lifecycle._13)
      return "13";
    if (code == Iso21089Lifecycle._21)
      return "21";
    if (code == Iso21089Lifecycle._19)
      return "19";
    if (code == Iso21089Lifecycle._1)
      return "1";
    if (code == Iso21089Lifecycle._11)
      return "11";
    if (code == Iso21089Lifecycle._18)
      return "18";
    if (code == Iso21089Lifecycle._9)
      return "9";
    if (code == Iso21089Lifecycle._6)
      return "6";
    if (code == Iso21089Lifecycle._12)
      return "12";
    if (code == Iso21089Lifecycle._24)
      return "24";
    if (code == Iso21089Lifecycle._15)
      return "15";
    if (code == Iso21089Lifecycle._3)
      return "3";
    if (code == Iso21089Lifecycle._8)
      return "8";
    if (code == Iso21089Lifecycle._22)
      return "22";
    if (code == Iso21089Lifecycle._20)
      return "20";
    if (code == Iso21089Lifecycle._25)
      return "25";
    return "?";
  }

    public String toSystem(Iso21089Lifecycle code) {
      return code.getSystem();
      }

}

