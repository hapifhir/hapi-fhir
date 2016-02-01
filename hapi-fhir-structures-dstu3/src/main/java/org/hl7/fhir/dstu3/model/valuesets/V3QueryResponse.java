package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3QueryResponse {

        /**
         * Query Error.  Application Error.
         */
        AE, 
        /**
         * No errors, but no data was found matching the query request specification.
         */
        NF, 
        /**
         * Query reponse data found for 1 or more result sets matching the query request specification.
         */
        OK, 
        /**
         * QueryError. Problem with input ParmetersError
         */
        QE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3QueryResponse fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AE".equals(codeString))
          return AE;
        if ("NF".equals(codeString))
          return NF;
        if ("OK".equals(codeString))
          return OK;
        if ("QE".equals(codeString))
          return QE;
        throw new FHIRException("Unknown V3QueryResponse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AE: return "AE";
            case NF: return "NF";
            case OK: return "OK";
            case QE: return "QE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/QueryResponse";
        }
        public String getDefinition() {
          switch (this) {
            case AE: return "Query Error.  Application Error.";
            case NF: return "No errors, but no data was found matching the query request specification.";
            case OK: return "Query reponse data found for 1 or more result sets matching the query request specification.";
            case QE: return "QueryError. Problem with input ParmetersError";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AE: return "ApplicationError";
            case NF: return "No data found";
            case OK: return "Data found";
            case QE: return "QueryParameterError";
            default: return "?";
          }
    }


}

