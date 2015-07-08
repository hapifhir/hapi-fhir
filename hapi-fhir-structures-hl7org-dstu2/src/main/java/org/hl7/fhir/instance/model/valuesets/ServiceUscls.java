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

// Generated on Wed, Jul 8, 2015 17:35-0400 for FHIR v0.5.0


public enum ServiceUscls {

        /**
         * null
         */
        _1101, 
        /**
         * null
         */
        _1102, 
        /**
         * null
         */
        _1103, 
        /**
         * null
         */
        _1201, 
        /**
         * null
         */
        _1205, 
        /**
         * null
         */
        _2101, 
        /**
         * null
         */
        _2102, 
        /**
         * null
         */
        _2141, 
        /**
         * null
         */
        _2601, 
        /**
         * null
         */
        _11101, 
        /**
         * null
         */
        _11102, 
        /**
         * null
         */
        _11103, 
        /**
         * null
         */
        _11104, 
        /**
         * null
         */
        _21211, 
        /**
         * null
         */
        _21212, 
        /**
         * null
         */
        _27211, 
        /**
         * null
         */
        _99111, 
        /**
         * null
         */
        _99333, 
        /**
         * null
         */
        _99555, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServiceUscls fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1101".equals(codeString))
          return _1101;
        if ("1102".equals(codeString))
          return _1102;
        if ("1103".equals(codeString))
          return _1103;
        if ("1201".equals(codeString))
          return _1201;
        if ("1205".equals(codeString))
          return _1205;
        if ("2101".equals(codeString))
          return _2101;
        if ("2102".equals(codeString))
          return _2102;
        if ("2141".equals(codeString))
          return _2141;
        if ("2601".equals(codeString))
          return _2601;
        if ("11101".equals(codeString))
          return _11101;
        if ("11102".equals(codeString))
          return _11102;
        if ("11103".equals(codeString))
          return _11103;
        if ("11104".equals(codeString))
          return _11104;
        if ("21211".equals(codeString))
          return _21211;
        if ("21212".equals(codeString))
          return _21212;
        if ("27211".equals(codeString))
          return _27211;
        if ("99111".equals(codeString))
          return _99111;
        if ("99333".equals(codeString))
          return _99333;
        if ("99555".equals(codeString))
          return _99555;
        throw new Exception("Unknown ServiceUscls code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1101: return "1101";
            case _1102: return "1102";
            case _1103: return "1103";
            case _1201: return "1201";
            case _1205: return "1205";
            case _2101: return "2101";
            case _2102: return "2102";
            case _2141: return "2141";
            case _2601: return "2601";
            case _11101: return "11101";
            case _11102: return "11102";
            case _11103: return "11103";
            case _11104: return "11104";
            case _21211: return "21211";
            case _21212: return "21212";
            case _27211: return "27211";
            case _99111: return "99111";
            case _99333: return "99333";
            case _99555: return "99555";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-USCLS";
        }
        public String getDefinition() {
          switch (this) {
            case _1101: return "";
            case _1102: return "";
            case _1103: return "";
            case _1201: return "";
            case _1205: return "";
            case _2101: return "";
            case _2102: return "";
            case _2141: return "";
            case _2601: return "";
            case _11101: return "";
            case _11102: return "";
            case _11103: return "";
            case _11104: return "";
            case _21211: return "";
            case _21212: return "";
            case _27211: return "";
            case _99111: return "";
            case _99333: return "";
            case _99555: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1101: return "1101";
            case _1102: return "1102";
            case _1103: return "1103";
            case _1201: return "1201";
            case _1205: return "1205";
            case _2101: return "2101";
            case _2102: return "2102";
            case _2141: return "2141";
            case _2601: return "2601";
            case _11101: return "11101";
            case _11102: return "11102";
            case _11103: return "11103";
            case _11104: return "11104";
            case _21211: return "21211";
            case _21212: return "21212";
            case _27211: return "27211";
            case _99111: return "99111";
            case _99333: return "99333";
            case _99555: return "99555";
            default: return "?";
          }
    }


}

