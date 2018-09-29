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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.r4.model.EnumFactory;

public class FhirVersionsEnumFactory implements EnumFactory<FhirVersions> {

  public FhirVersions fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("0.01".equals(codeString))
      return FhirVersions._0_01;
    if ("0.05".equals(codeString))
      return FhirVersions._0_05;
    if ("0.06".equals(codeString))
      return FhirVersions._0_06;
    if ("0.11".equals(codeString))
      return FhirVersions._0_11;
    if ("0.0.80".equals(codeString))
      return FhirVersions._0_0_80;
    if ("0.0.81".equals(codeString))
      return FhirVersions._0_0_81;
    if ("0.0.82".equals(codeString))
      return FhirVersions._0_0_82;
    if ("0.4.0".equals(codeString))
      return FhirVersions._0_4_0;
    if ("0.5.0".equals(codeString))
      return FhirVersions._0_5_0;
    if ("1.0.0".equals(codeString))
      return FhirVersions._1_0_0;
    if ("1.0.1".equals(codeString))
      return FhirVersions._1_0_1;
    if ("1.0.2".equals(codeString))
      return FhirVersions._1_0_2;
    if ("1.1.0".equals(codeString))
      return FhirVersions._1_1_0;
    if ("1.4.0".equals(codeString))
      return FhirVersions._1_4_0;
    if ("1.6.0".equals(codeString))
      return FhirVersions._1_6_0;
    if ("1.8.0".equals(codeString))
      return FhirVersions._1_8_0;
    if ("3.0.0".equals(codeString))
      return FhirVersions._3_0_0;
    if ("3.0.1".equals(codeString))
      return FhirVersions._3_0_1;
    if ("3.5.0".equals(codeString))
      return FhirVersions._3_5_0;
    throw new IllegalArgumentException("Unknown FhirVersions code '"+codeString+"'");
  }

  public String toCode(FhirVersions code) {
    if (code == FhirVersions._0_01)
      return "0.01";
    if (code == FhirVersions._0_05)
      return "0.05";
    if (code == FhirVersions._0_06)
      return "0.06";
    if (code == FhirVersions._0_11)
      return "0.11";
    if (code == FhirVersions._0_0_80)
      return "0.0.80";
    if (code == FhirVersions._0_0_81)
      return "0.0.81";
    if (code == FhirVersions._0_0_82)
      return "0.0.82";
    if (code == FhirVersions._0_4_0)
      return "0.4.0";
    if (code == FhirVersions._0_5_0)
      return "0.5.0";
    if (code == FhirVersions._1_0_0)
      return "1.0.0";
    if (code == FhirVersions._1_0_1)
      return "1.0.1";
    if (code == FhirVersions._1_0_2)
      return "1.0.2";
    if (code == FhirVersions._1_1_0)
      return "1.1.0";
    if (code == FhirVersions._1_4_0)
      return "1.4.0";
    if (code == FhirVersions._1_6_0)
      return "1.6.0";
    if (code == FhirVersions._1_8_0)
      return "1.8.0";
    if (code == FhirVersions._3_0_0)
      return "3.0.0";
    if (code == FhirVersions._3_0_1)
      return "3.0.1";
    if (code == FhirVersions._3_5_0)
      return "3.5.0";
    return "?";
  }

    public String toSystem(FhirVersions code) {
      return code.getSystem();
      }

}

