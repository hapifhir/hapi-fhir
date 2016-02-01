package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Fundsreserve {

        /**
         * null
         */
        PATIENT, 
        /**
         * null
         */
        PROVIDER, 
        /**
         * null
         */
        NONE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Fundsreserve fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("provider".equals(codeString))
          return PROVIDER;
        if ("none".equals(codeString))
          return NONE;
        throw new FHIRException("Unknown Fundsreserve code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "patient";
            case PROVIDER: return "provider";
            case NONE: return "none";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/fundsreserve";
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "";
            case PROVIDER: return "";
            case NONE: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "patient";
            case PROVIDER: return "provider";
            case NONE: return "none";
            default: return "?";
          }
    }


}

