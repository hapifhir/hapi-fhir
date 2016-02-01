package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ReasonMedicationNotGivenCodes {

        /**
         * No reason known.
         */
        A, 
        /**
         * The patient was not available when the dose was scheduled.
         */
        B, 
        /**
         * The patient was asleep when the dose was scheduled.
         */
        C, 
        /**
         * The patient was given the medication and immediately vomited it back.
         */
        D, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ReasonMedicationNotGivenCodes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("a".equals(codeString))
          return A;
        if ("b".equals(codeString))
          return B;
        if ("c".equals(codeString))
          return C;
        if ("d".equals(codeString))
          return D;
        throw new FHIRException("Unknown ReasonMedicationNotGivenCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "a";
            case B: return "b";
            case C: return "c";
            case D: return "d";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/reason-medication-not-given";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "No reason known.";
            case B: return "The patient was not available when the dose was scheduled.";
            case C: return "The patient was asleep when the dose was scheduled.";
            case D: return "The patient was given the medication and immediately vomited it back.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "None";
            case B: return "Away";
            case C: return "Asleep";
            case D: return "Vomit";
            default: return "?";
          }
    }


}

