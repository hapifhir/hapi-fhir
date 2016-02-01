package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ProcessingMode {

        /**
         * Identifies archive mode of processing.
         */
        A, 
        /**
         * Identifies initial load mode of processing.
         */
        I, 
        /**
         * Identifies restore mode of processing.
         */
        R, 
        /**
         * Identifies on-line mode of processing.
         */
        T, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ProcessingMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("I".equals(codeString))
          return I;
        if ("R".equals(codeString))
          return R;
        if ("T".equals(codeString))
          return T;
        throw new FHIRException("Unknown V3ProcessingMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case I: return "I";
            case R: return "R";
            case T: return "T";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ProcessingMode";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "Identifies archive mode of processing.";
            case I: return "Identifies initial load mode of processing.";
            case R: return "Identifies restore mode of processing.";
            case T: return "Identifies on-line mode of processing.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "Archive";
            case I: return "Initial load";
            case R: return "Restore from archive";
            case T: return "Current processing";
            default: return "?";
          }
    }


}

