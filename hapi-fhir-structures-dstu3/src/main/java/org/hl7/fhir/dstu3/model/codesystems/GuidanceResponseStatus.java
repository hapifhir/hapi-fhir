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

public enum GuidanceResponseStatus {

        /**
         * The request was processed successfully
         */
        SUCCESS, 
        /**
         * The request was processed successfully, but more data may result in a more complete evaluation
         */
        DATAREQUESTED, 
        /**
         * The request was processed, but more data is required to complete the evaluation
         */
        DATAREQUIRED, 
        /**
         * The request is currently being processed
         */
        INPROGRESS, 
        /**
         * The request was not processed successfully
         */
        FAILURE, 
        /**
         * The response was entered in error
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GuidanceResponseStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("success".equals(codeString))
          return SUCCESS;
        if ("data-requested".equals(codeString))
          return DATAREQUESTED;
        if ("data-required".equals(codeString))
          return DATAREQUIRED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("failure".equals(codeString))
          return FAILURE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SUCCESS: return "success";
            case DATAREQUESTED: return "data-requested";
            case DATAREQUIRED: return "data-required";
            case INPROGRESS: return "in-progress";
            case FAILURE: return "failure";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/guidance-response-status";
        }
        public String getDefinition() {
          switch (this) {
            case SUCCESS: return "The request was processed successfully";
            case DATAREQUESTED: return "The request was processed successfully, but more data may result in a more complete evaluation";
            case DATAREQUIRED: return "The request was processed, but more data is required to complete the evaluation";
            case INPROGRESS: return "The request is currently being processed";
            case FAILURE: return "The request was not processed successfully";
            case ENTEREDINERROR: return "The response was entered in error";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SUCCESS: return "Success";
            case DATAREQUESTED: return "Data Requested";
            case DATAREQUIRED: return "Data Required";
            case INPROGRESS: return "In Progress";
            case FAILURE: return "Failure";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
    }


}

