package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ObjectType {

        /**
         * Person
         */
        _1, 
        /**
         * System Object
         */
        _2, 
        /**
         * Organization
         */
        _3, 
        /**
         * Other
         */
        _4, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObjectType fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown ObjectType code '"+codeString+"'");
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
          return "http://hl7.org/fhir/object-type";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Person";
            case _2: return "System Object";
            case _3: return "Organization";
            case _4: return "Other";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Person";
            case _2: return "System Object";
            case _3: return "Organization";
            case _4: return "Other";
            default: return "?";
          }
    }


}

