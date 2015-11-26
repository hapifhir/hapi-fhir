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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


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
        public static V3TelecommunicationCapabilities fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown V3TelecommunicationCapabilities code '"+codeString+"'");
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

