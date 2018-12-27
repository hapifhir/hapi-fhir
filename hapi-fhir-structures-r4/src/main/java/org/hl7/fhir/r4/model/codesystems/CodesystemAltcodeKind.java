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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum CodesystemAltcodeKind {

        /**
         * The code is an alternative code that can be used in any of the circumstances that the primary code can be used.
         */
        ALTERNATE, 
        /**
         * The code should no longer be used, but was used in the past.
         */
        DEPRECATED, 
        /**
         * The code is an alternative to be used when a case insensitive code is required (when the primary codes are case sensitive).
         */
        CASEINSENSITIVE, 
        /**
         * The code is an alternative to be used when a case sensitive code is required (when the primary codes are case insensitive).
         */
        CASESENSITIVE, 
        /**
         * The code is an alternative for the primary code that is built using the expression grammar defined by the code system.
         */
        EXPRESSION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CodesystemAltcodeKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("alternate".equals(codeString))
          return ALTERNATE;
        if ("deprecated".equals(codeString))
          return DEPRECATED;
        if ("case-insensitive".equals(codeString))
          return CASEINSENSITIVE;
        if ("case-sensitive".equals(codeString))
          return CASESENSITIVE;
        if ("expression".equals(codeString))
          return EXPRESSION;
        throw new FHIRException("Unknown CodesystemAltcodeKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALTERNATE: return "alternate";
            case DEPRECATED: return "deprecated";
            case CASEINSENSITIVE: return "case-insensitive";
            case CASESENSITIVE: return "case-sensitive";
            case EXPRESSION: return "expression";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/codesystem-altcode-kind";
        }
        public String getDefinition() {
          switch (this) {
            case ALTERNATE: return "The code is an alternative code that can be used in any of the circumstances that the primary code can be used.";
            case DEPRECATED: return "The code should no longer be used, but was used in the past.";
            case CASEINSENSITIVE: return "The code is an alternative to be used when a case insensitive code is required (when the primary codes are case sensitive).";
            case CASESENSITIVE: return "The code is an alternative to be used when a case sensitive code is required (when the primary codes are case insensitive).";
            case EXPRESSION: return "The code is an alternative for the primary code that is built using the expression grammar defined by the code system.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALTERNATE: return "Alternate Code";
            case DEPRECATED: return "Deprecated";
            case CASEINSENSITIVE: return "Case Insensitive";
            case CASESENSITIVE: return "Case Sensitive";
            case EXPRESSION: return "Expression";
            default: return "?";
          }
    }


}

