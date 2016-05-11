package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ConditionCategory {

        /**
         * The patient considers the condition an issue to be addressed.
         */
        COMPLAINT, 
        /**
         * A symptom of a condition (as might be mentioned in a review of systems).
         */
        SYMPTOM, 
        /**
         * An observation made by a healthcare provider.
         */
        FINDING, 
        /**
         * This is a judgment made by a healthcare provider that the patient has a particular disease or condition.
         */
        DIAGNOSIS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complaint".equals(codeString))
          return COMPLAINT;
        if ("symptom".equals(codeString))
          return SYMPTOM;
        if ("finding".equals(codeString))
          return FINDING;
        if ("diagnosis".equals(codeString))
          return DIAGNOSIS;
        throw new FHIRException("Unknown ConditionCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLAINT: return "complaint";
            case SYMPTOM: return "symptom";
            case FINDING: return "finding";
            case DIAGNOSIS: return "diagnosis";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/condition-category";
        }
        public String getDefinition() {
          switch (this) {
            case COMPLAINT: return "The patient considers the condition an issue to be addressed.";
            case SYMPTOM: return "A symptom of a condition (as might be mentioned in a review of systems).";
            case FINDING: return "An observation made by a healthcare provider.";
            case DIAGNOSIS: return "This is a judgment made by a healthcare provider that the patient has a particular disease or condition.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLAINT: return "Complaint";
            case SYMPTOM: return "Symptom";
            case FINDING: return "Finding";
            case DIAGNOSIS: return "Diagnosis";
            default: return "?";
          }
    }


}

