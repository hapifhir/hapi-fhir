package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ImmunizationRecommendationStatus {

        /**
         * The patient is due for their next vaccination.
         */
        DUE, 
        /**
         * The patient is considered overdue for their next vaccination.
         */
        OVERDUE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ImmunizationRecommendationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("due".equals(codeString))
          return DUE;
        if ("overdue".equals(codeString))
          return OVERDUE;
        throw new FHIRException("Unknown ImmunizationRecommendationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DUE: return "due";
            case OVERDUE: return "overdue";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/immunization-recommendation-status";
        }
        public String getDefinition() {
          switch (this) {
            case DUE: return "The patient is due for their next vaccination.";
            case OVERDUE: return "The patient is considered overdue for their next vaccination.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DUE: return "Due";
            case OVERDUE: return "Overdue";
            default: return "?";
          }
    }


}

