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

public enum TaskStatus {

        /**
         * The task is not yet ready to be acted upon.
         */
        DRAFT, 
        /**
         * The task is ready to be acted upon and action is sought.
         */
        REQUESTED, 
        /**
         * A potential performer has claimed ownership of the task and is evaluating whether to perform it.
         */
        RECEIVED, 
        /**
         * The potential performer has agreed to execute the task but has not yet started work.
         */
        ACCEPTED, 
        /**
         * The potential performer who claimed ownership of the task has decided not to execute it prior to performing any action.
         */
        REJECTED, 
        /**
         * Task is ready to be performed, but no action has yet been taken.  Used in place of requested/received/accepted/rejected when request assignment and acceptance is a given.
         */
        READY, 
        /**
         * The task was not completed.
         */
        CANCELLED, 
        /**
         * Task has been started but is not yet complete.
         */
        INPROGRESS, 
        /**
         * Task has been started but work has been paused.
         */
        ONHOLD, 
        /**
         * The task was attempted but could not be completed due to some error.
         */
        FAILED, 
        /**
         * The task has been completed.
         */
        COMPLETED, 
        /**
         * The task should never have existed and is retained only because of the possibility it may have used.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static TaskStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("received".equals(codeString))
          return RECEIVED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("ready".equals(codeString))
          return READY;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("failed".equals(codeString))
          return FAILED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown TaskStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case ACCEPTED: return "accepted";
            case REJECTED: return "rejected";
            case READY: return "ready";
            case CANCELLED: return "cancelled";
            case INPROGRESS: return "in-progress";
            case ONHOLD: return "on-hold";
            case FAILED: return "failed";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/task-status";
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The task is not yet ready to be acted upon.";
            case REQUESTED: return "The task is ready to be acted upon and action is sought.";
            case RECEIVED: return "A potential performer has claimed ownership of the task and is evaluating whether to perform it.";
            case ACCEPTED: return "The potential performer has agreed to execute the task but has not yet started work.";
            case REJECTED: return "The potential performer who claimed ownership of the task has decided not to execute it prior to performing any action.";
            case READY: return "Task is ready to be performed, but no action has yet been taken.  Used in place of requested/received/accepted/rejected when request assignment and acceptance is a given.";
            case CANCELLED: return "The task was not completed.";
            case INPROGRESS: return "Task has been started but is not yet complete.";
            case ONHOLD: return "Task has been started but work has been paused.";
            case FAILED: return "The task was attempted but could not be completed due to some error.";
            case COMPLETED: return "The task has been completed.";
            case ENTEREDINERROR: return "The task should never have existed and is retained only because of the possibility it may have used.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case REQUESTED: return "Requested";
            case RECEIVED: return "Received";
            case ACCEPTED: return "Accepted";
            case REJECTED: return "Rejected";
            case READY: return "Ready";
            case CANCELLED: return "Cancelled";
            case INPROGRESS: return "In Progress";
            case ONHOLD: return "On Hold";
            case FAILED: return "Failed";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
    }


}

