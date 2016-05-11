package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ActRelationshipSplit {

        /**
         * The pre-condition associated with the branch is evaluated once and if true the branch may be entered. All other exclusive branches compete with each other and only one will be selected. This implements a COND, IF and CASE conditionals, or "XOR-split." The order in which the branches are considered may be specified in the priorityNumber attribute.
         */
        E1, 
        /**
         * A branch is selected as soon as the pre-condition associated with the branch evaluates to true.  If the condition is false, the branch may be entered later, when the condition turns true.  All other exclusive branches compete with each other and only one will be selected.  Each waiting branch executes in parallel with the default join code wait  (see below). The order in which the branches are considered may be specified in the Service_relationship.priority_nmb.
         */
        EW, 
        /**
         * A branch is executed if its associated preconditions permit. If associated preconditions do not permit, the branch is dropped.  Inclusive branches are not suppressed and do not suppress other branches.
         */
        I1, 
        /**
         * A branch is executed as soon as its associated conditions permit.  If the condition is false, the branch may be entered later, when the condition turns true.  Inclusive branches are not suppressed and do not suppress other branches.  Each waiting branch executes in parallel with the default join code wait  (see below).
         */
        IW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActRelationshipSplit fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("E1".equals(codeString))
          return E1;
        if ("EW".equals(codeString))
          return EW;
        if ("I1".equals(codeString))
          return I1;
        if ("IW".equals(codeString))
          return IW;
        throw new FHIRException("Unknown V3ActRelationshipSplit code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case E1: return "E1";
            case EW: return "EW";
            case I1: return "I1";
            case IW: return "IW";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActRelationshipSplit";
        }
        public String getDefinition() {
          switch (this) {
            case E1: return "The pre-condition associated with the branch is evaluated once and if true the branch may be entered. All other exclusive branches compete with each other and only one will be selected. This implements a COND, IF and CASE conditionals, or \"XOR-split.\" The order in which the branches are considered may be specified in the priorityNumber attribute.";
            case EW: return "A branch is selected as soon as the pre-condition associated with the branch evaluates to true.  If the condition is false, the branch may be entered later, when the condition turns true.  All other exclusive branches compete with each other and only one will be selected.  Each waiting branch executes in parallel with the default join code wait  (see below). The order in which the branches are considered may be specified in the Service_relationship.priority_nmb.";
            case I1: return "A branch is executed if its associated preconditions permit. If associated preconditions do not permit, the branch is dropped.  Inclusive branches are not suppressed and do not suppress other branches.";
            case IW: return "A branch is executed as soon as its associated conditions permit.  If the condition is false, the branch may be entered later, when the condition turns true.  Inclusive branches are not suppressed and do not suppress other branches.  Each waiting branch executes in parallel with the default join code wait  (see below).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case E1: return "exclusive try once";
            case EW: return "exclusive wait";
            case I1: return "inclusive try once";
            case IW: return "inclusive wait";
            default: return "?";
          }
    }


}

