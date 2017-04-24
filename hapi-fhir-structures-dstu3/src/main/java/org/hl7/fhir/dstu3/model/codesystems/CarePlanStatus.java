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

public enum CarePlanStatus {

        /**
         * The plan is in development or awaiting use but is not yet intended to be acted upon.
         */
        DRAFT, 
        /**
         * The plan is intended to be followed and used as part of patient care.
         */
        ACTIVE, 
        /**
         * The plan has been temporarily stopped but is expected to resume in the future.
         */
        SUSPENDED, 
        /**
         * The plan is no longer in use and is not expected to be followed or used in patient care.
         */
        COMPLETED, 
        /**
         * The plan was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * The plan has been terminated prior to reaching completion (though it may have been replaced by a new plan).
         */
        CANCELLED, 
        /**
         * The authoring system doesn't know the current state of the care plan.
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanStatus fromCode(String codeString) throws FHIRException {
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
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown CarePlanStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case CANCELLED: return "cancelled";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/care-plan-status";
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The plan is in development or awaiting use but is not yet intended to be acted upon.";
            case ACTIVE: return "The plan is intended to be followed and used as part of patient care.";
            case SUSPENDED: return "The plan has been temporarily stopped but is expected to resume in the future.";
            case COMPLETED: return "The plan is no longer in use and is not expected to be followed or used in patient care.";
            case ENTEREDINERROR: return "The plan was entered in error and voided.";
            case CANCELLED: return "The plan has been terminated prior to reaching completion (though it may have been replaced by a new plan).";
            case UNKNOWN: return "The authoring system doesn't know the current state of the care plan.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Pending";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered In Error";
            case CANCELLED: return "Cancelled";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

