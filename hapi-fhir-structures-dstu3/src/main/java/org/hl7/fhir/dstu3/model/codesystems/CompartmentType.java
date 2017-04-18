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

public enum CompartmentType {

        /**
         * The compartment definition is for the patient compartment
         */
        PATIENT, 
        /**
         * The compartment definition is for the encounter compartment
         */
        ENCOUNTER, 
        /**
         * The compartment definition is for the related-person compartment
         */
        RELATEDPERSON, 
        /**
         * The compartment definition is for the practitioner compartment
         */
        PRACTITIONER, 
        /**
         * The compartment definition is for the device compartment
         */
        DEVICE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CompartmentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Patient".equals(codeString))
          return PATIENT;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("Device".equals(codeString))
          return DEVICE;
        throw new FHIRException("Unknown CompartmentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "Patient";
            case ENCOUNTER: return "Encounter";
            case RELATEDPERSON: return "RelatedPerson";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/compartment-type";
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The compartment definition is for the patient compartment";
            case ENCOUNTER: return "The compartment definition is for the encounter compartment";
            case RELATEDPERSON: return "The compartment definition is for the related-person compartment";
            case PRACTITIONER: return "The compartment definition is for the practitioner compartment";
            case DEVICE: return "The compartment definition is for the device compartment";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case ENCOUNTER: return "Encounter";
            case RELATEDPERSON: return "RelatedPerson";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            default: return "?";
          }
    }


}

