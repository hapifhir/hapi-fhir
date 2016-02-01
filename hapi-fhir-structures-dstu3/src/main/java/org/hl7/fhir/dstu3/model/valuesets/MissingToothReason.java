package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum MissingToothReason {

        /**
         * Extraction
         */
        E, 
        /**
         * Congenital
         */
        C, 
        /**
         * Unknown
         */
        U, 
        /**
         * Other
         */
        O, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MissingToothReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("E".equals(codeString))
          return E;
        if ("C".equals(codeString))
          return C;
        if ("U".equals(codeString))
          return U;
        if ("O".equals(codeString))
          return O;
        throw new FHIRException("Unknown MissingToothReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case E: return "E";
            case C: return "C";
            case U: return "U";
            case O: return "O";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/missingtoothreason";
        }
        public String getDefinition() {
          switch (this) {
            case E: return "Extraction";
            case C: return "Congenital";
            case U: return "Unknown";
            case O: return "Other";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case E: return "E";
            case C: return "C";
            case U: return "U";
            case O: return "O";
            default: return "?";
          }
    }


}

