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

public enum ResearchStudyReasonStopped {

        /**
         * The study prematurely ended because the accrual goal was met.
         */
        ACCRUALGOALMET, 
        /**
         * The study prematurely ended due to toxicity.
         */
        CLOSEDDUETOTOXICITY, 
        /**
         * The study prematurely ended due to lack of study progress.
         */
        CLOSEDDUETOLACKOFSTUDYPROGRESS, 
        /**
         * The study prematurely ended temporarily per study design.
         */
        TEMPORARILYCLOSEDPERSTUDYDESIGN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResearchStudyReasonStopped fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("accrual-goal-met".equals(codeString))
          return ACCRUALGOALMET;
        if ("closed-due-to-toxicity".equals(codeString))
          return CLOSEDDUETOTOXICITY;
        if ("closed-due-to-lack-of-study-progress".equals(codeString))
          return CLOSEDDUETOLACKOFSTUDYPROGRESS;
        if ("temporarily-closed-per-study-design".equals(codeString))
          return TEMPORARILYCLOSEDPERSTUDYDESIGN;
        throw new FHIRException("Unknown ResearchStudyReasonStopped code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACCRUALGOALMET: return "accrual-goal-met";
            case CLOSEDDUETOTOXICITY: return "closed-due-to-toxicity";
            case CLOSEDDUETOLACKOFSTUDYPROGRESS: return "closed-due-to-lack-of-study-progress";
            case TEMPORARILYCLOSEDPERSTUDYDESIGN: return "temporarily-closed-per-study-design";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/research-study-reason-stopped";
        }
        public String getDefinition() {
          switch (this) {
            case ACCRUALGOALMET: return "The study prematurely ended because the accrual goal was met.";
            case CLOSEDDUETOTOXICITY: return "The study prematurely ended due to toxicity.";
            case CLOSEDDUETOLACKOFSTUDYPROGRESS: return "The study prematurely ended due to lack of study progress.";
            case TEMPORARILYCLOSEDPERSTUDYDESIGN: return "The study prematurely ended temporarily per study design.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCRUALGOALMET: return "Accrual Goal Met";
            case CLOSEDDUETOTOXICITY: return "Closed due to toxicity";
            case CLOSEDDUETOLACKOFSTUDYPROGRESS: return "Closed due to lack of study progress";
            case TEMPORARILYCLOSEDPERSTUDYDESIGN: return "Temporarily closed per study design";
            default: return "?";
          }
    }


}

