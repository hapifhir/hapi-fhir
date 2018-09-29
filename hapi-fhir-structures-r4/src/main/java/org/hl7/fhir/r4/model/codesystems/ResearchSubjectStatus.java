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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ResearchSubjectStatus {

        /**
         * An identified person that can be considered for inclusion in a study.
         */
        CANDIDATE, 
        /**
         * A person that has met the eligibility criteria for inclusion in a study.
         */
        ELIGIBLE, 
        /**
         * A person is no longer receiving study intervention and/or being evaluated with tests and procedures according to the protocol, but they are being monitored on a protocol-prescribed schedule.
         */
        FOLLOWUP, 
        /**
         * A person who did not meet one or more criteria required for participation in a study is considered to have failed screening or
is ineligible for the study.
         */
        INELIGIBLE, 
        /**
         * A person for whom registration was not completed.
         */
        NOTREGISTERED, 
        /**
         * A person that has ended their participation on a study either because their treatment/observation is complete or through not
responding, withdrawal, non-compliance and/or adverse event.
         */
        OFFSTUDY, 
        /**
         * A person that is enrolled or registered on a study.
         */
        ONSTUDY, 
        /**
         * The person is receiving the treatment or participating in an activity (e.g. yoga, diet, etc.) that the study is evaluating.
         */
        ONSTUDYINTERVENTION, 
        /**
         * The subject is being evaluated via tests and assessments according to the study calendar, but is not receiving any intervention. Note that this state is study-dependent and might not exist in all studies.  A synonym for this is "short-term follow-up".
         */
        ONSTUDYOBSERVATION, 
        /**
         * A person is pre-registered for a study.
         */
        PENDINGONSTUDY, 
        /**
         * A person that is potentially eligible for participation in the study.
         */
        POTENTIALCANDIDATE, 
        /**
         * A person who is being evaluated for eligibility for a study.
         */
        SCREENING, 
        /**
         * The person has withdrawn their participation in the study before registration.
         */
        WITHDRAWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResearchSubjectStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("candidate".equals(codeString))
          return CANDIDATE;
        if ("eligible".equals(codeString))
          return ELIGIBLE;
        if ("follow-up".equals(codeString))
          return FOLLOWUP;
        if ("ineligible".equals(codeString))
          return INELIGIBLE;
        if ("not-registered".equals(codeString))
          return NOTREGISTERED;
        if ("off-study".equals(codeString))
          return OFFSTUDY;
        if ("on-study".equals(codeString))
          return ONSTUDY;
        if ("on-study-intervention".equals(codeString))
          return ONSTUDYINTERVENTION;
        if ("on-study-observation".equals(codeString))
          return ONSTUDYOBSERVATION;
        if ("pending-on-study".equals(codeString))
          return PENDINGONSTUDY;
        if ("potential-candidate".equals(codeString))
          return POTENTIALCANDIDATE;
        if ("screening".equals(codeString))
          return SCREENING;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        throw new FHIRException("Unknown ResearchSubjectStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CANDIDATE: return "candidate";
            case ELIGIBLE: return "eligible";
            case FOLLOWUP: return "follow-up";
            case INELIGIBLE: return "ineligible";
            case NOTREGISTERED: return "not-registered";
            case OFFSTUDY: return "off-study";
            case ONSTUDY: return "on-study";
            case ONSTUDYINTERVENTION: return "on-study-intervention";
            case ONSTUDYOBSERVATION: return "on-study-observation";
            case PENDINGONSTUDY: return "pending-on-study";
            case POTENTIALCANDIDATE: return "potential-candidate";
            case SCREENING: return "screening";
            case WITHDRAWN: return "withdrawn";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/research-subject-status";
        }
        public String getDefinition() {
          switch (this) {
            case CANDIDATE: return "An identified person that can be considered for inclusion in a study.";
            case ELIGIBLE: return "A person that has met the eligibility criteria for inclusion in a study.";
            case FOLLOWUP: return "A person is no longer receiving study intervention and/or being evaluated with tests and procedures according to the protocol, but they are being monitored on a protocol-prescribed schedule.";
            case INELIGIBLE: return "A person who did not meet one or more criteria required for participation in a study is considered to have failed screening or\nis ineligible for the study.";
            case NOTREGISTERED: return "A person for whom registration was not completed.";
            case OFFSTUDY: return "A person that has ended their participation on a study either because their treatment/observation is complete or through not\nresponding, withdrawal, non-compliance and/or adverse event.";
            case ONSTUDY: return "A person that is enrolled or registered on a study.";
            case ONSTUDYINTERVENTION: return "The person is receiving the treatment or participating in an activity (e.g. yoga, diet, etc.) that the study is evaluating.";
            case ONSTUDYOBSERVATION: return "The subject is being evaluated via tests and assessments according to the study calendar, but is not receiving any intervention. Note that this state is study-dependent and might not exist in all studies.  A synonym for this is \"short-term follow-up\".";
            case PENDINGONSTUDY: return "A person is pre-registered for a study.";
            case POTENTIALCANDIDATE: return "A person that is potentially eligible for participation in the study.";
            case SCREENING: return "A person who is being evaluated for eligibility for a study.";
            case WITHDRAWN: return "The person has withdrawn their participation in the study before registration.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CANDIDATE: return "Candidate";
            case ELIGIBLE: return "Eligible";
            case FOLLOWUP: return "Follow-up";
            case INELIGIBLE: return "Ineligible";
            case NOTREGISTERED: return "Not Registered";
            case OFFSTUDY: return "Off-study";
            case ONSTUDY: return "On-study";
            case ONSTUDYINTERVENTION: return "On-study-intervention";
            case ONSTUDYOBSERVATION: return "On-study-observation";
            case PENDINGONSTUDY: return "Pending on-study";
            case POTENTIALCANDIDATE: return "Potential Candidate";
            case SCREENING: return "Screening";
            case WITHDRAWN: return "Withdrawn";
            default: return "?";
          }
    }


}

