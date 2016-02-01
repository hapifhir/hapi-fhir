package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum CarePlanActivityCategory {

        /**
         * Plan for the patient to consume food of a specified nature
         */
        DIET, 
        /**
         * Plan for the patient to consume/receive a drug, vaccine or other product
         */
        DRUG, 
        /**
         * Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.)
         */
        ENCOUNTER, 
        /**
         * Plan to capture information about a patient (vitals, labs, diagnostic images, etc.)
         */
        OBSERVATION, 
        /**
         * Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)
         */
        PROCEDURE, 
        /**
         * Plan to provide something to the patient (medication, medical supply, etc.)
         */
        SUPPLY, 
        /**
         * Some other form of action
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanActivityCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return DIET;
        if ("drug".equals(codeString))
          return DRUG;
        if ("encounter".equals(codeString))
          return ENCOUNTER;
        if ("observation".equals(codeString))
          return OBSERVATION;
        if ("procedure".equals(codeString))
          return PROCEDURE;
        if ("supply".equals(codeString))
          return SUPPLY;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown CarePlanActivityCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIET: return "diet";
            case DRUG: return "drug";
            case ENCOUNTER: return "encounter";
            case OBSERVATION: return "observation";
            case PROCEDURE: return "procedure";
            case SUPPLY: return "supply";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/care-plan-activity-category";
        }
        public String getDefinition() {
          switch (this) {
            case DIET: return "Plan for the patient to consume food of a specified nature";
            case DRUG: return "Plan for the patient to consume/receive a drug, vaccine or other product";
            case ENCOUNTER: return "Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.)";
            case OBSERVATION: return "Plan to capture information about a patient (vitals, labs, diagnostic images, etc.)";
            case PROCEDURE: return "Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)";
            case SUPPLY: return "Plan to provide something to the patient (medication, medical supply, etc.)";
            case OTHER: return "Some other form of action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIET: return "Diet";
            case DRUG: return "Drug";
            case ENCOUNTER: return "Encounter";
            case OBSERVATION: return "Observation";
            case PROCEDURE: return "Procedure";
            case SUPPLY: return "Supply";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

