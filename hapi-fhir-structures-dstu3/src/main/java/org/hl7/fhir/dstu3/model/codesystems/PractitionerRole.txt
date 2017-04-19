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

public enum PractitionerRole {

        /**
         * A qualified/registered medical practitioner
         */
        DOCTOR, 
        /**
         * A practitoner with nursing experience that may be qualified/registered
         */
        NURSE, 
        /**
         * A qualified/registered/licensed pharmacist
         */
        PHARMACIST, 
        /**
         * A practitioner that may perform research
         */
        RESEARCHER, 
        /**
         * Someone who is able to provide educational services
         */
        TEACHER, 
        /**
         * Someone who is qualified in Information and Communication Technologies
         */
        ICT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PractitionerRole fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("doctor".equals(codeString))
          return DOCTOR;
        if ("nurse".equals(codeString))
          return NURSE;
        if ("pharmacist".equals(codeString))
          return PHARMACIST;
        if ("researcher".equals(codeString))
          return RESEARCHER;
        if ("teacher".equals(codeString))
          return TEACHER;
        if ("ict".equals(codeString))
          return ICT;
        throw new FHIRException("Unknown PractitionerRole code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DOCTOR: return "doctor";
            case NURSE: return "nurse";
            case PHARMACIST: return "pharmacist";
            case RESEARCHER: return "researcher";
            case TEACHER: return "teacher";
            case ICT: return "ict";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/practitioner-role";
        }
        public String getDefinition() {
          switch (this) {
            case DOCTOR: return "A qualified/registered medical practitioner";
            case NURSE: return "A practitoner with nursing experience that may be qualified/registered";
            case PHARMACIST: return "A qualified/registered/licensed pharmacist";
            case RESEARCHER: return "A practitioner that may perform research";
            case TEACHER: return "Someone who is able to provide educational services";
            case ICT: return "Someone who is qualified in Information and Communication Technologies";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DOCTOR: return "Doctor";
            case NURSE: return "Nurse";
            case PHARMACIST: return "Pharmacist";
            case RESEARCHER: return "Researcher";
            case TEACHER: return "Teacher/educator";
            case ICT: return "ICT professional";
            default: return "?";
          }
    }


}

