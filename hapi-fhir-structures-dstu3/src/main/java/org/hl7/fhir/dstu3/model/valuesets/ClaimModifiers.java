package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ClaimModifiers {

        /**
         * Repair of prior service or installation.
         */
        A, 
        /**
         * Temporary service or installation.
         */
        B, 
        /**
         * Treatment associated with TMJ.
         */
        C, 
        /**
         * Implant or associated with an implant.
         */
        E, 
        /**
         * None.
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ClaimModifiers fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("B".equals(codeString))
          return B;
        if ("C".equals(codeString))
          return C;
        if ("E".equals(codeString))
          return E;
        if ("X".equals(codeString))
          return X;
        throw new FHIRException("Unknown ClaimModifiers code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case B: return "B";
            case C: return "C";
            case E: return "E";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/modifiers";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "Repair of prior service or installation.";
            case B: return "Temporary service or installation.";
            case C: return "Treatment associated with TMJ.";
            case E: return "Implant or associated with an implant.";
            case X: return "None.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "Repair of prior service or installation";
            case B: return "Temporary service or installation";
            case C: return "TMJ treatment";
            case E: return "Implant or associated with an implant";
            case X: return "None";
            default: return "?";
          }
    }


}

