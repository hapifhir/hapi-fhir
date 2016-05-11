package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ContractType {

        /**
         * null
         */
        PRIVACY, 
        /**
         * null
         */
        DISCLOSURE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("privacy".equals(codeString))
          return PRIVACY;
        if ("disclosure".equals(codeString))
          return DISCLOSURE;
        throw new FHIRException("Unknown ContractType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRIVACY: return "privacy";
            case DISCLOSURE: return "disclosure";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contracttypecodes";
        }
        public String getDefinition() {
          switch (this) {
            case PRIVACY: return "";
            case DISCLOSURE: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRIVACY: return "privacy";
            case DISCLOSURE: return "disclosure";
            default: return "?";
          }
    }


}

