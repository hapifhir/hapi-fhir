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

public enum ReportActionResultCodes {

        /**
         * The action was successful.
         */
        PASS, 
        /**
         * The action was skipped.
         */
        SKIP, 
        /**
         * The action failed.
         */
        FAIL, 
        /**
         * The action passed but with warnings.
         */
        WARNING, 
        /**
         * The action encountered a fatal error and the engine was unable to process.
         */
        ERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ReportActionResultCodes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("pass".equals(codeString))
          return PASS;
        if ("skip".equals(codeString))
          return SKIP;
        if ("fail".equals(codeString))
          return FAIL;
        if ("warning".equals(codeString))
          return WARNING;
        if ("error".equals(codeString))
          return ERROR;
        throw new FHIRException("Unknown ReportActionResultCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PASS: return "pass";
            case SKIP: return "skip";
            case FAIL: return "fail";
            case WARNING: return "warning";
            case ERROR: return "error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/report-action-result-codes";
        }
        public String getDefinition() {
          switch (this) {
            case PASS: return "The action was successful.";
            case SKIP: return "The action was skipped.";
            case FAIL: return "The action failed.";
            case WARNING: return "The action passed but with warnings.";
            case ERROR: return "The action encountered a fatal error and the engine was unable to process.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PASS: return "Pass";
            case SKIP: return "Skip";
            case FAIL: return "Fail";
            case WARNING: return "Warning";
            case ERROR: return "Error";
            default: return "?";
          }
    }


}

