package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EncounterAdmissionSource {

        /**
         * emergency
         */
        E, 
        /**
         * labor and delivery
         */
        LD, 
        /**
         * newborn
         */
        NB, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EncounterAdmissionSource fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("E".equals(codeString))
          return E;
        if ("LD".equals(codeString))
          return LD;
        if ("NB".equals(codeString))
          return NB;
        throw new FHIRException("Unknown V3EncounterAdmissionSource code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case E: return "E";
            case LD: return "LD";
            case NB: return "NB";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EncounterAdmissionSource";
        }
        public String getDefinition() {
          switch (this) {
            case E: return "emergency";
            case LD: return "labor and delivery";
            case NB: return "newborn";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case E: return "emergency";
            case LD: return "labor and delivery";
            case NB: return "newborn";
            default: return "?";
          }
    }


}

