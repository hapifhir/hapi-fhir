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

public class ReferencerangeMeaningEnumFactory implements EnumFactory<ReferencerangeMeaning> {

  public ReferencerangeMeaning fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("type".equals(codeString))
      return ReferencerangeMeaning.TYPE;
    if ("normal".equals(codeString))
      return ReferencerangeMeaning.NORMAL;
    if ("recommended".equals(codeString))
      return ReferencerangeMeaning.RECOMMENDED;
    if ("treatment".equals(codeString))
      return ReferencerangeMeaning.TREATMENT;
    if ("therapeutic".equals(codeString))
      return ReferencerangeMeaning.THERAPEUTIC;
    if ("pre".equals(codeString))
      return ReferencerangeMeaning.PRE;
    if ("post".equals(codeString))
      return ReferencerangeMeaning.POST;
    if ("endocrine".equals(codeString))
      return ReferencerangeMeaning.ENDOCRINE;
    if ("pre-puberty".equals(codeString))
      return ReferencerangeMeaning.PREPUBERTY;
    if ("follicular".equals(codeString))
      return ReferencerangeMeaning.FOLLICULAR;
    if ("midcycle".equals(codeString))
      return ReferencerangeMeaning.MIDCYCLE;
    if ("luteal".equals(codeString))
      return ReferencerangeMeaning.LUTEAL;
    if ("postmeopausal".equals(codeString))
      return ReferencerangeMeaning.POSTMEOPAUSAL;
    throw new IllegalArgumentException("Unknown ReferencerangeMeaning code '"+codeString+"'");
  }

  public String toCode(ReferencerangeMeaning code) {
    if (code == ReferencerangeMeaning.TYPE)
      return "type";
    if (code == ReferencerangeMeaning.NORMAL)
      return "normal";
    if (code == ReferencerangeMeaning.RECOMMENDED)
      return "recommended";
    if (code == ReferencerangeMeaning.TREATMENT)
      return "treatment";
    if (code == ReferencerangeMeaning.THERAPEUTIC)
      return "therapeutic";
    if (code == ReferencerangeMeaning.PRE)
      return "pre";
    if (code == ReferencerangeMeaning.POST)
      return "post";
    if (code == ReferencerangeMeaning.ENDOCRINE)
      return "endocrine";
    if (code == ReferencerangeMeaning.PREPUBERTY)
      return "pre-puberty";
    if (code == ReferencerangeMeaning.FOLLICULAR)
      return "follicular";
    if (code == ReferencerangeMeaning.MIDCYCLE)
      return "midcycle";
    if (code == ReferencerangeMeaning.LUTEAL)
      return "luteal";
    if (code == ReferencerangeMeaning.POSTMEOPAUSAL)
      return "postmeopausal";
    return "?";
  }

    public String toSystem(ReferencerangeMeaning code) {
      return code.getSystem();
      }

}

