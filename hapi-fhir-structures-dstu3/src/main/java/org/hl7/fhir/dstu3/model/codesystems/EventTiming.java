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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum EventTiming {

        /**
         * event occurs during the morning
         */
        MORN, 
        /**
         * event occurs during the afternoon
         */
        AFT, 
        /**
         * event occurs during the evening
         */
        EVE, 
        /**
         * event occurs during the night
         */
        NIGHT, 
        /**
         * event occurs [offset] after subject goes to sleep
         */
        PHS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EventTiming fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("MORN".equals(codeString))
          return MORN;
        if ("AFT".equals(codeString))
          return AFT;
        if ("EVE".equals(codeString))
          return EVE;
        if ("NIGHT".equals(codeString))
          return NIGHT;
        if ("PHS".equals(codeString))
          return PHS;
        throw new FHIRException("Unknown EventTiming code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MORN: return "MORN";
            case AFT: return "AFT";
            case EVE: return "EVE";
            case NIGHT: return "NIGHT";
            case PHS: return "PHS";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/event-timing";
        }
        public String getDefinition() {
          switch (this) {
            case MORN: return "event occurs during the morning";
            case AFT: return "event occurs during the afternoon";
            case EVE: return "event occurs during the evening";
            case NIGHT: return "event occurs during the night";
            case PHS: return "event occurs [offset] after subject goes to sleep";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MORN: return "Morning";
            case AFT: return "Afternoon";
            case EVE: return "Evening";
            case NIGHT: return "Night";
            case PHS: return "After Sleep";
            default: return "?";
          }
    }


}

