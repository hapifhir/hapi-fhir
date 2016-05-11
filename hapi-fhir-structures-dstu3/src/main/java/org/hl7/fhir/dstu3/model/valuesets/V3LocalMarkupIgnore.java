package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3LocalMarkupIgnore {

        /**
         * all
         */
        ALL, 
        /**
         * markup
         */
        MARKUP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3LocalMarkupIgnore fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("all".equals(codeString))
          return ALL;
        if ("markup".equals(codeString))
          return MARKUP;
        throw new FHIRException("Unknown V3LocalMarkupIgnore code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALL: return "all";
            case MARKUP: return "markup";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/LocalMarkupIgnore";
        }
        public String getDefinition() {
          switch (this) {
            case ALL: return "all";
            case MARKUP: return "markup";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALL: return "all";
            case MARKUP: return "markup";
            default: return "?";
          }
    }


}

