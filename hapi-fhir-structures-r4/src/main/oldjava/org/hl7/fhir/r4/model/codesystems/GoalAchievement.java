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

public enum GoalAchievement {

        /**
         * The goal is being sought but has not yet been reached. (Also applies if the goal was reached in the past but there has been regression and the goal is again being sought).
         */
        INPROGRESS, 
        /**
         * The goal is being sought, and is progressing.
         */
        IMPROVING, 
        /**
         * The goal is being sought, but is regressing.
         */
        WORSENING, 
        /**
         * The goal is being sought, but the trend is flat.
         */
        NOCHANGE, 
        /**
         * The goal has been met.
         */
        ACHIEVED, 
        /**
         * The goal has been met, but ongoing activity is needed to sustain the goal objective.
         */
        SUSTAINING, 
        /**
         * The goal has not been met and there might or might not have been progress towards target.
         */
        NOTACHIEVED, 
        /**
         * The goal has not been met and little to no progress towards target.
         */
        NOPROGRESS, 
        /**
         * The goal is not possible to be met.
         */
        NOTATTAINABLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalAchievement fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("improving".equals(codeString))
          return IMPROVING;
        if ("worsening".equals(codeString))
          return WORSENING;
        if ("no-change".equals(codeString))
          return NOCHANGE;
        if ("achieved".equals(codeString))
          return ACHIEVED;
        if ("sustaining".equals(codeString))
          return SUSTAINING;
        if ("not-achieved".equals(codeString))
          return NOTACHIEVED;
        if ("no-progress".equals(codeString))
          return NOPROGRESS;
        if ("not-attainable".equals(codeString))
          return NOTATTAINABLE;
        throw new FHIRException("Unknown GoalAchievement code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case IMPROVING: return "improving";
            case WORSENING: return "worsening";
            case NOCHANGE: return "no-change";
            case ACHIEVED: return "achieved";
            case SUSTAINING: return "sustaining";
            case NOTACHIEVED: return "not-achieved";
            case NOPROGRESS: return "no-progress";
            case NOTATTAINABLE: return "not-attainable";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/goal-achievement";
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The goal is being sought but has not yet been reached. (Also applies if the goal was reached in the past but there has been regression and the goal is again being sought).";
            case IMPROVING: return "The goal is being sought, and is progressing.";
            case WORSENING: return "The goal is being sought, but is regressing.";
            case NOCHANGE: return "The goal is being sought, but the trend is flat.";
            case ACHIEVED: return "The goal has been met.";
            case SUSTAINING: return "The goal has been met, but ongoing activity is needed to sustain the goal objective.";
            case NOTACHIEVED: return "The goal has not been met and there might or might not have been progress towards target.";
            case NOPROGRESS: return "The goal has not been met and little to no progress towards target.";
            case NOTATTAINABLE: return "The goal is not possible to be met.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case IMPROVING: return "Improving";
            case WORSENING: return "Worsening";
            case NOCHANGE: return "No Change";
            case ACHIEVED: return "Achieved";
            case SUSTAINING: return "Sustaining";
            case NOTACHIEVED: return "Not Achieved";
            case NOPROGRESS: return "No Progress";
            case NOTATTAINABLE: return "Not Attainable";
            default: return "?";
          }
    }


}

