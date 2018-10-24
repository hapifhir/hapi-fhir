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

public class V3Hl7PublishingSubSectionEnumFactory implements EnumFactory<V3Hl7PublishingSubSection> {

  public V3Hl7PublishingSubSection fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("CO".equals(codeString))
      return V3Hl7PublishingSubSection.CO;
    if ("FI".equals(codeString))
      return V3Hl7PublishingSubSection.FI;
    if ("MC".equals(codeString))
      return V3Hl7PublishingSubSection.MC;
    if ("MF".equals(codeString))
      return V3Hl7PublishingSubSection.MF;
    if ("PO".equals(codeString))
      return V3Hl7PublishingSubSection.PO;
    if ("PR".equals(codeString))
      return V3Hl7PublishingSubSection.PR;
    if ("QU".equals(codeString))
      return V3Hl7PublishingSubSection.QU;
    if ("RC".equals(codeString))
      return V3Hl7PublishingSubSection.RC;
    if ("RE".equals(codeString))
      return V3Hl7PublishingSubSection.RE;
    if ("UU".equals(codeString))
      return V3Hl7PublishingSubSection.UU;
    throw new IllegalArgumentException("Unknown V3Hl7PublishingSubSection code '"+codeString+"'");
  }

  public String toCode(V3Hl7PublishingSubSection code) {
    if (code == V3Hl7PublishingSubSection.CO)
      return "CO";
    if (code == V3Hl7PublishingSubSection.FI)
      return "FI";
    if (code == V3Hl7PublishingSubSection.MC)
      return "MC";
    if (code == V3Hl7PublishingSubSection.MF)
      return "MF";
    if (code == V3Hl7PublishingSubSection.PO)
      return "PO";
    if (code == V3Hl7PublishingSubSection.PR)
      return "PR";
    if (code == V3Hl7PublishingSubSection.QU)
      return "QU";
    if (code == V3Hl7PublishingSubSection.RC)
      return "RC";
    if (code == V3Hl7PublishingSubSection.RE)
      return "RE";
    if (code == V3Hl7PublishingSubSection.UU)
      return "UU";
    return "?";
  }

    public String toSystem(V3Hl7PublishingSubSection code) {
      return code.getSystem();
      }

}

