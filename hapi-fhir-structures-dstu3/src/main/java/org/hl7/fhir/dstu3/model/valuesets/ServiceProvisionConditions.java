package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ServiceProvisionConditions {

        /**
         * This service is available for no patient cost.
         */
        FREE, 
        /**
         * There are discounts available on this service for qualifying patients.
         */
        DISC, 
        /**
         * Fees apply for this service.
         */
        COST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServiceProvisionConditions fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("free".equals(codeString))
          return FREE;
        if ("disc".equals(codeString))
          return DISC;
        if ("cost".equals(codeString))
          return COST;
        throw new FHIRException("Unknown ServiceProvisionConditions code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FREE: return "free";
            case DISC: return "disc";
            case COST: return "cost";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/service-provision-conditions";
        }
        public String getDefinition() {
          switch (this) {
            case FREE: return "This service is available for no patient cost.";
            case DISC: return "There are discounts available on this service for qualifying patients.";
            case COST: return "Fees apply for this service.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FREE: return "Free";
            case DISC: return "Discounts Available";
            case COST: return "Fees apply";
            default: return "?";
          }
    }


}

