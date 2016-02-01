package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3RoleStatus {

        /**
         * The 'typical' state. Excludes "nullified" which represents the termination state of a Role instance that was created in error.
         */
        NORMAL, 
        /**
         * The state representing the fact that the Entity is currently active in the Role.
         */
        ACTIVE, 
        /**
         * The terminal state resulting from cancellation of the role prior to activation.
         */
        CANCELLED, 
        /**
         * The state representing that fact that the role has not yet become active.
         */
        PENDING, 
        /**
         * The state that represents a suspension of the Entity playing the Role. This state is arrived at from the "active" state.
         */
        SUSPENDED, 
        /**
         * The state representing the successful termination of the Role.
         */
        TERMINATED, 
        /**
         * The state representing the termination of a Role instance that was created in error.
         */
        NULLIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RoleStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("pending".equals(codeString))
          return PENDING;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("terminated".equals(codeString))
          return TERMINATED;
        if ("nullified".equals(codeString))
          return NULLIFIED;
        throw new FHIRException("Unknown V3RoleStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NORMAL: return "normal";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case PENDING: return "pending";
            case SUSPENDED: return "suspended";
            case TERMINATED: return "terminated";
            case NULLIFIED: return "nullified";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RoleStatus";
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "The 'typical' state. Excludes \"nullified\" which represents the termination state of a Role instance that was created in error.";
            case ACTIVE: return "The state representing the fact that the Entity is currently active in the Role.";
            case CANCELLED: return "The terminal state resulting from cancellation of the role prior to activation.";
            case PENDING: return "The state representing that fact that the role has not yet become active.";
            case SUSPENDED: return "The state that represents a suspension of the Entity playing the Role. This state is arrived at from the \"active\" state.";
            case TERMINATED: return "The state representing the successful termination of the Role.";
            case NULLIFIED: return "The state representing the termination of a Role instance that was created in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NORMAL: return "normal";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case PENDING: return "pending";
            case SUSPENDED: return "suspended";
            case TERMINATED: return "terminated";
            case NULLIFIED: return "nullified";
            default: return "?";
          }
    }


}

