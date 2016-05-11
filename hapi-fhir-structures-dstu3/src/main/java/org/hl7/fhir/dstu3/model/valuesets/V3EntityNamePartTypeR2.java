package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3EntityNamePartTypeR2 {

        /**
         * Description:A delimiter has no meaning other than being literally printed in this name representation. A delimiter has no implicit leading and trailing white space.
         */
        DEL, 
        /**
         * Description:Family name, this is the name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
         */
        FAM, 
        /**
         * Description:Given name. Note: don't call it "first name" since the given names do not always come first.
         */
        GIV, 
        /**
         * Description:Part of the name that is acquired as a title due to academic, legal, employment or nobility status etc.

                        
                           Note:Title name parts include name parts that come after the name such as qualifications, even if they are not always considered to be titles.
         */
        TITLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityNamePartTypeR2 fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("DEL".equals(codeString))
          return DEL;
        if ("FAM".equals(codeString))
          return FAM;
        if ("GIV".equals(codeString))
          return GIV;
        if ("TITLE".equals(codeString))
          return TITLE;
        throw new FHIRException("Unknown V3EntityNamePartTypeR2 code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DEL: return "DEL";
            case FAM: return "FAM";
            case GIV: return "GIV";
            case TITLE: return "TITLE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityNamePartTypeR2";
        }
        public String getDefinition() {
          switch (this) {
            case DEL: return "Description:A delimiter has no meaning other than being literally printed in this name representation. A delimiter has no implicit leading and trailing white space.";
            case FAM: return "Description:Family name, this is the name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.";
            case GIV: return "Description:Given name. Note: don't call it \"first name\" since the given names do not always come first.";
            case TITLE: return "Description:Part of the name that is acquired as a title due to academic, legal, employment or nobility status etc.\r\n\n                        \n                           Note:Title name parts include name parts that come after the name such as qualifications, even if they are not always considered to be titles.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DEL: return "delimiter";
            case FAM: return "family";
            case GIV: return "given";
            case TITLE: return "title";
            default: return "?";
          }
    }


}

