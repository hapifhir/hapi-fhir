package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3CommunicationFunctionType {

        /**
         * The entity is the receiver of the transmission.
         */
        RCV, 
        /**
         * The entity is the one to which the response or reply to the transmission should be sent.
         */
        RSP, 
        /**
         * The entity is the sender of the transmission.
         */
        SND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3CommunicationFunctionType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("RCV".equals(codeString))
          return RCV;
        if ("RSP".equals(codeString))
          return RSP;
        if ("SND".equals(codeString))
          return SND;
        throw new FHIRException("Unknown V3CommunicationFunctionType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RCV: return "RCV";
            case RSP: return "RSP";
            case SND: return "SND";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/CommunicationFunctionType";
        }
        public String getDefinition() {
          switch (this) {
            case RCV: return "The entity is the receiver of the transmission.";
            case RSP: return "The entity is the one to which the response or reply to the transmission should be sent.";
            case SND: return "The entity is the sender of the transmission.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RCV: return "receiver";
            case RSP: return "respond to";
            case SND: return "sender";
            default: return "?";
          }
    }


}

