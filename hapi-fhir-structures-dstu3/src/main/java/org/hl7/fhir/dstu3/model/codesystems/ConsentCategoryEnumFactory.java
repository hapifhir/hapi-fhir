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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class ConsentCategoryEnumFactory implements EnumFactory<ConsentCategory> {

  public ConsentCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("cat1".equals(codeString))
      return ConsentCategory.CAT1;
    if ("advance-directive".equals(codeString))
      return ConsentCategory.ADVANCEDIRECTIVE;
    if ("cat2".equals(codeString))
      return ConsentCategory.CAT2;
    if ("medical-consent".equals(codeString))
      return ConsentCategory.MEDICALCONSENT;
    if ("cat3".equals(codeString))
      return ConsentCategory.CAT3;
    if ("hipaa".equals(codeString))
      return ConsentCategory.HIPAA;
    if ("SSA-827".equals(codeString))
      return ConsentCategory.SSA827;
    if ("cat4".equals(codeString))
      return ConsentCategory.CAT4;
    if ("DCH-3927".equals(codeString))
      return ConsentCategory.DCH3927;
    if ("squaxin".equals(codeString))
      return ConsentCategory.SQUAXIN;
    if ("cat5".equals(codeString))
      return ConsentCategory.CAT5;
    if ("nl-lsp".equals(codeString))
      return ConsentCategory.NLLSP;
    if ("at-elga".equals(codeString))
      return ConsentCategory.ATELGA;
    if ("cat6".equals(codeString))
      return ConsentCategory.CAT6;
    if ("nih-hipaa".equals(codeString))
      return ConsentCategory.NIHHIPAA;
    if ("nci".equals(codeString))
      return ConsentCategory.NCI;
    if ("nih-grdr".equals(codeString))
      return ConsentCategory.NIHGRDR;
    if ("va-10-10116".equals(codeString))
      return ConsentCategory.VA1010116;
    if ("nih-527".equals(codeString))
      return ConsentCategory.NIH527;
    if ("ga4gh".equals(codeString))
      return ConsentCategory.GA4GH;
    throw new IllegalArgumentException("Unknown ConsentCategory code '"+codeString+"'");
  }

  public String toCode(ConsentCategory code) {
    if (code == ConsentCategory.CAT1)
      return "cat1";
    if (code == ConsentCategory.ADVANCEDIRECTIVE)
      return "advance-directive";
    if (code == ConsentCategory.CAT2)
      return "cat2";
    if (code == ConsentCategory.MEDICALCONSENT)
      return "medical-consent";
    if (code == ConsentCategory.CAT3)
      return "cat3";
    if (code == ConsentCategory.HIPAA)
      return "hipaa";
    if (code == ConsentCategory.SSA827)
      return "SSA-827";
    if (code == ConsentCategory.CAT4)
      return "cat4";
    if (code == ConsentCategory.DCH3927)
      return "DCH-3927";
    if (code == ConsentCategory.SQUAXIN)
      return "squaxin";
    if (code == ConsentCategory.CAT5)
      return "cat5";
    if (code == ConsentCategory.NLLSP)
      return "nl-lsp";
    if (code == ConsentCategory.ATELGA)
      return "at-elga";
    if (code == ConsentCategory.CAT6)
      return "cat6";
    if (code == ConsentCategory.NIHHIPAA)
      return "nih-hipaa";
    if (code == ConsentCategory.NCI)
      return "nci";
    if (code == ConsentCategory.NIHGRDR)
      return "nih-grdr";
    if (code == ConsentCategory.VA1010116)
      return "va-10-10116";
    if (code == ConsentCategory.NIH527)
      return "nih-527";
    if (code == ConsentCategory.GA4GH)
      return "ga4gh";
    return "?";
  }

    public String toSystem(ConsentCategory code) {
      return code.getSystem();
      }

}

