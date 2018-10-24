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

public enum VerificationresultStatus {

        /**
         * ***TODO***
         */
        ATTESTED, 
        /**
         * ***TODO***
         */
        VALIDATED, 
        /**
         * ***TODO***
         */
        INPROCESS, 
        /**
         * ***TODO***
         */
        REQREVALID, 
        /**
         * ***TODO***
         */
        VALFAIL, 
        /**
         * ***TODO***
         */
        REVALFAIL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VerificationresultStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("attested".equals(codeString))
          return ATTESTED;
        if ("validated".equals(codeString))
          return VALIDATED;
        if ("in-process".equals(codeString))
          return INPROCESS;
        if ("req-revalid".equals(codeString))
          return REQREVALID;
        if ("val-fail".equals(codeString))
          return VALFAIL;
        if ("reval-fail".equals(codeString))
          return REVALFAIL;
        throw new FHIRException("Unknown VerificationresultStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ATTESTED: return "attested";
            case VALIDATED: return "validated";
            case INPROCESS: return "in-process";
            case REQREVALID: return "req-revalid";
            case VALFAIL: return "val-fail";
            case REVALFAIL: return "reval-fail";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/CodeSystem/status";
        }
        public String getDefinition() {
          switch (this) {
            case ATTESTED: return "***TODO***";
            case VALIDATED: return "***TODO***";
            case INPROCESS: return "***TODO***";
            case REQREVALID: return "***TODO***";
            case VALFAIL: return "***TODO***";
            case REVALFAIL: return "***TODO***";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ATTESTED: return "Attested";
            case VALIDATED: return "Validated";
            case INPROCESS: return "In process";
            case REQREVALID: return "Requires revalidation";
            case VALFAIL: return "Validation failed";
            case REVALFAIL: return "Re-Validation failed";
            default: return "?";
          }
    }


}

