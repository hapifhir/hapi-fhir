package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum EncounterDischargeDisposition {

        /**
         * null
         */
        HOME, 
        /**
         * null
         */
        OTHERHCF, 
        /**
         * null
         */
        HOSP, 
        /**
         * null
         */
        LONG, 
        /**
         * null
         */
        AADVICE, 
        /**
         * null
         */
        EXP, 
        /**
         * null
         */
        PSY, 
        /**
         * null
         */
        REHAB, 
        /**
         * null
         */
        OTH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterDischargeDisposition fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return HOME;
        if ("other-hcf".equals(codeString))
          return OTHERHCF;
        if ("hosp".equals(codeString))
          return HOSP;
        if ("long".equals(codeString))
          return LONG;
        if ("aadvice".equals(codeString))
          return AADVICE;
        if ("exp".equals(codeString))
          return EXP;
        if ("psy".equals(codeString))
          return PSY;
        if ("rehab".equals(codeString))
          return REHAB;
        if ("oth".equals(codeString))
          return OTH;
        throw new Exception("Unknown EncounterDischargeDisposition code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HOME: return "home";
            case OTHERHCF: return "other-hcf";
            case HOSP: return "hosp";
            case LONG: return "long";
            case AADVICE: return "aadvice";
            case EXP: return "exp";
            case PSY: return "psy";
            case REHAB: return "rehab";
            case OTH: return "oth";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/discharge-disposition";
        }
        public String getDefinition() {
          switch (this) {
            case HOME: return "";
            case OTHERHCF: return "";
            case HOSP: return "";
            case LONG: return "";
            case AADVICE: return "";
            case EXP: return "";
            case PSY: return "";
            case REHAB: return "";
            case OTH: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HOME: return "Home";
            case OTHERHCF: return "Other healthcare facility";
            case HOSP: return "Hospice";
            case LONG: return "Long-term care";
            case AADVICE: return "Left against advice";
            case EXP: return "Expired";
            case PSY: return "Psychiatric hospital";
            case REHAB: return "Rehabilitation";
            case OTH: return "Other";
            default: return "?";
          }
    }


}

