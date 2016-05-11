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
        public static ProvenanceAgentType fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown ProvenanceAgentType code '"+codeString+"'");
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

