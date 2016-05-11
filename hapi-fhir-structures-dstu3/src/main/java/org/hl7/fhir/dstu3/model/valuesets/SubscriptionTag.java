package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum SubscriptionTag {

        /**
         * The message has been queued for processing on a destination systems.
         */
        QUEUED, 
        /**
         * The message has been delivered to its intended recipient.
         */
        DELIVERED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SubscriptionTag fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("queued".equals(codeString))
          return QUEUED;
        if ("delivered".equals(codeString))
          return DELIVERED;
        throw new FHIRException("Unknown SubscriptionTag code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case QUEUED: return "queued";
            case DELIVERED: return "delivered";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/subscription-tag";
        }
        public String getDefinition() {
          switch (this) {
            case QUEUED: return "The message has been queued for processing on a destination systems.";
            case DELIVERED: return "The message has been delivered to its intended recipient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case QUEUED: return "Queued";
            case DELIVERED: return "Delivered";
            default: return "?";
          }
    }


}

