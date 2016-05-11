package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum VisionProduct {

        /**
         * null
         */
        LENS, 
        /**
         * null
         */
        CONTACT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VisionProduct fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("lens".equals(codeString))
          return LENS;
        if ("contact".equals(codeString))
          return CONTACT;
        throw new FHIRException("Unknown VisionProduct code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LENS: return "lens";
            case CONTACT: return "contact";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-visionprescriptionproduct";
        }
        public String getDefinition() {
          switch (this) {
            case LENS: return "";
            case CONTACT: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LENS: return "lens";
            case CONTACT: return "contact";
            default: return "?";
          }
    }


}

