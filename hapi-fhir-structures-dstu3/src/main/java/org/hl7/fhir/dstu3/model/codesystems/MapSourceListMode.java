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

public enum MapSourceListMode {

        /**
         * Only process this rule for the first in the list
         */
        FIRST, 
        /**
         * Process this rule for all but the first
         */
        NOTFIRST, 
        /**
         * Only process this rule for the last in the list
         */
        LAST, 
        /**
         * Process this rule for all but the last
         */
        NOTLAST, 
        /**
         * Only process this rule is there is only item
         */
        ONLYONE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MapSourceListMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("first".equals(codeString))
          return FIRST;
        if ("not_first".equals(codeString))
          return NOTFIRST;
        if ("last".equals(codeString))
          return LAST;
        if ("not_last".equals(codeString))
          return NOTLAST;
        if ("only_one".equals(codeString))
          return ONLYONE;
        throw new FHIRException("Unknown MapSourceListMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FIRST: return "first";
            case NOTFIRST: return "not_first";
            case LAST: return "last";
            case NOTLAST: return "not_last";
            case ONLYONE: return "only_one";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/map-source-list-mode";
        }
        public String getDefinition() {
          switch (this) {
            case FIRST: return "Only process this rule for the first in the list";
            case NOTFIRST: return "Process this rule for all but the first";
            case LAST: return "Only process this rule for the last in the list";
            case NOTLAST: return "Process this rule for all but the last";
            case ONLYONE: return "Only process this rule is there is only item";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FIRST: return "First";
            case NOTFIRST: return "All but the first";
            case LAST: return "Last";
            case NOTLAST: return "All but the last";
            case ONLYONE: return "Enforce only one";
            default: return "?";
          }
    }


}

