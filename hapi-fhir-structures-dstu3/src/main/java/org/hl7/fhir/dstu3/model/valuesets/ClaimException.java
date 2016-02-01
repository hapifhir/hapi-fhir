package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ClaimException {

        /**
         * Fulltime Student
         */
        STUDENT, 
        /**
         * Disabled
         */
        DISABLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ClaimException fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("student".equals(codeString))
          return STUDENT;
        if ("disabled".equals(codeString))
          return DISABLED;
        throw new FHIRException("Unknown ClaimException code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case STUDENT: return "student";
            case DISABLED: return "disabled";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/exception";
        }
        public String getDefinition() {
          switch (this) {
            case STUDENT: return "Fulltime Student";
            case DISABLED: return "Disabled";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case STUDENT: return "Student (Fulltime)";
            case DISABLED: return "Disabled";
            default: return "?";
          }
    }


}

