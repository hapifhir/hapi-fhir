package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Payeetype {

        /**
         * null
         */
        SUBSCRIBER, 
        /**
         * null
         */
        PROVIDER, 
        /**
         * null
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Payeetype fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("subscriber".equals(codeString))
          return SUBSCRIBER;
        if ("provider".equals(codeString))
          return PROVIDER;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown Payeetype code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SUBSCRIBER: return "subscriber";
            case PROVIDER: return "provider";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/payeetype";
        }
        public String getDefinition() {
          switch (this) {
            case SUBSCRIBER: return "";
            case PROVIDER: return "";
            case OTHER: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SUBSCRIBER: return "subscriber";
            case PROVIDER: return "provider";
            case OTHER: return "other";
            default: return "?";
          }
    }


}

