package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum BenefitTerm {

        /**
         * Annual, renewing on the anniversary
         */
        ANNUAL, 
        /**
         * For the total term, lifetime, of the policy or coverage
         */
        LIFETIME, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BenefitTerm fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("annual".equals(codeString))
          return ANNUAL;
        if ("lifetime".equals(codeString))
          return LIFETIME;
        throw new FHIRException("Unknown BenefitTerm code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ANNUAL: return "annual";
            case LIFETIME: return "lifetime";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-term";
        }
        public String getDefinition() {
          switch (this) {
            case ANNUAL: return "Annual, renewing on the anniversary";
            case LIFETIME: return "For the total term, lifetime, of the policy or coverage";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ANNUAL: return "Annual";
            case LIFETIME: return "Lifetime";
            default: return "?";
          }
    }


}

