package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum Additionalmaterials {

        /**
         * XRay
         */
        XRAY, 
        /**
         * Image
         */
        IMAGE, 
        /**
         * Email
         */
        EMAIL, 
        /**
         * Model
         */
        MODEL, 
        /**
         * Document
         */
        DOCUMENT, 
        /**
         * Other
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Additionalmaterials fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xray".equals(codeString))
          return XRAY;
        if ("image".equals(codeString))
          return IMAGE;
        if ("email".equals(codeString))
          return EMAIL;
        if ("model".equals(codeString))
          return MODEL;
        if ("document".equals(codeString))
          return DOCUMENT;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown Additionalmaterials code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case XRAY: return "xray";
            case IMAGE: return "image";
            case EMAIL: return "email";
            case MODEL: return "model";
            case DOCUMENT: return "document";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/additionalmaterials";
        }
        public String getDefinition() {
          switch (this) {
            case XRAY: return "XRay";
            case IMAGE: return "Image";
            case EMAIL: return "Email";
            case MODEL: return "Model";
            case DOCUMENT: return "Document";
            case OTHER: return "Other";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case XRAY: return "XRay";
            case IMAGE: return "Image";
            case EMAIL: return "Email";
            case MODEL: return "Model";
            case DOCUMENT: return "Document";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

