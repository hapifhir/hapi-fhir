package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3QueryStatusCode {

        /**
         * Query status aborted
         */
        ABORTED, 
        /**
         * Query Status delivered response
         */
        DELIVEREDRESPONSE, 
        /**
         * Query Status executing
         */
        EXECUTING, 
        /**
         * Query Status new
         */
        NEW, 
        /**
         * Query Status wait continued
         */
        WAITCONTINUEDQUERYRESPONSE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3QueryStatusCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("aborted".equals(codeString))
          return ABORTED;
        if ("deliveredResponse".equals(codeString))
          return DELIVEREDRESPONSE;
        if ("executing".equals(codeString))
          return EXECUTING;
        if ("new".equals(codeString))
          return NEW;
        if ("waitContinuedQueryResponse".equals(codeString))
          return WAITCONTINUEDQUERYRESPONSE;
        throw new FHIRException("Unknown V3QueryStatusCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ABORTED: return "aborted";
            case DELIVEREDRESPONSE: return "deliveredResponse";
            case EXECUTING: return "executing";
            case NEW: return "new";
            case WAITCONTINUEDQUERYRESPONSE: return "waitContinuedQueryResponse";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/QueryStatusCode";
        }
        public String getDefinition() {
          switch (this) {
            case ABORTED: return "Query status aborted";
            case DELIVEREDRESPONSE: return "Query Status delivered response";
            case EXECUTING: return "Query Status executing";
            case NEW: return "Query Status new";
            case WAITCONTINUEDQUERYRESPONSE: return "Query Status wait continued";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ABORTED: return "aborted";
            case DELIVEREDRESPONSE: return "deliveredResponse";
            case EXECUTING: return "executing";
            case NEW: return "new";
            case WAITCONTINUEDQUERYRESPONSE: return "waitContinuedQueryResponse";
            default: return "?";
          }
    }


}

