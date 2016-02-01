package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum SupplydeliveryType {

        /**
         * Supply is a kind of medication.
         */
        MEDICATION, 
        /**
         * What is supplied (or requested) is a device.
         */
        DEVICE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SupplydeliveryType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("medication".equals(codeString))
          return MEDICATION;
        if ("device".equals(codeString))
          return DEVICE;
        throw new FHIRException("Unknown SupplydeliveryType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MEDICATION: return "medication";
            case DEVICE: return "device";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/supply-item-type";
        }
        public String getDefinition() {
          switch (this) {
            case MEDICATION: return "Supply is a kind of medication.";
            case DEVICE: return "What is supplied (or requested) is a device.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MEDICATION: return "Medication";
            case DEVICE: return "Device";
            default: return "?";
          }
    }


}

