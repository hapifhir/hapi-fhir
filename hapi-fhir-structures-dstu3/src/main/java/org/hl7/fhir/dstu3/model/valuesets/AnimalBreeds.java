package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum AnimalBreeds {

        /**
         * null
         */
        GSD, 
        /**
         * null
         */
        IRT, 
        /**
         * null
         */
        TIBMAS, 
        /**
         * null
         */
        GRET, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AnimalBreeds fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("gsd".equals(codeString))
          return GSD;
        if ("irt".equals(codeString))
          return IRT;
        if ("tibmas".equals(codeString))
          return TIBMAS;
        if ("gret".equals(codeString))
          return GRET;
        throw new FHIRException("Unknown AnimalBreeds code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GSD: return "gsd";
            case IRT: return "irt";
            case TIBMAS: return "tibmas";
            case GRET: return "gret";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/animal-breed";
        }
        public String getDefinition() {
          switch (this) {
            case GSD: return "";
            case IRT: return "";
            case TIBMAS: return "";
            case GRET: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GSD: return "German Shepherd Dog";
            case IRT: return "Irish Terrier";
            case TIBMAS: return "Tibetan Mastiff";
            case GRET: return "Golden Retriever";
            default: return "?";
          }
    }


}

