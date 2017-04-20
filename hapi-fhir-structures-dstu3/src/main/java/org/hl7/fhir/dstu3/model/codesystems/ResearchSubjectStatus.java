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

public enum ResearchSubjectStatus {

        /**
         * The subject has been identified as a potential participant in the study but has not yet agreed to participate
         */
        CANDIDATE, 
        /**
         * The subject has agreed to participate in the study but has not yet begun performing any action within the study
         */
        ENROLLED, 
        /**
         * The subject is currently being monitored and/or subject to treatment as part of the study
         */
        ACTIVE, 
        /**
         * The subject has temporarily discontinued monitoring/treatment as part of the study
         */
        SUSPENDED, 
        /**
         * The subject has permanently ended participation in the study prior to completion of the intended monitoring/treatment
         */
        WITHDRAWN, 
        /**
         * All intended monitoring/treatment of the subject has been completed and their engagement with the study is now ended
         */
        COMPLETED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResearchSubjectStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("candidate".equals(codeString))
          return CANDIDATE;
        if ("enrolled".equals(codeString))
          return ENROLLED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        if ("completed".equals(codeString))
          return COMPLETED;
        throw new FHIRException("Unknown ResearchSubjectStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CANDIDATE: return "candidate";
            case ENROLLED: return "enrolled";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case WITHDRAWN: return "withdrawn";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/research-subject-status";
        }
        public String getDefinition() {
          switch (this) {
            case CANDIDATE: return "The subject has been identified as a potential participant in the study but has not yet agreed to participate";
            case ENROLLED: return "The subject has agreed to participate in the study but has not yet begun performing any action within the study";
            case ACTIVE: return "The subject is currently being monitored and/or subject to treatment as part of the study";
            case SUSPENDED: return "The subject has temporarily discontinued monitoring/treatment as part of the study";
            case WITHDRAWN: return "The subject has permanently ended participation in the study prior to completion of the intended monitoring/treatment";
            case COMPLETED: return "All intended monitoring/treatment of the subject has been completed and their engagement with the study is now ended";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CANDIDATE: return "Candidate";
            case ENROLLED: return "Enrolled";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case WITHDRAWN: return "Withdrawn";
            case COMPLETED: return "Completed";
            default: return "?";
          }
    }


}

