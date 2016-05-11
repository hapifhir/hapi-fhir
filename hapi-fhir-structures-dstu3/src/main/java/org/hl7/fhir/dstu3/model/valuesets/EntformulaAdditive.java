package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum EntformulaAdditive {

        /**
         * null
         */
        LIPID, 
        /**
         * null
         */
        PROTEIN, 
        /**
         * null
         */
        CARBOHYDRATE, 
        /**
         * null
         */
        FIBER, 
        /**
         * null
         */
        WATER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EntformulaAdditive fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("lipid".equals(codeString))
          return LIPID;
        if ("protein".equals(codeString))
          return PROTEIN;
        if ("carbohydrate".equals(codeString))
          return CARBOHYDRATE;
        if ("fiber".equals(codeString))
          return FIBER;
        if ("water".equals(codeString))
          return WATER;
        throw new FHIRException("Unknown EntformulaAdditive code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LIPID: return "lipid";
            case PROTEIN: return "protein";
            case CARBOHYDRATE: return "carbohydrate";
            case FIBER: return "fiber";
            case WATER: return "water";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/entformula-additive";
        }
        public String getDefinition() {
          switch (this) {
            case LIPID: return "";
            case PROTEIN: return "";
            case CARBOHYDRATE: return "";
            case FIBER: return "";
            case WATER: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LIPID: return "Modular lipid enteral formula component";
            case PROTEIN: return "Modular protein enteral formula component";
            case CARBOHYDRATE: return "Modular carbohydrate enteral formula component";
            case FIBER: return "Modular fiber enteral formula component";
            case WATER: return "Added water";
            default: return "?";
          }
    }


}

