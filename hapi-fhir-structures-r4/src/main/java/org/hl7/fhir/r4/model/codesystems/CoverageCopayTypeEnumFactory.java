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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class CoverageCopayTypeEnumFactory implements EnumFactory<CoverageCopayType> {

  public CoverageCopayType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("gpvisit".equals(codeString))
      return CoverageCopayType.GPVISIT;
    if ("spvisit".equals(codeString))
      return CoverageCopayType.SPVISIT;
    if ("emergency".equals(codeString))
      return CoverageCopayType.EMERGENCY;
    if ("inpthosp".equals(codeString))
      return CoverageCopayType.INPTHOSP;
    if ("televisit".equals(codeString))
      return CoverageCopayType.TELEVISIT;
    if ("urgentcare".equals(codeString))
      return CoverageCopayType.URGENTCARE;
    if ("copaypct".equals(codeString))
      return CoverageCopayType.COPAYPCT;
    throw new IllegalArgumentException("Unknown CoverageCopayType code '"+codeString+"'");
  }

  public String toCode(CoverageCopayType code) {
    if (code == CoverageCopayType.GPVISIT)
      return "gpvisit";
    if (code == CoverageCopayType.SPVISIT)
      return "spvisit";
    if (code == CoverageCopayType.EMERGENCY)
      return "emergency";
    if (code == CoverageCopayType.INPTHOSP)
      return "inpthosp";
    if (code == CoverageCopayType.TELEVISIT)
      return "televisit";
    if (code == CoverageCopayType.URGENTCARE)
      return "urgentcare";
    if (code == CoverageCopayType.COPAYPCT)
      return "copaypct";
    return "?";
  }

    public String toSystem(CoverageCopayType code) {
      return code.getSystem();
      }

}

