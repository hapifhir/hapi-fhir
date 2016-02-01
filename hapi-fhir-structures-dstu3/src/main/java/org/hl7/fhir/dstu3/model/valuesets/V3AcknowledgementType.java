package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3AcknowledgementType {

        /**
         * Receiving application successfully processed message.
         */
        AA, 
        /**
         * Receiving application found error in processing message.  Sending error response with additional error detail information.
         */
        AE, 
        /**
         * Receiving application failed to process message for reason unrelated to content or format.  Original message sender must decide on whether to automatically send message again.
         */
        AR, 
        /**
         * Receiving message handling service accepts responsibility for passing message onto receiving application.
         */
        CA, 
        /**
         * Receiving message handling service cannot accept message for any other reason (e.g. message sequence number, etc.).
         */
        CE, 
        /**
         * Receiving message handling service rejects message if interaction identifier, version or processing mode is incompatible with known receiving application role information.
         */
        CR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3AcknowledgementType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AA".equals(codeString))
          return AA;
        if ("AE".equals(codeString))
          return AE;
        if ("AR".equals(codeString))
          return AR;
        if ("CA".equals(codeString))
          return CA;
        if ("CE".equals(codeString))
          return CE;
        if ("CR".equals(codeString))
          return CR;
        throw new FHIRException("Unknown V3AcknowledgementType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AA: return "AA";
            case AE: return "AE";
            case AR: return "AR";
            case CA: return "CA";
            case CE: return "CE";
            case CR: return "CR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/AcknowledgementType";
        }
        public String getDefinition() {
          switch (this) {
            case AA: return "Receiving application successfully processed message.";
            case AE: return "Receiving application found error in processing message.  Sending error response with additional error detail information.";
            case AR: return "Receiving application failed to process message for reason unrelated to content or format.  Original message sender must decide on whether to automatically send message again.";
            case CA: return "Receiving message handling service accepts responsibility for passing message onto receiving application.";
            case CE: return "Receiving message handling service cannot accept message for any other reason (e.g. message sequence number, etc.).";
            case CR: return "Receiving message handling service rejects message if interaction identifier, version or processing mode is incompatible with known receiving application role information.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AA: return "Application Acknowledgement Accept";
            case AE: return "Application Acknowledgement Error";
            case AR: return "Application Acknowledgement Reject";
            case CA: return "Accept Acknowledgement Commit Accept";
            case CE: return "Accept Acknowledgement Commit Error";
            case CR: return "Accept Acknowledgement Commit Reject";
            default: return "?";
          }
    }


}

