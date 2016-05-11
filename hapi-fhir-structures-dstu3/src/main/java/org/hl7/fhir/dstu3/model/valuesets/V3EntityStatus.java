package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EntityStatus {

        /**
         * The 'typical' state. Excludes "nullified" which represents the termination state of an Entity record instance that was created in error.
         */
        NORMAL, 
        /**
         * The state representing the fact that the Entity record is currently active.
         */
        ACTIVE, 
        /**
         * Definition: The state representing the fact that the entity is inactive.
         */
        INACTIVE, 
        /**
         * The state representing the normal termination of an Entity record.
         */
        TERMINATED, 
        /**
         * The state representing the termination of an Entity record instance that was created in error.
         */
        NULLIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("terminated".equals(codeString))
          return TERMINATED;
        if ("nullified".equals(codeString))
          return NULLIFIED;
        throw new FHIRException("Unknown V3EntityStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NORMAL: return "normal";
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case TERMINATED: return "terminated";
            case NULLIFIED: return "nullified";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityStatus";
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "The 'typical' state. Excludes \"nullified\" which represents the termination state of an Entity record instance that was created in error.";
            case ACTIVE: return "The state representing the fact that the Entity record is currently active.";
            case INACTIVE: return "Definition: The state representing the fact that the entity is inactive.";
            case TERMINATED: return "The state representing the normal termination of an Entity record.";
            case NULLIFIED: return "The state representing the termination of an Entity record instance that was created in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NORMAL: return "normal";
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case TERMINATED: return "terminated";
            case NULLIFIED: return "nullified";
            default: return "?";
          }
    }


}

