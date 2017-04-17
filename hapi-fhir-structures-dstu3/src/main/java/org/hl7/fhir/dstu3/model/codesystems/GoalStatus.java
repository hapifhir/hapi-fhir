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

public enum GoalStatus {

        /**
         * A goal is proposed for this patient
         */
        PROPOSED, 
        /**
         * A proposed goal was accepted or acknowledged
         */
        ACCEPTED, 
        /**
         * A goal is planned for this patient
         */
        PLANNED, 
        /**
         * The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)
         */
        INPROGRESS, 
        /**
         * The goal is on schedule for the planned timelines
         */
        ONTARGET, 
        /**
         * The goal is ahead of the planned timelines
         */
        AHEADOFTARGET, 
        /**
         * The goal is behind the planned timelines
         */
        BEHINDTARGET, 
        /**
         * The goal has been met, but ongoing activity is needed to sustain the goal objective
         */
        SUSTAINING, 
        /**
         * The goal has been met and no further action is needed
         */
        ACHIEVED, 
        /**
         * The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.
         */
        ONHOLD, 
        /**
         * The previously accepted goal is no longer being sought
         */
        CANCELLED, 
        /**
         * The goal was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * A proposed goal was rejected
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
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("on-target".equals(codeString))
          return ONTARGET;
        if ("ahead-of-target".equals(codeString))
          return AHEADOFTARGET;
        if ("behind-target".equals(codeString))
          return BEHINDTARGET;
        if ("sustaining".equals(codeString))
          return SUSTAINING;
        if ("achieved".equals(codeString))
          return ACHIEVED;
        if ("on-hold".equals(codeString))
          return ONHOLD;
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
            case ACCEPTED: return "accepted";
            case PLANNED: return "planned";
            case INPROGRESS: return "in-progress";
            case ONTARGET: return "on-target";
            case AHEADOFTARGET: return "ahead-of-target";
            case BEHINDTARGET: return "behind-target";
            case SUSTAINING: return "sustaining";
            case ACHIEVED: return "achieved";
            case ONHOLD: return "on-hold";
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
            case PROPOSED: return "A goal is proposed for this patient";
            case ACCEPTED: return "A proposed goal was accepted or acknowledged";
            case PLANNED: return "A goal is planned for this patient";
            case INPROGRESS: return "The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)";
            case ONTARGET: return "The goal is on schedule for the planned timelines";
            case AHEADOFTARGET: return "The goal is ahead of the planned timelines";
            case BEHINDTARGET: return "The goal is behind the planned timelines";
            case SUSTAINING: return "The goal has been met, but ongoing activity is needed to sustain the goal objective";
            case ACHIEVED: return "The goal has been met and no further action is needed";
            case ONHOLD: return "The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.";
            case CANCELLED: return "The previously accepted goal is no longer being sought";
            case ENTEREDINERROR: return "The goal was entered in error and voided.";
            case REJECTED: return "A proposed goal was rejected";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case ACCEPTED: return "Accepted";
            case PLANNED: return "Planned";
            case INPROGRESS: return "In Progress";
            case ONTARGET: return "On Target";
            case AHEADOFTARGET: return "Ahead of Target";
            case BEHINDTARGET: return "Behind Target";
            case SUSTAINING: return "Sustaining";
            case ACHIEVED: return "Achieved";
            case ONHOLD: return "On Hold";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered In Error";
            case REJECTED: return "Rejected";
            default: return "?";
          }
    }


}

