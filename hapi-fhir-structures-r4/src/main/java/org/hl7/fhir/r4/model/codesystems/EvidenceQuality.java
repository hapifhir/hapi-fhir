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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum EvidenceQuality {

        /**
         * High quality evidence.
         */
        HIGH, 
        /**
         * Moderate quality evidence.
         */
        MODERATE, 
        /**
         * Low quality evidence.
         */
        LOW, 
        /**
         * Very low quality evidence.
         */
        VERYLOW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EvidenceQuality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("high".equals(codeString))
          return HIGH;
        if ("moderate".equals(codeString))
          return MODERATE;
        if ("low".equals(codeString))
          return LOW;
        if ("very-low".equals(codeString))
          return VERYLOW;
        throw new FHIRException("Unknown EvidenceQuality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HIGH: return "high";
            case MODERATE: return "moderate";
            case LOW: return "low";
            case VERYLOW: return "very-low";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/evidence-quality";
        }
        public String getDefinition() {
          switch (this) {
            case HIGH: return "High quality evidence.";
            case MODERATE: return "Moderate quality evidence.";
            case LOW: return "Low quality evidence.";
            case VERYLOW: return "Very low quality evidence.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HIGH: return "High quality";
            case MODERATE: return "Moderate quality";
            case LOW: return "Low quality";
            case VERYLOW: return "Very low quality";
            default: return "?";
          }
    }


}

