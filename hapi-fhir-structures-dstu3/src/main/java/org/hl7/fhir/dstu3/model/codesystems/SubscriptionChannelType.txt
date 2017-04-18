package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum SubscriptionChannelType {

        /**
         * The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the service base, and an update (PUT) is made.
         */
        RESTHOOK, 
        /**
         * The channel is executed by sending a packet across a web socket connection maintained by the client. The URL identifies the websocket, and the client binds to this URL.
         */
        WEBSOCKET, 
        /**
         * The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).
         */
        EMAIL, 
        /**
         * The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).
         */
        SMS, 
        /**
         * The channel is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc.) to the application identified in the URI.
         */
        MESSAGE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SubscriptionChannelType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("rest-hook".equals(codeString))
          return RESTHOOK;
        if ("websocket".equals(codeString))
          return WEBSOCKET;
        if ("email".equals(codeString))
          return EMAIL;
        if ("sms".equals(codeString))
          return SMS;
        if ("message".equals(codeString))
          return MESSAGE;
        throw new FHIRException("Unknown SubscriptionChannelType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RESTHOOK: return "rest-hook";
            case WEBSOCKET: return "websocket";
            case EMAIL: return "email";
            case SMS: return "sms";
            case MESSAGE: return "message";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/subscription-channel-type";
        }
        public String getDefinition() {
          switch (this) {
            case RESTHOOK: return "The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the service base, and an update (PUT) is made.";
            case WEBSOCKET: return "The channel is executed by sending a packet across a web socket connection maintained by the client. The URL identifies the websocket, and the client binds to this URL.";
            case EMAIL: return "The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).";
            case SMS: return "The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).";
            case MESSAGE: return "The channel is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc.) to the application identified in the URI.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RESTHOOK: return "Rest Hook";
            case WEBSOCKET: return "Websocket";
            case EMAIL: return "Email";
            case SMS: return "SMS";
            case MESSAGE: return "Message";
            default: return "?";
          }
    }


}

