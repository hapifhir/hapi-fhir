package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class SubstanceCategoryEnumFactory implements EnumFactory<SubstanceCategory> {

  public SubstanceCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("allergen".equals(codeString))
      return SubstanceCategory.ALLERGEN;
    if ("biological".equals(codeString))
      return SubstanceCategory.BIOLOGICAL;
    if ("body".equals(codeString))
      return SubstanceCategory.BODY;
    if ("chemical".equals(codeString))
      return SubstanceCategory.CHEMICAL;
    if ("food".equals(codeString))
      return SubstanceCategory.FOOD;
    if ("drug".equals(codeString))
      return SubstanceCategory.DRUG;
    if ("material".equals(codeString))
      return SubstanceCategory.MATERIAL;
    throw new IllegalArgumentException("Unknown SubstanceCategory code '"+codeString+"'");
  }

  public String toCode(SubstanceCategory code) {
    if (code == SubstanceCategory.ALLERGEN)
      return "allergen";
    if (code == SubstanceCategory.BIOLOGICAL)
      return "biological";
    if (code == SubstanceCategory.BODY)
      return "body";
    if (code == SubstanceCategory.CHEMICAL)
      return "chemical";
    if (code == SubstanceCategory.FOOD)
      return "food";
    if (code == SubstanceCategory.DRUG)
      return "drug";
    if (code == SubstanceCategory.MATERIAL)
      return "material";
    return "?";
  }


}

