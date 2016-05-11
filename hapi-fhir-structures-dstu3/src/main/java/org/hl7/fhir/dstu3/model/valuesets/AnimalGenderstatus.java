package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum AnimalGenderstatus {

        /**
         * The animal has been sterilized, castrated or otherwise made infertile.
         */
        NEUTERED, 
        /**
         * The animal's reproductive organs are intact.
         */
        INTACT, 
        /**
         * Unable to determine whether the animal has been neutered.
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AnimalGenderstatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("neutered".equals(codeString))
          return NEUTERED;
        if ("intact".equals(codeString))
          return INTACT;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown AnimalGenderstatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NEUTERED: return "neutered";
            case INTACT: return "intact";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/animal-genderstatus";
        }
        public String getDefinition() {
          switch (this) {
            case NEUTERED: return "The animal has been sterilized, castrated or otherwise made infertile.";
            case INTACT: return "The animal's reproductive organs are intact.";
            case UNKNOWN: return "Unable to determine whether the animal has been neutered.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NEUTERED: return "Neutered";
            case INTACT: return "Intact";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

