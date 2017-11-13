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

// Generated on Sat, Nov 5, 2016 17:49-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ObservationParamcode {

        /**
         * The [mean](https://en.wikipedia.org/wiki/Arithmetic_mean)of N measurements over the stated period
         */
        AVERAGE, 
        /**
         * The [maximum](https://en.wikipedia.org/wiki/Maximal_element) value of N measurements over the stated period
         */
        MAX, 
        /**
         * The [minimum](https://en.wikipedia.org/wiki/Minimal_element) value of N measurements over the stated period
         */
        MIN, 
        /**
         * The [number] of measurements over the stated period
         */
        COUNT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObservationParamcode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("average".equals(codeString))
          return AVERAGE;
        if ("max".equals(codeString))
          return MAX;
        if ("min".equals(codeString))
          return MIN;
        if ("count".equals(codeString))
          return COUNT;
        throw new FHIRException("Unknown ObservationParamcode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AVERAGE: return "average";
            case MAX: return "max";
            case MIN: return "min";
            case COUNT: return "count";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/observation-paramcode";
        }
        public String getDefinition() {
          switch (this) {
            case AVERAGE: return "The [mean](https://en.wikipedia.org/wiki/Arithmetic_mean)of N measurements over the stated period";
            case MAX: return "The [maximum](https://en.wikipedia.org/wiki/Maximal_element) value of N measurements over the stated period";
            case MIN: return "The [minimum](https://en.wikipedia.org/wiki/Minimal_element) value of N measurements over the stated period";
            case COUNT: return "The [number] of measurements over the stated period";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AVERAGE: return "Average";
            case MAX: return "Maximum";
            case MIN: return "Minimum";
            case COUNT: return "Count";
            default: return "?";
          }
    }


}

