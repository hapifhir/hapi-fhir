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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ActivityDefinitionCategory {

        /**
         * To communicate with a participant in some way
         */
        COMMUNICATION, 
        /**
         * To use a specific device
         */
        DEVICE, 
        /**
         * To perform a particular diagnostic
         */
        DIAGNOSTIC, 
        /**
         * To consume food of a specified nature
         */
        DIET, 
        /**
         * To consume/receive a drug or other product
         */
        DRUG, 
        /**
         * To meet with the patient (in-patient, out-patient, etc.)
         */
        ENCOUNTER, 
        /**
         * To administer a particular immunization
         */
        IMMUNIZATION, 
        /**
         * To capture information about a patient (vitals, labs, etc.)
         */
        OBSERVATION, 
        /**
         * To modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)
         */
        PROCEDURE, 
        /**
         * To refer the patient to receive some service
         */
        REFERRAL, 
        /**
         * To provide something to the patient (medication, medical supply, etc.)
         */
        SUPPLY, 
        /**
         * To receive a particular vision correction device
         */
        VISION, 
        /**
         * Some other form of action
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ActivityDefinitionCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("communication".equals(codeString))
          return COMMUNICATION;
        if ("device".equals(codeString))
          return DEVICE;
        if ("diagnostic".equals(codeString))
          return DIAGNOSTIC;
        if ("diet".equals(codeString))
          return DIET;
        if ("drug".equals(codeString))
          return DRUG;
        if ("encounter".equals(codeString))
          return ENCOUNTER;
        if ("immunization".equals(codeString))
          return IMMUNIZATION;
        if ("observation".equals(codeString))
          return OBSERVATION;
        if ("procedure".equals(codeString))
          return PROCEDURE;
        if ("referral".equals(codeString))
          return REFERRAL;
        if ("supply".equals(codeString))
          return SUPPLY;
        if ("vision".equals(codeString))
          return VISION;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown ActivityDefinitionCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMMUNICATION: return "communication";
            case DEVICE: return "device";
            case DIAGNOSTIC: return "diagnostic";
            case DIET: return "diet";
            case DRUG: return "drug";
            case ENCOUNTER: return "encounter";
            case IMMUNIZATION: return "immunization";
            case OBSERVATION: return "observation";
            case PROCEDURE: return "procedure";
            case REFERRAL: return "referral";
            case SUPPLY: return "supply";
            case VISION: return "vision";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/activity-definition-category";
        }
        public String getDefinition() {
          switch (this) {
            case COMMUNICATION: return "To communicate with a participant in some way";
            case DEVICE: return "To use a specific device";
            case DIAGNOSTIC: return "To perform a particular diagnostic";
            case DIET: return "To consume food of a specified nature";
            case DRUG: return "To consume/receive a drug or other product";
            case ENCOUNTER: return "To meet with the patient (in-patient, out-patient, etc.)";
            case IMMUNIZATION: return "To administer a particular immunization";
            case OBSERVATION: return "To capture information about a patient (vitals, labs, etc.)";
            case PROCEDURE: return "To modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)";
            case REFERRAL: return "To refer the patient to receive some service";
            case SUPPLY: return "To provide something to the patient (medication, medical supply, etc.)";
            case VISION: return "To receive a particular vision correction device";
            case OTHER: return "Some other form of action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMMUNICATION: return "Communication";
            case DEVICE: return "Device";
            case DIAGNOSTIC: return "Diagnostic";
            case DIET: return "Diet";
            case DRUG: return "Drug";
            case ENCOUNTER: return "Encounter";
            case IMMUNIZATION: return "Immunization";
            case OBSERVATION: return "Observation";
            case PROCEDURE: return "Procedure";
            case REFERRAL: return "Referral";
            case SUPPLY: return "Supply";
            case VISION: return "Vision";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

