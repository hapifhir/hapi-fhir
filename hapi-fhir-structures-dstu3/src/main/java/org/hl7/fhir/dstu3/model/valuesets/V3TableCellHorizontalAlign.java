package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3TableCellHorizontalAlign {

        /**
         * center
         */
        CENTER, 
        /**
         * char
         */
        CHAR, 
        /**
         * justify
         */
        JUSTIFY, 
        /**
         * left
         */
        LEFT, 
        /**
         * right
         */
        RIGHT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TableCellHorizontalAlign fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("center".equals(codeString))
          return CENTER;
        if ("char".equals(codeString))
          return CHAR;
        if ("justify".equals(codeString))
          return JUSTIFY;
        if ("left".equals(codeString))
          return LEFT;
        if ("right".equals(codeString))
          return RIGHT;
        throw new FHIRException("Unknown V3TableCellHorizontalAlign code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CENTER: return "center";
            case CHAR: return "char";
            case JUSTIFY: return "justify";
            case LEFT: return "left";
            case RIGHT: return "right";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TableCellHorizontalAlign";
        }
        public String getDefinition() {
          switch (this) {
            case CENTER: return "center";
            case CHAR: return "char";
            case JUSTIFY: return "justify";
            case LEFT: return "left";
            case RIGHT: return "right";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CENTER: return "center";
            case CHAR: return "char";
            case JUSTIFY: return "justify";
            case LEFT: return "left";
            case RIGHT: return "right";
            default: return "?";
          }
    }


}

