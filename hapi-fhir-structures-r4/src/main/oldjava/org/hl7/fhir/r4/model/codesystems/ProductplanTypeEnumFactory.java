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

// Generated on Mon, Jul 2, 2018 20:32-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class ProductplanTypeEnumFactory implements EnumFactory<ProductplanType> {

  public ProductplanType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("medical".equals(codeString))
      return ProductplanType.MEDICAL;
    if ("dental".equals(codeString))
      return ProductplanType.DENTAL;
    if ("mental".equals(codeString))
      return ProductplanType.MENTAL;
    if ("subst-ab".equals(codeString))
      return ProductplanType.SUBSTAB;
    if ("vision".equals(codeString))
      return ProductplanType.VISION;
    if ("Drug".equals(codeString))
      return ProductplanType.DRUG;
    if ("short-term".equals(codeString))
      return ProductplanType.SHORTTERM;
    if ("long-term".equals(codeString))
      return ProductplanType.LONGTERM;
    if ("hospice".equals(codeString))
      return ProductplanType.HOSPICE;
    if ("home".equals(codeString))
      return ProductplanType.HOME;
    throw new IllegalArgumentException("Unknown ProductplanType code '"+codeString+"'");
  }

  public String toCode(ProductplanType code) {
    if (code == ProductplanType.MEDICAL)
      return "medical";
    if (code == ProductplanType.DENTAL)
      return "dental";
    if (code == ProductplanType.MENTAL)
      return "mental";
    if (code == ProductplanType.SUBSTAB)
      return "subst-ab";
    if (code == ProductplanType.VISION)
      return "vision";
    if (code == ProductplanType.DRUG)
      return "Drug";
    if (code == ProductplanType.SHORTTERM)
      return "short-term";
    if (code == ProductplanType.LONGTERM)
      return "long-term";
    if (code == ProductplanType.HOSPICE)
      return "hospice";
    if (code == ProductplanType.HOME)
      return "home";
    return "?";
  }

    public String toSystem(ProductplanType code) {
      return code.getSystem();
      }

}

