package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum LOINC480020Answerlist {

        /**
         * Germline
         */
        LA66832, 
        /**
         * Somatic
         */
        LA66840, 
        /**
         * Prenatal
         */
        LA66857, 
        /**
         * Likely Germline
         */
        LA181943, 
        /**
         * Likely Somatic
         */
        LA181950, 
        /**
         * Likely Prenatal
         */
        LA181968, 
        /**
         * Unknown Genomic Origin
         */
        LA181976, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LOINC480020Answerlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("LA6683-2".equals(codeString))
          return LA66832;
        if ("LA6684-0".equals(codeString))
          return LA66840;
        if ("LA6685-7".equals(codeString))
          return LA66857;
        if ("LA18194-3".equals(codeString))
          return LA181943;
        if ("LA18195-0".equals(codeString))
          return LA181950;
        if ("LA18196-8".equals(codeString))
          return LA181968;
        if ("LA18197-6".equals(codeString))
          return LA181976;
        throw new FHIRException("Unknown LOINC480020Answerlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LA66832: return "LA6683-2";
            case LA66840: return "LA6684-0";
            case LA66857: return "LA6685-7";
            case LA181943: return "LA18194-3";
            case LA181950: return "LA18195-0";
            case LA181968: return "LA18196-8";
            case LA181976: return "LA18197-6";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/LOINC-48002-0-answerlist";
        }
        public String getDefinition() {
          switch (this) {
            case LA66832: return "Germline";
            case LA66840: return "Somatic";
            case LA66857: return "Prenatal";
            case LA181943: return "Likely Germline";
            case LA181950: return "Likely Somatic";
            case LA181968: return "Likely Prenatal";
            case LA181976: return "Unknown Genomic Origin";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LA66832: return "Germline";
            case LA66840: return "Somatic";
            case LA66857: return "Prenatal";
            case LA181943: return "Likely Germline";
            case LA181950: return "Likely Somatic";
            case LA181968: return "Likely Prenatal";
            case LA181976: return "Unknown Genomic Origin";
            default: return "?";
          }
    }


}

