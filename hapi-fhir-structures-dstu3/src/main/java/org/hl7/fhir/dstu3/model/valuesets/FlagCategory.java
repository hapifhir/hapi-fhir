package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum FlagCategory {

        /**
         * Flags related to the subject's dietary needs.
         */
        DIET, 
        /**
         * Flags related to the patient's medications.
         */
        DRUG, 
        /**
         * Flags related to performing laboratory tests and related processes (e.g. phlebotomy).
         */
        LAB, 
        /**
         * Flags related to administrative and financial processes.
         */
        ADMIN, 
        /**
         * Flags related to coming into contact with the patient.
         */
        CONTACT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FlagCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return DIET;
        if ("drug".equals(codeString))
          return DRUG;
        if ("lab".equals(codeString))
          return LAB;
        if ("admin".equals(codeString))
          return ADMIN;
        if ("contact".equals(codeString))
          return CONTACT;
        throw new FHIRException("Unknown FlagCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIET: return "diet";
            case DRUG: return "drug";
            case LAB: return "lab";
            case ADMIN: return "admin";
            case CONTACT: return "contact";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/flag-category";
        }
        public String getDefinition() {
          switch (this) {
            case DIET: return "Flags related to the subject's dietary needs.";
            case DRUG: return "Flags related to the patient's medications.";
            case LAB: return "Flags related to performing laboratory tests and related processes (e.g. phlebotomy).";
            case ADMIN: return "Flags related to administrative and financial processes.";
            case CONTACT: return "Flags related to coming into contact with the patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIET: return "Diet";
            case DRUG: return "Drug";
            case LAB: return "Lab";
            case ADMIN: return "Administrative";
            case CONTACT: return "Subject contact";
            default: return "?";
          }
    }


}

