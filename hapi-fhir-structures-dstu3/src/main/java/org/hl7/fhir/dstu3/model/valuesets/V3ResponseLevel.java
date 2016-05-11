package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ResponseLevel {

        /**
         * Respond with exceptions and a notification of completion
         */
        C, 
        /**
         * Respond with exceptions, completion, modifications and include more detail information (if applicable)
         */
        D, 
        /**
         * Respond with exceptions only
         */
        E, 
        /**
         * Respond with exceptions, completion, and modification with detail (as above), and send positive confirmations even if no modifications are being made.
         */
        F, 
        /**
         * Respond only with message level acknowledgements, i.e., only notify acceptance or rejection of the message, do not include any application-level detail
         */
        N, 
        /**
         * Respond with exceptions, completions and modifications or revisions done before completion
         */
        R, 
        /**
         * Do not send any kind of response
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ResponseLevel fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("C".equals(codeString))
          return C;
        if ("D".equals(codeString))
          return D;
        if ("E".equals(codeString))
          return E;
        if ("F".equals(codeString))
          return F;
        if ("N".equals(codeString))
          return N;
        if ("R".equals(codeString))
          return R;
        if ("X".equals(codeString))
          return X;
        throw new FHIRException("Unknown V3ResponseLevel code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case C: return "C";
            case D: return "D";
            case E: return "E";
            case F: return "F";
            case N: return "N";
            case R: return "R";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ResponseLevel";
        }
        public String getDefinition() {
          switch (this) {
            case C: return "Respond with exceptions and a notification of completion";
            case D: return "Respond with exceptions, completion, modifications and include more detail information (if applicable)";
            case E: return "Respond with exceptions only";
            case F: return "Respond with exceptions, completion, and modification with detail (as above), and send positive confirmations even if no modifications are being made.";
            case N: return "Respond only with message level acknowledgements, i.e., only notify acceptance or rejection of the message, do not include any application-level detail";
            case R: return "Respond with exceptions, completions and modifications or revisions done before completion";
            case X: return "Do not send any kind of response";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case C: return "completion";
            case D: return "detail";
            case E: return "exception";
            case F: return "confirmation";
            case N: return "message-control";
            case R: return "modification";
            case X: return "none";
            default: return "?";
          }
    }


}

