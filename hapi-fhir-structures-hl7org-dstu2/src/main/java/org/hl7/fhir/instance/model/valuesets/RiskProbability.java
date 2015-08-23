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


public enum RiskProbability {

        /**
         * The specified outcome is exceptionally unlikely
         */
        NEGLIGIBLE, 
        /**
         * The specified outcome is possible but unlikely
         */
        LOW, 
        /**
         * The specified outcome has a reasonable likelihood of occurrence
         */
        MODERATE, 
        /**
         * The specified outcome is more likely to occur than not
         */
        HIGH, 
        /**
         * The specified outcome is effectively guaranteed
         */
        CERTAIN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RiskProbability fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("negligible".equals(codeString))
          return NEGLIGIBLE;
        if ("low".equals(codeString))
          return LOW;
        if ("moderate".equals(codeString))
          return MODERATE;
        if ("high".equals(codeString))
          return HIGH;
        if ("certain".equals(codeString))
          return CERTAIN;
        throw new Exception("Unknown RiskProbability code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NEGLIGIBLE: return "negligible";
            case LOW: return "low";
            case MODERATE: return "moderate";
            case HIGH: return "high";
            case CERTAIN: return "certain";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/risk-probability";
        }
        public String getDefinition() {
          switch (this) {
            case NEGLIGIBLE: return "The specified outcome is exceptionally unlikely";
            case LOW: return "The specified outcome is possible but unlikely";
            case MODERATE: return "The specified outcome has a reasonable likelihood of occurrence";
            case HIGH: return "The specified outcome is more likely to occur than not";
            case CERTAIN: return "The specified outcome is effectively guaranteed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NEGLIGIBLE: return "negligible likelihood";
            case LOW: return "low likelihood";
            case MODERATE: return "moderate likelihood";
            case HIGH: return "high likelihood";
            case CERTAIN: return "certain";
            default: return "?";
          }
    }


}

