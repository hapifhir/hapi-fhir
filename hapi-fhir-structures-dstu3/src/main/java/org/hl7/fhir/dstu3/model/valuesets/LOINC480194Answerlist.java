package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum LOINC480194Answerlist {

        /**
         * Wild type
         */
        LA96581, 
        /**
         * Deletion
         */
        LA66923, 
        /**
         * Duplication
         */
        LA66865, 
        /**
         * Insertion
         */
        LA66873, 
        /**
         * Insertion/Deletion
         */
        LA66881, 
        /**
         * Inversion
         */
        LA66899, 
        /**
         * Substitution
         */
        LA66907, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LOINC480194Answerlist fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("LA9658-1".equals(codeString))
          return LA96581;
        if ("LA6692-3".equals(codeString))
          return LA66923;
        if ("LA6686-5".equals(codeString))
          return LA66865;
        if ("LA6687-3".equals(codeString))
          return LA66873;
        if ("LA6688-1".equals(codeString))
          return LA66881;
        if ("LA6689-9".equals(codeString))
          return LA66899;
        if ("LA6690-7".equals(codeString))
          return LA66907;
        throw new FHIRException("Unknown LOINC480194Answerlist code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LA96581: return "LA9658-1";
            case LA66923: return "LA6692-3";
            case LA66865: return "LA6686-5";
            case LA66873: return "LA6687-3";
            case LA66881: return "LA6688-1";
            case LA66899: return "LA6689-9";
            case LA66907: return "LA6690-7";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/LOINC-48019-4-answerlist";
        }
        public String getDefinition() {
          switch (this) {
            case LA96581: return "Wild type";
            case LA66923: return "Deletion";
            case LA66865: return "Duplication";
            case LA66873: return "Insertion";
            case LA66881: return "Insertion/Deletion";
            case LA66899: return "Inversion";
            case LA66907: return "Substitution";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LA96581: return "Wild type";
            case LA66923: return "Deletion";
            case LA66865: return "Duplication";
            case LA66873: return "Insertion";
            case LA66881: return "Insertion/Deletion";
            case LA66899: return "Inversion";
            case LA66907: return "Substitution";
            default: return "?";
          }
    }


}

