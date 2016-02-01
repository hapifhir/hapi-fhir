package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum GoalPriority {

        /**
         * Indicates that the goal is of considerable importance and should be a primary focus of care delivery.
         */
        HIGH, 
        /**
         * Indicates that the goal has a reasonable degree of importance and that concrete action should be taken towards the goal.  Attainment is not as critical as high-priority goals.
         */
        MEDIUM, 
        /**
         * The goal is desirable but is not sufficiently important to devote significant resources to.  Achievement of the goal may be sought when incidental to achieving other goals.
         */
        LOW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("high".equals(codeString))
          return HIGH;
        if ("medium".equals(codeString))
          return MEDIUM;
        if ("low".equals(codeString))
          return LOW;
        throw new FHIRException("Unknown GoalPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HIGH: return "high";
            case MEDIUM: return "medium";
            case LOW: return "low";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/goal-priority";
        }
        public String getDefinition() {
          switch (this) {
            case HIGH: return "Indicates that the goal is of considerable importance and should be a primary focus of care delivery.";
            case MEDIUM: return "Indicates that the goal has a reasonable degree of importance and that concrete action should be taken towards the goal.  Attainment is not as critical as high-priority goals.";
            case LOW: return "The goal is desirable but is not sufficiently important to devote significant resources to.  Achievement of the goal may be sought when incidental to achieving other goals.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HIGH: return "high";
            case MEDIUM: return "medium";
            case LOW: return "low";
            default: return "?";
          }
    }


}

