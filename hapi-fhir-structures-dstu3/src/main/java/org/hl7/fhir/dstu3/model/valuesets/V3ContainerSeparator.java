package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ContainerSeparator {

        /**
         * A gelatinous type of separator material.
         */
        GEL, 
        /**
         * No separator material is present in the container.
         */
        NONE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ContainerSeparator fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("GEL".equals(codeString))
          return GEL;
        if ("NONE".equals(codeString))
          return NONE;
        throw new FHIRException("Unknown V3ContainerSeparator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GEL: return "GEL";
            case NONE: return "NONE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ContainerSeparator";
        }
        public String getDefinition() {
          switch (this) {
            case GEL: return "A gelatinous type of separator material.";
            case NONE: return "No separator material is present in the container.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GEL: return "Gel";
            case NONE: return "None";
            default: return "?";
          }
    }


}

