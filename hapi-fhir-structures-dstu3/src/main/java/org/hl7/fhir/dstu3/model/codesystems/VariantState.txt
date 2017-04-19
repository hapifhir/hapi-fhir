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

public enum VariantState {

        /**
         * the variant is detected
         */
        POSITIVE, 
        /**
         * no variant is detected
         */
        NEGATIVE, 
        /**
         * result of the variant is missing
         */
        ABSENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VariantState fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("positive".equals(codeString))
          return POSITIVE;
        if ("negative".equals(codeString))
          return NEGATIVE;
        if ("absent".equals(codeString))
          return ABSENT;
        throw new FHIRException("Unknown VariantState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case POSITIVE: return "positive";
            case NEGATIVE: return "negative";
            case ABSENT: return "absent";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/variant-state";
        }
        public String getDefinition() {
          switch (this) {
            case POSITIVE: return "the variant is detected";
            case NEGATIVE: return "no variant is detected";
            case ABSENT: return "result of the variant is missing";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case POSITIVE: return "positive";
            case NEGATIVE: return "negative";
            case ABSENT: return "absent";
            default: return "?";
          }
    }


}

