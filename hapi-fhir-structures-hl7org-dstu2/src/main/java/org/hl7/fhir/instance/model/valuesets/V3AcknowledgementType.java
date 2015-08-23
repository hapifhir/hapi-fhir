package org.hl7.fhir.instance.model.valuesets;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


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
        public static V3AcknowledgementType fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown V3AcknowledgementType code '"+codeString+"'");
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

