package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum SupplyrequestKind {

        /**
         * Supply is stored and requested from central supply.
         */
        CENTRAL, 
        /**
         * Supply is not onsite and must be requested from an outside vendor using a non-stock requisition.
         */
        NONSTOCK, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SupplyrequestKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("central".equals(codeString))
          return CENTRAL;
        if ("nonstock".equals(codeString))
          return NONSTOCK;
        throw new FHIRException("Unknown SupplyrequestKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CENTRAL: return "central";
            case NONSTOCK: return "nonstock";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/supply-kind";
        }
        public String getDefinition() {
          switch (this) {
            case CENTRAL: return "Supply is stored and requested from central supply.";
            case NONSTOCK: return "Supply is not onsite and must be requested from an outside vendor using a non-stock requisition.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CENTRAL: return "Central Supply";
            case NONSTOCK: return "Non-Stock";
            default: return "?";
          }
    }


}

