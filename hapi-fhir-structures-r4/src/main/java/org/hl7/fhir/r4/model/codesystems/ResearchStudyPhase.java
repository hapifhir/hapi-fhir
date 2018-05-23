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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ResearchStudyPhase {

        /**
         * Trials without phases (for example, studies of devices or behavioral interventions).
         */
        NA, 
        /**
         * Designation for optional exploratory trials conducted in accordance with the United States Food and Drug Administration's (FDA) 2006 Guidance on Exploratory Investigational New Drug (IND) Studies. Formerly called Phase 0.
         */
        EARLYPHASE1, 
        /**
         * Includes initial studies to determine the metabolism and pharmacologic actions of drugs in humans, the side effects associated with increasing doses, and to gain early evidence of effectiveness; may include healthy participants and/or patients.
         */
        PHASE1, 
        /**
         * Trials that are a combination of phases 1 and 2.
         */
        PHASE1PHASE2, 
        /**
         * Includes controlled clinical studies conducted to evaluate the effectiveness of the drug for a particular indication or indications in participants with the disease or condition under study and to determine the common short-term side effects and risks.
         */
        PHASE2, 
        /**
         * Trials that are a combination of phases 2 and 3.
         */
        PHASE2PHASE3, 
        /**
         * Includes trials conducted after preliminary evidence suggesting effectiveness of the drug has been obtained, and are intended to gather additional information to evaluate the overall benefit-risk relationship of the drug.
         */
        PHASE3, 
        /**
         * Studies of FDA-approved drugs to delineate additional information including the drug's risks, benefits, and optimal use.
         */
        PHASE4, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResearchStudyPhase fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("n-a".equals(codeString))
          return NA;
        if ("early-phase-1".equals(codeString))
          return EARLYPHASE1;
        if ("phase-1".equals(codeString))
          return PHASE1;
        if ("phase-1-phase-2".equals(codeString))
          return PHASE1PHASE2;
        if ("phase-2".equals(codeString))
          return PHASE2;
        if ("phase-2-phase-3".equals(codeString))
          return PHASE2PHASE3;
        if ("phase-3".equals(codeString))
          return PHASE3;
        if ("phase-4".equals(codeString))
          return PHASE4;
        throw new FHIRException("Unknown ResearchStudyPhase code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NA: return "n-a";
            case EARLYPHASE1: return "early-phase-1";
            case PHASE1: return "phase-1";
            case PHASE1PHASE2: return "phase-1-phase-2";
            case PHASE2: return "phase-2";
            case PHASE2PHASE3: return "phase-2-phase-3";
            case PHASE3: return "phase-3";
            case PHASE4: return "phase-4";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/research-study-phase";
        }
        public String getDefinition() {
          switch (this) {
            case NA: return "Trials without phases (for example, studies of devices or behavioral interventions).";
            case EARLYPHASE1: return "Designation for optional exploratory trials conducted in accordance with the United States Food and Drug Administration's (FDA) 2006 Guidance on Exploratory Investigational New Drug (IND) Studies. Formerly called Phase 0.";
            case PHASE1: return "Includes initial studies to determine the metabolism and pharmacologic actions of drugs in humans, the side effects associated with increasing doses, and to gain early evidence of effectiveness; may include healthy participants and/or patients.";
            case PHASE1PHASE2: return "Trials that are a combination of phases 1 and 2.";
            case PHASE2: return "Includes controlled clinical studies conducted to evaluate the effectiveness of the drug for a particular indication or indications in participants with the disease or condition under study and to determine the common short-term side effects and risks.";
            case PHASE2PHASE3: return "Trials that are a combination of phases 2 and 3.";
            case PHASE3: return "Includes trials conducted after preliminary evidence suggesting effectiveness of the drug has been obtained, and are intended to gather additional information to evaluate the overall benefit-risk relationship of the drug.";
            case PHASE4: return "Studies of FDA-approved drugs to delineate additional information including the drug's risks, benefits, and optimal use.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NA: return "N/A";
            case EARLYPHASE1: return "Early Phase 1";
            case PHASE1: return "Phase 1";
            case PHASE1PHASE2: return "Phase 1/Phase 2";
            case PHASE2: return "Phase 2";
            case PHASE2PHASE3: return "Phase 2/Phase 3";
            case PHASE3: return "Phase 3";
            case PHASE4: return "Phase 4";
            default: return "?";
          }
    }


}

