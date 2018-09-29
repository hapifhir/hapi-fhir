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

public class V3Hl7VoteResolutionEnumFactory implements EnumFactory<V3Hl7VoteResolution> {

  public V3Hl7VoteResolution fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("affirmativeResolution".equals(codeString))
      return V3Hl7VoteResolution.AFFIRMATIVERESOLUTION;
    if ("affdef".equals(codeString))
      return V3Hl7VoteResolution.AFFDEF;
    if ("affi".equals(codeString))
      return V3Hl7VoteResolution.AFFI;
    if ("affr".equals(codeString))
      return V3Hl7VoteResolution.AFFR;
    if ("negativeResolution".equals(codeString))
      return V3Hl7VoteResolution.NEGATIVERESOLUTION;
    if ("nonsubp".equals(codeString))
      return V3Hl7VoteResolution.NONSUBP;
    if ("nonsubv".equals(codeString))
      return V3Hl7VoteResolution.NONSUBV;
    if ("notrelp".equals(codeString))
      return V3Hl7VoteResolution.NOTRELP;
    if ("notrelv".equals(codeString))
      return V3Hl7VoteResolution.NOTRELV;
    if ("prevcons".equals(codeString))
      return V3Hl7VoteResolution.PREVCONS;
    if ("retract".equals(codeString))
      return V3Hl7VoteResolution.RETRACT;
    if ("unresolved".equals(codeString))
      return V3Hl7VoteResolution.UNRESOLVED;
    if ("withdraw".equals(codeString))
      return V3Hl7VoteResolution.WITHDRAW;
    throw new IllegalArgumentException("Unknown V3Hl7VoteResolution code '"+codeString+"'");
  }

  public String toCode(V3Hl7VoteResolution code) {
    if (code == V3Hl7VoteResolution.AFFIRMATIVERESOLUTION)
      return "affirmativeResolution";
    if (code == V3Hl7VoteResolution.AFFDEF)
      return "affdef";
    if (code == V3Hl7VoteResolution.AFFI)
      return "affi";
    if (code == V3Hl7VoteResolution.AFFR)
      return "affr";
    if (code == V3Hl7VoteResolution.NEGATIVERESOLUTION)
      return "negativeResolution";
    if (code == V3Hl7VoteResolution.NONSUBP)
      return "nonsubp";
    if (code == V3Hl7VoteResolution.NONSUBV)
      return "nonsubv";
    if (code == V3Hl7VoteResolution.NOTRELP)
      return "notrelp";
    if (code == V3Hl7VoteResolution.NOTRELV)
      return "notrelv";
    if (code == V3Hl7VoteResolution.PREVCONS)
      return "prevcons";
    if (code == V3Hl7VoteResolution.RETRACT)
      return "retract";
    if (code == V3Hl7VoteResolution.UNRESOLVED)
      return "unresolved";
    if (code == V3Hl7VoteResolution.WITHDRAW)
      return "withdraw";
    return "?";
  }

    public String toSystem(V3Hl7VoteResolution code) {
      return code.getSystem();
      }

}

