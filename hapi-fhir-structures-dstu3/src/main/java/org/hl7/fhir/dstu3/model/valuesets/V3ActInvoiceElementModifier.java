package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3ActInvoiceElementModifier {

        /**
         * Electronic form with supporting information to follow.
         */
        EFORM, 
        /**
         * Fax with supporting information to follow.
         */
        FAX, 
        /**
         * Represents the last invoice from the perspective of the provider.
         */
        LINV, 
        /**
         * Paper documentation (or other physical format) with supporting information to follow.
         */
        PAPER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActInvoiceElementModifier fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("EFORM".equals(codeString))
          return EFORM;
        if ("FAX".equals(codeString))
          return FAX;
        if ("LINV".equals(codeString))
          return LINV;
        if ("PAPER".equals(codeString))
          return PAPER;
        throw new FHIRException("Unknown V3ActInvoiceElementModifier code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EFORM: return "EFORM";
            case FAX: return "FAX";
            case LINV: return "LINV";
            case PAPER: return "PAPER";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActInvoiceElementModifier";
        }
        public String getDefinition() {
          switch (this) {
            case EFORM: return "Electronic form with supporting information to follow.";
            case FAX: return "Fax with supporting information to follow.";
            case LINV: return "Represents the last invoice from the perspective of the provider.";
            case PAPER: return "Paper documentation (or other physical format) with supporting information to follow.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EFORM: return "Electronic Form To Follow";
            case FAX: return "Fax To Follow";
            case LINV: return "Last Invoice";
            case PAPER: return "Paper Documentation To Follow";
            default: return "?";
          }
    }


}

