package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum BenefitCategory {

        /**
         * Dental and Oral Health Coverage
         */
        ORAL, 
        /**
         * Vision Health Coverage
         */
        VISION, 
        /**
         * Medical Health Coverage
         */
        MEDICAL, 
        /**
         * Pharmacy Coverage
         */
        PHARMACY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BenefitCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("oral".equals(codeString))
          return ORAL;
        if ("vision".equals(codeString))
          return VISION;
        if ("medical".equals(codeString))
          return MEDICAL;
        if ("pharmacy".equals(codeString))
          return PHARMACY;
        throw new FHIRException("Unknown BenefitCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ORAL: return "oral";
            case VISION: return "vision";
            case MEDICAL: return "medical";
            case PHARMACY: return "pharmacy";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-category";
        }
        public String getDefinition() {
          switch (this) {
            case ORAL: return "Dental and Oral Health Coverage";
            case VISION: return "Vision Health Coverage";
            case MEDICAL: return "Medical Health Coverage";
            case PHARMACY: return "Pharmacy Coverage";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ORAL: return "Dental and Oral Health Coverage";
            case VISION: return "Vision Health Coverage";
            case MEDICAL: return "Medical Health Coverage";
            case PHARMACY: return "Pharmacy Coverage";
            default: return "?";
          }
    }


}

