package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3LocalRemoteControlState {

        /**
         * An equipment can either work autonomously ('Local' control state).
         */
        L, 
        /**
         * An equipment can be controlled by another system, e.g., LAS computer ('Remote' control state).
         */
        R, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3LocalRemoteControlState fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("L".equals(codeString))
          return L;
        if ("R".equals(codeString))
          return R;
        throw new FHIRException("Unknown V3LocalRemoteControlState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case L: return "L";
            case R: return "R";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/LocalRemoteControlState";
        }
        public String getDefinition() {
          switch (this) {
            case L: return "An equipment can either work autonomously ('Local' control state).";
            case R: return "An equipment can be controlled by another system, e.g., LAS computer ('Remote' control state).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case L: return "Local";
            case R: return "Remote";
            default: return "?";
          }
    }


}

