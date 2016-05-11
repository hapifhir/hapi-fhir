package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ManagedParticipationStatus {

        /**
         * The 'typical' state. Excludes "nullified" which represents the termination state of a participation instance that was created in error.
         */
        NORMAL, 
        /**
         * The state representing the fact that the Participation is in progress.
         */
        ACTIVE, 
        /**
         * The terminal state resulting from cancellation of the Participation prior to activation.
         */
        CANCELLED, 
        /**
         * The terminal state representing the successful completion of the Participation.
         */
        COMPLETED, 
        /**
         * The state representing that fact that the Participation has not yet become active.
         */
        PENDING, 
        /**
         * The state representing the termination of a Participation instance that was created in error.
         */
        NULLIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ManagedParticipationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("pending".equals(codeString))
          return PENDING;
        if ("nullified".equals(codeString))
          return NULLIFIED;
        throw new FHIRException("Unknown V3ManagedParticipationStatus code '"+codeString+"'");
        }
        public String toCode() {
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
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ManagedParticipationStatus";
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "The 'typical' state. Excludes \"nullified\" which represents the termination state of a participation instance that was created in error.";
            case ACTIVE: return "The state representing the fact that the Participation is in progress.";
            case CANCELLED: return "The terminal state resulting from cancellation of the Participation prior to activation.";
            case COMPLETED: return "The terminal state representing the successful completion of the Participation.";
            case PENDING: return "The state representing that fact that the Participation has not yet become active.";
            case NULLIFIED: return "The state representing the termination of a Participation instance that was created in error.";
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

