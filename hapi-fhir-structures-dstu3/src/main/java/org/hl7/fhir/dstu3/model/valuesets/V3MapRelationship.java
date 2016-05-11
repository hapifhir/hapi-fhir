package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3MapRelationship {

        /**
         * The first concept is at a more abstract level than the second concept.  For example, Hepatitis is broader than Hepatitis A, and endocrine disease is broader than Diabetes Mellitus.  Broader than is the opposite of the narrower than relationship.
         */
        BT, 
        /**
         * The two concepts have identical meaning.
         */
        E, 
        /**
         * The first concept is at a more detailed level than the second concept.  For example, Pennicillin G is narrower than Pennicillin, and vellus hair is narrower than hair.  Narrower than is the opposite of broader than.
         */
        NT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3MapRelationship fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BT".equals(codeString))
          return BT;
        if ("E".equals(codeString))
          return E;
        if ("NT".equals(codeString))
          return NT;
        throw new FHIRException("Unknown V3MapRelationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BT: return "BT";
            case E: return "E";
            case NT: return "NT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/MapRelationship";
        }
        public String getDefinition() {
          switch (this) {
            case BT: return "The first concept is at a more abstract level than the second concept.  For example, Hepatitis is broader than Hepatitis A, and endocrine disease is broader than Diabetes Mellitus.  Broader than is the opposite of the narrower than relationship.";
            case E: return "The two concepts have identical meaning.";
            case NT: return "The first concept is at a more detailed level than the second concept.  For example, Pennicillin G is narrower than Pennicillin, and vellus hair is narrower than hair.  Narrower than is the opposite of broader than.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BT: return "Broader Than";
            case E: return "Exact";
            case NT: return "Narrower Than";
            default: return "?";
          }
    }


}

