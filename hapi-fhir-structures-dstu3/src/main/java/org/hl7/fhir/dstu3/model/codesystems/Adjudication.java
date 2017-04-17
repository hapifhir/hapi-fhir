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

public enum Adjudication {

        /**
         * Total submitted
         */
        TOTAL, 
        /**
         * Patient Co-Payment
         */
        COPAY, 
        /**
         * Amount of the change which is considered for adjudication
         */
        ELIGIBLE, 
        /**
         * Amount deducted from the eligible amount prior to adjudication
         */
        DEDUCTIBLE, 
        /**
         * Eligible Percentage
         */
        ELIGPERCENT, 
        /**
         * Emergency Department
         */
        TAX, 
        /**
         * Amount payable under the coverage
         */
        BENEFIT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Adjudication fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("total".equals(codeString))
          return TOTAL;
        if ("copay".equals(codeString))
          return COPAY;
        if ("eligible".equals(codeString))
          return ELIGIBLE;
        if ("deductible".equals(codeString))
          return DEDUCTIBLE;
        if ("eligpercent".equals(codeString))
          return ELIGPERCENT;
        if ("tax".equals(codeString))
          return TAX;
        if ("benefit".equals(codeString))
          return BENEFIT;
        throw new FHIRException("Unknown Adjudication code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TOTAL: return "total";
            case COPAY: return "copay";
            case ELIGIBLE: return "eligible";
            case DEDUCTIBLE: return "deductible";
            case ELIGPERCENT: return "eligpercent";
            case TAX: return "tax";
            case BENEFIT: return "benefit";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/adjudication";
        }
        public String getDefinition() {
          switch (this) {
            case TOTAL: return "Total submitted";
            case COPAY: return "Patient Co-Payment";
            case ELIGIBLE: return "Amount of the change which is considered for adjudication";
            case DEDUCTIBLE: return "Amount deducted from the eligible amount prior to adjudication";
            case ELIGPERCENT: return "Eligible Percentage";
            case TAX: return "Emergency Department";
            case BENEFIT: return "Amount payable under the coverage";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TOTAL: return "Total";
            case COPAY: return "CoPay";
            case ELIGIBLE: return "Eligible Amount";
            case DEDUCTIBLE: return "Deductable";
            case ELIGPERCENT: return "Eligible %";
            case TAX: return "Emergency Department";
            case BENEFIT: return "Benefit Amount";
            default: return "?";
          }
    }


}

