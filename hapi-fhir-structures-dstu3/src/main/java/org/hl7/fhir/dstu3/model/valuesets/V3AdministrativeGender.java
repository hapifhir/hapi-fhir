package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3AdministrativeGender {

        /**
         * Female
         */
        F, 
        /**
         * Male
         */
        M, 
        /**
         * The gender of a person could not be uniquely defined as male or female, such as hermaphrodite.
         */
        UN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3AdministrativeGender fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("F".equals(codeString))
          return F;
        if ("M".equals(codeString))
          return M;
        if ("UN".equals(codeString))
          return UN;
        throw new FHIRException("Unknown V3AdministrativeGender code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case F: return "F";
            case M: return "M";
            case UN: return "UN";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/AdministrativeGender";
        }
        public String getDefinition() {
          switch (this) {
            case F: return "Female";
            case M: return "Male";
            case UN: return "The gender of a person could not be uniquely defined as male or female, such as hermaphrodite.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case F: return "Female";
            case M: return "Male";
            case UN: return "Undifferentiated";
            default: return "?";
          }
    }


}

