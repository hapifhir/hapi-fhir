package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Ruleset {

        /**
         * null
         */
        X124010, 
        /**
         * null
         */
        X125010, 
        /**
         * null
         */
        X127010, 
        /**
         * null
         */
        CDANETV2, 
        /**
         * null
         */
        CDANETV4, 
        /**
         * null
         */
        CPHA3, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Ruleset fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("x12-4010".equals(codeString))
          return X124010;
        if ("x12-5010".equals(codeString))
          return X125010;
        if ("x12-7010".equals(codeString))
          return X127010;
        if ("cdanet-v2".equals(codeString))
          return CDANETV2;
        if ("cdanet-v4".equals(codeString))
          return CDANETV4;
        if ("cpha-3".equals(codeString))
          return CPHA3;
        throw new FHIRException("Unknown Ruleset code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case X124010: return "x12-4010";
            case X125010: return "x12-5010";
            case X127010: return "x12-7010";
            case CDANETV2: return "cdanet-v2";
            case CDANETV4: return "cdanet-v4";
            case CPHA3: return "cpha-3";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ruleset";
        }
        public String getDefinition() {
          switch (this) {
            case X124010: return "";
            case X125010: return "";
            case X127010: return "";
            case CDANETV2: return "";
            case CDANETV4: return "";
            case CPHA3: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case X124010: return "x12-4010";
            case X125010: return "x12-5010";
            case X127010: return "x12-7010";
            case CDANETV2: return "cdanet-v2";
            case CDANETV4: return "cdanet-v4";
            case CPHA3: return "cpha-3";
            default: return "?";
          }
    }


}

