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

public enum PractitionerSpecialty {

        /**
         * null
         */
        CARDIO, 
        /**
         * null
         */
        DENT, 
        /**
         * null
         */
        DIETARY, 
        /**
         * null
         */
        MIDW, 
        /**
         * null
         */
        SYSARCH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PractitionerSpecialty fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cardio".equals(codeString))
          return CARDIO;
        if ("dent".equals(codeString))
          return DENT;
        if ("dietary".equals(codeString))
          return DIETARY;
        if ("midw".equals(codeString))
          return MIDW;
        if ("sysarch".equals(codeString))
          return SYSARCH;
        throw new FHIRException("Unknown PractitionerSpecialty code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CARDIO: return "cardio";
            case DENT: return "dent";
            case DIETARY: return "dietary";
            case MIDW: return "midw";
            case SYSARCH: return "sysarch";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/practitioner-specialty";
        }
        public String getDefinition() {
          switch (this) {
            case CARDIO: return "";
            case DENT: return "";
            case DIETARY: return "";
            case MIDW: return "";
            case SYSARCH: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CARDIO: return "Cardiologist";
            case DENT: return "Dentist";
            case DIETARY: return "Dietary consultant";
            case MIDW: return "Midwife";
            case SYSARCH: return "Systems architect";
            default: return "?";
          }
    }


}

