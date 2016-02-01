package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ProcedureRelationshipType {

        /**
         * This procedure had to be performed because of the related one.
         */
        CAUSEDBY, 
        /**
         * This procedure caused the related one to be performed.
         */
        BECAUSEOF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcedureRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("caused-by".equals(codeString))
          return CAUSEDBY;
        if ("because-of".equals(codeString))
          return BECAUSEOF;
        throw new FHIRException("Unknown ProcedureRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CAUSEDBY: return "caused-by";
            case BECAUSEOF: return "because-of";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/procedure-relationship-type";
        }
        public String getDefinition() {
          switch (this) {
            case CAUSEDBY: return "This procedure had to be performed because of the related one.";
            case BECAUSEOF: return "This procedure caused the related one to be performed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CAUSEDBY: return "Caused By";
            case BECAUSEOF: return "Because Of";
            default: return "?";
          }
    }


}

