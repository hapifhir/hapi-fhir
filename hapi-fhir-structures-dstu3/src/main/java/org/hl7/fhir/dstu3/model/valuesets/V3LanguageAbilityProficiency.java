package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3LanguageAbilityProficiency {

        /**
         * Excellent
         */
        E, 
        /**
         * Fair
         */
        F, 
        /**
         * Good
         */
        G, 
        /**
         * Poor
         */
        P, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3LanguageAbilityProficiency fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("E".equals(codeString))
          return E;
        if ("F".equals(codeString))
          return F;
        if ("G".equals(codeString))
          return G;
        if ("P".equals(codeString))
          return P;
        throw new FHIRException("Unknown V3LanguageAbilityProficiency code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case E: return "E";
            case F: return "F";
            case G: return "G";
            case P: return "P";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/LanguageAbilityProficiency";
        }
        public String getDefinition() {
          switch (this) {
            case E: return "Excellent";
            case F: return "Fair";
            case G: return "Good";
            case P: return "Poor";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case E: return "Excellent";
            case F: return "Fair";
            case G: return "Good";
            case P: return "Poor";
            default: return "?";
          }
    }


}

