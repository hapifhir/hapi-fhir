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

public class RejectionCriteriaEnumFactory implements EnumFactory<RejectionCriteria> {

  public RejectionCriteria fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("hemolized".equals(codeString))
      return RejectionCriteria.HEMOLIZED;
    if ("insufficient".equals(codeString))
      return RejectionCriteria.INSUFFICIENT;
    if ("broken".equals(codeString))
      return RejectionCriteria.BROKEN;
    if ("clotted".equals(codeString))
      return RejectionCriteria.CLOTTED;
    if ("wrong-temperature".equals(codeString))
      return RejectionCriteria.WRONGTEMPERATURE;
    throw new IllegalArgumentException("Unknown RejectionCriteria code '"+codeString+"'");
  }

  public String toCode(RejectionCriteria code) {
    if (code == RejectionCriteria.HEMOLIZED)
      return "hemolized";
    if (code == RejectionCriteria.INSUFFICIENT)
      return "insufficient";
    if (code == RejectionCriteria.BROKEN)
      return "broken";
    if (code == RejectionCriteria.CLOTTED)
      return "clotted";
    if (code == RejectionCriteria.WRONGTEMPERATURE)
      return "wrong-temperature";
    return "?";
  }

    public String toSystem(RejectionCriteria code) {
      return code.getSystem();
      }

}

