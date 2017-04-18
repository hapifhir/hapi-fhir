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

public enum ConformanceExpectation {

        /**
         * Support for the specified capability is required to be considered conformant.
         */
        SHALL, 
        /**
         * Support for the specified capability is strongly encouraged, and failure to support it should only occur after careful consideration.
         */
        SHOULD, 
        /**
         * Support for the specified capability is not necessary to be considered conformant, and the requirement should be considered strictly optional.
         */
        MAY, 
        /**
         * Support for the specified capability is strongly discouraged and should occur only after careful consideration.
         */
        SHOULDNOT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConformanceExpectation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("SHALL".equals(codeString))
          return SHALL;
        if ("SHOULD".equals(codeString))
          return SHOULD;
        if ("MAY".equals(codeString))
          return MAY;
        if ("SHOULD-NOT".equals(codeString))
          return SHOULDNOT;
        throw new FHIRException("Unknown ConformanceExpectation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SHALL: return "SHALL";
            case SHOULD: return "SHOULD";
            case MAY: return "MAY";
            case SHOULDNOT: return "SHOULD-NOT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/conformance-expectation";
        }
        public String getDefinition() {
          switch (this) {
            case SHALL: return "Support for the specified capability is required to be considered conformant.";
            case SHOULD: return "Support for the specified capability is strongly encouraged, and failure to support it should only occur after careful consideration.";
            case MAY: return "Support for the specified capability is not necessary to be considered conformant, and the requirement should be considered strictly optional.";
            case SHOULDNOT: return "Support for the specified capability is strongly discouraged and should occur only after careful consideration.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SHALL: return "SHALL";
            case SHOULD: return "SHOULD";
            case MAY: return "MAY";
            case SHOULDNOT: return "SHOULD-NOT";
            default: return "?";
          }
    }


}

