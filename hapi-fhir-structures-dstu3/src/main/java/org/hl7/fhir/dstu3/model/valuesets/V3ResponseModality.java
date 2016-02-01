package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ResponseModality {

        /**
         * Query response to be sent as an HL7 Batch.
         */
        B, 
        /**
         * Query response to occur in real time.
         */
        R, 
        /**
         * Query response to sent as a series of responses at the same time without the use of batch formatting.
         */
        T, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ResponseModality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("B".equals(codeString))
          return B;
        if ("R".equals(codeString))
          return R;
        if ("T".equals(codeString))
          return T;
        throw new FHIRException("Unknown V3ResponseModality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case B: return "B";
            case R: return "R";
            case T: return "T";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ResponseModality";
        }
        public String getDefinition() {
          switch (this) {
            case B: return "Query response to be sent as an HL7 Batch.";
            case R: return "Query response to occur in real time.";
            case T: return "Query response to sent as a series of responses at the same time without the use of batch formatting.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case B: return "Batch";
            case R: return "Real Time";
            case T: return "Bolus";
            default: return "?";
          }
    }


}

