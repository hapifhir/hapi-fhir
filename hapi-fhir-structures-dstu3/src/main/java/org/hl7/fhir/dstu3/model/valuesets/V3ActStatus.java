package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ActStatus {

        /**
         * Encompasses the expected states of an Act, but excludes "nullified" and "obsolete" which represent unusual terminal states for the life-cycle.
         */
        NORMAL, 
        /**
         * The Act has been terminated prior to the originally intended completion.
         */
        ABORTED, 
        /**
         * The Act can be performed or is being performed
         */
        ACTIVE, 
        /**
         * The Act has been abandoned before activation.
         */
        CANCELLED, 
        /**
         * An Act that has terminated normally after all of its constituents have been performed.
         */
        COMPLETED, 
        /**
         * An Act that is still in the preparatory stages has been put aside.  No action can occur until the Act is released.
         */
        HELD, 
        /**
         * An Act that is in the preparatory stages and may not yet be acted upon
         */
        NEW, 
        /**
         * An Act that has been activated (actions could or have been performed against it), but has been temporarily disabled.  No further action should be taken against it until it is released
         */
        SUSPENDED, 
        /**
         * This Act instance was created in error and has been 'removed' and is treated as though it never existed.  A record is retained for audit purposes only.
         */
        NULLIFIED, 
        /**
         * This Act instance has been replaced by a new instance.
         */
        OBSOLETE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("aborted".equals(codeString))
          return ABORTED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("held".equals(codeString))
          return HELD;
        if ("new".equals(codeString))
          return NEW;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("nullified".equals(codeString))
          return NULLIFIED;
        if ("obsolete".equals(codeString))
          return OBSOLETE;
        throw new FHIRException("Unknown V3ActStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NORMAL: return "normal";
            case ABORTED: return "aborted";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case COMPLETED: return "completed";
            case HELD: return "held";
            case NEW: return "new";
            case SUSPENDED: return "suspended";
            case NULLIFIED: return "nullified";
            case OBSOLETE: return "obsolete";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActStatus";
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "Encompasses the expected states of an Act, but excludes \"nullified\" and \"obsolete\" which represent unusual terminal states for the life-cycle.";
            case ABORTED: return "The Act has been terminated prior to the originally intended completion.";
            case ACTIVE: return "The Act can be performed or is being performed";
            case CANCELLED: return "The Act has been abandoned before activation.";
            case COMPLETED: return "An Act that has terminated normally after all of its constituents have been performed.";
            case HELD: return "An Act that is still in the preparatory stages has been put aside.  No action can occur until the Act is released.";
            case NEW: return "An Act that is in the preparatory stages and may not yet be acted upon";
            case SUSPENDED: return "An Act that has been activated (actions could or have been performed against it), but has been temporarily disabled.  No further action should be taken against it until it is released";
            case NULLIFIED: return "This Act instance was created in error and has been 'removed' and is treated as though it never existed.  A record is retained for audit purposes only.";
            case OBSOLETE: return "This Act instance has been replaced by a new instance.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NORMAL: return "normal";
            case ABORTED: return "aborted";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case COMPLETED: return "completed";
            case HELD: return "held";
            case NEW: return "new";
            case SUSPENDED: return "suspended";
            case NULLIFIED: return "nullified";
            case OBSOLETE: return "obsolete";
            default: return "?";
          }
    }


}

