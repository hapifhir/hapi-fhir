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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class CertaintySubcomponentRatingEnumFactory implements EnumFactory<CertaintySubcomponentRating> {

  public CertaintySubcomponentRating fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("no-change".equals(codeString))
      return CertaintySubcomponentRating.NOCHANGE;
    if ("downcode1".equals(codeString))
      return CertaintySubcomponentRating.DOWNCODE1;
    if ("downcode2".equals(codeString))
      return CertaintySubcomponentRating.DOWNCODE2;
    if ("downcode3".equals(codeString))
      return CertaintySubcomponentRating.DOWNCODE3;
    if ("upcode1".equals(codeString))
      return CertaintySubcomponentRating.UPCODE1;
    if ("upcode2".equals(codeString))
      return CertaintySubcomponentRating.UPCODE2;
    if ("no-concern".equals(codeString))
      return CertaintySubcomponentRating.NOCONCERN;
    if ("serious-concern".equals(codeString))
      return CertaintySubcomponentRating.SERIOUSCONCERN;
    if ("critical-concern".equals(codeString))
      return CertaintySubcomponentRating.CRITICALCONCERN;
    if ("present".equals(codeString))
      return CertaintySubcomponentRating.PRESENT;
    if ("absent".equals(codeString))
      return CertaintySubcomponentRating.ABSENT;
    throw new IllegalArgumentException("Unknown CertaintySubcomponentRating code '"+codeString+"'");
  }

  public String toCode(CertaintySubcomponentRating code) {
    if (code == CertaintySubcomponentRating.NOCHANGE)
      return "no-change";
    if (code == CertaintySubcomponentRating.DOWNCODE1)
      return "downcode1";
    if (code == CertaintySubcomponentRating.DOWNCODE2)
      return "downcode2";
    if (code == CertaintySubcomponentRating.DOWNCODE3)
      return "downcode3";
    if (code == CertaintySubcomponentRating.UPCODE1)
      return "upcode1";
    if (code == CertaintySubcomponentRating.UPCODE2)
      return "upcode2";
    if (code == CertaintySubcomponentRating.NOCONCERN)
      return "no-concern";
    if (code == CertaintySubcomponentRating.SERIOUSCONCERN)
      return "serious-concern";
    if (code == CertaintySubcomponentRating.CRITICALCONCERN)
      return "critical-concern";
    if (code == CertaintySubcomponentRating.PRESENT)
      return "present";
    if (code == CertaintySubcomponentRating.ABSENT)
      return "absent";
    return "?";
  }

    public String toSystem(CertaintySubcomponentRating code) {
      return code.getSystem();
      }

}

