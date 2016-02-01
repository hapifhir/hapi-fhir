package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ProcessingID {

        /**
         * Identifies debugging type of processing.
         */
        D, 
        /**
         * Identifies production type of processing.
         */
        P, 
        /**
         * Identifies training type of processing.
         */
        T, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ProcessingID fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("D".equals(codeString))
          return D;
        if ("P".equals(codeString))
          return P;
        if ("T".equals(codeString))
          return T;
        throw new FHIRException("Unknown V3ProcessingID code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case D: return "D";
            case P: return "P";
            case T: return "T";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ProcessingID";
        }
        public String getDefinition() {
          switch (this) {
            case D: return "Identifies debugging type of processing.";
            case P: return "Identifies production type of processing.";
            case T: return "Identifies training type of processing.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case D: return "Debugging";
            case P: return "Production";
            case T: return "Training";
            default: return "?";
          }
    }


}

