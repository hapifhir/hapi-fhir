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

public enum CareTeamCategory {

        /**
         * This type of team focuses on one specific type of incident, which is non-patient specific. The incident is determined by the context of use.  For example, code team (code red, code blue, medical emergency treatment) or the PICC line team.
         */
        EVENT, 
        /**
         * This type of team focuses on one specific encounter. The encounter is determined by the context of use.  For example, during an inpatient encounter, the nutrition support care team
         */
        ENCOUNTER, 
        /**
         * This type of team focuses on one specific episode of care with a defined time period or self-limiting process (e.g. 10 visits). The episode of care is determined by the context of use.  For example, a maternity care team over 9 months.
         */
        EPISODE, 
        /**
         * This type of team focuses on overall care coordination managing one or more conditions across the continuum of care ensuring there are smooth transitions of care. The members of the team are determined or selected by an individual or organization. When determined by an organization, the team may be assigned or based on the person's enrollment in a particular program. For example, disease management team or patient centered medical home team.
         */
        LONGITUDINAL, 
        /**
         * This type of team focuses on one specific condition. The condition is determined by the context of use.  For example, a disease management team focused on one condition (e.g. diabetes).
         */
        CONDITION, 
        /**
         * This type of team is responsible for establishing, conducting, coordinating and monitoring the outcomes of clinical trials. The team focuses on research, clinical care and education.
         */
        CLINICALRESEARCH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CareTeamCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("event".equals(codeString))
          return EVENT;
        if ("encounter".equals(codeString))
          return ENCOUNTER;
        if ("episode".equals(codeString))
          return EPISODE;
        if ("longitudinal".equals(codeString))
          return LONGITUDINAL;
        if ("condition".equals(codeString))
          return CONDITION;
        if ("clinical-research".equals(codeString))
          return CLINICALRESEARCH;
        throw new FHIRException("Unknown CareTeamCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EVENT: return "event";
            case ENCOUNTER: return "encounter";
            case EPISODE: return "episode";
            case LONGITUDINAL: return "longitudinal";
            case CONDITION: return "condition";
            case CLINICALRESEARCH: return "clinical-research";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/care-team-category";
        }
        public String getDefinition() {
          switch (this) {
            case EVENT: return "This type of team focuses on one specific type of incident, which is non-patient specific. The incident is determined by the context of use.  For example, code team (code red, code blue, medical emergency treatment) or the PICC line team.";
            case ENCOUNTER: return "This type of team focuses on one specific encounter. The encounter is determined by the context of use.  For example, during an inpatient encounter, the nutrition support care team";
            case EPISODE: return "This type of team focuses on one specific episode of care with a defined time period or self-limiting process (e.g. 10 visits). The episode of care is determined by the context of use.  For example, a maternity care team over 9 months.";
            case LONGITUDINAL: return "This type of team focuses on overall care coordination managing one or more conditions across the continuum of care ensuring there are smooth transitions of care. The members of the team are determined or selected by an individual or organization. When determined by an organization, the team may be assigned or based on the person's enrollment in a particular program. For example, disease management team or patient centered medical home team.";
            case CONDITION: return "This type of team focuses on one specific condition. The condition is determined by the context of use.  For example, a disease management team focused on one condition (e.g. diabetes).";
            case CLINICALRESEARCH: return "This type of team is responsible for establishing, conducting, coordinating and monitoring the outcomes of clinical trials. The team focuses on research, clinical care and education.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EVENT: return "Event";
            case ENCOUNTER: return "Encounter";
            case EPISODE: return "Episode";
            case LONGITUDINAL: return "Longitudinal Care Coordination";
            case CONDITION: return "Condition";
            case CLINICALRESEARCH: return "Clinical Research";
            default: return "?";
          }
    }


}

