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


import org.hl7.fhir.exceptions.FHIRException;

public enum MedicationKnowledgeCharacter {

        /**
         * Identyifying marks on product
         */
        IMPRINTCD, 
        /**
         * Description of size of the product
         */
        SIZE, 
        /**
         * Description of shape of the product
         */
        SHAPE, 
        /**
         * Description of color of the product
         */
        COLOR, 
        /**
         * Description of coating of the product
         */
        COATING, 
        /**
         * Description of scoring of the product
         */
        SCORING, 
        /**
         * Description of logo of the product
         */
        LOGO, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationKnowledgeCharacter fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("imprintcd".equals(codeString))
          return IMPRINTCD;
        if ("size".equals(codeString))
          return SIZE;
        if ("shape".equals(codeString))
          return SHAPE;
        if ("color".equals(codeString))
          return COLOR;
        if ("coating".equals(codeString))
          return COATING;
        if ("scoring".equals(codeString))
          return SCORING;
        if ("logo".equals(codeString))
          return LOGO;
        throw new FHIRException("Unknown MedicationKnowledgeCharacter code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IMPRINTCD: return "imprintcd";
            case SIZE: return "size";
            case SHAPE: return "shape";
            case COLOR: return "color";
            case COATING: return "coating";
            case SCORING: return "scoring";
            case LOGO: return "logo";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/medicationKnowledge-character";
        }
        public String getDefinition() {
          switch (this) {
            case IMPRINTCD: return "Identyifying marks on product";
            case SIZE: return "Description of size of the product";
            case SHAPE: return "Description of shape of the product";
            case COLOR: return "Description of color of the product";
            case COATING: return "Description of coating of the product";
            case SCORING: return "Description of scoring of the product";
            case LOGO: return "Description of logo of the product";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IMPRINTCD: return "Imprint Code";
            case SIZE: return "Size";
            case SHAPE: return "Shape";
            case COLOR: return "Color";
            case COATING: return "Coating";
            case SCORING: return "Scoring";
            case LOGO: return "Logo";
            default: return "?";
          }
    }


}

