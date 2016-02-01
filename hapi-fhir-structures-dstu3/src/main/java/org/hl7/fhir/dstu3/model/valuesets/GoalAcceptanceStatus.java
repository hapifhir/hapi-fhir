package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum GoalAcceptanceStatus {

        /**
         * Stakeholder supports pursuit of the goal
         */
        AGREE, 
        /**
         * Stakeholder is not in support of the pursuit of the goal
         */
        DISAGREE, 
        /**
         * Stakeholder has not yet made a decision on whether they support the goal
         */
        PENDING, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalAcceptanceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("agree".equals(codeString))
          return AGREE;
        if ("disagree".equals(codeString))
          return DISAGREE;
        if ("pending".equals(codeString))
          return PENDING;
        throw new FHIRException("Unknown GoalAcceptanceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AGREE: return "agree";
            case DISAGREE: return "disagree";
            case PENDING: return "pending";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/goal-acceptance-status";
        }
        public String getDefinition() {
          switch (this) {
            case AGREE: return "Stakeholder supports pursuit of the goal";
            case DISAGREE: return "Stakeholder is not in support of the pursuit of the goal";
            case PENDING: return "Stakeholder has not yet made a decision on whether they support the goal";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AGREE: return "Agree";
            case DISAGREE: return "Disagree";
            case PENDING: return "Pending";
            default: return "?";
          }
    }


}

