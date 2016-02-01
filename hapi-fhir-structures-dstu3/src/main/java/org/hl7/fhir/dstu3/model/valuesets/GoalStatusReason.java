package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum GoalStatusReason {

        /**
         * Goal suspended or ended because of a surgical procedure.
         */
        SURGERY, 
        /**
         * Goal suspended or ended because of a significant life event (marital change, bereavement, etc.).
         */
        LIFEEVENT, 
        /**
         * Goal has been superseded by a new goal.
         */
        REPLACED, 
        /**
         * Patient wishes the goal to be set aside, at least temporarily.
         */
        PATIENTREQUEST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalStatusReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("surgery".equals(codeString))
          return SURGERY;
        if ("life-event".equals(codeString))
          return LIFEEVENT;
        if ("replaced".equals(codeString))
          return REPLACED;
        if ("patient-request".equals(codeString))
          return PATIENTREQUEST;
        throw new FHIRException("Unknown GoalStatusReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SURGERY: return "surgery";
            case LIFEEVENT: return "life-event";
            case REPLACED: return "replaced";
            case PATIENTREQUEST: return "patient-request";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/goal-status-reason";
        }
        public String getDefinition() {
          switch (this) {
            case SURGERY: return "Goal suspended or ended because of a surgical procedure.";
            case LIFEEVENT: return "Goal suspended or ended because of a significant life event (marital change, bereavement, etc.).";
            case REPLACED: return "Goal has been superseded by a new goal.";
            case PATIENTREQUEST: return "Patient wishes the goal to be set aside, at least temporarily.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SURGERY: return "surgery";
            case LIFEEVENT: return "life event";
            case REPLACED: return "replaced";
            case PATIENTREQUEST: return "patient request";
            default: return "?";
          }
    }


}

