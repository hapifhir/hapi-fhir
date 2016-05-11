package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ServicePharmacy {

        /**
         * Smoking cessation
         */
        SMOKECESS, 
        /**
         * Flu Shot
         */
        FLUSHOT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServicePharmacy fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("smokecess".equals(codeString))
          return SMOKECESS;
        if ("flushot".equals(codeString))
          return FLUSHOT;
        throw new FHIRException("Unknown ServicePharmacy code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SMOKECESS: return "smokecess";
            case FLUSHOT: return "flushot";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-pharmaservice";
        }
        public String getDefinition() {
          switch (this) {
            case SMOKECESS: return "Smoking cessation";
            case FLUSHOT: return "Flu Shot";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SMOKECESS: return "Smoking cessation";
            case FLUSHOT: return "Flu Shot";
            default: return "?";
          }
    }


}

