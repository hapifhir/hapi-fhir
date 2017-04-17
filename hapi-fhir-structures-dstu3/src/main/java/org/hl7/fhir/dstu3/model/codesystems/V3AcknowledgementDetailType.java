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

