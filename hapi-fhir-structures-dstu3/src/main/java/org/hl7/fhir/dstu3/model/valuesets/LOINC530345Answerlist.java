package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum LOINC530345Answerlist {

        /**
         * Heteroplasmic
         */
        LA67038, 
        /**
         * Homoplasmic
         */
        LA67046, 
        /**
         * Homozygous
         */
        LA67053, 
        /**
         * Heterozygous
         */
        LA67061, 
        /**
         * Hemizygous
         */
        LA67079, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LOINC530345Answerlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("LA6703-8".equals(codeString))
          return LA67038;
        if ("LA6704-6".equals(codeString))
          return LA67046;
        if ("LA6705-3".equals(codeString))
          return LA67053;
        if ("LA6706-1".equals(codeString))
          return LA67061;
        if ("LA6707-9".equals(codeString))
          return LA67079;
        throw new FHIRException("Unknown LOINC530345Answerlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LA67038: return "LA6703-8";
            case LA67046: return "LA6704-6";
            case LA67053: return "LA6705-3";
            case LA67061: return "LA6706-1";
            case LA67079: return "LA6707-9";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/LOINC-53034-5-answerlist";
        }
        public String getDefinition() {
          switch (this) {
            case LA67038: return "Heteroplasmic";
            case LA67046: return "Homoplasmic";
            case LA67053: return "Homozygous";
            case LA67061: return "Heterozygous";
            case LA67079: return "Hemizygous";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LA67038: return "Heteroplasmic";
            case LA67046: return "Homoplasmic";
            case LA67053: return "Homozygous";
            case LA67061: return "Heterozygous";
            case LA67079: return "Hemizygous";
            default: return "?";
          }
    }


}

