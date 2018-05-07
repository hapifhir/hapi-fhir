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

public enum V3LanguageAbilityMode {

        /**
         * Expressed signed
         */
        ESGN, 
        /**
         * Expressed spoken
         */
        ESP, 
        /**
         * Expressed written
         */
        EWR, 
        /**
         * Received signed
         */
        RSGN, 
        /**
         * Received spoken
         */
        RSP, 
        /**
         * Received written
         */
        RWR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3LanguageAbilityMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ESGN".equals(codeString))
          return ESGN;
        if ("ESP".equals(codeString))
          return ESP;
        if ("EWR".equals(codeString))
          return EWR;
        if ("RSGN".equals(codeString))
          return RSGN;
        if ("RSP".equals(codeString))
          return RSP;
        if ("RWR".equals(codeString))
          return RWR;
        throw new FHIRException("Unknown V3LanguageAbilityMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ESGN: return "ESGN";
            case ESP: return "ESP";
            case EWR: return "EWR";
            case RSGN: return "RSGN";
            case RSP: return "RSP";
            case RWR: return "RWR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/LanguageAbilityMode";
        }
        public String getDefinition() {
          switch (this) {
            case ESGN: return "Expressed signed";
            case ESP: return "Expressed spoken";
            case EWR: return "Expressed written";
            case RSGN: return "Received signed";
            case RSP: return "Received spoken";
            case RWR: return "Received written";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ESGN: return "Expressed signed";
            case ESP: return "Expressed spoken";
            case EWR: return "Expressed written";
            case RSGN: return "Received signed";
            case RSP: return "Received spoken";
            case RWR: return "Received written";
            default: return "?";
          }
    }


}

