package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3SubstitutionCondition {

        /**
         * Some conditions may be attached to an allowable substitution.  An allowable substitution is based on a match to any other attributes that may be specified.
         */
        _CONDITIONAL, 
        /**
         * Confirmation with Contact Person prior to making any substitutions has or will occur.
         */
        CONFIRM, 
        /**
         * Notification to the Contact Person, prior to substitution and through normal institutional procedures, has or will be made.
         */
        NOTIFY, 
        /**
         * Substitution is not permitted.
         */
        NOSUB, 
        /**
         * No conditions are required.
         */
        UNCOND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3SubstitutionCondition fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_Conditional".equals(codeString))
          return _CONDITIONAL;
        if ("CONFIRM".equals(codeString))
          return CONFIRM;
        if ("NOTIFY".equals(codeString))
          return NOTIFY;
        if ("NOSUB".equals(codeString))
          return NOSUB;
        if ("UNCOND".equals(codeString))
          return UNCOND;
        throw new FHIRException("Unknown V3SubstitutionCondition code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _CONDITIONAL: return "_Conditional";
            case CONFIRM: return "CONFIRM";
            case NOTIFY: return "NOTIFY";
            case NOSUB: return "NOSUB";
            case UNCOND: return "UNCOND";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/SubstitutionCondition";
        }
        public String getDefinition() {
          switch (this) {
            case _CONDITIONAL: return "Some conditions may be attached to an allowable substitution.  An allowable substitution is based on a match to any other attributes that may be specified.";
            case CONFIRM: return "Confirmation with Contact Person prior to making any substitutions has or will occur.";
            case NOTIFY: return "Notification to the Contact Person, prior to substitution and through normal institutional procedures, has or will be made.";
            case NOSUB: return "Substitution is not permitted.";
            case UNCOND: return "No conditions are required.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _CONDITIONAL: return "Conditional";
            case CONFIRM: return "Confirm first";
            case NOTIFY: return "Notify first";
            case NOSUB: return "No substitution";
            case UNCOND: return "Unconditional";
            default: return "?";
          }
    }


}

