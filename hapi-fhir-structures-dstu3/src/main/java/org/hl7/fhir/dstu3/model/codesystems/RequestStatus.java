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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum RequestStatus {

        /**
         * The request is in preliminary form prior to being sent.
         */
        DRAFT, 
        /**
         * The request is complete and is ready for fulfillment.
         */
        ACTIVE, 
        /**
         * The request has been held by originating system/user request.
         */
        SUSPENDED, 
        /**
         * The work has been completed, the report(s) released, and no further work is planned.
         */
        COMPLETED, 
        /**
         * The request was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * The request has been withdrawn.
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new FHIRException("Unknown RequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/request-status";
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The request is in preliminary form prior to being sent.";
            case ACTIVE: return "The request is complete and is ready for fulfillment.";
            case SUSPENDED: return "The request has been held by originating system/user request.";
            case COMPLETED: return "The work has been completed, the report(s) released, and no further work is planned.";
            case ENTEREDINERROR: return "The request was entered in error and voided.";
            case CANCELLED: return "The request has been withdrawn.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
    }


}

