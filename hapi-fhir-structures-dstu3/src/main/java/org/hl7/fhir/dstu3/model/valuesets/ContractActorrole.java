package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ContractActorrole {

        /**
         * null
         */
        PRACTITIONER, 
        /**
         * null
         */
        PATIENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractActorrole fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("patient".equals(codeString))
          return PATIENT;
        throw new FHIRException("Unknown ContractActorrole code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRACTITIONER: return "practitioner";
            case PATIENT: return "patient";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://www.hl7.org/fhir/contractactorrole";
        }
        public String getDefinition() {
          switch (this) {
            case PRACTITIONER: return "";
            case PATIENT: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRACTITIONER: return "Practitioner";
            case PATIENT: return "Patient";
            default: return "?";
          }
    }


}

