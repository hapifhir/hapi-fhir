package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EquipmentAlertLevel {

        /**
         * Shut Down, Fix Problem and Re-init
         */
        C, 
        /**
         * No Corrective Action Needed
         */
        N, 
        /**
         * Corrective Action Required
         */
        S, 
        /**
         * Corrective Action Anticipated
         */
        W, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EquipmentAlertLevel fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("C".equals(codeString))
          return C;
        if ("N".equals(codeString))
          return N;
        if ("S".equals(codeString))
          return S;
        if ("W".equals(codeString))
          return W;
        throw new FHIRException("Unknown V3EquipmentAlertLevel code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case C: return "C";
            case N: return "N";
            case S: return "S";
            case W: return "W";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EquipmentAlertLevel";
        }
        public String getDefinition() {
          switch (this) {
            case C: return "Shut Down, Fix Problem and Re-init";
            case N: return "No Corrective Action Needed";
            case S: return "Corrective Action Required";
            case W: return "Corrective Action Anticipated";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case C: return "Critical";
            case N: return "Normal";
            case S: return "Serious";
            case W: return "Warning";
            default: return "?";
          }
    }


}

