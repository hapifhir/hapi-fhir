package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ActExposureLevelCode {

        /**
         * A qualitative measure of the degree of exposure to the causative agent.  This includes concepts such as "low", "medium" and "high".  This quantifies how the quantity that was available to be administered to the target differs from typical or background levels of the substance.
         */
        _ACTEXPOSURELEVELCODE, 
        /**
         * Description: Exposure to an agent at a relatively high level above background.
         */
        HIGH, 
        /**
         * Description: Exposure to an agent at a relatively low level above background.
         */
        LOW, 
        /**
         * Description: Exposure to an agent at a relatively moderate level above background.A
         */
        MEDIUM, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActExposureLevelCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ActExposureLevelCode".equals(codeString))
          return _ACTEXPOSURELEVELCODE;
        if ("HIGH".equals(codeString))
          return HIGH;
        if ("LOW".equals(codeString))
          return LOW;
        if ("MEDIUM".equals(codeString))
          return MEDIUM;
        throw new FHIRException("Unknown V3ActExposureLevelCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACTEXPOSURELEVELCODE: return "_ActExposureLevelCode";
            case HIGH: return "HIGH";
            case LOW: return "LOW";
            case MEDIUM: return "MEDIUM";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActExposureLevelCode";
        }
        public String getDefinition() {
          switch (this) {
            case _ACTEXPOSURELEVELCODE: return "A qualitative measure of the degree of exposure to the causative agent.  This includes concepts such as \"low\", \"medium\" and \"high\".  This quantifies how the quantity that was available to be administered to the target differs from typical or background levels of the substance.";
            case HIGH: return "Description: Exposure to an agent at a relatively high level above background.";
            case LOW: return "Description: Exposure to an agent at a relatively low level above background.";
            case MEDIUM: return "Description: Exposure to an agent at a relatively moderate level above background.A";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACTEXPOSURELEVELCODE: return "ActExposureLevelCode";
            case HIGH: return "high";
            case LOW: return "low";
            case MEDIUM: return "medium";
            default: return "?";
          }
    }


}

