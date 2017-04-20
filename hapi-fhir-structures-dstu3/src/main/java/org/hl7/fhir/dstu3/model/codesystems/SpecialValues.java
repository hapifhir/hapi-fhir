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

public enum SpecialValues {

        /**
         * Boolean true.
         */
        TRUE, 
        /**
         * Boolean false.
         */
        FALSE, 
        /**
         * The content is greater than zero, but too small to be quantified.
         */
        TRACE, 
        /**
         * The specific quantity is not known, but is known to be non-zero and is not specified because it makes up the bulk of the material.
         */
        SUFFICIENT, 
        /**
         * The value is no longer available.
         */
        WITHDRAWN, 
        /**
         * The are no known applicable values in this context.
         */
        NILKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SpecialValues fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("true".equals(codeString))
          return TRUE;
        if ("false".equals(codeString))
          return FALSE;
        if ("trace".equals(codeString))
          return TRACE;
        if ("sufficient".equals(codeString))
          return SUFFICIENT;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        if ("nil-known".equals(codeString))
          return NILKNOWN;
        throw new FHIRException("Unknown SpecialValues code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TRUE: return "true";
            case FALSE: return "false";
            case TRACE: return "trace";
            case SUFFICIENT: return "sufficient";
            case WITHDRAWN: return "withdrawn";
            case NILKNOWN: return "nil-known";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/special-values";
        }
        public String getDefinition() {
          switch (this) {
            case TRUE: return "Boolean true.";
            case FALSE: return "Boolean false.";
            case TRACE: return "The content is greater than zero, but too small to be quantified.";
            case SUFFICIENT: return "The specific quantity is not known, but is known to be non-zero and is not specified because it makes up the bulk of the material.";
            case WITHDRAWN: return "The value is no longer available.";
            case NILKNOWN: return "The are no known applicable values in this context.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TRUE: return "true";
            case FALSE: return "false";
            case TRACE: return "Trace Amount Detected";
            case SUFFICIENT: return "Sufficient Quantity";
            case WITHDRAWN: return "Value Withdrawn";
            case NILKNOWN: return "Nil Known";
            default: return "?";
          }
    }


}

