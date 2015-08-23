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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum PatientContactRelationship {

        /**
         * Contact for use in case of emergency
         */
        EMERGENCY, 
        /**
         * null
         */
        FAMILY, 
        /**
         * null
         */
        GUARDIAN, 
        /**
         * null
         */
        FRIEND, 
        /**
         * null
         */
        PARTNER, 
        /**
         * Contact for matters related to the patients occupation/employment
         */
        WORK, 
        /**
         * (Non)professional caregiver
         */
        CAREGIVER, 
        /**
         * Contact that acts on behalf of the patient
         */
        AGENT, 
        /**
         * Contact for financial matters
         */
        GUARANTOR, 
        /**
         * For animals, the owner of the animal
         */
        OWNER, 
        /**
         * Parent of the patient
         */
        PARENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PatientContactRelationship fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("emergency".equals(codeString))
          return EMERGENCY;
        if ("family".equals(codeString))
          return FAMILY;
        if ("guardian".equals(codeString))
          return GUARDIAN;
        if ("friend".equals(codeString))
          return FRIEND;
        if ("partner".equals(codeString))
          return PARTNER;
        if ("work".equals(codeString))
          return WORK;
        if ("caregiver".equals(codeString))
          return CAREGIVER;
        if ("agent".equals(codeString))
          return AGENT;
        if ("guarantor".equals(codeString))
          return GUARANTOR;
        if ("owner".equals(codeString))
          return OWNER;
        if ("parent".equals(codeString))
          return PARENT;
        throw new Exception("Unknown PatientContactRelationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EMERGENCY: return "emergency";
            case FAMILY: return "family";
            case GUARDIAN: return "guardian";
            case FRIEND: return "friend";
            case PARTNER: return "partner";
            case WORK: return "work";
            case CAREGIVER: return "caregiver";
            case AGENT: return "agent";
            case GUARANTOR: return "guarantor";
            case OWNER: return "owner";
            case PARENT: return "parent";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/patient-contact-relationship";
        }
        public String getDefinition() {
          switch (this) {
            case EMERGENCY: return "Contact for use in case of emergency";
            case FAMILY: return "";
            case GUARDIAN: return "";
            case FRIEND: return "";
            case PARTNER: return "";
            case WORK: return "Contact for matters related to the patients occupation/employment";
            case CAREGIVER: return "(Non)professional caregiver";
            case AGENT: return "Contact that acts on behalf of the patient";
            case GUARANTOR: return "Contact for financial matters";
            case OWNER: return "For animals, the owner of the animal";
            case PARENT: return "Parent of the patient";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EMERGENCY: return "Emergency";
            case FAMILY: return "Family";
            case GUARDIAN: return "Guardian";
            case FRIEND: return "Friend";
            case PARTNER: return "Partner";
            case WORK: return "Work";
            case CAREGIVER: return "Caregiver";
            case AGENT: return "Agent";
            case GUARANTOR: return "Guarantor";
            case OWNER: return "Owner of animal";
            case PARENT: return "Parent";
            default: return "?";
          }
    }


}

