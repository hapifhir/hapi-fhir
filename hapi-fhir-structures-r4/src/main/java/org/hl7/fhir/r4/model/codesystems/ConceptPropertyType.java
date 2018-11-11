package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ConceptPropertyType {

        /**
         * The property value is a code that identifies a concept defined in the code system.
         */
        CODE, 
        /**
         * The property  value is a code defined in an external code system. This may be used for translations, but is not the intent.
         */
        CODING, 
        /**
         * The property value is a string.
         */
        STRING, 
        /**
         * The property value is a string (often used to assign ranking values to concepts for supporting score assessments).
         */
        INTEGER, 
        /**
         * The property value is a boolean true | false.
         */
        BOOLEAN, 
        /**
         * The property is a date or a date + time.
         */
        DATETIME, 
        /**
         * The property value is a decimal number.
         */
        DECIMAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConceptPropertyType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("code".equals(codeString))
          return CODE;
        if ("Coding".equals(codeString))
          return CODING;
        if ("string".equals(codeString))
          return STRING;
        if ("integer".equals(codeString))
          return INTEGER;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("dateTime".equals(codeString))
          return DATETIME;
        if ("decimal".equals(codeString))
          return DECIMAL;
        throw new FHIRException("Unknown ConceptPropertyType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CODE: return "code";
            case CODING: return "Coding";
            case STRING: return "string";
            case INTEGER: return "integer";
            case BOOLEAN: return "boolean";
            case DATETIME: return "dateTime";
            case DECIMAL: return "decimal";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/concept-property-type";
        }
        public String getDefinition() {
          switch (this) {
            case CODE: return "The property value is a code that identifies a concept defined in the code system.";
            case CODING: return "The property  value is a code defined in an external code system. This may be used for translations, but is not the intent.";
            case STRING: return "The property value is a string.";
            case INTEGER: return "The property value is a string (often used to assign ranking values to concepts for supporting score assessments).";
            case BOOLEAN: return "The property value is a boolean true | false.";
            case DATETIME: return "The property is a date or a date + time.";
            case DECIMAL: return "The property value is a decimal number.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CODE: return "code (internal reference)";
            case CODING: return "Coding (external reference)";
            case STRING: return "string";
            case INTEGER: return "integer";
            case BOOLEAN: return "boolean";
            case DATETIME: return "dateTime";
            case DECIMAL: return "decimal";
            default: return "?";
          }
    }


}

