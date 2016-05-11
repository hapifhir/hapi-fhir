package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3GenderStatus {

        /**
         * Reproductively intact
         */
        I, 
        /**
         * Reproductively neutered
         */
        N, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3GenderStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("I".equals(codeString))
          return I;
        if ("N".equals(codeString))
          return N;
        throw new FHIRException("Unknown V3GenderStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case I: return "I";
            case N: return "N";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/GenderStatus";
        }
        public String getDefinition() {
          switch (this) {
            case I: return "Reproductively intact";
            case N: return "Reproductively neutered";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case I: return "Intact";
            case N: return "Neutered";
            default: return "?";
          }
    }


}

