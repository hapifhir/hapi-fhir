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

public enum ResponseCode {

        /**
         * The message was accepted and processed without error.
         */
        OK, 
        /**
         * Some internal unexpected error occurred - wait and try again. Note - this is usually used for things like database unavailable, which may be expected to resolve, though human intervention may be required.
         */
        TRANSIENTERROR, 
        /**
         * The message was rejected because of a problem with the content. There is no point in re-sending without change. The response narrative SHALL describe the issue.
         */
        FATALERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResponseCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ok".equals(codeString))
          return OK;
        if ("transient-error".equals(codeString))
          return TRANSIENTERROR;
        if ("fatal-error".equals(codeString))
          return FATALERROR;
        throw new FHIRException("Unknown ResponseCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OK: return "ok";
            case TRANSIENTERROR: return "transient-error";
            case FATALERROR: return "fatal-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/response-code";
        }
        public String getDefinition() {
          switch (this) {
            case OK: return "The message was accepted and processed without error.";
            case TRANSIENTERROR: return "Some internal unexpected error occurred - wait and try again. Note - this is usually used for things like database unavailable, which may be expected to resolve, though human intervention may be required.";
            case FATALERROR: return "The message was rejected because of a problem with the content. There is no point in re-sending without change. The response narrative SHALL describe the issue.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OK: return "OK";
            case TRANSIENTERROR: return "Transient Error";
            case FATALERROR: return "Fatal Error";
            default: return "?";
          }
    }


}

