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


public enum V3ProcessingMode {

        /**
         * Identifies archive mode of processing.
         */
        A, 
        /**
         * Identifies initial load mode of processing.
         */
        I, 
        /**
         * Identifies restore mode of processing.
         */
        R, 
        /**
         * Identifies on-line mode of processing.
         */
        T, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ProcessingMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("I".equals(codeString))
          return I;
        if ("R".equals(codeString))
          return R;
        if ("T".equals(codeString))
          return T;
        throw new Exception("Unknown V3ProcessingMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case I: return "I";
            case R: return "R";
            case T: return "T";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ProcessingMode";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "Identifies archive mode of processing.";
            case I: return "Identifies initial load mode of processing.";
            case R: return "Identifies restore mode of processing.";
            case T: return "Identifies on-line mode of processing.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "Archive";
            case I: return "Initial load";
            case R: return "Restore from archive";
            case T: return "Current processing";
            default: return "?";
          }
    }


}

