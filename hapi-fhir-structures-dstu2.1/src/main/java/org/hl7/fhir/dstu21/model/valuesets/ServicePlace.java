package org.hl7.fhir.dstu21.model.valuesets;

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

// Generated on Mon, Dec 21, 2015 19:58-0500 for FHIR v1.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ServicePlace {

        /**
         * Emergency Department
         */
        EMERGENCY, 
        /**
         * Clinic
         */
        CLINIC, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServicePlace fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("emergency".equals(codeString))
          return EMERGENCY;
        if ("clinic".equals(codeString))
          return CLINIC;
        throw new FHIRException("Unknown ServicePlace code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EMERGENCY: return "emergency";
            case CLINIC: return "clinic";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-serviceplace";
        }
        public String getDefinition() {
          switch (this) {
            case EMERGENCY: return "Emergency Department";
            case CLINIC: return "Clinic";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EMERGENCY: return "Emergency Department";
            case CLINIC: return "Clinic";
            default: return "?";
          }
    }


}

