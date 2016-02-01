package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3TableCellVerticalAlign {

        /**
         * baseline
         */
        BASELINE, 
        /**
         * bottom
         */
        BOTTOM, 
        /**
         * middle
         */
        MIDDLE, 
        /**
         * top
         */
        TOP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TableCellVerticalAlign fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("baseline".equals(codeString))
          return BASELINE;
        if ("bottom".equals(codeString))
          return BOTTOM;
        if ("middle".equals(codeString))
          return MIDDLE;
        if ("top".equals(codeString))
          return TOP;
        throw new FHIRException("Unknown V3TableCellVerticalAlign code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BASELINE: return "baseline";
            case BOTTOM: return "bottom";
            case MIDDLE: return "middle";
            case TOP: return "top";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TableCellVerticalAlign";
        }
        public String getDefinition() {
          switch (this) {
            case BASELINE: return "baseline";
            case BOTTOM: return "bottom";
            case MIDDLE: return "middle";
            case TOP: return "top";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BASELINE: return "baseline";
            case BOTTOM: return "bottom";
            case MIDDLE: return "middle";
            case TOP: return "top";
            default: return "?";
          }
    }


}

