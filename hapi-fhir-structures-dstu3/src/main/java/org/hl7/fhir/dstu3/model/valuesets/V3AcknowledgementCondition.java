package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3AcknowledgementCondition {

        /**
         * Always send an acknowledgement.
         */
        AL, 
        /**
         * Send an acknowledgement for error/reject conditions only.
         */
        ER, 
        /**
         * Never send an acknowledgement.
         */
        NE, 
        /**
         * Send an acknowledgement for successful completions only.
         */
        SU, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3AcknowledgementCondition fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AL".equals(codeString))
          return AL;
        if ("ER".equals(codeString))
          return ER;
        if ("NE".equals(codeString))
          return NE;
        if ("SU".equals(codeString))
          return SU;
        throw new FHIRException("Unknown V3AcknowledgementCondition code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AL: return "AL";
            case ER: return "ER";
            case NE: return "NE";
            case SU: return "SU";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/AcknowledgementCondition";
        }
        public String getDefinition() {
          switch (this) {
            case AL: return "Always send an acknowledgement.";
            case ER: return "Send an acknowledgement for error/reject conditions only.";
            case NE: return "Never send an acknowledgement.";
            case SU: return "Send an acknowledgement for successful completions only.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AL: return "Always";
            case ER: return "Error/reject only";
            case NE: return "Never";
            case SU: return "Successful only";
            default: return "?";
          }
    }


}

