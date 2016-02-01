package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum MessageTransport {

        /**
         * The application sends or receives messages using HTTP POST (may be over http: or https:).
         */
        HTTP, 
        /**
         * The application sends or receives messages using File Transfer Protocol.
         */
        FTP, 
        /**
         * The application sends or receives messages using HL7's Minimal Lower Level Protocol.
         */
        MLLP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MessageTransport fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("http".equals(codeString))
          return HTTP;
        if ("ftp".equals(codeString))
          return FTP;
        if ("mllp".equals(codeString))
          return MLLP;
        throw new FHIRException("Unknown MessageTransport code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HTTP: return "http";
            case FTP: return "ftp";
            case MLLP: return "mllp";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/message-transport";
        }
        public String getDefinition() {
          switch (this) {
            case HTTP: return "The application sends or receives messages using HTTP POST (may be over http: or https:).";
            case FTP: return "The application sends or receives messages using File Transfer Protocol.";
            case MLLP: return "The application sends or receives messages using HL7's Minimal Lower Level Protocol.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HTTP: return "HTTP";
            case FTP: return "FTP";
            case MLLP: return "MLLP";
            default: return "?";
          }
    }


}

