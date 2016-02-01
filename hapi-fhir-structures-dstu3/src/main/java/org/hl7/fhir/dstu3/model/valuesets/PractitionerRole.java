package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum PractitionerRole {

        /**
         * null
         */
        DOCTOR, 
        /**
         * null
         */
        NURSE, 
        /**
         * null
         */
        PHARMACIST, 
        /**
         * null
         */
        RESEARCHER, 
        /**
         * null
         */
        TEACHER, 
        /**
         * null
         */
        ICT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PractitionerRole fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("doctor".equals(codeString))
          return DOCTOR;
        if ("nurse".equals(codeString))
          return NURSE;
        if ("pharmacist".equals(codeString))
          return PHARMACIST;
        if ("researcher".equals(codeString))
          return RESEARCHER;
        if ("teacher".equals(codeString))
          return TEACHER;
        if ("ict".equals(codeString))
          return ICT;
        throw new FHIRException("Unknown PractitionerRole code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DOCTOR: return "doctor";
            case NURSE: return "nurse";
            case PHARMACIST: return "pharmacist";
            case RESEARCHER: return "researcher";
            case TEACHER: return "teacher";
            case ICT: return "ict";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/practitioner-role";
        }
        public String getDefinition() {
          switch (this) {
            case DOCTOR: return "";
            case NURSE: return "";
            case PHARMACIST: return "";
            case RESEARCHER: return "";
            case TEACHER: return "";
            case ICT: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DOCTOR: return "Doctor";
            case NURSE: return "Nurse";
            case PHARMACIST: return "Pharmacist";
            case RESEARCHER: return "Researcher";
            case TEACHER: return "Teacher/educator";
            case ICT: return "ICT professional";
            default: return "?";
          }
    }


}

