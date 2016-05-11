package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum OralProsthodonticMaterial {

        /**
         * Fixed Bridge
         */
        _1, 
        /**
         * Maryland Bridge
         */
        _2, 
        /**
         * Denture Acrylic
         */
        _3, 
        /**
         * Denture Chrome Cobalt
         */
        _4, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OralProsthodonticMaterial fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        throw new FHIRException("Unknown OralProsthodonticMaterial code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-oralprostho";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Fixed Bridge";
            case _2: return "Maryland Bridge";
            case _3: return "Denture Acrylic";
            case _4: return "Denture Chrome Cobalt";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Fixed Bridge";
            case _2: return "Maryland Bridge";
            case _3: return "Denture Acrylic";
            case _4: return "Denture Chrome Cobalt";
            default: return "?";
          }
    }


}

