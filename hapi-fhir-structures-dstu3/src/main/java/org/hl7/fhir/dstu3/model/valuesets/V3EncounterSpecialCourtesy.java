package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EncounterSpecialCourtesy {

        /**
         * extended courtesy
         */
        EXT, 
        /**
         * normal courtesy
         */
        NRM, 
        /**
         * professional courtesy
         */
        PRF, 
        /**
         * Courtesies extended to the staff of the entity providing service.
         */
        STF, 
        /**
         * very important person
         */
        VIP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EncounterSpecialCourtesy fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("EXT".equals(codeString))
          return EXT;
        if ("NRM".equals(codeString))
          return NRM;
        if ("PRF".equals(codeString))
          return PRF;
        if ("STF".equals(codeString))
          return STF;
        if ("VIP".equals(codeString))
          return VIP;
        throw new FHIRException("Unknown V3EncounterSpecialCourtesy code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EXT: return "EXT";
            case NRM: return "NRM";
            case PRF: return "PRF";
            case STF: return "STF";
            case VIP: return "VIP";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EncounterSpecialCourtesy";
        }
        public String getDefinition() {
          switch (this) {
            case EXT: return "extended courtesy";
            case NRM: return "normal courtesy";
            case PRF: return "professional courtesy";
            case STF: return "Courtesies extended to the staff of the entity providing service.";
            case VIP: return "very important person";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EXT: return "extended courtesy";
            case NRM: return "normal courtesy";
            case PRF: return "professional courtesy";
            case STF: return "staff";
            case VIP: return "very important person";
            default: return "?";
          }
    }


}

