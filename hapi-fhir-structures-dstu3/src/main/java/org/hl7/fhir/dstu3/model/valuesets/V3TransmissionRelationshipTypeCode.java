package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3TransmissionRelationshipTypeCode {

        /**
         * Description:A transmission relationship indicating that the source transmission follows the target transmission.
         */
        SEQL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TransmissionRelationshipTypeCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("SEQL".equals(codeString))
          return SEQL;
        throw new FHIRException("Unknown V3TransmissionRelationshipTypeCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SEQL: return "SEQL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TransmissionRelationshipTypeCode";
        }
        public String getDefinition() {
          switch (this) {
            case SEQL: return "Description:A transmission relationship indicating that the source transmission follows the target transmission.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SEQL: return "sequence";
            default: return "?";
          }
    }


}

