package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum XdsRelationshipType {

        /**
         * A separate XDS document that references a prior document, and may extend or alter the observations in the prior document.
         */
        APND, 
        /**
         * A new version of an existing document.
         */
        RPLC, 
        /**
         * A transformed document is derived by a machine translation from some other format.
         */
        XFRM, 
        /**
         * Both a XFRM and a RPLC relationship.
         */
        XFRMRPLC, 
        /**
         * This document signs the target document.
         */
        SIGNS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static XdsRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("APND".equals(codeString))
          return APND;
        if ("RPLC".equals(codeString))
          return RPLC;
        if ("XFRM".equals(codeString))
          return XFRM;
        if ("XFRM_RPLC".equals(codeString))
          return XFRMRPLC;
        if ("signs".equals(codeString))
          return SIGNS;
        throw new FHIRException("Unknown XdsRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case APND: return "APND";
            case RPLC: return "RPLC";
            case XFRM: return "XFRM";
            case XFRMRPLC: return "XFRM_RPLC";
            case SIGNS: return "signs";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/xds-relationship-type";
        }
        public String getDefinition() {
          switch (this) {
            case APND: return "A separate XDS document that references a prior document, and may extend or alter the observations in the prior document.";
            case RPLC: return "A new version of an existing document.";
            case XFRM: return "A transformed document is derived by a machine translation from some other format.";
            case XFRMRPLC: return "Both a XFRM and a RPLC relationship.";
            case SIGNS: return "This document signs the target document.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case APND: return "APND";
            case RPLC: return "RPLC";
            case XFRM: return "XFRM";
            case XFRMRPLC: return "XFRM_RPLC";
            case SIGNS: return "Signs";
            default: return "?";
          }
    }


}

