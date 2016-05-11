package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3CalendarType {

        /**
         * The Gregorian calendar is in effect in the most countries of Christian influence since approximately 1582.  This calendar superceded the Julian calendar.
         */
        GREG, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3CalendarType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("GREG".equals(codeString))
          return GREG;
        throw new FHIRException("Unknown V3CalendarType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GREG: return "GREG";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/CalendarType";
        }
        public String getDefinition() {
          switch (this) {
            case GREG: return "The Gregorian calendar is in effect in the most countries of Christian influence since approximately 1582.  This calendar superceded the Julian calendar.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GREG: return "Gregorian";
            default: return "?";
          }
    }


}

