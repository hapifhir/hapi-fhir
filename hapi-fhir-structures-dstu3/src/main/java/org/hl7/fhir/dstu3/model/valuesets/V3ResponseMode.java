package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ResponseMode {

        /**
         * The receiver may respond in a non-immediate manner. Note: this will be the default value.
         */
        D, 
        /**
         * The receiver is required to assume that the sender is blocking and behave appropriately by sending an immediate response.
         */
        I, 
        /**
         * The receiver shall keep any application responses in a queue until such time as the queue is polled.
         */
        Q, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ResponseMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("D".equals(codeString))
          return D;
        if ("I".equals(codeString))
          return I;
        if ("Q".equals(codeString))
          return Q;
        throw new FHIRException("Unknown V3ResponseMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case D: return "D";
            case I: return "I";
            case Q: return "Q";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ResponseMode";
        }
        public String getDefinition() {
          switch (this) {
            case D: return "The receiver may respond in a non-immediate manner. Note: this will be the default value.";
            case I: return "The receiver is required to assume that the sender is blocking and behave appropriately by sending an immediate response.";
            case Q: return "The receiver shall keep any application responses in a queue until such time as the queue is polled.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case D: return "deferred";
            case I: return "immediate";
            case Q: return "queue";
            default: return "?";
          }
    }


}

