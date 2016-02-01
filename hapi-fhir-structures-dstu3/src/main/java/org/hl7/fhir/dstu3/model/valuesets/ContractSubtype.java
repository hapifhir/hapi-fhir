package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ContractSubtype {

        /**
         * null
         */
        DISCLOSURECA, 
        /**
         * null
         */
        DISCLOSUREUS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractSubtype fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("disclosure-CA".equals(codeString))
          return DISCLOSURECA;
        if ("disclosure-US".equals(codeString))
          return DISCLOSUREUS;
        throw new FHIRException("Unknown ContractSubtype code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DISCLOSURECA: return "disclosure-CA";
            case DISCLOSUREUS: return "disclosure-US";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contractsubtypecodes";
        }
        public String getDefinition() {
          switch (this) {
            case DISCLOSURECA: return "";
            case DISCLOSUREUS: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DISCLOSURECA: return "disclosure-CA";
            case DISCLOSUREUS: return "disclosure-US";
            default: return "?";
          }
    }


}

