package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3Sequencing {

        /**
         * Ascending sequence order.
         */
        A, 
        /**
         * Descending sequence order.
         */
        D, 
        /**
         * No enforced sequence order.
         */
        N, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Sequencing fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("D".equals(codeString))
          return D;
        if ("N".equals(codeString))
          return N;
        throw new FHIRException("Unknown V3Sequencing code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case D: return "D";
            case N: return "N";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/Sequencing";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "Ascending sequence order.";
            case D: return "Descending sequence order.";
            case N: return "No enforced sequence order.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "Ascending";
            case D: return "Descending";
            case N: return "None";
            default: return "?";
          }
    }


}

