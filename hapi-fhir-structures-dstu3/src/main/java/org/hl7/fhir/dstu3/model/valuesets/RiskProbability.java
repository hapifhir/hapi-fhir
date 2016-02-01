package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum RiskProbability {

        /**
         * The specified outcome is exceptionally unlikely.
         */
        NEGLIGIBLE, 
        /**
         * The specified outcome is possible but unlikely.
         */
        LOW, 
        /**
         * The specified outcome has a reasonable likelihood of occurrence.
         */
        MODERATE, 
        /**
         * The specified outcome is more likely to occur than not.
         */
        HIGH, 
        /**
         * The specified outcome is effectively guaranteed.
         */
        CERTAIN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RiskProbability fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("negligible".equals(codeString))
          return NEGLIGIBLE;
        if ("low".equals(codeString))
          return LOW;
        if ("moderate".equals(codeString))
          return MODERATE;
        if ("high".equals(codeString))
          return HIGH;
        if ("certain".equals(codeString))
          return CERTAIN;
        throw new FHIRException("Unknown RiskProbability code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NEGLIGIBLE: return "negligible";
            case LOW: return "low";
            case MODERATE: return "moderate";
            case HIGH: return "high";
            case CERTAIN: return "certain";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/risk-probability";
        }
        public String getDefinition() {
          switch (this) {
            case NEGLIGIBLE: return "The specified outcome is exceptionally unlikely.";
            case LOW: return "The specified outcome is possible but unlikely.";
            case MODERATE: return "The specified outcome has a reasonable likelihood of occurrence.";
            case HIGH: return "The specified outcome is more likely to occur than not.";
            case CERTAIN: return "The specified outcome is effectively guaranteed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NEGLIGIBLE: return "Negligible likelihood";
            case LOW: return "Low likelihood";
            case MODERATE: return "Moderate likelihood";
            case HIGH: return "High likelihood";
            case CERTAIN: return "Certain";
            default: return "?";
          }
    }


}

