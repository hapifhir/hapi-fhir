package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Forms {

        /**
         * null
         */
        _1, 
        /**
         * null
         */
        _2, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Forms fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        throw new FHIRException("Unknown Forms code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/forms-codes";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "";
            case _2: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            default: return "?";
          }
    }


}

