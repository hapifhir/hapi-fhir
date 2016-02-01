package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ActUncertainty {

        /**
         * Specifies that the act statement is made without explicit tagging of uncertainty. This is the normal statement, meaning that it is not free of errors and uncertainty may still exist.
         */
        N, 
        /**
         * Specifies that the originator of the Act statement does not have full confidence in the applicability (i.e., in event mood: factual truth) of the statement.
         */
        U, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActUncertainty fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("N".equals(codeString))
          return N;
        if ("U".equals(codeString))
          return U;
        throw new FHIRException("Unknown V3ActUncertainty code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case N: return "N";
            case U: return "U";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActUncertainty";
        }
        public String getDefinition() {
          switch (this) {
            case N: return "Specifies that the act statement is made without explicit tagging of uncertainty. This is the normal statement, meaning that it is not free of errors and uncertainty may still exist.";
            case U: return "Specifies that the originator of the Act statement does not have full confidence in the applicability (i.e., in event mood: factual truth) of the statement.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case N: return "stated with no assertion of uncertainty";
            case U: return "stated with uncertainty";
            default: return "?";
          }
    }


}

