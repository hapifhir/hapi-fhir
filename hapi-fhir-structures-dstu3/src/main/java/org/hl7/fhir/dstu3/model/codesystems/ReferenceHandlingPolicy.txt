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

public enum ReferenceHandlingPolicy {

        /**
         * The server supports and populates Literal references where they are known (this code does not guarantee that all references are literal; see 'enforced')
         */
        LITERAL, 
        /**
         * The server allows logical references
         */
        LOGICAL, 
        /**
         * The server will attempt to resolve logical references to literal references (if resolution fails, the server may still accept resources; see logical)
         */
        RESOLVES, 
        /**
         * The server enforces that references have integrity - e.g. it ensures that references can always be resolved. This is typically the case for clinical record systems, but often not the case for middleware/proxy systems
         */
        ENFORCED, 
        /**
         * The server does not support references that point to other servers
         */
        LOCAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ReferenceHandlingPolicy fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("literal".equals(codeString))
          return LITERAL;
        if ("logical".equals(codeString))
          return LOGICAL;
        if ("resolves".equals(codeString))
          return RESOLVES;
        if ("enforced".equals(codeString))
          return ENFORCED;
        if ("local".equals(codeString))
          return LOCAL;
        throw new FHIRException("Unknown ReferenceHandlingPolicy code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LITERAL: return "literal";
            case LOGICAL: return "logical";
            case RESOLVES: return "resolves";
            case ENFORCED: return "enforced";
            case LOCAL: return "local";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/reference-handling-policy";
        }
        public String getDefinition() {
          switch (this) {
            case LITERAL: return "The server supports and populates Literal references where they are known (this code does not guarantee that all references are literal; see 'enforced')";
            case LOGICAL: return "The server allows logical references";
            case RESOLVES: return "The server will attempt to resolve logical references to literal references (if resolution fails, the server may still accept resources; see logical)";
            case ENFORCED: return "The server enforces that references have integrity - e.g. it ensures that references can always be resolved. This is typically the case for clinical record systems, but often not the case for middleware/proxy systems";
            case LOCAL: return "The server does not support references that point to other servers";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LITERAL: return "Literal References";
            case LOGICAL: return "Logical References";
            case RESOLVES: return "Resolves References";
            case ENFORCED: return "Reference Integrity Enforced";
            case LOCAL: return "Local References Only";
            default: return "?";
          }
    }


}

