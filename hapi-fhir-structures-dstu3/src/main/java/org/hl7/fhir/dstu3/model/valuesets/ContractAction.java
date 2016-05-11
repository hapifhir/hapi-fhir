package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ContractAction {

        /**
         * null
         */
        ACTIONA, 
        /**
         * null
         */
        ACTIONB, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractAction fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("action-a".equals(codeString))
          return ACTIONA;
        if ("action-b".equals(codeString))
          return ACTIONB;
        throw new FHIRException("Unknown ContractAction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIONA: return "action-a";
            case ACTIONB: return "action-b";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://www.hl7.org/fhir/contractaction";
        }
        public String getDefinition() {
          switch (this) {
            case ACTIONA: return "";
            case ACTIONB: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIONA: return "action-a";
            case ACTIONB: return "action-b";
            default: return "?";
          }
    }


}

