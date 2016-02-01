package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum EncounterSpecialArrangements {

        /**
         * null
         */
        WHEEL, 
        /**
         * null
         */
        STRET, 
        /**
         * null
         */
        INT, 
        /**
         * null
         */
        ATT, 
        /**
         * null
         */
        DOG, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterSpecialArrangements fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("wheel".equals(codeString))
          return WHEEL;
        if ("stret".equals(codeString))
          return STRET;
        if ("int".equals(codeString))
          return INT;
        if ("att".equals(codeString))
          return ATT;
        if ("dog".equals(codeString))
          return DOG;
        throw new FHIRException("Unknown EncounterSpecialArrangements code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case WHEEL: return "wheel";
            case STRET: return "stret";
            case INT: return "int";
            case ATT: return "att";
            case DOG: return "dog";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/encounter-special-arrangements";
        }
        public String getDefinition() {
          switch (this) {
            case WHEEL: return "";
            case STRET: return "";
            case INT: return "";
            case ATT: return "";
            case DOG: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case WHEEL: return "Wheelchair";
            case STRET: return "Stretcher";
            case INT: return "Interpreter";
            case ATT: return "Attendant";
            case DOG: return "Guide dog";
            default: return "?";
          }
    }


}

