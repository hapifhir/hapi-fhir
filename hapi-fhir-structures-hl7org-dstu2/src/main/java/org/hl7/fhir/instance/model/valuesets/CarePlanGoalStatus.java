package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum CarePlanGoalStatus {

        /**
         * The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)
         */
        INPROGRESS, 
        /**
         * The goal has been met and no further action is needed
         */
        ACHIEVED, 
        /**
         * The goal has been met, but ongoing activity is needed to sustain the goal objective
         */
        SUSTAINING, 
        /**
         * The goal is no longer being sought
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanGoalStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("achieved".equals(codeString))
          return ACHIEVED;
        if ("sustaining".equals(codeString))
          return SUSTAINING;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new Exception("Unknown CarePlanGoalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case ACHIEVED: return "achieved";
            case SUSTAINING: return "sustaining";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/care-plan-goal-status";
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)";
            case ACHIEVED: return "The goal has been met and no further action is needed";
            case SUSTAINING: return "The goal has been met, but ongoing activity is needed to sustain the goal objective";
            case CANCELLED: return "The goal is no longer being sought";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case ACHIEVED: return "Achieved";
            case SUSTAINING: return "Sustaining";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
    }


}

