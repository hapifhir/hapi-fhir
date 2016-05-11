package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3TableRules {

        /**
         * all
         */
        ALL, 
        /**
         * cols
         */
        COLS, 
        /**
         * groups
         */
        GROUPS, 
        /**
         * none
         */
        NONE, 
        /**
         * rows
         */
        ROWS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TableRules fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("all".equals(codeString))
          return ALL;
        if ("cols".equals(codeString))
          return COLS;
        if ("groups".equals(codeString))
          return GROUPS;
        if ("none".equals(codeString))
          return NONE;
        if ("rows".equals(codeString))
          return ROWS;
        throw new FHIRException("Unknown V3TableRules code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALL: return "all";
            case COLS: return "cols";
            case GROUPS: return "groups";
            case NONE: return "none";
            case ROWS: return "rows";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TableRules";
        }
        public String getDefinition() {
          switch (this) {
            case ALL: return "all";
            case COLS: return "cols";
            case GROUPS: return "groups";
            case NONE: return "none";
            case ROWS: return "rows";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALL: return "all";
            case COLS: return "cols";
            case GROUPS: return "groups";
            case NONE: return "none";
            case ROWS: return "rows";
            default: return "?";
          }
    }


}

