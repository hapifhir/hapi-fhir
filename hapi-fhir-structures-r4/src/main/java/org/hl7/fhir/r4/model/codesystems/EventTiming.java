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

public enum EventTiming {

        /**
         * Event occurs during the morning. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        MORN, 
        /**
         * Event occurs during the early morning. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        MORN_EARLY, 
        /**
         * Event occurs during the late morning. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        MORN_LATE, 
        /**
         * Event occurs around 12:00pm. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        NOON, 
        /**
         * Event occurs during the afternoon. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        AFT, 
        /**
         * Event occurs during the early afternoon. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        AFT_EARLY, 
        /**
         * Event occurs during the late afternoon. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        AFT_LATE, 
        /**
         * Event occurs during the evening. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        EVE, 
        /**
         * Event occurs during the early evening. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        EVE_EARLY, 
        /**
         * Event occurs during the late evening. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        EVE_LATE, 
        /**
         * Event occurs during the night. The exact time is unspecified and established by institution convention or patient interpretation.
         */
        NIGHT, 
        /**
         * Event occurs [offset] after subject goes to sleep. The exact time is unspecified and established by institution convention or patient interpretation.
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
        if ("MORN.early".equals(codeString))
          return MORN_EARLY;
        if ("MORN.late".equals(codeString))
          return MORN_LATE;
        if ("NOON".equals(codeString))
          return NOON;
        if ("AFT".equals(codeString))
          return AFT;
        if ("AFT.early".equals(codeString))
          return AFT_EARLY;
        if ("AFT.late".equals(codeString))
          return AFT_LATE;
        if ("EVE".equals(codeString))
          return EVE;
        if ("EVE.early".equals(codeString))
          return EVE_EARLY;
        if ("EVE.late".equals(codeString))
          return EVE_LATE;
        if ("NIGHT".equals(codeString))
          return NIGHT;
        if ("PHS".equals(codeString))
          return PHS;
        throw new FHIRException("Unknown EventTiming code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MORN: return "MORN";
            case MORN_EARLY: return "MORN.early";
            case MORN_LATE: return "MORN.late";
            case NOON: return "NOON";
            case AFT: return "AFT";
            case AFT_EARLY: return "AFT.early";
            case AFT_LATE: return "AFT.late";
            case EVE: return "EVE";
            case EVE_EARLY: return "EVE.early";
            case EVE_LATE: return "EVE.late";
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
            case MORN: return "Event occurs during the morning. The exact time is unspecified and established by institution convention or patient interpretation.";
            case MORN_EARLY: return "Event occurs during the early morning. The exact time is unspecified and established by institution convention or patient interpretation.";
            case MORN_LATE: return "Event occurs during the late morning. The exact time is unspecified and established by institution convention or patient interpretation.";
            case NOON: return "Event occurs around 12:00pm. The exact time is unspecified and established by institution convention or patient interpretation.";
            case AFT: return "Event occurs during the afternoon. The exact time is unspecified and established by institution convention or patient interpretation.";
            case AFT_EARLY: return "Event occurs during the early afternoon. The exact time is unspecified and established by institution convention or patient interpretation.";
            case AFT_LATE: return "Event occurs during the late afternoon. The exact time is unspecified and established by institution convention or patient interpretation.";
            case EVE: return "Event occurs during the evening. The exact time is unspecified and established by institution convention or patient interpretation.";
            case EVE_EARLY: return "Event occurs during the early evening. The exact time is unspecified and established by institution convention or patient interpretation.";
            case EVE_LATE: return "Event occurs during the late evening. The exact time is unspecified and established by institution convention or patient interpretation.";
            case NIGHT: return "Event occurs during the night. The exact time is unspecified and established by institution convention or patient interpretation.";
            case PHS: return "Event occurs [offset] after subject goes to sleep. The exact time is unspecified and established by institution convention or patient interpretation.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MORN: return "Morning";
            case MORN_EARLY: return "Early Morning";
            case MORN_LATE: return "Late Morning";
            case NOON: return "Noon";
            case AFT: return "Afternoon";
            case AFT_EARLY: return "Early Afternoon";
            case AFT_LATE: return "Late Afternoon";
            case EVE: return "Evening";
            case EVE_EARLY: return "Early Evening";
            case EVE_LATE: return "Late Evening";
            case NIGHT: return "Night";
            case PHS: return "After Sleep";
            default: return "?";
          }
    }


}

