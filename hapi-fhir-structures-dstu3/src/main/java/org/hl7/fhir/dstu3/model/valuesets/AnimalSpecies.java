package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum AnimalSpecies {

        /**
         * Canis lupus familiaris
         */
        CANISLF, 
        /**
         * Ovis aries
         */
        OVISA, 
        /**
         * Serinus canaria domestica
         */
        SERINUSCD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AnimalSpecies fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("canislf".equals(codeString))
          return CANISLF;
        if ("ovisa".equals(codeString))
          return OVISA;
        if ("serinuscd".equals(codeString))
          return SERINUSCD;
        throw new FHIRException("Unknown AnimalSpecies code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CANISLF: return "canislf";
            case OVISA: return "ovisa";
            case SERINUSCD: return "serinuscd";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/animal-species";
        }
        public String getDefinition() {
          switch (this) {
            case CANISLF: return "Canis lupus familiaris";
            case OVISA: return "Ovis aries";
            case SERINUSCD: return "Serinus canaria domestica";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CANISLF: return "Dog";
            case OVISA: return "Sheep";
            case SERINUSCD: return "Domestic Canary";
            default: return "?";
          }
    }


}

