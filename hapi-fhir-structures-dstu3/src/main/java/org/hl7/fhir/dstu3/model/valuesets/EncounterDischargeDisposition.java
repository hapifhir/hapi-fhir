package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum EncounterDischargeDisposition {

        /**
         * null
         */
        HOME, 
        /**
         * null
         */
        OTHERHCF, 
        /**
         * null
         */
        HOSP, 
        /**
         * null
         */
        LONG, 
        /**
         * null
         */
        AADVICE, 
        /**
         * null
         */
        EXP, 
        /**
         * null
         */
        PSY, 
        /**
         * null
         */
        REHAB, 
        /**
         * null
         */
        OTH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterDischargeDisposition fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return HOME;
        if ("other-hcf".equals(codeString))
          return OTHERHCF;
        if ("hosp".equals(codeString))
          return HOSP;
        if ("long".equals(codeString))
          return LONG;
        if ("aadvice".equals(codeString))
          return AADVICE;
        if ("exp".equals(codeString))
          return EXP;
        if ("psy".equals(codeString))
          return PSY;
        if ("rehab".equals(codeString))
          return REHAB;
        if ("oth".equals(codeString))
          return OTH;
        throw new FHIRException("Unknown EncounterDischargeDisposition code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HOME: return "home";
            case OTHERHCF: return "other-hcf";
            case HOSP: return "hosp";
            case LONG: return "long";
            case AADVICE: return "aadvice";
            case EXP: return "exp";
            case PSY: return "psy";
            case REHAB: return "rehab";
            case OTH: return "oth";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/discharge-disposition";
        }
        public String getDefinition() {
          switch (this) {
            case HOME: return "";
            case OTHERHCF: return "";
            case HOSP: return "";
            case LONG: return "";
            case AADVICE: return "";
            case EXP: return "";
            case PSY: return "";
            case REHAB: return "";
            case OTH: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HOME: return "Home";
            case OTHERHCF: return "Other healthcare facility";
            case HOSP: return "Hospice";
            case LONG: return "Long-term care";
            case AADVICE: return "Left against advice";
            case EXP: return "Expired";
            case PSY: return "Psychiatric hospital";
            case REHAB: return "Rehabilitation";
            case OTH: return "Other";
            default: return "?";
          }
    }


}

