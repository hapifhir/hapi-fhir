package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum DeviceAction {

        /**
         * The device was implanted in the patient during the procedure.
         */
        IMPLANTED, 
        /**
         * The device was explanted from the patient during the procedure.
         */
        EXPLANTED, 
        /**
         * The device remains in that patient, but its location, settings, or functionality was changed.
         */
        MANIPULATED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceAction fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("implanted".equals(codeString))
          return IMPLANTED;
        if ("explanted".equals(codeString))
          return EXPLANTED;
        if ("manipulated".equals(codeString))
          return MANIPULATED;
        throw new FHIRException("Unknown DeviceAction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IMPLANTED: return "implanted";
            case EXPLANTED: return "explanted";
            case MANIPULATED: return "manipulated";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/device-action";
        }
        public String getDefinition() {
          switch (this) {
            case IMPLANTED: return "The device was implanted in the patient during the procedure.";
            case EXPLANTED: return "The device was explanted from the patient during the procedure.";
            case MANIPULATED: return "The device remains in that patient, but its location, settings, or functionality was changed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IMPLANTED: return "Implanted";
            case EXPLANTED: return "Explanted";
            case MANIPULATED: return "Manipulated";
            default: return "?";
          }
    }


}

