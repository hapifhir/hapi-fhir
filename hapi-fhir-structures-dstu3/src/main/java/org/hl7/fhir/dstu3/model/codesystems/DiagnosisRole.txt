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

public enum DiagnosisRole {

        /**
         * null
         */
        AD, 
        /**
         * null
         */
        DD, 
        /**
         * null
         */
        CC, 
        /**
         * null
         */
        CM, 
        /**
         * null
         */
        PREOP, 
        /**
         * null
         */
        POSTOP, 
        /**
         * null
         */
        BILLING, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DiagnosisRole fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AD".equals(codeString))
          return AD;
        if ("DD".equals(codeString))
          return DD;
        if ("CC".equals(codeString))
          return CC;
        if ("CM".equals(codeString))
          return CM;
        if ("pre-op".equals(codeString))
          return PREOP;
        if ("post-op".equals(codeString))
          return POSTOP;
        if ("billing".equals(codeString))
          return BILLING;
        throw new FHIRException("Unknown DiagnosisRole code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AD: return "AD";
            case DD: return "DD";
            case CC: return "CC";
            case CM: return "CM";
            case PREOP: return "pre-op";
            case POSTOP: return "post-op";
            case BILLING: return "billing";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/diagnosis-role";
        }
        public String getDefinition() {
          switch (this) {
            case AD: return "";
            case DD: return "";
            case CC: return "";
            case CM: return "";
            case PREOP: return "";
            case POSTOP: return "";
            case BILLING: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AD: return "Admission diagnosis";
            case DD: return "Discharge diagnosis";
            case CC: return "Chief complaint";
            case CM: return "Comorbidity diagnosis";
            case PREOP: return "pre-op diagnosis";
            case POSTOP: return "post-op diagnosis";
            case BILLING: return "Billing";
            default: return "?";
          }
    }


}

