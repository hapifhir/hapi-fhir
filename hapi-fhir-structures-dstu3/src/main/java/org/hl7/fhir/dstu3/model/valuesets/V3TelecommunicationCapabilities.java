package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3TelecommunicationCapabilities {

        /**
         * Description: This device can receive data calls (i.e. modem).
         */
        DATA, 
        /**
         * Description: This device can receive faxes.
         */
        FAX, 
        /**
         * Description: This device can receive SMS messages.
         */
        SMS, 
        /**
         * Description: This device is a text telephone.
         */
        TTY, 
        /**
         * Description: This device can receive voice calls (i.e. talking to another person, or a recording device, or a voice activated computer).
         */
        VOICE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TelecommunicationCapabilities fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("data".equals(codeString))
          return DATA;
        if ("fax".equals(codeString))
          return FAX;
        if ("sms".equals(codeString))
          return SMS;
        if ("tty".equals(codeString))
          return TTY;
        if ("voice".equals(codeString))
          return VOICE;
        throw new FHIRException("Unknown V3TelecommunicationCapabilities code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DATA: return "data";
            case FAX: return "fax";
            case SMS: return "sms";
            case TTY: return "tty";
            case VOICE: return "voice";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TelecommunicationCapabilities";
        }
        public String getDefinition() {
          switch (this) {
            case DATA: return "Description: This device can receive data calls (i.e. modem).";
            case FAX: return "Description: This device can receive faxes.";
            case SMS: return "Description: This device can receive SMS messages.";
            case TTY: return "Description: This device is a text telephone.";
            case VOICE: return "Description: This device can receive voice calls (i.e. talking to another person, or a recording device, or a voice activated computer).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DATA: return "data";
            case FAX: return "fax";
            case SMS: return "sms";
            case TTY: return "text";
            case VOICE: return "voice";
            default: return "?";
          }
    }


}

