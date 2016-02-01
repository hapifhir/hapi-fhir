package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum PatientContactRelationship {

        /**
         * Contact for use in case of emergency.
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
         * Contact for matters related to the patients occupation/employment.
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
        public static PatientContactRelationship fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown PatientContactRelationship code '"+codeString+"'");
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
            case EMERGENCY: return "Contact for use in case of emergency.";
            case FAMILY: return "";
            case GUARDIAN: return "";
            case FRIEND: return "";
            case PARTNER: return "";
            case WORK: return "Contact for matters related to the patients occupation/employment.";
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

