package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum QuestionMaxOccurs {

        /**
         * Element can repeat an unlimited number of times
         */
        ASTERISK, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QuestionMaxOccurs fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("*".equals(codeString))
          return ASTERISK;
        throw new FHIRException("Unknown QuestionMaxOccurs code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ASTERISK: return "*";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/question-max-occurs";
        }
        public String getDefinition() {
          switch (this) {
            case ASTERISK: return "Element can repeat an unlimited number of times";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ASTERISK: return "Repeating";
            default: return "?";
          }
    }


}

