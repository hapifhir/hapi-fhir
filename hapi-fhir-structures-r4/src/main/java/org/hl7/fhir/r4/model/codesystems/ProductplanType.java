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


import org.hl7.fhir.exceptions.FHIRException;

public enum ProductplanType {

        /**
         * null
         */
        MEDICAL, 
        /**
         * null
         */
        DENTAL, 
        /**
         * null
         */
        MENTAL, 
        /**
         * null
         */
        SUBSTAB, 
        /**
         * null
         */
        VISION, 
        /**
         * null
         */
        DRUG, 
        /**
         * null
         */
        SHORTTERM, 
        /**
         * null
         */
        LONGTERM, 
        /**
         * null
         */
        HOSPICE, 
        /**
         * null
         */
        HOME, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProductplanType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("medical".equals(codeString))
          return MEDICAL;
        if ("dental".equals(codeString))
          return DENTAL;
        if ("mental".equals(codeString))
          return MENTAL;
        if ("subst-ab".equals(codeString))
          return SUBSTAB;
        if ("vision".equals(codeString))
          return VISION;
        if ("Drug".equals(codeString))
          return DRUG;
        if ("short-term".equals(codeString))
          return SHORTTERM;
        if ("long-term".equals(codeString))
          return LONGTERM;
        if ("hospice".equals(codeString))
          return HOSPICE;
        if ("home".equals(codeString))
          return HOME;
        throw new FHIRException("Unknown ProductplanType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MEDICAL: return "medical";
            case DENTAL: return "dental";
            case MENTAL: return "mental";
            case SUBSTAB: return "subst-ab";
            case VISION: return "vision";
            case DRUG: return "Drug";
            case SHORTTERM: return "short-term";
            case LONGTERM: return "long-term";
            case HOSPICE: return "hospice";
            case HOME: return "home";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/product-plan-type";
        }
        public String getDefinition() {
          switch (this) {
            case MEDICAL: return "";
            case DENTAL: return "";
            case MENTAL: return "";
            case SUBSTAB: return "";
            case VISION: return "";
            case DRUG: return "";
            case SHORTTERM: return "";
            case LONGTERM: return "";
            case HOSPICE: return "";
            case HOME: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MEDICAL: return "Medical";
            case DENTAL: return "Dental";
            case MENTAL: return "Mental Health";
            case SUBSTAB: return "Substance Abuse";
            case VISION: return "Vision";
            case DRUG: return "Drug";
            case SHORTTERM: return "Short Term";
            case LONGTERM: return "Long Term Care";
            case HOSPICE: return "Hospice";
            case HOME: return "Home Health";
            default: return "?";
          }
    }


}

