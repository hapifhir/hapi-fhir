package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

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

