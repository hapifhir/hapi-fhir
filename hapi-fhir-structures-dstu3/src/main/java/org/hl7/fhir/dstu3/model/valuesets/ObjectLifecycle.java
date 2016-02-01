package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ObjectLifecycle {

        /**
         * Origination / Creation
         */
        _1, 
        /**
         * Import / Copy from original
         */
        _2, 
        /**
         * Amendment
         */
        _3, 
        /**
         * Verification
         */
        _4, 
        /**
         * Translation
         */
        _5, 
        /**
         * Access / Use
         */
        _6, 
        /**
         * De-identification
         */
        _7, 
        /**
         * Aggregation, summarization, derivation
         */
        _8, 
        /**
         * Report
         */
        _9, 
        /**
         * Export / Copy to target
         */
        _10, 
        /**
         * Disclosure
         */
        _11, 
        /**
         * Receipt of disclosure
         */
        _12, 
        /**
         * Archiving
         */
        _13, 
        /**
         * Logical deletion
         */
        _14, 
        /**
         * Permanent erasure / Physical destruction
         */
        _15, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObjectLifecycle fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        if ("6".equals(codeString))
          return _6;
        if ("7".equals(codeString))
          return _7;
        if ("8".equals(codeString))
          return _8;
        if ("9".equals(codeString))
          return _9;
        if ("10".equals(codeString))
          return _10;
        if ("11".equals(codeString))
          return _11;
        if ("12".equals(codeString))
          return _12;
        if ("13".equals(codeString))
          return _13;
        if ("14".equals(codeString))
          return _14;
        if ("15".equals(codeString))
          return _15;
        throw new FHIRException("Unknown ObjectLifecycle code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _6: return "6";
            case _7: return "7";
            case _8: return "8";
            case _9: return "9";
            case _10: return "10";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/object-lifecycle";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Origination / Creation";
            case _2: return "Import / Copy from original";
            case _3: return "Amendment";
            case _4: return "Verification";
            case _5: return "Translation";
            case _6: return "Access / Use";
            case _7: return "De-identification";
            case _8: return "Aggregation, summarization, derivation";
            case _9: return "Report";
            case _10: return "Export / Copy to target";
            case _11: return "Disclosure";
            case _12: return "Receipt of disclosure";
            case _13: return "Archiving";
            case _14: return "Logical deletion";
            case _15: return "Permanent erasure / Physical destruction";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Origination / Creation";
            case _2: return "Import / Copy from original";
            case _3: return "Amendment";
            case _4: return "Verification";
            case _5: return "Translation";
            case _6: return "Access / Use";
            case _7: return "De-identification";
            case _8: return "Aggregation, summarization, derivation";
            case _9: return "Report";
            case _10: return "Export / Copy to target";
            case _11: return "Disclosure";
            case _12: return "Receipt of disclosure";
            case _13: return "Archiving";
            case _14: return "Logical deletion";
            case _15: return "Permanent erasure / Physical destruction";
            default: return "?";
          }
    }


}

