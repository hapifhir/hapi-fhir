package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3QueryRequestLimit {

        /**
         * Definition: The number of matching instances (number of focal classes). The document header class is the focal class of a document, a record would therefore be equal to a document.
         */
        _QUERYREQUESTLIMIT, 
        /**
         * Definition: The number of matching instances (number of focal classes). The document header class is the focal class of a document, a record would therefore be equal to a document.
         */
        RD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3QueryRequestLimit fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_QueryRequestLimit".equals(codeString))
          return _QUERYREQUESTLIMIT;
        if ("RD".equals(codeString))
          return RD;
        throw new FHIRException("Unknown V3QueryRequestLimit code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _QUERYREQUESTLIMIT: return "_QueryRequestLimit";
            case RD: return "RD";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/QueryRequestLimit";
        }
        public String getDefinition() {
          switch (this) {
            case _QUERYREQUESTLIMIT: return "Definition: The number of matching instances (number of focal classes). The document header class is the focal class of a document, a record would therefore be equal to a document.";
            case RD: return "Definition: The number of matching instances (number of focal classes). The document header class is the focal class of a document, a record would therefore be equal to a document.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _QUERYREQUESTLIMIT: return "QueryRequestLimit";
            case RD: return "record";
            default: return "?";
          }
    }


}

