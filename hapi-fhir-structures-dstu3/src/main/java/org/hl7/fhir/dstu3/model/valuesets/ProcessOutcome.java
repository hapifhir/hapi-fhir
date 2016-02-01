package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ProcessOutcome {

        /**
         * null
         */
        COMPLETE, 
        /**
         * null
         */
        PENDED, 
        /**
         * null
         */
        ERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcessOutcome fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("pended".equals(codeString))
          return PENDED;
        if ("error".equals(codeString))
          return ERROR;
        throw new FHIRException("Unknown ProcessOutcome code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLETE: return "complete";
            case PENDED: return "pended";
            case ERROR: return "error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/processoutcomecodes";
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETE: return "";
            case PENDED: return "";
            case ERROR: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETE: return "complete";
            case PENDED: return "pended";
            case ERROR: return "error";
            default: return "?";
          }
    }


}

