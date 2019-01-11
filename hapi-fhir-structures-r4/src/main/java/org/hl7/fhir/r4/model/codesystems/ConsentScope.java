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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ConsentScope {

        /**
         * Actions to be taken if they are no longer able to make decisions for themselves
         */
        ADR, 
        /**
         * Consent to participate in research protocol and information sharing required
         */
        RESEARCH, 
        /**
         * Agreement to collect, access, use or disclose (share) information
         */
        PATIENTPRIVACY, 
        /**
         * Consent to undergo a specific treatment
         */
        TREATMENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConsentScope fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("adr".equals(codeString))
          return ADR;
        if ("research".equals(codeString))
          return RESEARCH;
        if ("patient-privacy".equals(codeString))
          return PATIENTPRIVACY;
        if ("treatment".equals(codeString))
          return TREATMENT;
        throw new FHIRException("Unknown ConsentScope code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADR: return "adr";
            case RESEARCH: return "research";
            case PATIENTPRIVACY: return "patient-privacy";
            case TREATMENT: return "treatment";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/consentscope";
        }
        public String getDefinition() {
          switch (this) {
            case ADR: return "Actions to be taken if they are no longer able to make decisions for themselves";
            case RESEARCH: return "Consent to participate in research protocol and information sharing required";
            case PATIENTPRIVACY: return "Agreement to collect, access, use or disclose (share) information";
            case TREATMENT: return "Consent to undergo a specific treatment";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADR: return "Advanced Care Directive";
            case RESEARCH: return "Research";
            case PATIENTPRIVACY: return "Privacy Consent";
            case TREATMENT: return "Treatment";
            default: return "?";
          }
    }


}

