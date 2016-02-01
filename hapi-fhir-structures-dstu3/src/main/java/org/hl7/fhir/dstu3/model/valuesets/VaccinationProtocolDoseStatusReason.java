package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum VaccinationProtocolDoseStatusReason {

        /**
         * null
         */
        ADVSTORAGE, 
        /**
         * null
         */
        COLDCHBRK, 
        /**
         * null
         */
        EXPLOT, 
        /**
         * null
         */
        OUTSIDESCHED, 
        /**
         * null
         */
        PRODRECALL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VaccinationProtocolDoseStatusReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("advstorage".equals(codeString))
          return ADVSTORAGE;
        if ("coldchbrk".equals(codeString))
          return COLDCHBRK;
        if ("explot".equals(codeString))
          return EXPLOT;
        if ("outsidesched".equals(codeString))
          return OUTSIDESCHED;
        if ("prodrecall".equals(codeString))
          return PRODRECALL;
        throw new FHIRException("Unknown VaccinationProtocolDoseStatusReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADVSTORAGE: return "advstorage";
            case COLDCHBRK: return "coldchbrk";
            case EXPLOT: return "explot";
            case OUTSIDESCHED: return "outsidesched";
            case PRODRECALL: return "prodrecall";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/vaccination-protocol-dose-status-reason";
        }
        public String getDefinition() {
          switch (this) {
            case ADVSTORAGE: return "";
            case COLDCHBRK: return "";
            case EXPLOT: return "";
            case OUTSIDESCHED: return "";
            case PRODRECALL: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADVSTORAGE: return "Adverse storage condition";
            case COLDCHBRK: return "Cold chain break";
            case EXPLOT: return "Expired lot";
            case OUTSIDESCHED: return "Administered outside recommended schedule";
            case PRODRECALL: return "Product recall";
            default: return "?";
          }
    }


}

