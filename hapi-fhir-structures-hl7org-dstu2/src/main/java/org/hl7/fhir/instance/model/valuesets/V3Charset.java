package org.hl7.fhir.instance.model.valuesets;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


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
        public static V3Charset fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown V3Charset code '"+codeString+"'");
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

