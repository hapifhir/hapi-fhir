package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3DocumentStorage {

        /**
         * A storage status in which a document is available on-line.
         */
        AC, 
        /**
         * A storage status in which a document is available on-line and is also stored off-line for long-term access.
         */
        AA, 
        /**
         * A storage status in which a document has been stored off-line for long-term access.
         */
        AR, 
        /**
         * A storage status in which a document is no longer available in this system.
         */
        PU, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3DocumentStorage fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AC".equals(codeString))
          return AC;
        if ("AA".equals(codeString))
          return AA;
        if ("AR".equals(codeString))
          return AR;
        if ("PU".equals(codeString))
          return PU;
        throw new FHIRException("Unknown V3DocumentStorage code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AC: return "AC";
            case AA: return "AA";
            case AR: return "AR";
            case PU: return "PU";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/DocumentStorage";
        }
        public String getDefinition() {
          switch (this) {
            case AC: return "A storage status in which a document is available on-line.";
            case AA: return "A storage status in which a document is available on-line and is also stored off-line for long-term access.";
            case AR: return "A storage status in which a document has been stored off-line for long-term access.";
            case PU: return "A storage status in which a document is no longer available in this system.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AC: return "active";
            case AA: return "active and archived";
            case AR: return "archived (not active)";
            case PU: return "purged";
            default: return "?";
          }
    }


}

