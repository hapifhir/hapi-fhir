package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum PatientMpiMatch {

        /**
         * This record meets the MPI criteria to be automatically considered as a full match.
         */
        CERTAIN, 
        /**
         * This record is a close match, but not a certain match. Additional review (e.g. by a human) may be required before using this as a match.
         */
        PROBABLE, 
        /**
         * This record may be a matching one. Additional review (e.g. by a human) SHOULD be performed before using this as a match.
         */
        POSSIBLE, 
        /**
         * This record is known not to be a match. Note that usually non-matching records are not returned, but in some cases records previously or likely considered as a match may specifically be negated by the MPI.
         */
        CERTAINLYNOT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PatientMpiMatch fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("certain".equals(codeString))
          return CERTAIN;
        if ("probable".equals(codeString))
          return PROBABLE;
        if ("possible".equals(codeString))
          return POSSIBLE;
        if ("certainly-not".equals(codeString))
          return CERTAINLYNOT;
        throw new FHIRException("Unknown PatientMpiMatch code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CERTAIN: return "certain";
            case PROBABLE: return "probable";
            case POSSIBLE: return "possible";
            case CERTAINLYNOT: return "certainly-not";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/patient-mpi-match";
        }
        public String getDefinition() {
          switch (this) {
            case CERTAIN: return "This record meets the MPI criteria to be automatically considered as a full match.";
            case PROBABLE: return "This record is a close match, but not a certain match. Additional review (e.g. by a human) may be required before using this as a match.";
            case POSSIBLE: return "This record may be a matching one. Additional review (e.g. by a human) SHOULD be performed before using this as a match.";
            case CERTAINLYNOT: return "This record is known not to be a match. Note that usually non-matching records are not returned, but in some cases records previously or likely considered as a match may specifically be negated by the MPI.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CERTAIN: return "Certain Match";
            case PROBABLE: return "Probable Match";
            case POSSIBLE: return "Possible Match";
            case CERTAINLYNOT: return "Certainly Not a Match";
            default: return "?";
          }
    }


}

