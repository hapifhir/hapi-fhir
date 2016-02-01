package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum VaccinationProtocolDoseStatus {

        /**
         * null
         */
        COUNT, 
        /**
         * null
         */
        NOCOUNT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VaccinationProtocolDoseStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("count".equals(codeString))
          return COUNT;
        if ("nocount".equals(codeString))
          return NOCOUNT;
        throw new FHIRException("Unknown VaccinationProtocolDoseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COUNT: return "count";
            case NOCOUNT: return "nocount";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/vaccination-protocol-dose-status";
        }
        public String getDefinition() {
          switch (this) {
            case COUNT: return "";
            case NOCOUNT: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COUNT: return "Counts";
            case NOCOUNT: return "Does not Count";
            default: return "?";
          }
    }


}

