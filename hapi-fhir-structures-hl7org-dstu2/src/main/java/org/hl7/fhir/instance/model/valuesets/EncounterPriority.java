package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum EncounterPriority {

        /**
         * Within seconds
         */
        IMM, 
        /**
         * Within 10 minutes
         */
        EMG, 
        /**
         * Within 30 minutes
         */
        URG, 
        /**
         * Within 60 minutes
         */
        SURG, 
        /**
         * Within 120 minutes
         */
        NOURG, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterPriority fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("imm".equals(codeString))
          return IMM;
        if ("emg".equals(codeString))
          return EMG;
        if ("urg".equals(codeString))
          return URG;
        if ("s-urg".equals(codeString))
          return SURG;
        if ("no-urg".equals(codeString))
          return NOURG;
        throw new Exception("Unknown EncounterPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IMM: return "imm";
            case EMG: return "emg";
            case URG: return "urg";
            case SURG: return "s-urg";
            case NOURG: return "no-urg";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/encounter-priority";
        }
        public String getDefinition() {
          switch (this) {
            case IMM: return "Within seconds";
            case EMG: return "Within 10 minutes";
            case URG: return "Within 30 minutes";
            case SURG: return "Within 60 minutes";
            case NOURG: return "Within 120 minutes";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IMM: return "Immediate";
            case EMG: return "Emergency";
            case URG: return "Urgent";
            case SURG: return "Semi-urgent";
            case NOURG: return "Non-urgent";
            default: return "?";
          }
    }


}

