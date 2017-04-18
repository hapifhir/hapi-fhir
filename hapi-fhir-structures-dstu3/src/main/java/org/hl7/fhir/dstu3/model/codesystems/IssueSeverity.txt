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

public enum IssueSeverity {

        /**
         * The issue caused the action to fail, and no further checking could be performed.
         */
        FATAL, 
        /**
         * The issue is sufficiently important to cause the action to fail.
         */
        ERROR, 
        /**
         * The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired.
         */
        WARNING, 
        /**
         * The issue has no relation to the degree of success of the action.
         */
        INFORMATION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static IssueSeverity fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("fatal".equals(codeString))
          return FATAL;
        if ("error".equals(codeString))
          return ERROR;
        if ("warning".equals(codeString))
          return WARNING;
        if ("information".equals(codeString))
          return INFORMATION;
        throw new FHIRException("Unknown IssueSeverity code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FATAL: return "fatal";
            case ERROR: return "error";
            case WARNING: return "warning";
            case INFORMATION: return "information";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/issue-severity";
        }
        public String getDefinition() {
          switch (this) {
            case FATAL: return "The issue caused the action to fail, and no further checking could be performed.";
            case ERROR: return "The issue is sufficiently important to cause the action to fail.";
            case WARNING: return "The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired.";
            case INFORMATION: return "The issue has no relation to the degree of success of the action.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FATAL: return "Fatal";
            case ERROR: return "Error";
            case WARNING: return "Warning";
            case INFORMATION: return "Information";
            default: return "?";
          }
    }


}

