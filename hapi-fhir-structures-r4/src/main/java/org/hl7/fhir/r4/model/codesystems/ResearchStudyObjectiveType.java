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

public enum ResearchStudyObjectiveType {

        /**
         * The main question to be answered, and the one that drives any statistical planning for the study—e.g., calculation of the sample size to provide the appropriate power for statistical testing.
         */
        PRIMARY, 
        /**
         * Question to be answered in the study that is of lesser importance than the primary objective.
         */
        SECONDARY, 
        /**
         * Exploratory questions to be answered in the study.
         */
        EXPLORATORY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResearchStudyObjectiveType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("primary".equals(codeString))
          return PRIMARY;
        if ("secondary".equals(codeString))
          return SECONDARY;
        if ("exploratory".equals(codeString))
          return EXPLORATORY;
        throw new FHIRException("Unknown ResearchStudyObjectiveType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRIMARY: return "primary";
            case SECONDARY: return "secondary";
            case EXPLORATORY: return "exploratory";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/research-study-objective-type";
        }
        public String getDefinition() {
          switch (this) {
            case PRIMARY: return "The main question to be answered, and the one that drives any statistical planning for the study—e.g., calculation of the sample size to provide the appropriate power for statistical testing.";
            case SECONDARY: return "Question to be answered in the study that is of lesser importance than the primary objective.";
            case EXPLORATORY: return "Exploratory questions to be answered in the study.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRIMARY: return "Primary";
            case SECONDARY: return "Secondary";
            case EXPLORATORY: return "Exploratory";
            default: return "?";
          }
    }


}

