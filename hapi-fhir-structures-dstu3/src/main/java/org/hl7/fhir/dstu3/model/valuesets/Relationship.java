package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Relationship {

        /**
         * null
         */
        _1, 
        /**
         * null
         */
        _2, 
        /**
         * null
         */
        _3, 
        /**
         * null
         */
        _4, 
        /**
         * null
         */
        _5, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Relationship fromCode(String codeString) throws FHIRException {
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
        if ("5".equals(codeString))
          return _5;
        throw new FHIRException("Unknown Relationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/relationship";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "";
            case _2: return "";
            case _3: return "";
            case _4: return "";
            case _5: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            default: return "?";
          }
    }


}

