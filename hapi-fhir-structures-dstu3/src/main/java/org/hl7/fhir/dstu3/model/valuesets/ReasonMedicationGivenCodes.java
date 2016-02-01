package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ReasonMedicationGivenCodes {

        /**
         * No reason known.
         */
        A, 
        /**
         * The administration was following an ordered protocol.
         */
        B, 
        /**
         * The administration was needed to treat an emergency.
         */
        C, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ReasonMedicationGivenCodes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("a".equals(codeString))
          return A;
        if ("b".equals(codeString))
          return B;
        if ("c".equals(codeString))
          return C;
        throw new FHIRException("Unknown ReasonMedicationGivenCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "a";
            case B: return "b";
            case C: return "c";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/reason-medication-given";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "No reason known.";
            case B: return "The administration was following an ordered protocol.";
            case C: return "The administration was needed to treat an emergency.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "None";
            case B: return "Given as Ordered";
            case C: return "Emergency";
            default: return "?";
          }
    }


}

