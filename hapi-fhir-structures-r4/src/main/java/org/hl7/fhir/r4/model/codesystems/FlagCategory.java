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

public enum FlagCategory {

        /**
         * Flags related to the subject's dietary needs.
         */
        DIET, 
        /**
         * Flags related to the subject's medications.
         */
        DRUG, 
        /**
         * Flags related to performing laboratory tests and related processes (e.g. phlebotomy).
         */
        LAB, 
        /**
         * Flags related to administrative and financial processes.
         */
        ADMIN, 
        /**
         * Flags related to coming into contact with the patient.
         */
        CONTACT, 
        /**
         * Flags related to the subject's clinical data.
         */
        CLINICAL, 
        /**
         * Flags related to behavior.
         */
        BEHAVIORAL, 
        /**
         * Flags related to research.
         */
        RESEARCH, 
        /**
         * Flags related to subject's advance directives.
         */
        ADVANCEDIRECTIVE, 
        /**
         * Flags related to safety precautions.
         */
        SAFETY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FlagCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return DIET;
        if ("drug".equals(codeString))
          return DRUG;
        if ("lab".equals(codeString))
          return LAB;
        if ("admin".equals(codeString))
          return ADMIN;
        if ("contact".equals(codeString))
          return CONTACT;
        if ("clinical".equals(codeString))
          return CLINICAL;
        if ("behavioral".equals(codeString))
          return BEHAVIORAL;
        if ("research".equals(codeString))
          return RESEARCH;
        if ("advance-directive".equals(codeString))
          return ADVANCEDIRECTIVE;
        if ("safety".equals(codeString))
          return SAFETY;
        throw new FHIRException("Unknown FlagCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIET: return "diet";
            case DRUG: return "drug";
            case LAB: return "lab";
            case ADMIN: return "admin";
            case CONTACT: return "contact";
            case CLINICAL: return "clinical";
            case BEHAVIORAL: return "behavioral";
            case RESEARCH: return "research";
            case ADVANCEDIRECTIVE: return "advance-directive";
            case SAFETY: return "safety";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/flag-category";
        }
        public String getDefinition() {
          switch (this) {
            case DIET: return "Flags related to the subject's dietary needs.";
            case DRUG: return "Flags related to the subject's medications.";
            case LAB: return "Flags related to performing laboratory tests and related processes (e.g. phlebotomy).";
            case ADMIN: return "Flags related to administrative and financial processes.";
            case CONTACT: return "Flags related to coming into contact with the patient.";
            case CLINICAL: return "Flags related to the subject's clinical data.";
            case BEHAVIORAL: return "Flags related to behavior.";
            case RESEARCH: return "Flags related to research.";
            case ADVANCEDIRECTIVE: return "Flags related to subject's advance directives.";
            case SAFETY: return "Flags related to safety precautions.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIET: return "Diet";
            case DRUG: return "Drug";
            case LAB: return "Lab";
            case ADMIN: return "Administrative";
            case CONTACT: return "Subject contact";
            case CLINICAL: return "Clinical";
            case BEHAVIORAL: return "Behavioral";
            case RESEARCH: return "Research";
            case ADVANCEDIRECTIVE: return "Advance Directive";
            case SAFETY: return "Safety";
            default: return "?";
          }
    }


}

