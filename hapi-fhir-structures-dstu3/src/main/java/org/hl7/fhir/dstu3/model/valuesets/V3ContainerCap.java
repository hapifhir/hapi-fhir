package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ContainerCap {

        /**
         * Cap types for medication containers
         */
        _MEDICATIONCAP, 
        /**
         * A cap designed to be difficult to open for children.  Generally requires multiple simultaneous actions (e.g. squeeze and twist) to open.  Used for products that may be dangerous if ingested or overdosed by children.
         */
        CHILD, 
        /**
         * A cap designed to be easily removed.  For products intended to be opened by persons with limited strength or dexterity.
         */
        EASY, 
        /**
         * A non-reactive plastic film covering over the opening of a container.
         */
        FILM, 
        /**
         * A foil covering (type of foil not specified) over the opening of a container
         */
        FOIL, 
        /**
         * A non-threaded cap that forms a tight-fitting closure on a container by pushing the fitted end into the conatiner opening
         */
        PUSH, 
        /**
         * A threaded cap that is screwed onto the opening of a container
         */
        SCR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ContainerCap fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_MedicationCap".equals(codeString))
          return _MEDICATIONCAP;
        if ("CHILD".equals(codeString))
          return CHILD;
        if ("EASY".equals(codeString))
          return EASY;
        if ("FILM".equals(codeString))
          return FILM;
        if ("FOIL".equals(codeString))
          return FOIL;
        if ("PUSH".equals(codeString))
          return PUSH;
        if ("SCR".equals(codeString))
          return SCR;
        throw new FHIRException("Unknown V3ContainerCap code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _MEDICATIONCAP: return "_MedicationCap";
            case CHILD: return "CHILD";
            case EASY: return "EASY";
            case FILM: return "FILM";
            case FOIL: return "FOIL";
            case PUSH: return "PUSH";
            case SCR: return "SCR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ContainerCap";
        }
        public String getDefinition() {
          switch (this) {
            case _MEDICATIONCAP: return "Cap types for medication containers";
            case CHILD: return "A cap designed to be difficult to open for children.  Generally requires multiple simultaneous actions (e.g. squeeze and twist) to open.  Used for products that may be dangerous if ingested or overdosed by children.";
            case EASY: return "A cap designed to be easily removed.  For products intended to be opened by persons with limited strength or dexterity.";
            case FILM: return "A non-reactive plastic film covering over the opening of a container.";
            case FOIL: return "A foil covering (type of foil not specified) over the opening of a container";
            case PUSH: return "A non-threaded cap that forms a tight-fitting closure on a container by pushing the fitted end into the conatiner opening";
            case SCR: return "A threaded cap that is screwed onto the opening of a container";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _MEDICATIONCAP: return "MedicationCap";
            case CHILD: return "ChildProof";
            case EASY: return "EasyOpen";
            case FILM: return "Film";
            case FOIL: return "Foil";
            case PUSH: return "Push Cap";
            case SCR: return "Screw Cap";
            default: return "?";
          }
    }


}

