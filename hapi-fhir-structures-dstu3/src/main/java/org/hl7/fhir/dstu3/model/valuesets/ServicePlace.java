package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ServicePlace {

        /**
         * Emergency Department
         */
        EMERGENCY, 
        /**
         * Clinic
         */
        CLINIC, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServicePlace fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("emergency".equals(codeString))
          return EMERGENCY;
        if ("clinic".equals(codeString))
          return CLINIC;
        throw new FHIRException("Unknown ServicePlace code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EMERGENCY: return "emergency";
            case CLINIC: return "clinic";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-serviceplace";
        }
        public String getDefinition() {
          switch (this) {
            case EMERGENCY: return "Emergency Department";
            case CLINIC: return "Clinic";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EMERGENCY: return "Emergency Department";
            case CLINIC: return "Clinic";
            default: return "?";
          }
    }


}

