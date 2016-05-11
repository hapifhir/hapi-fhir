package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ChoiceListOrientation {

        /**
         * List choices along the horizontal axis
         */
        HORIZONTAL, 
        /**
         * List choices down the vertical axis
         */
        VERTICAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ChoiceListOrientation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("horizontal".equals(codeString))
          return HORIZONTAL;
        if ("vertical".equals(codeString))
          return VERTICAL;
        throw new FHIRException("Unknown ChoiceListOrientation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HORIZONTAL: return "horizontal";
            case VERTICAL: return "vertical";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/choice-list-orientation";
        }
        public String getDefinition() {
          switch (this) {
            case HORIZONTAL: return "List choices along the horizontal axis";
            case VERTICAL: return "List choices down the vertical axis";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HORIZONTAL: return "Horizontal";
            case VERTICAL: return "Vertical";
            default: return "?";
          }
    }


}

