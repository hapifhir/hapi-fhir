package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum GoalCategory {

        /**
         * Goals related to the consumption of food and/or beverages.
         */
        DIETARY, 
        /**
         * Goals related to the personal protection of the subject.
         */
        SAFETY, 
        /**
         * Goals related to the manner in which the subject acts.
         */
        BEHAVIORAL, 
        /**
         * Goals related to the practice of nursing or established by nurses.
         */
        NURSING, 
        /**
         * Goals related to the mobility and motor capability of the subject.
         */
        PHYSIOTHERAPY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("dietary".equals(codeString))
          return DIETARY;
        if ("safety".equals(codeString))
          return SAFETY;
        if ("behavioral".equals(codeString))
          return BEHAVIORAL;
        if ("nursing".equals(codeString))
          return NURSING;
        if ("physiotherapy".equals(codeString))
          return PHYSIOTHERAPY;
        throw new FHIRException("Unknown GoalCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIETARY: return "dietary";
            case SAFETY: return "safety";
            case BEHAVIORAL: return "behavioral";
            case NURSING: return "nursing";
            case PHYSIOTHERAPY: return "physiotherapy";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/goal-category";
        }
        public String getDefinition() {
          switch (this) {
            case DIETARY: return "Goals related to the consumption of food and/or beverages.";
            case SAFETY: return "Goals related to the personal protection of the subject.";
            case BEHAVIORAL: return "Goals related to the manner in which the subject acts.";
            case NURSING: return "Goals related to the practice of nursing or established by nurses.";
            case PHYSIOTHERAPY: return "Goals related to the mobility and motor capability of the subject.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIETARY: return "dietary";
            case SAFETY: return "safety";
            case BEHAVIORAL: return "behavioral";
            case NURSING: return "nursing";
            case PHYSIOTHERAPY: return "physiotherapy";
            default: return "?";
          }
    }


}

