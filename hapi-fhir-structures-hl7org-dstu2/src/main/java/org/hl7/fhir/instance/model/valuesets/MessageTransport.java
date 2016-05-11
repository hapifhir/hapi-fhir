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
        public static MessageTransport fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("http".equals(codeString))
          return HTTP;
        if ("ftp".equals(codeString))
          return FTP;
        if ("mllp".equals(codeString))
          return MLLP;
        throw new Exception("Unknown MessageTransport code '"+codeString+"'");
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

