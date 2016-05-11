package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum PaymentAdjustmentReason {

        /**
         * Prior Payment Reversal
         */
        A001, 
        /**
         * Prior Overpayment
         */
        A002, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PaymentAdjustmentReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("a001".equals(codeString))
          return A001;
        if ("a002".equals(codeString))
          return A002;
        throw new FHIRException("Unknown PaymentAdjustmentReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A001: return "a001";
            case A002: return "a002";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/payment-adjustment-reason";
        }
        public String getDefinition() {
          switch (this) {
            case A001: return "Prior Payment Reversal";
            case A002: return "Prior Overpayment";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A001: return "Prior Payment Reversal";
            case A002: return "Prior Overpayment";
            default: return "?";
          }
    }


}

