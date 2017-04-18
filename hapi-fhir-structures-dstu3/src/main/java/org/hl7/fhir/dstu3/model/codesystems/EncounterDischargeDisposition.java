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

public enum EncounterDischargeDisposition {

        /**
         * The patient was dicharged and has indicated that they are going to return home afterwards.
         */
        HOME, 
        /**
         * The patient was transferred to another healthcare facility.
         */
        OTHERHCF, 
        /**
         * The patient has been discharged into palliative care.
         */
        HOSP, 
        /**
         * The patient has been discharged into long-term care where is likely to be monitored through an ongoing episode-of-care.
         */
        LONG, 
        /**
         * The patient self discharged against medical advice.
         */
        AADVICE, 
        /**
         * The patient has deceased during this encounter.
         */
        EXP, 
        /**
         * The patient has been transferred to a psychiatric facility.
         */
        PSY, 
        /**
         * The patient was discharged and is to receive post acute care rehabilitation services.
         */
        REHAB, 
        /**
         * The patient has been discharged to a skilled nursing facility for the patient to receive additional care.
         */
        SNF, 
        /**
         * The discharge disposition has not otherwise defined.
         */
        OTH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterDischargeDisposition fromCode(String codeString) throws FHIRException {
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
        if ("snf".equals(codeString))
          return SNF;
        if ("oth".equals(codeString))
          return OTH;
        throw new FHIRException("Unknown EncounterDischargeDisposition code '"+codeString+"'");
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
            case SNF: return "snf";
            case OTH: return "oth";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/discharge-disposition";
        }
        public String getDefinition() {
          switch (this) {
            case HOME: return "The patient was dicharged and has indicated that they are going to return home afterwards.";
            case OTHERHCF: return "The patient was transferred to another healthcare facility.";
            case HOSP: return "The patient has been discharged into palliative care.";
            case LONG: return "The patient has been discharged into long-term care where is likely to be monitored through an ongoing episode-of-care.";
            case AADVICE: return "The patient self discharged against medical advice.";
            case EXP: return "The patient has deceased during this encounter.";
            case PSY: return "The patient has been transferred to a psychiatric facility.";
            case REHAB: return "The patient was discharged and is to receive post acute care rehabilitation services.";
            case SNF: return "The patient has been discharged to a skilled nursing facility for the patient to receive additional care.";
            case OTH: return "The discharge disposition has not otherwise defined.";
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
            case SNF: return "Skilled nursing facility";
            case OTH: return "Other";
            default: return "?";
          }
    }


}

