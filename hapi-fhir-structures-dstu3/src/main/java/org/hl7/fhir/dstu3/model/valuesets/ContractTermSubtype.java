package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ContractTermSubtype {

        /**
         * null
         */
        ORALHEALTHBASIC, 
        /**
         * null
         */
        ORALHEALTHMAJOR, 
        /**
         * null
         */
        ORALHEALTHORTHODONTIC, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractTermSubtype fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("OralHealth-Basic".equals(codeString))
          return ORALHEALTHBASIC;
        if ("OralHealth-Major".equals(codeString))
          return ORALHEALTHMAJOR;
        if ("OralHealth-Orthodontic".equals(codeString))
          return ORALHEALTHORTHODONTIC;
        throw new FHIRException("Unknown ContractTermSubtype code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ORALHEALTHBASIC: return "OralHealth-Basic";
            case ORALHEALTHMAJOR: return "OralHealth-Major";
            case ORALHEALTHORTHODONTIC: return "OralHealth-Orthodontic";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contracttermsubtypecodes";
        }
        public String getDefinition() {
          switch (this) {
            case ORALHEALTHBASIC: return "";
            case ORALHEALTHMAJOR: return "";
            case ORALHEALTHORTHODONTIC: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ORALHEALTHBASIC: return "OralHealth-Basic";
            case ORALHEALTHMAJOR: return "OralHealth-Major";
            case ORALHEALTHORTHODONTIC: return "OralHealth-Orthodontic";
            default: return "?";
          }
    }


}

