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

public enum DataAbsentReason {

        /**
         * The value is not known.
         */
        UNKNOWN, 
        /**
         * The source human does not know the value.
         */
        ASKED, 
        /**
         * There is reason to expect (from the workflow) that the value may become known.
         */
        TEMP, 
        /**
         * The workflow didn't lead to this value being known.
         */
        NOTASKED, 
        /**
         * The information is not available due to security, privacy or related reasons.
         */
        MASKED, 
        /**
         * The source system wasn't capable of supporting this element.
         */
        UNSUPPORTED, 
        /**
         * The content of the data is represented in the resource narrative.
         */
        ASTEXT, 
        /**
         * Some system or workflow process error means that the information is not available.
         */
        ERROR, 
        /**
         * NaN, standing for not a number, is a numeric data type value representing an undefined or unrepresentable value.
         */
        NAN, 
        /**
         * The value is not available because the observation procedure (test, etc.) was not performed.
         */
        NOTPERFORMED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataAbsentReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("asked".equals(codeString))
          return ASKED;
        if ("temp".equals(codeString))
          return TEMP;
        if ("not-asked".equals(codeString))
          return NOTASKED;
        if ("masked".equals(codeString))
          return MASKED;
        if ("unsupported".equals(codeString))
          return UNSUPPORTED;
        if ("astext".equals(codeString))
          return ASTEXT;
        if ("error".equals(codeString))
          return ERROR;
        if ("NaN".equals(codeString))
          return NAN;
        if ("not-performed".equals(codeString))
          return NOTPERFORMED;
        throw new FHIRException("Unknown DataAbsentReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case ASKED: return "asked";
            case TEMP: return "temp";
            case NOTASKED: return "not-asked";
            case MASKED: return "masked";
            case UNSUPPORTED: return "unsupported";
            case ASTEXT: return "astext";
            case ERROR: return "error";
            case NAN: return "NaN";
            case NOTPERFORMED: return "not-performed";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/data-absent-reason";
        }
        public String getDefinition() {
          switch (this) {
            case UNKNOWN: return "The value is not known.";
            case ASKED: return "The source human does not know the value.";
            case TEMP: return "There is reason to expect (from the workflow) that the value may become known.";
            case NOTASKED: return "The workflow didn't lead to this value being known.";
            case MASKED: return "The information is not available due to security, privacy or related reasons.";
            case UNSUPPORTED: return "The source system wasn't capable of supporting this element.";
            case ASTEXT: return "The content of the data is represented in the resource narrative.";
            case ERROR: return "Some system or workflow process error means that the information is not available.";
            case NAN: return "NaN, standing for not a number, is a numeric data type value representing an undefined or unrepresentable value.";
            case NOTPERFORMED: return "The value is not available because the observation procedure (test, etc.) was not performed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNKNOWN: return "Unknown";
            case ASKED: return "Asked";
            case TEMP: return "Temp";
            case NOTASKED: return "Not Asked";
            case MASKED: return "Masked";
            case UNSUPPORTED: return "Unsupported";
            case ASTEXT: return "As Text";
            case ERROR: return "Error";
            case NAN: return "Not a Number";
            case NOTPERFORMED: return "Not Performed";
            default: return "?";
          }
    }


}

