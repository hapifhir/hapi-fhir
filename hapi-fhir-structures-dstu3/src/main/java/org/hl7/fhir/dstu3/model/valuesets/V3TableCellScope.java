package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3TableCellScope {

        /**
         * col
         */
        COL, 
        /**
         * colgroup
         */
        COLGROUP, 
        /**
         * row
         */
        ROW, 
        /**
         * rowgroup
         */
        ROWGROUP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TableCellScope fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("col".equals(codeString))
          return COL;
        if ("colgroup".equals(codeString))
          return COLGROUP;
        if ("row".equals(codeString))
          return ROW;
        if ("rowgroup".equals(codeString))
          return ROWGROUP;
        throw new FHIRException("Unknown V3TableCellScope code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COL: return "col";
            case COLGROUP: return "colgroup";
            case ROW: return "row";
            case ROWGROUP: return "rowgroup";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TableCellScope";
        }
        public String getDefinition() {
          switch (this) {
            case COL: return "col";
            case COLGROUP: return "colgroup";
            case ROW: return "row";
            case ROWGROUP: return "rowgroup";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COL: return "col";
            case COLGROUP: return "colgroup";
            case ROW: return "row";
            case ROWGROUP: return "rowgroup";
            default: return "?";
          }
    }


}

