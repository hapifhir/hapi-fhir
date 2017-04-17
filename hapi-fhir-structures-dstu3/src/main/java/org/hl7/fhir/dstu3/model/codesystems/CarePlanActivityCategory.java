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

public enum CarePlanActivityCategory {

        /**
         * Plan for the patient to consume food of a specified nature
         */
        DIET, 
        /**
         * Plan for the patient to consume/receive a drug, vaccine or other product
         */
        DRUG, 
        /**
         * Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.)
         */
        ENCOUNTER, 
        /**
         * Plan to capture information about a patient (vitals, labs, diagnostic images, etc.)
         */
        OBSERVATION, 
        /**
         * Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)
         */
        PROCEDURE, 
        /**
         * Plan to provide something to the patient (medication, medical supply, etc.)
         */
        SUPPLY, 
        /**
         * Some other form of action
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanActivityCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return DIET;
        if ("drug".equals(codeString))
          return DRUG;
        if ("encounter".equals(codeString))
          return ENCOUNTER;
        if ("observation".equals(codeString))
          return OBSERVATION;
        if ("procedure".equals(codeString))
          return PROCEDURE;
        if ("supply".equals(codeString))
          return SUPPLY;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown CarePlanActivityCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIET: return "diet";
            case DRUG: return "drug";
            case ENCOUNTER: return "encounter";
            case OBSERVATION: return "observation";
            case PROCEDURE: return "procedure";
            case SUPPLY: return "supply";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/care-plan-activity-category";
        }
        public String getDefinition() {
          switch (this) {
            case DIET: return "Plan for the patient to consume food of a specified nature";
            case DRUG: return "Plan for the patient to consume/receive a drug, vaccine or other product";
            case ENCOUNTER: return "Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.)";
            case OBSERVATION: return "Plan to capture information about a patient (vitals, labs, diagnostic images, etc.)";
            case PROCEDURE: return "Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)";
            case SUPPLY: return "Plan to provide something to the patient (medication, medical supply, etc.)";
            case OTHER: return "Some other form of action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIET: return "Diet";
            case DRUG: return "Drug";
            case ENCOUNTER: return "Encounter";
            case OBSERVATION: return "Observation";
            case PROCEDURE: return "Procedure";
            case SUPPLY: return "Supply";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

