package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum GoalStatus {

        /**
         * A goal is proposed for this patient.
         */
        PROPOSED, 
        /**
         * A goal is planned for this patient.
         */
        PLANNED, 
        /**
         * A proposed goal was accepted or acknowledged.
         */
        ACCEPTED, 
        /**
         * The goal is being sought actively.
         */
        ACTIVE, 
        /**
         * The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.
         */
        ONHOLD, 
        /**
         * The goal is no longer being sought.
         */
        COMPLETED, 
        /**
         * The goal has been abandoned.
         */
        CANCELLED, 
        /**
         * The goal was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * A proposed goal was rejected.
         */
        REJECTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
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
        if ("rejected".equals(codeString))
          return REJECTED;
        throw new FHIRException("Unknown GoalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case PLANNED: return "planned";
            case ACCEPTED: return "accepted";
            case ACTIVE: return "active";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            case REJECTED: return "rejected";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/goal-status";
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "A goal is proposed for this patient.";
            case PLANNED: return "A goal is planned for this patient.";
            case ACCEPTED: return "A proposed goal was accepted or acknowledged.";
            case ACTIVE: return "The goal is being sought actively.";
            case ONHOLD: return "The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.";
            case COMPLETED: return "The goal is no longer being sought.";
            case CANCELLED: return "The goal has been abandoned.";
            case ENTEREDINERROR: return "The goal was entered in error and voided.";
            case REJECTED: return "A proposed goal was rejected.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case PLANNED: return "Planned";
            case ACCEPTED: return "Accepted";
            case ACTIVE: return "Active";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            case REJECTED: return "Rejected";
            default: return "?";
          }
    }


}

