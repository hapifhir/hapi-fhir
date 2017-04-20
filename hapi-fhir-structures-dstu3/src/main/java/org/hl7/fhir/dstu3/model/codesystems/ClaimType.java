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

public enum ClaimType {

        /**
         * Hospital, clinic and typically inpatient claims.
         */
        INSTITUTIONAL, 
        /**
         * Dental, Denture and Hygiene claims.
         */
        ORAL, 
        /**
         * Pharmacy claims for goods and services.
         */
        PHARMACY, 
        /**
         * Typically outpatient claims from Physician, Psychological, Chiropractor, Physiotherapy, Speach Pathology, rehabilitative, consulting.
         */
        PROFESSIONAL, 
        /**
         * Vision claims for professional services and products such as glasses and contact lenses.
         */
        VISION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ClaimType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("institutional".equals(codeString))
          return INSTITUTIONAL;
        if ("oral".equals(codeString))
          return ORAL;
        if ("pharmacy".equals(codeString))
          return PHARMACY;
        if ("professional".equals(codeString))
          return PROFESSIONAL;
        if ("vision".equals(codeString))
          return VISION;
        throw new FHIRException("Unknown ClaimType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTITUTIONAL: return "institutional";
            case ORAL: return "oral";
            case PHARMACY: return "pharmacy";
            case PROFESSIONAL: return "professional";
            case VISION: return "vision";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-claimtype";
        }
        public String getDefinition() {
          switch (this) {
            case INSTITUTIONAL: return "Hospital, clinic and typically inpatient claims.";
            case ORAL: return "Dental, Denture and Hygiene claims.";
            case PHARMACY: return "Pharmacy claims for goods and services.";
            case PROFESSIONAL: return "Typically outpatient claims from Physician, Psychological, Chiropractor, Physiotherapy, Speach Pathology, rehabilitative, consulting.";
            case VISION: return "Vision claims for professional services and products such as glasses and contact lenses.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTITUTIONAL: return "Institutional";
            case ORAL: return "Oral";
            case PHARMACY: return "Pharmacy";
            case PROFESSIONAL: return "Professional";
            case VISION: return "Vision";
            default: return "?";
          }
    }


}

