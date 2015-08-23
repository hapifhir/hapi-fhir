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


public enum V3ActExposureLevelCode {

        /**
         * A qualitative measure of the degree of exposure to the causative agent.  This includes concepts such as "low", "medium" and "high".  This quantifies how the quantity that was available to be administered to the target differs from typical or background levels of the substance.
         */
        _ACTEXPOSURELEVELCODE, 
        /**
         * Description: Exposure to an agent at a relatively high level above background.
         */
        HIGH, 
        /**
         * Description: Exposure to an agent at a relatively low level above background.
         */
        LOW, 
        /**
         * Description: Exposure to an agent at a relatively moderate level above background.A
         */
        MEDIUM, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActExposureLevelCode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ActExposureLevelCode".equals(codeString))
          return _ACTEXPOSURELEVELCODE;
        if ("HIGH".equals(codeString))
          return HIGH;
        if ("LOW".equals(codeString))
          return LOW;
        if ("MEDIUM".equals(codeString))
          return MEDIUM;
        throw new Exception("Unknown V3ActExposureLevelCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACTEXPOSURELEVELCODE: return "_ActExposureLevelCode";
            case HIGH: return "HIGH";
            case LOW: return "LOW";
            case MEDIUM: return "MEDIUM";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActExposureLevelCode";
        }
        public String getDefinition() {
          switch (this) {
            case _ACTEXPOSURELEVELCODE: return "A qualitative measure of the degree of exposure to the causative agent.  This includes concepts such as \"low\", \"medium\" and \"high\".  This quantifies how the quantity that was available to be administered to the target differs from typical or background levels of the substance.";
            case HIGH: return "Description: Exposure to an agent at a relatively high level above background.";
            case LOW: return "Description: Exposure to an agent at a relatively low level above background.";
            case MEDIUM: return "Description: Exposure to an agent at a relatively moderate level above background.A";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACTEXPOSURELEVELCODE: return "ActExposureLevelCode";
            case HIGH: return "high";
            case LOW: return "low";
            case MEDIUM: return "medium";
            default: return "?";
          }
    }


}

