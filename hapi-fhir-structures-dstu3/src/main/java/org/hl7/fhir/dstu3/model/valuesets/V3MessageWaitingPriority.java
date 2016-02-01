package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3MessageWaitingPriority {

        /**
         * High priority messages are available
         */
        H, 
        /**
         * Low priority messages are available
         */
        L, 
        /**
         * Medium priority messages are available
         */
        M, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3MessageWaitingPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("H".equals(codeString))
          return H;
        if ("L".equals(codeString))
          return L;
        if ("M".equals(codeString))
          return M;
        throw new FHIRException("Unknown V3MessageWaitingPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case H: return "H";
            case L: return "L";
            case M: return "M";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/MessageWaitingPriority";
        }
        public String getDefinition() {
          switch (this) {
            case H: return "High priority messages are available";
            case L: return "Low priority messages are available";
            case M: return "Medium priority messages are available";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case H: return "High";
            case L: return "Low";
            case M: return "Medium";
            default: return "?";
          }
    }


}

