package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3RoleLinkStatus {

        /**
         * Description: The 'typical' state. Excludes "nullified" which represents the termination state of a RoleLink instance that was created in error.
         */
        NORMAL, 
        /**
         * Description: The state indicates the RoleLink is in progress.
         */
        ACTIVE, 
        /**
         * Description: The terminal state resulting from abandoning the RoleLink prior to or after activation.
         */
        CANCELLED, 
        /**
         * Description: The terminal state representing the successful completion of the RoleLink.
         */
        COMPLETED, 
        /**
         * Description: The state indicates the RoleLink has not yet become active.
         */
        PENDING, 
        /**
         * Description: The state representing the termination of a RoleLink instance that was created in error.
         */
        NULLIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RoleLinkStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("NORMAL".equals(codeString))
          return NORMAL;
        if ("ACTIVE".equals(codeString))
          return ACTIVE;
        if ("CANCELLED".equals(codeString))
          return CANCELLED;
        if ("COMPLETED".equals(codeString))
          return COMPLETED;
        if ("PENDING".equals(codeString))
          return PENDING;
        if ("NULLIFIED".equals(codeString))
          return NULLIFIED;
        throw new FHIRException("Unknown V3RoleLinkStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NORMAL: return "NORMAL";
            case ACTIVE: return "ACTIVE";
            case CANCELLED: return "CANCELLED";
            case COMPLETED: return "COMPLETED";
            case PENDING: return "PENDING";
            case NULLIFIED: return "NULLIFIED";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RoleLinkStatus";
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "Description: The 'typical' state. Excludes \"nullified\" which represents the termination state of a RoleLink instance that was created in error.";
            case ACTIVE: return "Description: The state indicates the RoleLink is in progress.";
            case CANCELLED: return "Description: The terminal state resulting from abandoning the RoleLink prior to or after activation.";
            case COMPLETED: return "Description: The terminal state representing the successful completion of the RoleLink.";
            case PENDING: return "Description: The state indicates the RoleLink has not yet become active.";
            case NULLIFIED: return "Description: The state representing the termination of a RoleLink instance that was created in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NORMAL: return "normal";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case COMPLETED: return "completed";
            case PENDING: return "pending";
            case NULLIFIED: return "nullified";
            default: return "?";
          }
    }


}

