package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum SupplyrequestReason {

        /**
         * The supply has been requested for use in direct patient care.
         */
        PATIENTCARE, 
        /**
         * The supply has been requested for for creating or replenishing ward stock.
         */
        WARDSTOCK, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SupplyrequestReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient-care".equals(codeString))
          return PATIENTCARE;
        if ("ward-stock".equals(codeString))
          return WARDSTOCK;
        throw new FHIRException("Unknown SupplyrequestReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENTCARE: return "patient-care";
            case WARDSTOCK: return "ward-stock";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/supplyrequest-reason";
        }
        public String getDefinition() {
          switch (this) {
            case PATIENTCARE: return "The supply has been requested for use in direct patient care.";
            case WARDSTOCK: return "The supply has been requested for for creating or replenishing ward stock.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENTCARE: return "Patient Care";
            case WARDSTOCK: return "Ward Stock";
            default: return "?";
          }
    }


}

