package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Intervention {

        /**
         * Unknown
         */
        UNKNOWN, 
        /**
         * Other
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Intervention fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown Intervention code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/intervention";
        }
        public String getDefinition() {
          switch (this) {
            case UNKNOWN: return "Unknown";
            case OTHER: return "Other";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNKNOWN: return "Unknown";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

