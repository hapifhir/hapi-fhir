package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum AdjudicationReason {

        /**
         * Not covered
         */
        AR001, 
        /**
         * Plan Limit Reached
         */
        AR002, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AdjudicationReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ar001".equals(codeString))
          return AR001;
        if ("ar002".equals(codeString))
          return AR002;
        throw new FHIRException("Unknown AdjudicationReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AR001: return "ar001";
            case AR002: return "ar002";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/adjudication-reason";
        }
        public String getDefinition() {
          switch (this) {
            case AR001: return "Not covered";
            case AR002: return "Plan Limit Reached";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AR001: return "Not covered";
            case AR002: return "Plan Limit Reached";
            default: return "?";
          }
    }


}

