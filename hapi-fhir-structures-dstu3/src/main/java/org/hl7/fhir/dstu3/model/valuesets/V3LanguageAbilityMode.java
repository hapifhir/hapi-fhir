package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3LanguageAbilityMode {

        /**
         * Expressed signed
         */
        ESGN, 
        /**
         * Expressed spoken
         */
        ESP, 
        /**
         * Expressed written
         */
        EWR, 
        /**
         * Received signed
         */
        RSGN, 
        /**
         * Received spoken
         */
        RSP, 
        /**
         * Received written
         */
        RWR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3LanguageAbilityMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ESGN".equals(codeString))
          return ESGN;
        if ("ESP".equals(codeString))
          return ESP;
        if ("EWR".equals(codeString))
          return EWR;
        if ("RSGN".equals(codeString))
          return RSGN;
        if ("RSP".equals(codeString))
          return RSP;
        if ("RWR".equals(codeString))
          return RWR;
        throw new FHIRException("Unknown V3LanguageAbilityMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ESGN: return "ESGN";
            case ESP: return "ESP";
            case EWR: return "EWR";
            case RSGN: return "RSGN";
            case RSP: return "RSP";
            case RWR: return "RWR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/LanguageAbilityMode";
        }
        public String getDefinition() {
          switch (this) {
            case ESGN: return "Expressed signed";
            case ESP: return "Expressed spoken";
            case EWR: return "Expressed written";
            case RSGN: return "Received signed";
            case RSP: return "Received spoken";
            case RWR: return "Received written";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ESGN: return "Expressed signed";
            case ESP: return "Expressed spoken";
            case EWR: return "Expressed written";
            case RSGN: return "Received signed";
            case RSP: return "Received spoken";
            case RWR: return "Received written";
            default: return "?";
          }
    }


}

