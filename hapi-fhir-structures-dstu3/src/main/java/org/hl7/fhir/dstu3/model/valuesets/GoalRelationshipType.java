package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum GoalRelationshipType {

        /**
         * Indicates that the target goal is one which must be met before striving for the current goal
         */
        PREDECESSOR, 
        /**
         * Indicates that the target goal is a desired objective once the current goal is met
         */
        SUCCESSOR, 
        /**
         * Indicates that this goal has been replaced by the target goal
         */
        REPLACEMENT, 
        /**
         * Indicates that the target goal is considered to be a "piece" of attaining this goal.
         */
        COMPONENT, 
        /**
         * Indicates that the relationship is not covered by one of the pre-defined codes.  (An extension may convey more information about the meaning of the relationship.)
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("predecessor".equals(codeString))
          return PREDECESSOR;
        if ("successor".equals(codeString))
          return SUCCESSOR;
        if ("replacement".equals(codeString))
          return REPLACEMENT;
        if ("component".equals(codeString))
          return COMPONENT;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown GoalRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PREDECESSOR: return "predecessor";
            case SUCCESSOR: return "successor";
            case REPLACEMENT: return "replacement";
            case COMPONENT: return "component";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/goal-relationship-type";
        }
        public String getDefinition() {
          switch (this) {
            case PREDECESSOR: return "Indicates that the target goal is one which must be met before striving for the current goal";
            case SUCCESSOR: return "Indicates that the target goal is a desired objective once the current goal is met";
            case REPLACEMENT: return "Indicates that this goal has been replaced by the target goal";
            case COMPONENT: return "Indicates that the target goal is considered to be a \"piece\" of attaining this goal.";
            case OTHER: return "Indicates that the relationship is not covered by one of the pre-defined codes.  (An extension may convey more information about the meaning of the relationship.)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PREDECESSOR: return "Predecessor";
            case SUCCESSOR: return "Successor";
            case REPLACEMENT: return "Replacement";
            case COMPONENT: return "Component";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

