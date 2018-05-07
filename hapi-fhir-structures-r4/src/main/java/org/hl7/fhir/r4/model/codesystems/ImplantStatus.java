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

// Generated on Sat, Mar 3, 2018 18:00-0500 for FHIR v3.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ImplantStatus {

        /**
         * The implanted device is working normally
         */
        FUNCTIONAL, 
        /**
         * The implanted device is not working
         */
        NONFUNCTIONAL, 
        /**
         * The implanted device has been turned off
         */
        DISABLED, 
        /**
         * the functional status of the implant has not been determined
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ImplantStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("functional".equals(codeString))
          return FUNCTIONAL;
        if ("non-functional".equals(codeString))
          return NONFUNCTIONAL;
        if ("disabled".equals(codeString))
          return DISABLED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown ImplantStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FUNCTIONAL: return "functional";
            case NONFUNCTIONAL: return "non-functional";
            case DISABLED: return "disabled";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/implant-status";
        }
        public String getDefinition() {
          switch (this) {
            case FUNCTIONAL: return "The implanted device is working normally";
            case NONFUNCTIONAL: return "The implanted device is not working";
            case DISABLED: return "The implanted device has been turned off";
            case UNKNOWN: return "the functional status of the implant has not been determined";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FUNCTIONAL: return "Functional";
            case NONFUNCTIONAL: return "Non-Functional";
            case DISABLED: return "Disabled";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

