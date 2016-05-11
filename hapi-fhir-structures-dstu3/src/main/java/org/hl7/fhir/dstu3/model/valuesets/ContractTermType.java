package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ContractTermType {

        /**
         * null
         */
        ORALHEALTH, 
        /**
         * null
         */
        VISION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractTermType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("OralHealth".equals(codeString))
          return ORALHEALTH;
        if ("Vision".equals(codeString))
          return VISION;
        throw new FHIRException("Unknown ContractTermType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ORALHEALTH: return "OralHealth";
            case VISION: return "Vision";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contracttermtypecodes";
        }
        public String getDefinition() {
          switch (this) {
            case ORALHEALTH: return "";
            case VISION: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ORALHEALTH: return "OralHealth";
            case VISION: return "Vision";
            default: return "?";
          }
    }


}

