package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum FmConditions {

        /**
         * Headache
         */
        _123987, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FmConditions fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("123987".equals(codeString))
          return _123987;
        throw new FHIRException("Unknown FmConditions code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _123987: return "123987";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/fm-conditions";
        }
        public String getDefinition() {
          switch (this) {
            case _123987: return "Headache";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _123987: return "Headache";
            default: return "?";
          }
    }


}

