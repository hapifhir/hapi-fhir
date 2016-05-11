package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ProvenanceAgentType {

        /**
         * The participant is a person acting on their on behalf or on behalf of the patient rather than as an practitioner for an organization.  I.e. "not a healthcare provider".
         */
        PERSON, 
        /**
         * The participant is a practitioner, a person (provider) who is directly or indirectly involved in the provisioning of healthcare.
         */
        PRACTITIONER, 
        /**
         * The participant is an organization.
         */
        ORGANIZATION, 
        /**
         * The participant is a software application including services, algorithms, etc.
         */
        SOFTWARE, 
        /**
         * The participant is the patient, a person or animal receiving care or other health-related services.
         */
        PATIENT, 
        /**
         * The participant is a device, an instance of a manufactured thing that is used in the provision of healthcare without being substantially changed through that activity. The device may be a machine, an insert, a computer, an application, etc. This includes durable (reusable) medical equipment as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.
         */
        DEVICE, 
        /**
         * The participant is a related person, a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.
         */
        RELATEDPERSON, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProvenanceAgentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("person".equals(codeString))
          return PERSON;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("organization".equals(codeString))
          return ORGANIZATION;
        if ("software".equals(codeString))
          return SOFTWARE;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("device".equals(codeString))
          return DEVICE;
        if ("related-person".equals(codeString))
          return RELATEDPERSON;
        throw new FHIRException("Unknown ProvenanceAgentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PERSON: return "person";
            case PRACTITIONER: return "practitioner";
            case ORGANIZATION: return "organization";
            case SOFTWARE: return "software";
            case PATIENT: return "patient";
            case DEVICE: return "device";
            case RELATEDPERSON: return "related-person";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/provenance-participant-type";
        }
        public String getDefinition() {
          switch (this) {
            case PERSON: return "The participant is a person acting on their on behalf or on behalf of the patient rather than as an practitioner for an organization.  I.e. \"not a healthcare provider\".";
            case PRACTITIONER: return "The participant is a practitioner, a person (provider) who is directly or indirectly involved in the provisioning of healthcare.";
            case ORGANIZATION: return "The participant is an organization.";
            case SOFTWARE: return "The participant is a software application including services, algorithms, etc.";
            case PATIENT: return "The participant is the patient, a person or animal receiving care or other health-related services.";
            case DEVICE: return "The participant is a device, an instance of a manufactured thing that is used in the provision of healthcare without being substantially changed through that activity. The device may be a machine, an insert, a computer, an application, etc. This includes durable (reusable) medical equipment as well as disposable equipment used for diagnostic, treatment, and research for healthcare and public health.";
            case RELATEDPERSON: return "The participant is a related person, a person that is involved in the care for a patient, but who is not the target of healthcare, nor has a formal responsibility in the care process.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PERSON: return "Person";
            case PRACTITIONER: return "Practitioner";
            case ORGANIZATION: return "Organization";
            case SOFTWARE: return "Software";
            case PATIENT: return "Patient";
            case DEVICE: return "Device";
            case RELATEDPERSON: return "Related Person";
            default: return "?";
          }
    }


}

