package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum LOINC530378Answerlist {

        /**
         * Pathogenic
         */
        LA66683, 
        /**
         * Presumed pathogenic
         */
        LA66691, 
        /**
         * Unknown significance
         */
        LA66824, 
        /**
         * Benign
         */
        LA66758, 
        /**
         * Presumed benign
         */
        LA66741, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LOINC530378Answerlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("LA6668-3".equals(codeString))
          return LA66683;
        if ("LA6669-1".equals(codeString))
          return LA66691;
        if ("LA6682-4".equals(codeString))
          return LA66824;
        if ("LA6675-8".equals(codeString))
          return LA66758;
        if ("LA6674-1".equals(codeString))
          return LA66741;
        throw new FHIRException("Unknown LOINC530378Answerlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LA66683: return "LA6668-3";
            case LA66691: return "LA6669-1";
            case LA66824: return "LA6682-4";
            case LA66758: return "LA6675-8";
            case LA66741: return "LA6674-1";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/LOINC-53037-8-answerlist";
        }
        public String getDefinition() {
          switch (this) {
            case LA66683: return "Pathogenic";
            case LA66691: return "Presumed pathogenic";
            case LA66824: return "Unknown significance";
            case LA66758: return "Benign";
            case LA66741: return "Presumed benign";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LA66683: return "Pathogenic";
            case LA66691: return "Presumed pathogenic";
            case LA66824: return "Unknown significance";
            case LA66758: return "Benign";
            case LA66741: return "Presumed benign";
            default: return "?";
          }
    }


}

