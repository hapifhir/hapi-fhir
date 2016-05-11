package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum EncounterPriority {

        /**
         * Within seconds.
         */
        IMM, 
        /**
         * Within 10 minutes.
         */
        EMG, 
        /**
         * Within 30 minutes.
         */
        URG, 
        /**
         * Within 60 minutes.
         */
        SURG, 
        /**
         * Within 120 minutes.
         */
        NOURG, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("imm".equals(codeString))
          return IMM;
        if ("emg".equals(codeString))
          return EMG;
        if ("urg".equals(codeString))
          return URG;
        if ("s-urg".equals(codeString))
          return SURG;
        if ("no-urg".equals(codeString))
          return NOURG;
        throw new FHIRException("Unknown EncounterPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IMM: return "imm";
            case EMG: return "emg";
            case URG: return "urg";
            case SURG: return "s-urg";
            case NOURG: return "no-urg";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/encounter-priority";
        }
        public String getDefinition() {
          switch (this) {
            case IMM: return "Within seconds.";
            case EMG: return "Within 10 minutes.";
            case URG: return "Within 30 minutes.";
            case SURG: return "Within 60 minutes.";
            case NOURG: return "Within 120 minutes.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IMM: return "Immediate";
            case EMG: return "Emergency";
            case URG: return "Urgent";
            case SURG: return "Semi-urgent";
            case NOURG: return "Non-urgent";
            default: return "?";
          }
    }


}

