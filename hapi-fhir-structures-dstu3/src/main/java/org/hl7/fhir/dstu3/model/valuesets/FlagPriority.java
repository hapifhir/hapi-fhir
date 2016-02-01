package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum FlagPriority {

        /**
         * No alarm.
         */
        PN, 
        /**
         * Low priority.
         */
        PL, 
        /**
         * Medium priority.
         */
        PM, 
        /**
         * High priority.
         */
        PH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FlagPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("PN".equals(codeString))
          return PN;
        if ("PL".equals(codeString))
          return PL;
        if ("PM".equals(codeString))
          return PM;
        if ("PH".equals(codeString))
          return PH;
        throw new FHIRException("Unknown FlagPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PN: return "PN";
            case PL: return "PL";
            case PM: return "PM";
            case PH: return "PH";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/flag-priority-code";
        }
        public String getDefinition() {
          switch (this) {
            case PN: return "No alarm.";
            case PL: return "Low priority.";
            case PM: return "Medium priority.";
            case PH: return "High priority.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PN: return "No alarm";
            case PL: return "Low priority";
            case PM: return "Medium priority";
            case PH: return "High priority";
            default: return "?";
          }
    }


}

