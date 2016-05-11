package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum BenefitUnit {

        /**
         * A single individual
         */
        INDIVIDUAL, 
        /**
         * A family, typically includes self, spous(s) and children to a defined age
         */
        FAMILY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BenefitUnit fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("individual".equals(codeString))
          return INDIVIDUAL;
        if ("family".equals(codeString))
          return FAMILY;
        throw new FHIRException("Unknown BenefitUnit code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INDIVIDUAL: return "individual";
            case FAMILY: return "family";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-unit";
        }
        public String getDefinition() {
          switch (this) {
            case INDIVIDUAL: return "A single individual";
            case FAMILY: return "A family, typically includes self, spous(s) and children to a defined age";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INDIVIDUAL: return "Individual";
            case FAMILY: return "Family";
            default: return "?";
          }
    }


}

