package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum PaymentStatus {

        /**
         * null
         */
        PAID, 
        /**
         * null
         */
        CLEARED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PaymentStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("paid".equals(codeString))
          return PAID;
        if ("cleared".equals(codeString))
          return CLEARED;
        throw new FHIRException("Unknown PaymentStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PAID: return "paid";
            case CLEARED: return "cleared";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/paymentstatus";
        }
        public String getDefinition() {
          switch (this) {
            case PAID: return "";
            case CLEARED: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PAID: return "paid";
            case CLEARED: return "cleared";
            default: return "?";
          }
    }


}

