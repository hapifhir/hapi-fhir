package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum AdjudicationError {

        /**
         * Missing Identifier
         */
        A001, 
        /**
         * Missing Creation Date
         */
        A002, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AdjudicationError fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("a001".equals(codeString))
          return A001;
        if ("a002".equals(codeString))
          return A002;
        throw new FHIRException("Unknown AdjudicationError code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A001: return "a001";
            case A002: return "a002";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/adjudication-error";
        }
        public String getDefinition() {
          switch (this) {
            case A001: return "Missing Identifier";
            case A002: return "Missing Creation Date";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A001: return "Missing Identifier";
            case A002: return "Missing Creation Date";
            default: return "?";
          }
    }


}

