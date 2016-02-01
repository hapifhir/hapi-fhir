package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum VariantState {

        /**
         * the variant is detected
         */
        POSITIVE, 
        /**
         * no variant is detected
         */
        NEGATIVE, 
        /**
         * result of the variant is missing
         */
        ABSENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VariantState fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("positive".equals(codeString))
          return POSITIVE;
        if ("negative".equals(codeString))
          return NEGATIVE;
        if ("absent".equals(codeString))
          return ABSENT;
        throw new FHIRException("Unknown VariantState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case POSITIVE: return "positive";
            case NEGATIVE: return "negative";
            case ABSENT: return "absent";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/variant-state";
        }
        public String getDefinition() {
          switch (this) {
            case POSITIVE: return "the variant is detected";
            case NEGATIVE: return "no variant is detected";
            case ABSENT: return "result of the variant is missing";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case POSITIVE: return "positive";
            case NEGATIVE: return "negative";
            case ABSENT: return "absent";
            default: return "?";
          }
    }


}

