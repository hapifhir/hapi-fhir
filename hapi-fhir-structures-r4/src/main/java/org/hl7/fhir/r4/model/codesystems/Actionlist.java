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

public enum Actionlist {

        /**
         * Cancel, reverse or nullify the target resource.
         */
        CANCEL, 
        /**
         * Check for previously un-read/ not-retrieved resources.
         */
        POLL, 
        /**
         * Re-process the target resource.
         */
        REPROCESS, 
        /**
         * Retrieve the processing status of the target resource.
         */
        STATUS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Actionlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cancel".equals(codeString))
          return CANCEL;
        if ("poll".equals(codeString))
          return POLL;
        if ("reprocess".equals(codeString))
          return REPROCESS;
        if ("status".equals(codeString))
          return STATUS;
        throw new FHIRException("Unknown Actionlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CANCEL: return "cancel";
            case POLL: return "poll";
            case REPROCESS: return "reprocess";
            case STATUS: return "status";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/actionlist";
        }
        public String getDefinition() {
          switch (this) {
            case CANCEL: return "Cancel, reverse or nullify the target resource.";
            case POLL: return "Check for previously un-read/ not-retrieved resources.";
            case REPROCESS: return "Re-process the target resource.";
            case STATUS: return "Retrieve the processing status of the target resource.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CANCEL: return "Cancel, Reverse or Nullify";
            case POLL: return "Poll";
            case REPROCESS: return "Re-Process";
            case STATUS: return "Status Check";
            default: return "?";
          }
    }


}

