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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum QuestionnaireEnableOperator {

        /**
         * True if whether an answer exists is equal to the enableWhen answer (which must be a boolean)
         */
        EXISTS, 
        /**
         * True if whether at least one answer has a value that is equal to the enableWhen answer
         */
        EQUAL, 
        /**
         * True if whether at least no answer has a value that is equal to the enableWhen answer
         */
        NOT_EQUAL, 
        /**
         * True if whether at least no answer has a value that is greater than the enableWhen answer
         */
        GREATER_THAN, 
        /**
         * True if whether at least no answer has a value that is less than the enableWhen answer
         */
        LESS_THAN, 
        /**
         * True if whether at least no answer has a value that is greater or equal to the enableWhen answer
         */
        GREATER_OR_EQUAL, 
        /**
         * True if whether at least no answer has a value that is less or equal to the enableWhen answer
         */
        LESS_OR_EQUAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QuestionnaireEnableOperator fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("exists".equals(codeString))
          return EXISTS;
        if ("=".equals(codeString))
          return EQUAL;
        if ("!=".equals(codeString))
          return NOT_EQUAL;
        if (">".equals(codeString))
          return GREATER_THAN;
        if ("<".equals(codeString))
          return LESS_THAN;
        if (">=".equals(codeString))
          return GREATER_OR_EQUAL;
        if ("<=".equals(codeString))
          return LESS_OR_EQUAL;
        throw new FHIRException("Unknown QuestionnaireEnableOperator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EXISTS: return "exists";
            case EQUAL: return "=";
            case NOT_EQUAL: return "!=";
            case GREATER_THAN: return ">";
            case LESS_THAN: return "<";
            case GREATER_OR_EQUAL: return ">=";
            case LESS_OR_EQUAL: return "<=";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/questionnaire-enable-operator";
        }
        public String getDefinition() {
          switch (this) {
            case EXISTS: return "True if whether an answer exists is equal to the enableWhen answer (which must be a boolean)";
            case EQUAL: return "True if whether at least one answer has a value that is equal to the enableWhen answer";
            case NOT_EQUAL: return "True if whether at least no answer has a value that is equal to the enableWhen answer";
            case GREATER_THAN: return "True if whether at least no answer has a value that is greater than the enableWhen answer";
            case LESS_THAN: return "True if whether at least no answer has a value that is less than the enableWhen answer";
            case GREATER_OR_EQUAL: return "True if whether at least no answer has a value that is greater or equal to the enableWhen answer";
            case LESS_OR_EQUAL: return "True if whether at least no answer has a value that is less or equal to the enableWhen answer";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EXISTS: return "Exists";
            case EQUAL: return "Equals";
            case NOT_EQUAL: return "Not Equals";
            case GREATER_THAN: return "Greater Than";
            case LESS_THAN: return "Less Than";
            case GREATER_OR_EQUAL: return "Greater or Equals";
            case LESS_OR_EQUAL: return "Less or Equals";
            default: return "?";
          }
    }


}

