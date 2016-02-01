package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum EncounterType {

        /**
         * null
         */
        ADMS, 
        /**
         * null
         */
        BD_BMCLIN, 
        /**
         * null
         */
        CCS60, 
        /**
         * null
         */
        OKI, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ADMS".equals(codeString))
          return ADMS;
        if ("BD/BM-clin".equals(codeString))
          return BD_BMCLIN;
        if ("CCS60".equals(codeString))
          return CCS60;
        if ("OKI".equals(codeString))
          return OKI;
        throw new FHIRException("Unknown EncounterType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADMS: return "ADMS";
            case BD_BMCLIN: return "BD/BM-clin";
            case CCS60: return "CCS60";
            case OKI: return "OKI";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/encounter-type";
        }
        public String getDefinition() {
          switch (this) {
            case ADMS: return "";
            case BD_BMCLIN: return "";
            case CCS60: return "";
            case OKI: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADMS: return "Annual diabetes mellitus screening";
            case BD_BMCLIN: return "Bone drilling/bone marrow punction in clinic";
            case CCS60: return "Infant colon screening - 60 minutes";
            case OKI: return "Outpatient Kenacort injection";
            default: return "?";
          }
    }


}

