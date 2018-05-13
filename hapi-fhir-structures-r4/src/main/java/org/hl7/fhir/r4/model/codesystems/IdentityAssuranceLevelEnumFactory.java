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

public class IdentityAssuranceLevelEnumFactory implements EnumFactory<IdentityAssuranceLevel> {

  public IdentityAssuranceLevel fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("level1".equals(codeString))
      return IdentityAssuranceLevel.LEVEL1;
    if ("level2".equals(codeString))
      return IdentityAssuranceLevel.LEVEL2;
    if ("level3".equals(codeString))
      return IdentityAssuranceLevel.LEVEL3;
    if ("level4".equals(codeString))
      return IdentityAssuranceLevel.LEVEL4;
    throw new IllegalArgumentException("Unknown IdentityAssuranceLevel code '"+codeString+"'");
  }

  public String toCode(IdentityAssuranceLevel code) {
    if (code == IdentityAssuranceLevel.LEVEL1)
      return "level1";
    if (code == IdentityAssuranceLevel.LEVEL2)
      return "level2";
    if (code == IdentityAssuranceLevel.LEVEL3)
      return "level3";
    if (code == IdentityAssuranceLevel.LEVEL4)
      return "level4";
    return "?";
  }

    public String toSystem(IdentityAssuranceLevel code) {
      return code.getSystem();
      }

}

