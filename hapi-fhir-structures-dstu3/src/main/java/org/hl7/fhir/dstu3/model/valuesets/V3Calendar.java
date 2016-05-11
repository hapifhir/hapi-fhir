package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3Calendar {

        /**
         * The Gregorian calendar is the calendar in effect in most countries of Christian influence since approximately 1582. This calendar superceded the Julian calendar.
         */
        GREG, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Calendar fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("GREG".equals(codeString))
          return GREG;
        throw new FHIRException("Unknown V3Calendar code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GREG: return "GREG";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/Calendar";
        }
        public String getDefinition() {
          switch (this) {
            case GREG: return "The Gregorian calendar is the calendar in effect in most countries of Christian influence since approximately 1582. This calendar superceded the Julian calendar.";
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

