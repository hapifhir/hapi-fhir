package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ServiceProduct {

        /**
         * Exam
         */
        EXAM, 
        /**
         * Flu shot
         */
        FLUSHOT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServiceProduct fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("exam".equals(codeString))
          return EXAM;
        if ("flushot".equals(codeString))
          return FLUSHOT;
        throw new FHIRException("Unknown ServiceProduct code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EXAM: return "exam";
            case FLUSHOT: return "flushot";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-serviceproduct";
        }
        public String getDefinition() {
          switch (this) {
            case EXAM: return "Exam";
            case FLUSHOT: return "Flu shot";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EXAM: return "Exam";
            case FLUSHOT: return "Flu shot";
            default: return "?";
          }
    }


}

