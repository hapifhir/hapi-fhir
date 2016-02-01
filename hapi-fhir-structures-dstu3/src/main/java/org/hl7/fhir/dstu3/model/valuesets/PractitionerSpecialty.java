package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum PractitionerSpecialty {

        /**
         * null
         */
        CARDIO, 
        /**
         * null
         */
        DENT, 
        /**
         * null
         */
        DIETARY, 
        /**
         * null
         */
        MIDW, 
        /**
         * null
         */
        SYSARCH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PractitionerSpecialty fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cardio".equals(codeString))
          return CARDIO;
        if ("dent".equals(codeString))
          return DENT;
        if ("dietary".equals(codeString))
          return DIETARY;
        if ("midw".equals(codeString))
          return MIDW;
        if ("sysarch".equals(codeString))
          return SYSARCH;
        throw new FHIRException("Unknown PractitionerSpecialty code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CARDIO: return "cardio";
            case DENT: return "dent";
            case DIETARY: return "dietary";
            case MIDW: return "midw";
            case SYSARCH: return "sysarch";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/practitioner-specialty";
        }
        public String getDefinition() {
          switch (this) {
            case CARDIO: return "";
            case DENT: return "";
            case DIETARY: return "";
            case MIDW: return "";
            case SYSARCH: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CARDIO: return "Cardiologist";
            case DENT: return "Dentist";
            case DIETARY: return "Dietary consultant";
            case MIDW: return "Midwife";
            case SYSARCH: return "Systems architect";
            default: return "?";
          }
    }


}

