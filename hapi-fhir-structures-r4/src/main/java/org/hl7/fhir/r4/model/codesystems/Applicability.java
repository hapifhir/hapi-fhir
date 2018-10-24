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

// Generated on Thu, May 24, 2018 06:34-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum Applicability {

        /**
         * Provider is contracted with the health insurance company to provide services to plan members for specific pre-negotiated rates
         */
        INNETWORK, 
        /**
         * Provider is  not contracted with the health insurance company to provide services to plan members for specific pre-negotiated rates
         */
        OUTOFNETWORK, 
        /**
         * Other applicability
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Applicability fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-network".equals(codeString))
          return INNETWORK;
        if ("out-of-network".equals(codeString))
          return OUTOFNETWORK;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown Applicability code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INNETWORK: return "in-network";
            case OUTOFNETWORK: return "out-of-network";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/applicability";
        }
        public String getDefinition() {
          switch (this) {
            case INNETWORK: return "Provider is contracted with the health insurance company to provide services to plan members for specific pre-negotiated rates";
            case OUTOFNETWORK: return "Provider is  not contracted with the health insurance company to provide services to plan members for specific pre-negotiated rates";
            case OTHER: return "Other applicability";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INNETWORK: return "In Network";
            case OUTOFNETWORK: return "Out of Network";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

