package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3Charset {

        /**
         * HL7 is indifferent to the use of this Charset.
         */
        EBCDIC, 
        /**
         * Deprecated for HL7 use.
         */
        ISO10646UCS2, 
        /**
         * Deprecated for HL7 use.
         */
        ISO10646UCS4, 
        /**
         * HL7 is indifferent to the use of this Charset.
         */
        ISO88591, 
        /**
         * HL7 is indifferent to the use of this Charset.
         */
        ISO88592, 
        /**
         * HL7 is indifferent to the use of this Charset.
         */
        ISO88595, 
        /**
         * HL7 is indifferent to the use of this Charset.
         */
        JIS2022JP, 
        /**
         * Required for HL7 use.
         */
        USASCII, 
        /**
         * HL7 is indifferent to the use of this Charset.
         */
        UTF7, 
        /**
         * Required for Unicode support.
         */
        UTF8, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Charset fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("EBCDIC".equals(codeString))
          return EBCDIC;
        if ("ISO-10646-UCS-2".equals(codeString))
          return ISO10646UCS2;
        if ("ISO-10646-UCS-4".equals(codeString))
          return ISO10646UCS4;
        if ("ISO-8859-1".equals(codeString))
          return ISO88591;
        if ("ISO-8859-2".equals(codeString))
          return ISO88592;
        if ("ISO-8859-5".equals(codeString))
          return ISO88595;
        if ("JIS-2022-JP".equals(codeString))
          return JIS2022JP;
        if ("US-ASCII".equals(codeString))
          return USASCII;
        if ("UTF-7".equals(codeString))
          return UTF7;
        if ("UTF-8".equals(codeString))
          return UTF8;
        throw new FHIRException("Unknown V3Charset code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EBCDIC: return "EBCDIC";
            case ISO10646UCS2: return "ISO-10646-UCS-2";
            case ISO10646UCS4: return "ISO-10646-UCS-4";
            case ISO88591: return "ISO-8859-1";
            case ISO88592: return "ISO-8859-2";
            case ISO88595: return "ISO-8859-5";
            case JIS2022JP: return "JIS-2022-JP";
            case USASCII: return "US-ASCII";
            case UTF7: return "UTF-7";
            case UTF8: return "UTF-8";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/Charset";
        }
        public String getDefinition() {
          switch (this) {
            case EBCDIC: return "HL7 is indifferent to the use of this Charset.";
            case ISO10646UCS2: return "Deprecated for HL7 use.";
            case ISO10646UCS4: return "Deprecated for HL7 use.";
            case ISO88591: return "HL7 is indifferent to the use of this Charset.";
            case ISO88592: return "HL7 is indifferent to the use of this Charset.";
            case ISO88595: return "HL7 is indifferent to the use of this Charset.";
            case JIS2022JP: return "HL7 is indifferent to the use of this Charset.";
            case USASCII: return "Required for HL7 use.";
            case UTF7: return "HL7 is indifferent to the use of this Charset.";
            case UTF8: return "Required for Unicode support.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EBCDIC: return "EBCDIC";
            case ISO10646UCS2: return "ISO-10646-UCS-2";
            case ISO10646UCS4: return "ISO-10646-UCS-4";
            case ISO88591: return "ISO-8859-1";
            case ISO88592: return "ISO-8859-2";
            case ISO88595: return "ISO-8859-5";
            case JIS2022JP: return "JIS-2022-JP";
            case USASCII: return "US-ASCII";
            case UTF7: return "UTF-7";
            case UTF8: return "UTF-8";
            default: return "?";
          }
    }


}

