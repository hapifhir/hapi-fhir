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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum EncounterDiet {

        /**
         * Food without meat, poultry or seafood.
         */
        VEGETARIAN, 
        /**
         * Excludes dairy products.
         */
        DAIRYFREE, 
        /**
         * Excludes ingredients containing nuts.
         */
        NUTFREE, 
        /**
         * Excludes ingredients containing gluten.
         */
        GLUTENFREE, 
        /**
         * Food without meat, poultry, seafood, eggs, dairy products and other animal-derived substances.
         */
        VEGAN, 
        /**
         * Foods that conform to Islamic law.
         */
        HALAL, 
        /**
         * Foods that conform to Jewish dietary law.
         */
        KOSHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterDiet fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("vegetarian".equals(codeString))
          return VEGETARIAN;
        if ("dairy-free".equals(codeString))
          return DAIRYFREE;
        if ("nut-free".equals(codeString))
          return NUTFREE;
        if ("gluten-free".equals(codeString))
          return GLUTENFREE;
        if ("vegan".equals(codeString))
          return VEGAN;
        if ("halal".equals(codeString))
          return HALAL;
        if ("kosher".equals(codeString))
          return KOSHER;
        throw new FHIRException("Unknown EncounterDiet code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case VEGETARIAN: return "vegetarian";
            case DAIRYFREE: return "dairy-free";
            case NUTFREE: return "nut-free";
            case GLUTENFREE: return "gluten-free";
            case VEGAN: return "vegan";
            case HALAL: return "halal";
            case KOSHER: return "kosher";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/diet";
        }
        public String getDefinition() {
          switch (this) {
            case VEGETARIAN: return "Food without meat, poultry or seafood.";
            case DAIRYFREE: return "Excludes dairy products.";
            case NUTFREE: return "Excludes ingredients containing nuts.";
            case GLUTENFREE: return "Excludes ingredients containing gluten.";
            case VEGAN: return "Food without meat, poultry, seafood, eggs, dairy products and other animal-derived substances.";
            case HALAL: return "Foods that conform to Islamic law.";
            case KOSHER: return "Foods that conform to Jewish dietary law.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case VEGETARIAN: return "Vegetarian";
            case DAIRYFREE: return "Dairy Free";
            case NUTFREE: return "Nut Free";
            case GLUTENFREE: return "Gluten Free";
            case VEGAN: return "Vegan";
            case HALAL: return "Halal";
            case KOSHER: return "Kosher";
            default: return "?";
          }
    }


}

