package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3AcknowledgementDetailType {

        /**
         * Definition:An issue which has prevented, or will prevent (unless a management is provided for the issue by the sender), the successful processing of an interaction.  Response interactions which include an issue which is an Error are a 'rejection', indicating that the request was not successfully processed. 

                        
                           Example:Unable to find specified patient.
         */
        E, 
        /**
         * Definition: The message relates to an issue which has no bearing on the successful processing of the request.  Information issues cannot be overridden by specifying a management.

                        
                           Example: A Patient's coverage will expire in 5 days.
         */
        I, 
        /**
         * Definition: The message relates to an issue which cannot prevent the successful processing of a request, but which could result in the processing not having the ideal or intended effect.  Managing a warning issue is not required for successful processing, but will suppress the warning from being raised. 

                        
                           Example:
                        

                        Unexpected additional repetitions of phone number have been ignored.
         */
        W, 
        /**
         * null
         */
        ERR, 
        /**
         * null
         */
        INFO, 
        /**
         * null
         */
        WARN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3AcknowledgementDetailType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("E".equals(codeString))
          return E;
        if ("I".equals(codeString))
          return I;
        if ("W".equals(codeString))
          return W;
        if ("ERR".equals(codeString))
          return ERR;
        if ("INFO".equals(codeString))
          return INFO;
        if ("WARN".equals(codeString))
          return WARN;
        throw new FHIRException("Unknown V3AcknowledgementDetailType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case E: return "E";
            case I: return "I";
            case W: return "W";
            case ERR: return "ERR";
            case INFO: return "INFO";
            case WARN: return "WARN";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/AcknowledgementDetailType";
        }
        public String getDefinition() {
          switch (this) {
            case E: return "Definition:An issue which has prevented, or will prevent (unless a management is provided for the issue by the sender), the successful processing of an interaction.  Response interactions which include an issue which is an Error are a 'rejection', indicating that the request was not successfully processed. \r\n\n                        \n                           Example:Unable to find specified patient.";
            case I: return "Definition: The message relates to an issue which has no bearing on the successful processing of the request.  Information issues cannot be overridden by specifying a management.\r\n\n                        \n                           Example: A Patient's coverage will expire in 5 days.";
            case W: return "Definition: The message relates to an issue which cannot prevent the successful processing of a request, but which could result in the processing not having the ideal or intended effect.  Managing a warning issue is not required for successful processing, but will suppress the warning from being raised. \r\n\n                        \n                           Example:\n                        \r\n\n                        Unexpected additional repetitions of phone number have been ignored.";
            case ERR: return "";
            case INFO: return "";
            case WARN: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case E: return "Error";
            case I: return "Information";
            case W: return "Warning";
            case ERR: return "ERR";
            case INFO: return "INFO";
            case WARN: return "WARN";
            default: return "?";
          }
    }


}

