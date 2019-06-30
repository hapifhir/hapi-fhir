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

// Generated on Wed, Jan 10, 2018 14:53-0500 for FHIR v3.2.0


import org.hl7.fhir.r4.model.EnumFactory;

public class SupervisoryLevelEnumFactory implements EnumFactory<SupervisoryLevel> {

  public SupervisoryLevel fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("E-1".equals(codeString))
      return SupervisoryLevel.E1;
    if ("E-2".equals(codeString))
      return SupervisoryLevel.E2;
    if ("E-3".equals(codeString))
      return SupervisoryLevel.E3;
    if ("E-4".equals(codeString))
      return SupervisoryLevel.E4;
    if ("E-5".equals(codeString))
      return SupervisoryLevel.E5;
    if ("E-6".equals(codeString))
      return SupervisoryLevel.E6;
    if ("E-7".equals(codeString))
      return SupervisoryLevel.E7;
    if ("E-8".equals(codeString))
      return SupervisoryLevel.E8;
    if ("E-9".equals(codeString))
      return SupervisoryLevel.E9;
    if ("O-1".equals(codeString))
      return SupervisoryLevel.O1;
    if ("O-2".equals(codeString))
      return SupervisoryLevel.O2;
    if ("O-3".equals(codeString))
      return SupervisoryLevel.O3;
    if ("O-4".equals(codeString))
      return SupervisoryLevel.O4;
    if ("O-5".equals(codeString))
      return SupervisoryLevel.O5;
    if ("O-6".equals(codeString))
      return SupervisoryLevel.O6;
    if ("O-7".equals(codeString))
      return SupervisoryLevel.O7;
    if ("O-8".equals(codeString))
      return SupervisoryLevel.O8;
    if ("O-9".equals(codeString))
      return SupervisoryLevel.O9;
    if ("O-10".equals(codeString))
      return SupervisoryLevel.O10;
    if ("W-1".equals(codeString))
      return SupervisoryLevel.W1;
    if ("W-2".equals(codeString))
      return SupervisoryLevel.W2;
    if ("W-3".equals(codeString))
      return SupervisoryLevel.W3;
    if ("W-4".equals(codeString))
      return SupervisoryLevel.W4;
    if ("W-5".equals(codeString))
      return SupervisoryLevel.W5;
    if ("C-3".equals(codeString))
      return SupervisoryLevel.C3;
    throw new IllegalArgumentException("Unknown SupervisoryLevel code '"+codeString+"'");
  }

  public String toCode(SupervisoryLevel code) {
    if (code == SupervisoryLevel.E1)
      return "E-1";
    if (code == SupervisoryLevel.E2)
      return "E-2";
    if (code == SupervisoryLevel.E3)
      return "E-3";
    if (code == SupervisoryLevel.E4)
      return "E-4";
    if (code == SupervisoryLevel.E5)
      return "E-5";
    if (code == SupervisoryLevel.E6)
      return "E-6";
    if (code == SupervisoryLevel.E7)
      return "E-7";
    if (code == SupervisoryLevel.E8)
      return "E-8";
    if (code == SupervisoryLevel.E9)
      return "E-9";
    if (code == SupervisoryLevel.O1)
      return "O-1";
    if (code == SupervisoryLevel.O2)
      return "O-2";
    if (code == SupervisoryLevel.O3)
      return "O-3";
    if (code == SupervisoryLevel.O4)
      return "O-4";
    if (code == SupervisoryLevel.O5)
      return "O-5";
    if (code == SupervisoryLevel.O6)
      return "O-6";
    if (code == SupervisoryLevel.O7)
      return "O-7";
    if (code == SupervisoryLevel.O8)
      return "O-8";
    if (code == SupervisoryLevel.O9)
      return "O-9";
    if (code == SupervisoryLevel.O10)
      return "O-10";
    if (code == SupervisoryLevel.W1)
      return "W-1";
    if (code == SupervisoryLevel.W2)
      return "W-2";
    if (code == SupervisoryLevel.W3)
      return "W-3";
    if (code == SupervisoryLevel.W4)
      return "W-4";
    if (code == SupervisoryLevel.W5)
      return "W-5";
    if (code == SupervisoryLevel.C3)
      return "C-3";
    return "?";
  }

    public String toSystem(SupervisoryLevel code) {
      return code.getSystem();
      }

}

