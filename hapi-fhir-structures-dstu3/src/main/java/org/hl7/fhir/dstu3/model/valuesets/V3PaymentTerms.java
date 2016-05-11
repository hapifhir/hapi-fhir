package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3PaymentTerms {

        /**
         * Payment in full for products and/or services is required as soon as the service is performed or goods delivered.
         */
        COD, 
        /**
         * Payment in full for products and/or services is required 30 days from the time the service is performed or goods delivered.
         */
        N30, 
        /**
         * Payment in full for products and/or services is required 60 days from the time the service is performed or goods delivered.
         */
        N60, 
        /**
         * Payment in full for products and/or services is required 90 days from the time the service is performed or goods delivered.
         */
        N90, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3PaymentTerms fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("COD".equals(codeString))
          return COD;
        if ("N30".equals(codeString))
          return N30;
        if ("N60".equals(codeString))
          return N60;
        if ("N90".equals(codeString))
          return N90;
        throw new FHIRException("Unknown V3PaymentTerms code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COD: return "COD";
            case N30: return "N30";
            case N60: return "N60";
            case N90: return "N90";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/PaymentTerms";
        }
        public String getDefinition() {
          switch (this) {
            case COD: return "Payment in full for products and/or services is required as soon as the service is performed or goods delivered.";
            case N30: return "Payment in full for products and/or services is required 30 days from the time the service is performed or goods delivered.";
            case N60: return "Payment in full for products and/or services is required 60 days from the time the service is performed or goods delivered.";
            case N90: return "Payment in full for products and/or services is required 90 days from the time the service is performed or goods delivered.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COD: return "Cash on Delivery";
            case N30: return "Net 30 days";
            case N60: return "Net 60 days";
            case N90: return "Net 90 days";
            default: return "?";
          }
    }


}

