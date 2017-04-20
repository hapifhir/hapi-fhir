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

public enum ConditionalReadStatus {

        /**
         * No support for conditional deletes.
         */
        NOTSUPPORTED, 
        /**
         * Conditional reads are supported, but only with the If-Modified-Since HTTP Header.
         */
        MODIFIEDSINCE, 
        /**
         * Conditional reads are supported, but only with the If-None-Match HTTP Header.
         */
        NOTMATCH, 
        /**
         * Conditional reads are supported, with both If-Modified-Since and If-None-Match HTTP Headers.
         */
        FULLSUPPORT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionalReadStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-supported".equals(codeString))
          return NOTSUPPORTED;
        if ("modified-since".equals(codeString))
          return MODIFIEDSINCE;
        if ("not-match".equals(codeString))
          return NOTMATCH;
        if ("full-support".equals(codeString))
          return FULLSUPPORT;
        throw new FHIRException("Unknown ConditionalReadStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTSUPPORTED: return "not-supported";
            case MODIFIEDSINCE: return "modified-since";
            case NOTMATCH: return "not-match";
            case FULLSUPPORT: return "full-support";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/conditional-read-status";
        }
        public String getDefinition() {
          switch (this) {
            case NOTSUPPORTED: return "No support for conditional deletes.";
            case MODIFIEDSINCE: return "Conditional reads are supported, but only with the If-Modified-Since HTTP Header.";
            case NOTMATCH: return "Conditional reads are supported, but only with the If-None-Match HTTP Header.";
            case FULLSUPPORT: return "Conditional reads are supported, with both If-Modified-Since and If-None-Match HTTP Headers.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTSUPPORTED: return "Not Supported";
            case MODIFIEDSINCE: return "If-Modified-Since";
            case NOTMATCH: return "If-None-Match";
            case FULLSUPPORT: return "Full Support";
            default: return "?";
          }
    }


}

