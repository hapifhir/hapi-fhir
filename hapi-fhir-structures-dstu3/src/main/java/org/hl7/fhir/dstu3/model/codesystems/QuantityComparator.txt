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

public enum QuantityComparator {

        /**
         * The actual value is less than the given value.
         */
        LESS_THAN, 
        /**
         * The actual value is less than or equal to the given value.
         */
        LESS_OR_EQUAL, 
        /**
         * The actual value is greater than or equal to the given value.
         */
        GREATER_OR_EQUAL, 
        /**
         * The actual value is greater than the given value.
         */
        GREATER_THAN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QuantityComparator fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("<".equals(codeString))
          return LESS_THAN;
        if ("<=".equals(codeString))
          return LESS_OR_EQUAL;
        if (">=".equals(codeString))
          return GREATER_OR_EQUAL;
        if (">".equals(codeString))
          return GREATER_THAN;
        throw new FHIRException("Unknown QuantityComparator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LESS_THAN: return "<";
            case LESS_OR_EQUAL: return "<=";
            case GREATER_OR_EQUAL: return ">=";
            case GREATER_THAN: return ">";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/quantity-comparator";
        }
        public String getDefinition() {
          switch (this) {
            case LESS_THAN: return "The actual value is less than the given value.";
            case LESS_OR_EQUAL: return "The actual value is less than or equal to the given value.";
            case GREATER_OR_EQUAL: return "The actual value is greater than or equal to the given value.";
            case GREATER_THAN: return "The actual value is greater than the given value.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LESS_THAN: return "Less than";
            case LESS_OR_EQUAL: return "Less or Equal to";
            case GREATER_OR_EQUAL: return "Greater or Equal to";
            case GREATER_THAN: return "Greater than";
            default: return "?";
          }
    }


}

