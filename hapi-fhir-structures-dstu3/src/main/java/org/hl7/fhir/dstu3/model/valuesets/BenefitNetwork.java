package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum BenefitNetwork {

        /**
         * Services rendered by a Network provider
         */
        IN, 
        /**
         * Services rendered by a provider who is not in the Network
         */
        OUT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BenefitNetwork fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in".equals(codeString))
          return IN;
        if ("out".equals(codeString))
          return OUT;
        throw new FHIRException("Unknown BenefitNetwork code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IN: return "in";
            case OUT: return "out";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-network";
        }
        public String getDefinition() {
          switch (this) {
            case IN: return "Services rendered by a Network provider";
            case OUT: return "Services rendered by a provider who is not in the Network";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IN: return "In Network";
            case OUT: return "Out of Network";
            default: return "?";
          }
    }


}

