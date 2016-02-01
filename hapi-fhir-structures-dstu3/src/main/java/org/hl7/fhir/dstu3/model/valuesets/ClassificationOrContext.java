package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ClassificationOrContext {

        /**
         * Indicates the useContext is a classification - e.g. Administrative, financial, etc.
         */
        CLASSIFICATION, 
        /**
         * Indicates the useContext is a context - a domain of use - e.g. Particular country, organization or system
         */
        CONTEXT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ClassificationOrContext fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("classification".equals(codeString))
          return CLASSIFICATION;
        if ("context".equals(codeString))
          return CONTEXT;
        throw new FHIRException("Unknown ClassificationOrContext code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CLASSIFICATION: return "classification";
            case CONTEXT: return "context";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/classification-or-context";
        }
        public String getDefinition() {
          switch (this) {
            case CLASSIFICATION: return "Indicates the useContext is a classification - e.g. Administrative, financial, etc.";
            case CONTEXT: return "Indicates the useContext is a context - a domain of use - e.g. Particular country, organization or system";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CLASSIFICATION: return "Classification";
            case CONTEXT: return "Context";
            default: return "?";
          }
    }


}

