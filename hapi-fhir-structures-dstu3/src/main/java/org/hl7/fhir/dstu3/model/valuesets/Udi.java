package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Udi {

        /**
         * GUDID (FDA) US Repository
         */
        GUDID, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Udi fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("gudid".equals(codeString))
          return GUDID;
        throw new FHIRException("Unknown Udi code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GUDID: return "gudid";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-udi";
        }
        public String getDefinition() {
          switch (this) {
            case GUDID: return "GUDID (FDA) US Repository";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GUDID: return "GUDID (FDA)";
            default: return "?";
          }
    }


}

