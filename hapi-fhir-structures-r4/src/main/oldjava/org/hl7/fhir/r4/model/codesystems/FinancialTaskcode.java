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

public enum FinancialTaskcode {

        /**
         * Cancel or reverse a resource, such as a claim or preauthorization, which is in-process or complete.
         */
        CANCEL, 
        /**
         * Retrieve selected or all queued resources or messages.
         */
        POLL, 
        /**
         * Release any reserved funds or material obligations associated with a resource. For example, any unused but reserved funds or treatment allowance associated with a preauthorization once treatment is complete.
         */
        RELEASE, 
        /**
         * Indication that the processing of a resource, such as a claim, for some or all of the required work is now being requested.
         */
        REPROCESS, 
        /**
         * Check on the processing status of a resource such as the adjudication of a claim.
         */
        STATUS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FinancialTaskcode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cancel".equals(codeString))
          return CANCEL;
        if ("poll".equals(codeString))
          return POLL;
        if ("release".equals(codeString))
          return RELEASE;
        if ("reprocess".equals(codeString))
          return REPROCESS;
        if ("status".equals(codeString))
          return STATUS;
        throw new FHIRException("Unknown FinancialTaskcode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CANCEL: return "cancel";
            case POLL: return "poll";
            case RELEASE: return "release";
            case REPROCESS: return "reprocess";
            case STATUS: return "status";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/financialtaskcode";
        }
        public String getDefinition() {
          switch (this) {
            case CANCEL: return "Cancel or reverse a resource, such as a claim or preauthorization, which is in-process or complete.";
            case POLL: return "Retrieve selected or all queued resources or messages.";
            case RELEASE: return "Release any reserved funds or material obligations associated with a resource. For example, any unused but reserved funds or treatment allowance associated with a preauthorization once treatment is complete.";
            case REPROCESS: return "Indication that the processing of a resource, such as a claim, for some or all of the required work is now being requested.";
            case STATUS: return "Check on the processing status of a resource such as the adjudication of a claim.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CANCEL: return "Cancel";
            case POLL: return "Poll";
            case RELEASE: return "Release";
            case REPROCESS: return "Reprocess";
            case STATUS: return "Status check";
            default: return "?";
          }
    }


}

