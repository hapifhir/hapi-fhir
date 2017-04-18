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

public enum CarePlanActivityStatus {

        /**
         * Activity is planned but no action has yet been taken.
         */
        NOTSTARTED, 
        /**
         * Appointment or other booking has occurred but activity has not yet begun.
         */
        SCHEDULED, 
        /**
         * Activity has been started but is not yet complete.
         */
        INPROGRESS, 
        /**
         * Activity was started but has temporarily ceased with an expectation of resumption at a future time.
         */
        ONHOLD, 
        /**
         * The activities have been completed (more or less) as planned.
         */
        COMPLETED, 
        /**
         * The activities have been ended prior to completion (perhaps even before they were started).
         */
        CANCELLED, 
        /**
         * The authoring system doesn't know the current state of the activity.
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanActivityStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-started".equals(codeString))
          return NOTSTARTED;
        if ("scheduled".equals(codeString))
          return SCHEDULED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown CarePlanActivityStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTSTARTED: return "not-started";
            case SCHEDULED: return "scheduled";
            case INPROGRESS: return "in-progress";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/care-plan-activity-status";
        }
        public String getDefinition() {
          switch (this) {
            case NOTSTARTED: return "Activity is planned but no action has yet been taken.";
            case SCHEDULED: return "Appointment or other booking has occurred but activity has not yet begun.";
            case INPROGRESS: return "Activity has been started but is not yet complete.";
            case ONHOLD: return "Activity was started but has temporarily ceased with an expectation of resumption at a future time.";
            case COMPLETED: return "The activities have been completed (more or less) as planned.";
            case CANCELLED: return "The activities have been ended prior to completion (perhaps even before they were started).";
            case UNKNOWN: return "The authoring system doesn't know the current state of the activity.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTSTARTED: return "Not Started";
            case SCHEDULED: return "Scheduled";
            case INPROGRESS: return "In Progress";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

