package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3QueryPriority {

        /**
         * Query response is deferred.
         */
        D, 
        /**
         * Query response is immediate.
         */
        I, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3QueryPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("D".equals(codeString))
          return D;
        if ("I".equals(codeString))
          return I;
        throw new FHIRException("Unknown V3QueryPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case D: return "D";
            case I: return "I";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/QueryPriority";
        }
        public String getDefinition() {
          switch (this) {
            case D: return "Query response is deferred.";
            case I: return "Query response is immediate.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case D: return "Deferred";
            case I: return "Immediate";
            default: return "?";
          }
    }


}

