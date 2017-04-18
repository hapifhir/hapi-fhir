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

public class ServiceUsclsEnumFactory implements EnumFactory<ServiceUscls> {

  public ServiceUscls fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1101".equals(codeString))
      return ServiceUscls._1101;
    if ("1102".equals(codeString))
      return ServiceUscls._1102;
    if ("1103".equals(codeString))
      return ServiceUscls._1103;
    if ("1201".equals(codeString))
      return ServiceUscls._1201;
    if ("1205".equals(codeString))
      return ServiceUscls._1205;
    if ("2101".equals(codeString))
      return ServiceUscls._2101;
    if ("2102".equals(codeString))
      return ServiceUscls._2102;
    if ("2141".equals(codeString))
      return ServiceUscls._2141;
    if ("2601".equals(codeString))
      return ServiceUscls._2601;
    if ("11101".equals(codeString))
      return ServiceUscls._11101;
    if ("11102".equals(codeString))
      return ServiceUscls._11102;
    if ("11103".equals(codeString))
      return ServiceUscls._11103;
    if ("11104".equals(codeString))
      return ServiceUscls._11104;
    if ("21211".equals(codeString))
      return ServiceUscls._21211;
    if ("21212".equals(codeString))
      return ServiceUscls._21212;
    if ("27211".equals(codeString))
      return ServiceUscls._27211;
    if ("67211".equals(codeString))
      return ServiceUscls._67211;
    if ("99111".equals(codeString))
      return ServiceUscls._99111;
    if ("99333".equals(codeString))
      return ServiceUscls._99333;
    if ("99555".equals(codeString))
      return ServiceUscls._99555;
    throw new IllegalArgumentException("Unknown ServiceUscls code '"+codeString+"'");
  }

  public String toCode(ServiceUscls code) {
    if (code == ServiceUscls._1101)
      return "1101";
    if (code == ServiceUscls._1102)
      return "1102";
    if (code == ServiceUscls._1103)
      return "1103";
    if (code == ServiceUscls._1201)
      return "1201";
    if (code == ServiceUscls._1205)
      return "1205";
    if (code == ServiceUscls._2101)
      return "2101";
    if (code == ServiceUscls._2102)
      return "2102";
    if (code == ServiceUscls._2141)
      return "2141";
    if (code == ServiceUscls._2601)
      return "2601";
    if (code == ServiceUscls._11101)
      return "11101";
    if (code == ServiceUscls._11102)
      return "11102";
    if (code == ServiceUscls._11103)
      return "11103";
    if (code == ServiceUscls._11104)
      return "11104";
    if (code == ServiceUscls._21211)
      return "21211";
    if (code == ServiceUscls._21212)
      return "21212";
    if (code == ServiceUscls._27211)
      return "27211";
    if (code == ServiceUscls._67211)
      return "67211";
    if (code == ServiceUscls._99111)
      return "99111";
    if (code == ServiceUscls._99333)
      return "99333";
    if (code == ServiceUscls._99555)
      return "99555";
    return "?";
  }

    public String toSystem(ServiceUscls code) {
      return code.getSystem();
      }

}

