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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum ConditionCategory {

        /**
         * The patient considers the condition an issue to be addressed.
         */
        COMPLAINT, 
        /**
         * A symptom of a condition (as might be mentioned in a review of systems).
         */
        SYMPTOM, 
        /**
         * An observation made by a healthcare provider.
         */
        FINDING, 
        /**
         * This is a judgment made by a healthcare provider that the patient has a particular disease or condition.
         */
        DIAGNOSIS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionCategory fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complaint".equals(codeString))
          return COMPLAINT;
        if ("symptom".equals(codeString))
          return SYMPTOM;
        if ("finding".equals(codeString))
          return FINDING;
        if ("diagnosis".equals(codeString))
          return DIAGNOSIS;
        throw new Exception("Unknown ConditionCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLAINT: return "complaint";
            case SYMPTOM: return "symptom";
            case FINDING: return "finding";
            case DIAGNOSIS: return "diagnosis";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/condition-category";
        }
        public String getDefinition() {
          switch (this) {
            case COMPLAINT: return "The patient considers the condition an issue to be addressed.";
            case SYMPTOM: return "A symptom of a condition (as might be mentioned in a review of systems).";
            case FINDING: return "An observation made by a healthcare provider.";
            case DIAGNOSIS: return "This is a judgment made by a healthcare provider that the patient has a particular disease or condition.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLAINT: return "Complaint";
            case SYMPTOM: return "Symptom";
            case FINDING: return "Finding";
            case DIAGNOSIS: return "Diagnosis";
            default: return "?";
          }
    }


}

