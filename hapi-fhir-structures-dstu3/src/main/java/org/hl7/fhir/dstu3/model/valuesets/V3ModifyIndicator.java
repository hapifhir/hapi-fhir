package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ModifyIndicator {

        /**
         * Modified subscription to a query server.
         */
        M, 
        /**
         * New subscription to a query server.
         */
        N, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ModifyIndicator fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("M".equals(codeString))
          return M;
        if ("N".equals(codeString))
          return N;
        throw new FHIRException("Unknown V3ModifyIndicator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case M: return "M";
            case N: return "N";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ModifyIndicator";
        }
        public String getDefinition() {
          switch (this) {
            case M: return "Modified subscription to a query server.";
            case N: return "New subscription to a query server.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case M: return "Modified subscription";
            case N: return "New subscription";
            default: return "?";
          }
    }


}

