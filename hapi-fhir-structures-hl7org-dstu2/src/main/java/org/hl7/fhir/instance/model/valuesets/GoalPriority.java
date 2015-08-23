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


public enum GoalPriority {

        /**
         * Indicates that the goal is of considerable importance and should be a primary focus of care delivery
         */
        HIGH, 
        /**
         * Indicates that the goal has a reasonable degree of importance and that concrete action should be taken towards the goal.  Attainment is not as critical as high-priority goals.
         */
        MEDIUM, 
        /**
         * The goal is desirable but is not sufficiently important to devote significant resources to.  Achievement of the goal may be sought when incidental to achieving other goals.
         */
        LOW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalPriority fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("high".equals(codeString))
          return HIGH;
        if ("medium".equals(codeString))
          return MEDIUM;
        if ("low".equals(codeString))
          return LOW;
        throw new Exception("Unknown GoalPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HIGH: return "high";
            case MEDIUM: return "medium";
            case LOW: return "low";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/goal-priority";
        }
        public String getDefinition() {
          switch (this) {
            case HIGH: return "Indicates that the goal is of considerable importance and should be a primary focus of care delivery";
            case MEDIUM: return "Indicates that the goal has a reasonable degree of importance and that concrete action should be taken towards the goal.  Attainment is not as critical as high-priority goals.";
            case LOW: return "The goal is desirable but is not sufficiently important to devote significant resources to.  Achievement of the goal may be sought when incidental to achieving other goals.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HIGH: return "high";
            case MEDIUM: return "medium";
            case LOW: return "low";
            default: return "?";
          }
    }


}

