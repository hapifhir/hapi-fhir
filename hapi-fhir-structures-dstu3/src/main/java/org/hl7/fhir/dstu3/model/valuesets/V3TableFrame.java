package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3TableFrame {

        /**
         * above
         */
        ABOVE, 
        /**
         * below
         */
        BELOW, 
        /**
         * border
         */
        BORDER, 
        /**
         * box
         */
        BOX, 
        /**
         * hsides
         */
        HSIDES, 
        /**
         * lhs
         */
        LHS, 
        /**
         * rhs
         */
        RHS, 
        /**
         * void
         */
        VOID, 
        /**
         * vsides
         */
        VSIDES, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TableFrame fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("above".equals(codeString))
          return ABOVE;
        if ("below".equals(codeString))
          return BELOW;
        if ("border".equals(codeString))
          return BORDER;
        if ("box".equals(codeString))
          return BOX;
        if ("hsides".equals(codeString))
          return HSIDES;
        if ("lhs".equals(codeString))
          return LHS;
        if ("rhs".equals(codeString))
          return RHS;
        if ("void".equals(codeString))
          return VOID;
        if ("vsides".equals(codeString))
          return VSIDES;
        throw new FHIRException("Unknown V3TableFrame code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ABOVE: return "above";
            case BELOW: return "below";
            case BORDER: return "border";
            case BOX: return "box";
            case HSIDES: return "hsides";
            case LHS: return "lhs";
            case RHS: return "rhs";
            case VOID: return "void";
            case VSIDES: return "vsides";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TableFrame";
        }
        public String getDefinition() {
          switch (this) {
            case ABOVE: return "above";
            case BELOW: return "below";
            case BORDER: return "border";
            case BOX: return "box";
            case HSIDES: return "hsides";
            case LHS: return "lhs";
            case RHS: return "rhs";
            case VOID: return "void";
            case VSIDES: return "vsides";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ABOVE: return "above";
            case BELOW: return "below";
            case BORDER: return "border";
            case BOX: return "box";
            case HSIDES: return "hsides";
            case LHS: return "lhs";
            case RHS: return "rhs";
            case VOID: return "void";
            case VSIDES: return "vsides";
            default: return "?";
          }
    }


}

