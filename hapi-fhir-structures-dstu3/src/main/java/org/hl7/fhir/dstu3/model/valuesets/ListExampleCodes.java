package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ListExampleCodes {

        /**
         * A list of alerts for the patient.
         */
        ALERTS, 
        /**
         * A list of part adverse reactions.
         */
        ADVERSERXNS, 
        /**
         * A list of Allergies for the patient.
         */
        ALLERGIES, 
        /**
         * A list of medication statements for the patient.
         */
        MEDICATIONS, 
        /**
         * A list of problems that the patient is known of have (or have had in the past).
         */
        PROBLEMS, 
        /**
         * A list of items that constitute a set of work to be performed (typically this code would be specialized for more specific uses, such as a ward round list).
         */
        WORKLIST, 
        /**
         * A list of items waiting for an event (perhaps a surgical patient waiting list).
         */
        WAITING, 
        /**
         * A set of protocols to be followed.
         */
        PROTOCOLS, 
        /**
         * A set of care plans that apply in a particular context of care.
         */
        PLANS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ListExampleCodes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("alerts".equals(codeString))
          return ALERTS;
        if ("adverserxns".equals(codeString))
          return ADVERSERXNS;
        if ("allergies".equals(codeString))
          return ALLERGIES;
        if ("medications".equals(codeString))
          return MEDICATIONS;
        if ("problems".equals(codeString))
          return PROBLEMS;
        if ("worklist".equals(codeString))
          return WORKLIST;
        if ("waiting".equals(codeString))
          return WAITING;
        if ("protocols".equals(codeString))
          return PROTOCOLS;
        if ("plans".equals(codeString))
          return PLANS;
        throw new FHIRException("Unknown ListExampleCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALERTS: return "alerts";
            case ADVERSERXNS: return "adverserxns";
            case ALLERGIES: return "allergies";
            case MEDICATIONS: return "medications";
            case PROBLEMS: return "problems";
            case WORKLIST: return "worklist";
            case WAITING: return "waiting";
            case PROTOCOLS: return "protocols";
            case PLANS: return "plans";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/list-example-use-codes";
        }
        public String getDefinition() {
          switch (this) {
            case ALERTS: return "A list of alerts for the patient.";
            case ADVERSERXNS: return "A list of part adverse reactions.";
            case ALLERGIES: return "A list of Allergies for the patient.";
            case MEDICATIONS: return "A list of medication statements for the patient.";
            case PROBLEMS: return "A list of problems that the patient is known of have (or have had in the past).";
            case WORKLIST: return "A list of items that constitute a set of work to be performed (typically this code would be specialized for more specific uses, such as a ward round list).";
            case WAITING: return "A list of items waiting for an event (perhaps a surgical patient waiting list).";
            case PROTOCOLS: return "A set of protocols to be followed.";
            case PLANS: return "A set of care plans that apply in a particular context of care.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALERTS: return "Alerts";
            case ADVERSERXNS: return "Adverse Reactions";
            case ALLERGIES: return "Allergies";
            case MEDICATIONS: return "Medication List";
            case PROBLEMS: return "Problem List";
            case WORKLIST: return "Worklist";
            case WAITING: return "Waiting List";
            case PROTOCOLS: return "Protocols";
            case PLANS: return "Care Plans";
            default: return "?";
          }
    }


}

