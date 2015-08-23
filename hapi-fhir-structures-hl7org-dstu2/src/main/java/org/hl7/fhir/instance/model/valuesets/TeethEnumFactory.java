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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class TeethEnumFactory implements EnumFactory<Teeth> {

  public Teeth fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("11".equals(codeString))
      return Teeth._11;
    if ("12".equals(codeString))
      return Teeth._12;
    if ("13".equals(codeString))
      return Teeth._13;
    if ("14".equals(codeString))
      return Teeth._14;
    if ("15".equals(codeString))
      return Teeth._15;
    if ("16".equals(codeString))
      return Teeth._16;
    if ("17".equals(codeString))
      return Teeth._17;
    if ("18".equals(codeString))
      return Teeth._18;
    if ("21".equals(codeString))
      return Teeth._21;
    if ("22".equals(codeString))
      return Teeth._22;
    if ("23".equals(codeString))
      return Teeth._23;
    if ("24".equals(codeString))
      return Teeth._24;
    if ("25".equals(codeString))
      return Teeth._25;
    if ("26".equals(codeString))
      return Teeth._26;
    if ("27".equals(codeString))
      return Teeth._27;
    if ("28".equals(codeString))
      return Teeth._28;
    if ("31".equals(codeString))
      return Teeth._31;
    if ("32".equals(codeString))
      return Teeth._32;
    if ("33".equals(codeString))
      return Teeth._33;
    if ("34".equals(codeString))
      return Teeth._34;
    if ("35".equals(codeString))
      return Teeth._35;
    if ("36".equals(codeString))
      return Teeth._36;
    if ("37".equals(codeString))
      return Teeth._37;
    if ("38".equals(codeString))
      return Teeth._38;
    if ("41".equals(codeString))
      return Teeth._41;
    if ("42".equals(codeString))
      return Teeth._42;
    if ("43".equals(codeString))
      return Teeth._43;
    if ("44".equals(codeString))
      return Teeth._44;
    if ("45".equals(codeString))
      return Teeth._45;
    if ("46".equals(codeString))
      return Teeth._46;
    if ("47".equals(codeString))
      return Teeth._47;
    if ("48".equals(codeString))
      return Teeth._48;
    throw new IllegalArgumentException("Unknown Teeth code '"+codeString+"'");
  }

  public String toCode(Teeth code) {
    if (code == Teeth._11)
      return "11";
    if (code == Teeth._12)
      return "12";
    if (code == Teeth._13)
      return "13";
    if (code == Teeth._14)
      return "14";
    if (code == Teeth._15)
      return "15";
    if (code == Teeth._16)
      return "16";
    if (code == Teeth._17)
      return "17";
    if (code == Teeth._18)
      return "18";
    if (code == Teeth._21)
      return "21";
    if (code == Teeth._22)
      return "22";
    if (code == Teeth._23)
      return "23";
    if (code == Teeth._24)
      return "24";
    if (code == Teeth._25)
      return "25";
    if (code == Teeth._26)
      return "26";
    if (code == Teeth._27)
      return "27";
    if (code == Teeth._28)
      return "28";
    if (code == Teeth._31)
      return "31";
    if (code == Teeth._32)
      return "32";
    if (code == Teeth._33)
      return "33";
    if (code == Teeth._34)
      return "34";
    if (code == Teeth._35)
      return "35";
    if (code == Teeth._36)
      return "36";
    if (code == Teeth._37)
      return "37";
    if (code == Teeth._38)
      return "38";
    if (code == Teeth._41)
      return "41";
    if (code == Teeth._42)
      return "42";
    if (code == Teeth._43)
      return "43";
    if (code == Teeth._44)
      return "44";
    if (code == Teeth._45)
      return "45";
    if (code == Teeth._46)
      return "46";
    if (code == Teeth._47)
      return "47";
    if (code == Teeth._48)
      return "48";
    return "?";
  }


}

