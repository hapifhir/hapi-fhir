package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ConformanceExpectation {

        /**
         * Support for the specified capability is required to be considered conformant.
         */
        SHALL, 
        /**
         * Support for the specified capability is strongly encouraged and failure to support it should only occur after careful consideration.
         */
        SHOULD, 
        /**
         * Support for the specified capability is not necessary to be considered conformant and the requirement should be considered strictly optional.
         */
        MAY, 
        /**
         * Support for the specified capability is strongly discouraged and should occur only after careful consideration.
         */
        SHOULDNOT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConformanceExpectation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("SHALL".equals(codeString))
          return SHALL;
        if ("SHOULD".equals(codeString))
          return SHOULD;
        if ("MAY".equals(codeString))
          return MAY;
        if ("SHOULD-NOT".equals(codeString))
          return SHOULDNOT;
        throw new FHIRException("Unknown ConformanceExpectation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SHALL: return "SHALL";
            case SHOULD: return "SHOULD";
            case MAY: return "MAY";
            case SHOULDNOT: return "SHOULD-NOT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/conformance-expectation";
        }
        public String getDefinition() {
          switch (this) {
            case SHALL: return "Support for the specified capability is required to be considered conformant.";
            case SHOULD: return "Support for the specified capability is strongly encouraged and failure to support it should only occur after careful consideration.";
            case MAY: return "Support for the specified capability is not necessary to be considered conformant and the requirement should be considered strictly optional.";
            case SHOULDNOT: return "Support for the specified capability is strongly discouraged and should occur only after careful consideration.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SHALL: return "SHALL";
            case SHOULD: return "SHOULD";
            case MAY: return "MAY";
            case SHOULDNOT: return "SHOULD-NOT";
            default: return "?";
          }
    }


}

