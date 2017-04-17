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


import org.hl7.fhir.exceptions.FHIRException;

public enum SubstanceCategory {

        /**
         * A substance that causes an allergic reaction.
         */
        ALLERGEN, 
        /**
         * A substance that is produced by or extracted from a biological source.
         */
        BIOLOGICAL, 
        /**
         * A substance that comes directly from a human or an animal (e.g. blood, urine, feces, tears, etc.).
         */
        BODY, 
        /**
         * Any organic or inorganic substance of a particular molecular identity, including -- (i) any combination of such substances occurring in whole or in part as a result of a chemical reaction or occurring in nature and (ii) any element or uncombined radical (http://www.epa.gov/opptintr/import-export/pubs/importguide.pdf).
         */
        CHEMICAL, 
        /**
         * A food, dietary ingredient, or dietary supplement for human or animal.
         */
        FOOD, 
        /**
         * A substance intended for use in the diagnosis, cure, mitigation, treatment, or prevention of disease in man or other animals (Federal Food Drug and Cosmetic Act).
         */
        DRUG, 
        /**
         * A finished product which is not normally ingested, absorbed or injected (e.g. steel, iron, wood, plastic and paper).
         */
        MATERIAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SubstanceCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("allergen".equals(codeString))
          return ALLERGEN;
        if ("biological".equals(codeString))
          return BIOLOGICAL;
        if ("body".equals(codeString))
          return BODY;
        if ("chemical".equals(codeString))
          return CHEMICAL;
        if ("food".equals(codeString))
          return FOOD;
        if ("drug".equals(codeString))
          return DRUG;
        if ("material".equals(codeString))
          return MATERIAL;
        throw new FHIRException("Unknown SubstanceCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALLERGEN: return "allergen";
            case BIOLOGICAL: return "biological";
            case BODY: return "body";
            case CHEMICAL: return "chemical";
            case FOOD: return "food";
            case DRUG: return "drug";
            case MATERIAL: return "material";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/substance-category";
        }
        public String getDefinition() {
          switch (this) {
            case ALLERGEN: return "A substance that causes an allergic reaction.";
            case BIOLOGICAL: return "A substance that is produced by or extracted from a biological source.";
            case BODY: return "A substance that comes directly from a human or an animal (e.g. blood, urine, feces, tears, etc.).";
            case CHEMICAL: return "Any organic or inorganic substance of a particular molecular identity, including -- (i) any combination of such substances occurring in whole or in part as a result of a chemical reaction or occurring in nature and (ii) any element or uncombined radical (http://www.epa.gov/opptintr/import-export/pubs/importguide.pdf).";
            case FOOD: return "A food, dietary ingredient, or dietary supplement for human or animal.";
            case DRUG: return "A substance intended for use in the diagnosis, cure, mitigation, treatment, or prevention of disease in man or other animals (Federal Food Drug and Cosmetic Act).";
            case MATERIAL: return "A finished product which is not normally ingested, absorbed or injected (e.g. steel, iron, wood, plastic and paper).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALLERGEN: return "Allergen";
            case BIOLOGICAL: return "Biological Substance";
            case BODY: return "Body Substance";
            case CHEMICAL: return "Chemical";
            case FOOD: return "Dietary Substance";
            case DRUG: return "Drug or Medicament";
            case MATERIAL: return "Material";
            default: return "?";
          }
    }


}

