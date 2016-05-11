package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ActRelationshipJoin {

        /**
         * Detach this branch from the other branches so it will not be resynchronized with the other branches.
         */
        D, 
        /**
         * When all other concurrent branches are terminated, interrupt and discontinue this branch.
         */
        K, 
        /**
         * Wait for this branch to terminate.
         */
        W, 
        /**
         * Wait for any one of the branches in the set of exclusive wait branches to terminate, then discontinue all the other exclusive wait branches.
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActRelationshipJoin fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("D".equals(codeString))
          return D;
        if ("K".equals(codeString))
          return K;
        if ("W".equals(codeString))
          return W;
        if ("X".equals(codeString))
          return X;
        throw new FHIRException("Unknown V3ActRelationshipJoin code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case D: return "D";
            case K: return "K";
            case W: return "W";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActRelationshipJoin";
        }
        public String getDefinition() {
          switch (this) {
            case D: return "Detach this branch from the other branches so it will not be resynchronized with the other branches.";
            case K: return "When all other concurrent branches are terminated, interrupt and discontinue this branch.";
            case W: return "Wait for this branch to terminate.";
            case X: return "Wait for any one of the branches in the set of exclusive wait branches to terminate, then discontinue all the other exclusive wait branches.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case D: return "detached";
            case K: return "kill";
            case W: return "wait";
            case X: return "exclusive wait";
            default: return "?";
          }
    }


}

