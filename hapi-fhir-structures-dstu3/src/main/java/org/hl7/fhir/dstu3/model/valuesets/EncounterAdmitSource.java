package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum EncounterAdmitSource {

        /**
         * null
         */
        HOSPTRANS, 
        /**
         * null
         */
        EMD, 
        /**
         * null
         */
        OUTP, 
        /**
         * null
         */
        BORN, 
        /**
         * null
         */
        GP, 
        /**
         * null
         */
        MP, 
        /**
         * null
         */
        NURSING, 
        /**
         * null
         */
        PSYCH, 
        /**
         * null
         */
        REHAB, 
        /**
         * null
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterAdmitSource fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("hosp-trans".equals(codeString))
          return HOSPTRANS;
        if ("emd".equals(codeString))
          return EMD;
        if ("outp".equals(codeString))
          return OUTP;
        if ("born".equals(codeString))
          return BORN;
        if ("gp".equals(codeString))
          return GP;
        if ("mp".equals(codeString))
          return MP;
        if ("nursing".equals(codeString))
          return NURSING;
        if ("psych".equals(codeString))
          return PSYCH;
        if ("rehab".equals(codeString))
          return REHAB;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown EncounterAdmitSource code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HOSPTRANS: return "hosp-trans";
            case EMD: return "emd";
            case OUTP: return "outp";
            case BORN: return "born";
            case GP: return "gp";
            case MP: return "mp";
            case NURSING: return "nursing";
            case PSYCH: return "psych";
            case REHAB: return "rehab";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/admit-source";
        }
        public String getDefinition() {
          switch (this) {
            case HOSPTRANS: return "";
            case EMD: return "";
            case OUTP: return "";
            case BORN: return "";
            case GP: return "";
            case MP: return "";
            case NURSING: return "";
            case PSYCH: return "";
            case REHAB: return "";
            case OTHER: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HOSPTRANS: return "Transferred from other hospital";
            case EMD: return "From accident/emergency department";
            case OUTP: return "From outpatient department";
            case BORN: return "Born in hospital";
            case GP: return "General Practitioner referral";
            case MP: return "Medical Practitioner/physician referral";
            case NURSING: return "From nursing home";
            case PSYCH: return "From psychiatric hospital";
            case REHAB: return "From rehabilitation facility";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

