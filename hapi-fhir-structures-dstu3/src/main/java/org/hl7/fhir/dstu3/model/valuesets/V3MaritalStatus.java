package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3MaritalStatus {

        /**
         * Marriage contract has been declared null and to not have existed
         */
        A, 
        /**
         * Marriage contract has been declared dissolved and inactive
         */
        D, 
        /**
         * Subject to an Interlocutory Decree.
         */
        I, 
        /**
         * Legally Separated
         */
        L, 
        /**
         * A current marriage contract is active
         */
        M, 
        /**
         * More than 1 current spouse
         */
        P, 
        /**
         * No marriage contract has ever been entered
         */
        S, 
        /**
         * Person declares that a domestic partner relationship exists.
         */
        T, 
        /**
         * Currently not in a marriage contract.
         */
        U, 
        /**
         * The spouse has died
         */
        W, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3MaritalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("D".equals(codeString))
          return D;
        if ("I".equals(codeString))
          return I;
        if ("L".equals(codeString))
          return L;
        if ("M".equals(codeString))
          return M;
        if ("P".equals(codeString))
          return P;
        if ("S".equals(codeString))
          return S;
        if ("T".equals(codeString))
          return T;
        if ("U".equals(codeString))
          return U;
        if ("W".equals(codeString))
          return W;
        throw new FHIRException("Unknown V3MaritalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case D: return "D";
            case I: return "I";
            case L: return "L";
            case M: return "M";
            case P: return "P";
            case S: return "S";
            case T: return "T";
            case U: return "U";
            case W: return "W";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/MaritalStatus";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "Marriage contract has been declared null and to not have existed";
            case D: return "Marriage contract has been declared dissolved and inactive";
            case I: return "Subject to an Interlocutory Decree.";
            case L: return "Legally Separated";
            case M: return "A current marriage contract is active";
            case P: return "More than 1 current spouse";
            case S: return "No marriage contract has ever been entered";
            case T: return "Person declares that a domestic partner relationship exists.";
            case U: return "Currently not in a marriage contract.";
            case W: return "The spouse has died";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "Annulled";
            case D: return "Divorced";
            case I: return "Interlocutory";
            case L: return "Legally Separated";
            case M: return "Married";
            case P: return "Polygamous";
            case S: return "Never Married";
            case T: return "Domestic partner";
            case U: return "unmarried";
            case W: return "Widowed";
            default: return "?";
          }
    }


}

