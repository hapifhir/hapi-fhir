package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum MessageReasonEncounter {

        /**
         * The patient has been admitted.
         */
        ADMIT, 
        /**
         * The patient has been discharged.
         */
        DISCHARGE, 
        /**
         * The patient has temporarily left the institution.
         */
        ABSENT, 
        /**
         * The patient has returned from a temporary absence.
         */
        RETURN, 
        /**
         * The patient has been moved to a new location.
         */
        MOVED, 
        /**
         * Encounter details have been updated (e.g. to correct a coding error).
         */
        EDIT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MessageReasonEncounter fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("admit".equals(codeString))
          return ADMIT;
        if ("discharge".equals(codeString))
          return DISCHARGE;
        if ("absent".equals(codeString))
          return ABSENT;
        if ("return".equals(codeString))
          return RETURN;
        if ("moved".equals(codeString))
          return MOVED;
        if ("edit".equals(codeString))
          return EDIT;
        throw new FHIRException("Unknown MessageReasonEncounter code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADMIT: return "admit";
            case DISCHARGE: return "discharge";
            case ABSENT: return "absent";
            case RETURN: return "return";
            case MOVED: return "moved";
            case EDIT: return "edit";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/message-reasons-encounter";
        }
        public String getDefinition() {
          switch (this) {
            case ADMIT: return "The patient has been admitted.";
            case DISCHARGE: return "The patient has been discharged.";
            case ABSENT: return "The patient has temporarily left the institution.";
            case RETURN: return "The patient has returned from a temporary absence.";
            case MOVED: return "The patient has been moved to a new location.";
            case EDIT: return "Encounter details have been updated (e.g. to correct a coding error).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADMIT: return "Admit";
            case DISCHARGE: return "Discharge";
            case ABSENT: return "Absent";
            case RETURN: return "Returned";
            case MOVED: return "Moved";
            case EDIT: return "Edit";
            default: return "?";
          }
    }


}

