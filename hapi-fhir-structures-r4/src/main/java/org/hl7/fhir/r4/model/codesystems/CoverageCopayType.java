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

public enum CoverageCopayType {

        /**
         * An office visit for a general practitioner of a discipline.
         */
        GPVISIT, 
        /**
         * An office visit for a specialist practitioner of a discipline
         */
        SPVISIT, 
        /**
         * An episode in an emergency department.
         */
        EMERGENCY, 
        /**
         * An episode of an Inpatient hospital stay.
         */
        INPTHOSP, 
        /**
         * A visit held where the patient is remote relative to the practitioner, eg. by phone, computer or video conference.
         */
        TELEVISIT, 
        /**
         * A visit to an urgent care facility - typically a community care clinic.
         */
        URGENTCARE, 
        /**
         * A standard amount, percentage or fixed currency amount, applied to all classes or service or product not otherwise specified.
         */
        COPAYPCT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CoverageCopayType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("gpvisit".equals(codeString))
          return GPVISIT;
        if ("spvisit".equals(codeString))
          return SPVISIT;
        if ("emergency".equals(codeString))
          return EMERGENCY;
        if ("inpthosp".equals(codeString))
          return INPTHOSP;
        if ("televisit".equals(codeString))
          return TELEVISIT;
        if ("urgentcare".equals(codeString))
          return URGENTCARE;
        if ("copaypct".equals(codeString))
          return COPAYPCT;
        throw new FHIRException("Unknown CoverageCopayType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GPVISIT: return "gpvisit";
            case SPVISIT: return "spvisit";
            case EMERGENCY: return "emergency";
            case INPTHOSP: return "inpthosp";
            case TELEVISIT: return "televisit";
            case URGENTCARE: return "urgentcare";
            case COPAYPCT: return "copaypct";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/coverage-copay-type";
        }
        public String getDefinition() {
          switch (this) {
            case GPVISIT: return "An office visit for a general practitioner of a discipline.";
            case SPVISIT: return "An office visit for a specialist practitioner of a discipline";
            case EMERGENCY: return "An episode in an emergency department.";
            case INPTHOSP: return "An episode of an Inpatient hospital stay.";
            case TELEVISIT: return "A visit held where the patient is remote relative to the practitioner, eg. by phone, computer or video conference.";
            case URGENTCARE: return "A visit to an urgent care facility - typically a community care clinic.";
            case COPAYPCT: return "A standard amount, percentage or fixed currency amount, applied to all classes or service or product not otherwise specified.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GPVISIT: return "GP Office Visit";
            case SPVISIT: return "Specialist Office Visit";
            case EMERGENCY: return "Emergency";
            case INPTHOSP: return "InPatient Hospital";
            case TELEVISIT: return "Tele-visit";
            case URGENTCARE: return "Urgent Care";
            case COPAYPCT: return "Copay Percentage";
            default: return "?";
          }
    }


}

