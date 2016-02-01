package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ContentProcessingMode {

        /**
         * Description:The content should be processed in a sequential fashion.
         */
        SEQL, 
        /**
         * Description:The content may be processed in any order.
         */
        UNOR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ContentProcessingMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("SEQL".equals(codeString))
          return SEQL;
        if ("UNOR".equals(codeString))
          return UNOR;
        throw new FHIRException("Unknown V3ContentProcessingMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SEQL: return "SEQL";
            case UNOR: return "UNOR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ContentProcessingMode";
        }
        public String getDefinition() {
          switch (this) {
            case SEQL: return "Description:The content should be processed in a sequential fashion.";
            case UNOR: return "Description:The content may be processed in any order.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SEQL: return "Sequential";
            case UNOR: return "Unordered";
            default: return "?";
          }
    }


}

