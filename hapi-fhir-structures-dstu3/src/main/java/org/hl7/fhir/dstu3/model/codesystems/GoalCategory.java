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

public enum GoalCategory {

        /**
         * Goals related to the consumption of food and/or beverages.
         */
        DIETARY, 
        /**
         * Goals related to the personal protection of the subject.
         */
        SAFETY, 
        /**
         * Goals related to the manner in which the subject acts.
         */
        BEHAVIORAL, 
        /**
         * Goals related to the practice of nursing or established by nurses.
         */
        NURSING, 
        /**
         * Goals related to the mobility and motor capability of the subject.
         */
        PHYSIOTHERAPY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("dietary".equals(codeString))
          return DIETARY;
        if ("safety".equals(codeString))
          return SAFETY;
        if ("behavioral".equals(codeString))
          return BEHAVIORAL;
        if ("nursing".equals(codeString))
          return NURSING;
        if ("physiotherapy".equals(codeString))
          return PHYSIOTHERAPY;
        throw new FHIRException("Unknown GoalCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIETARY: return "dietary";
            case SAFETY: return "safety";
            case BEHAVIORAL: return "behavioral";
            case NURSING: return "nursing";
            case PHYSIOTHERAPY: return "physiotherapy";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/goal-category";
        }
        public String getDefinition() {
          switch (this) {
            case DIETARY: return "Goals related to the consumption of food and/or beverages.";
            case SAFETY: return "Goals related to the personal protection of the subject.";
            case BEHAVIORAL: return "Goals related to the manner in which the subject acts.";
            case NURSING: return "Goals related to the practice of nursing or established by nurses.";
            case PHYSIOTHERAPY: return "Goals related to the mobility and motor capability of the subject.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIETARY: return "Dietary";
            case SAFETY: return "Safety";
            case BEHAVIORAL: return "Behavioral";
            case NURSING: return "Nursing";
            case PHYSIOTHERAPY: return "Physiotherapy";
            default: return "?";
          }
    }


}

