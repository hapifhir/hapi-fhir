package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ConditionState {

        /**
         * The condition is active.
         */
        ACTIVE, 
        /**
         * The condition inactive but not resolved.
         */
        INACTIVE, 
        /**
         * The condition is resolved.
         */
        RESOLVED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionState fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("resolved".equals(codeString))
          return RESOLVED;
        throw new FHIRException("Unknown ConditionState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case RESOLVED: return "resolved";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/condition-state";
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The condition is active.";
            case INACTIVE: return "The condition inactive but not resolved.";
            case RESOLVED: return "The condition is resolved.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case RESOLVED: return "Resolved";
            default: return "?";
          }
    }


}

