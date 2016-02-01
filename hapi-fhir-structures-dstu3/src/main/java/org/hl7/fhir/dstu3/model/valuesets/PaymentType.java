package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum PaymentType {

        /**
         * null
         */
        PAYMENT, 
        /**
         * null
         */
        ADJUSTMENT, 
        /**
         * null
         */
        ADVANCE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PaymentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("payment".equals(codeString))
          return PAYMENT;
        if ("adjustment".equals(codeString))
          return ADJUSTMENT;
        if ("advance".equals(codeString))
          return ADVANCE;
        throw new FHIRException("Unknown PaymentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PAYMENT: return "payment";
            case ADJUSTMENT: return "adjustment";
            case ADVANCE: return "advance";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/payment-type";
        }
        public String getDefinition() {
          switch (this) {
            case PAYMENT: return "";
            case ADJUSTMENT: return "";
            case ADVANCE: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PAYMENT: return "payment";
            case ADJUSTMENT: return "adjustment";
            case ADVANCE: return "advance";
            default: return "?";
          }
    }


}

