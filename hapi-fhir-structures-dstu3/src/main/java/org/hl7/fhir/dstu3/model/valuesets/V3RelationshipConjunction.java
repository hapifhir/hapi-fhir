package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3RelationshipConjunction {

        /**
         * This condition must be true.
         */
        AND, 
        /**
         * At least one of the condition among all OR conditions must be true.
         */
        OR, 
        /**
         * One and only one of the XOR conditions must be true.
         */
        XOR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RelationshipConjunction fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AND".equals(codeString))
          return AND;
        if ("OR".equals(codeString))
          return OR;
        if ("XOR".equals(codeString))
          return XOR;
        throw new FHIRException("Unknown V3RelationshipConjunction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AND: return "AND";
            case OR: return "OR";
            case XOR: return "XOR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RelationshipConjunction";
        }
        public String getDefinition() {
          switch (this) {
            case AND: return "This condition must be true.";
            case OR: return "At least one of the condition among all OR conditions must be true.";
            case XOR: return "One and only one of the XOR conditions must be true.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AND: return "and";
            case OR: return "or";
            case XOR: return "exclusive or";
            default: return "?";
          }
    }


}

