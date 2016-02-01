package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ProcedureProgressStatusCodes {

        /**
         * A patient is in the Operating Room.
         */
        A, 
        /**
         * The patient is prepared for a procedure.
         */
        B, 
        /**
         * The patient is under anesthesia.
         */
        C, 
        /**
         * null
         */
        D, 
        /**
         * null
         */
        E, 
        /**
         * The patient is in the recovery room.
         */
        F, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcedureProgressStatusCodes fromCode(String codeString) throws FHIRException {
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
        if ("e".equals(codeString))
          return E;
        if ("f".equals(codeString))
          return F;
        throw new FHIRException("Unknown ProcedureProgressStatusCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "a";
            case B: return "b";
            case C: return "c";
            case D: return "d";
            case E: return "e";
            case F: return "f";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/procedure-progress-status-code";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "A patient is in the Operating Room.";
            case B: return "The patient is prepared for a procedure.";
            case C: return "The patient is under anesthesia.";
            case D: return "";
            case E: return "";
            case F: return "The patient is in the recovery room.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "In Operating Room";
            case B: return "Prepared";
            case C: return "Anesthesia induced";
            case D: return "Opened (skin)";
            case E: return "Closed (skin)";
            case F: return "In Recovery Room";
            default: return "?";
          }
    }


}

