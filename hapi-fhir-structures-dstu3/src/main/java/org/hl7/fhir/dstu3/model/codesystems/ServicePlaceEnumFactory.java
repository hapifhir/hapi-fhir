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

public class ServicePlaceEnumFactory implements EnumFactory<ServicePlace> {

  public ServicePlace fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("01".equals(codeString))
      return ServicePlace._01;
    if ("03".equals(codeString))
      return ServicePlace._03;
    if ("04".equals(codeString))
      return ServicePlace._04;
    if ("05".equals(codeString))
      return ServicePlace._05;
    if ("06".equals(codeString))
      return ServicePlace._06;
    if ("07".equals(codeString))
      return ServicePlace._07;
    if ("08".equals(codeString))
      return ServicePlace._08;
    if ("09".equals(codeString))
      return ServicePlace._09;
    if ("11".equals(codeString))
      return ServicePlace._11;
    if ("12".equals(codeString))
      return ServicePlace._12;
    if ("13".equals(codeString))
      return ServicePlace._13;
    if ("14".equals(codeString))
      return ServicePlace._14;
    if ("15".equals(codeString))
      return ServicePlace._15;
    if ("19".equals(codeString))
      return ServicePlace._19;
    if ("20".equals(codeString))
      return ServicePlace._20;
    if ("21".equals(codeString))
      return ServicePlace._21;
    if ("41".equals(codeString))
      return ServicePlace._41;
    throw new IllegalArgumentException("Unknown ServicePlace code '"+codeString+"'");
  }

  public String toCode(ServicePlace code) {
    if (code == ServicePlace._01)
      return "01";
    if (code == ServicePlace._03)
      return "03";
    if (code == ServicePlace._04)
      return "04";
    if (code == ServicePlace._05)
      return "05";
    if (code == ServicePlace._06)
      return "06";
    if (code == ServicePlace._07)
      return "07";
    if (code == ServicePlace._08)
      return "08";
    if (code == ServicePlace._09)
      return "09";
    if (code == ServicePlace._11)
      return "11";
    if (code == ServicePlace._12)
      return "12";
    if (code == ServicePlace._13)
      return "13";
    if (code == ServicePlace._14)
      return "14";
    if (code == ServicePlace._15)
      return "15";
    if (code == ServicePlace._19)
      return "19";
    if (code == ServicePlace._20)
      return "20";
    if (code == ServicePlace._21)
      return "21";
    if (code == ServicePlace._41)
      return "41";
    return "?";
  }

    public String toSystem(ServicePlace code) {
      return code.getSystem();
      }

}

