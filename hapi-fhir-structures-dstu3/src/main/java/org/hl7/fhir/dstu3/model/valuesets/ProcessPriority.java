package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ProcessPriority {

        /**
         * Immediately in real time.
         */
        STAT, 
        /**
         * With best effort.
         */
        NORMAL, 
        /**
         * Later, when possible.
         */
        DEFERRED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcessPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("stat".equals(codeString))
          return STAT;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("deferred".equals(codeString))
          return DEFERRED;
        throw new FHIRException("Unknown ProcessPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case STAT: return "stat";
            case NORMAL: return "normal";
            case DEFERRED: return "deferred";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/processpriority";
        }
        public String getDefinition() {
          switch (this) {
            case STAT: return "Immediately in real time.";
            case NORMAL: return "With best effort.";
            case DEFERRED: return "Later, when possible.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case STAT: return "Immediate";
            case NORMAL: return "Normal";
            case DEFERRED: return "Deferred";
            default: return "?";
          }
    }


}

