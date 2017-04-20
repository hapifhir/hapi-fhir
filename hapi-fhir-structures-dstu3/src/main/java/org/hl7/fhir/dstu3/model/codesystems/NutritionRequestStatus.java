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

public enum NutritionRequestStatus {

        /**
         * The request has been proposed.
         */
        PROPOSED, 
        /**
         * The request is in preliminary form prior to being sent.
         */
        DRAFT, 
        /**
         * The request has been planned.
         */
        PLANNED, 
        /**
         * The request has been placed.
         */
        REQUESTED, 
        /**
         * The request is 'actionable', but not all actions that are implied by it have occurred yet.
         */
        ACTIVE, 
        /**
         * Actions implied by the request have been temporarily halted, but are expected to continue later. May also be called "suspended".
         */
        ONHOLD, 
        /**
         * All actions that are implied by the order have occurred and no continuation is planned (this will rarely be made explicit).
         */
        COMPLETED, 
        /**
         * The request has been withdrawn and is no longer actionable.
         */
        CANCELLED, 
        /**
         * The request was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NutritionRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown NutritionRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case DRAFT: return "draft";
            case PLANNED: return "planned";
            case REQUESTED: return "requested";
            case ACTIVE: return "active";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/nutrition-request-status";
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "The request has been proposed.";
            case DRAFT: return "The request is in preliminary form prior to being sent.";
            case PLANNED: return "The request has been planned.";
            case REQUESTED: return "The request has been placed.";
            case ACTIVE: return "The request is 'actionable', but not all actions that are implied by it have occurred yet.";
            case ONHOLD: return "Actions implied by the request have been temporarily halted, but are expected to continue later. May also be called \"suspended\".";
            case COMPLETED: return "All actions that are implied by the order have occurred and no continuation is planned (this will rarely be made explicit).";
            case CANCELLED: return "The request has been withdrawn and is no longer actionable.";
            case ENTEREDINERROR: return "The request was entered in error and voided.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case DRAFT: return "Draft";
            case PLANNED: return "Planned";
            case REQUESTED: return "Requested";
            case ACTIVE: return "Active";
            case ONHOLD: return "On-Hold";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
    }


}

