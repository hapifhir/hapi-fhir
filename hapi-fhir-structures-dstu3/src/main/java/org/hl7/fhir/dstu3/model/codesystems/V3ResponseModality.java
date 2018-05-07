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

public enum V3ResponseModality {

        /**
         * Query response to be sent as an HL7 Batch.
         */
        B, 
        /**
         * Query response to occur in real time.
         */
        R, 
        /**
         * Query response to sent as a series of responses at the same time without the use of batch formatting.
         */
        T, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ResponseModality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("B".equals(codeString))
          return B;
        if ("R".equals(codeString))
          return R;
        if ("T".equals(codeString))
          return T;
        throw new FHIRException("Unknown V3ResponseModality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case B: return "B";
            case R: return "R";
            case T: return "T";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ResponseModality";
        }
        public String getDefinition() {
          switch (this) {
            case B: return "Query response to be sent as an HL7 Batch.";
            case R: return "Query response to occur in real time.";
            case T: return "Query response to sent as a series of responses at the same time without the use of batch formatting.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case B: return "Batch";
            case R: return "Real Time";
            case T: return "Bolus";
            default: return "?";
          }
    }


}

