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


public enum Teeth {

        /**
         * null
         */
        _11, 
        /**
         * null
         */
        _12, 
        /**
         * null
         */
        _13, 
        /**
         * null
         */
        _14, 
        /**
         * null
         */
        _15, 
        /**
         * null
         */
        _16, 
        /**
         * null
         */
        _17, 
        /**
         * null
         */
        _18, 
        /**
         * null
         */
        _21, 
        /**
         * null
         */
        _22, 
        /**
         * null
         */
        _23, 
        /**
         * null
         */
        _24, 
        /**
         * null
         */
        _25, 
        /**
         * null
         */
        _26, 
        /**
         * null
         */
        _27, 
        /**
         * null
         */
        _28, 
        /**
         * null
         */
        _31, 
        /**
         * null
         */
        _32, 
        /**
         * null
         */
        _33, 
        /**
         * null
         */
        _34, 
        /**
         * null
         */
        _35, 
        /**
         * null
         */
        _36, 
        /**
         * null
         */
        _37, 
        /**
         * null
         */
        _38, 
        /**
         * null
         */
        _41, 
        /**
         * null
         */
        _42, 
        /**
         * null
         */
        _43, 
        /**
         * null
         */
        _44, 
        /**
         * null
         */
        _45, 
        /**
         * null
         */
        _46, 
        /**
         * null
         */
        _47, 
        /**
         * null
         */
        _48, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Teeth fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("11".equals(codeString))
          return _11;
        if ("12".equals(codeString))
          return _12;
        if ("13".equals(codeString))
          return _13;
        if ("14".equals(codeString))
          return _14;
        if ("15".equals(codeString))
          return _15;
        if ("16".equals(codeString))
          return _16;
        if ("17".equals(codeString))
          return _17;
        if ("18".equals(codeString))
          return _18;
        if ("21".equals(codeString))
          return _21;
        if ("22".equals(codeString))
          return _22;
        if ("23".equals(codeString))
          return _23;
        if ("24".equals(codeString))
          return _24;
        if ("25".equals(codeString))
          return _25;
        if ("26".equals(codeString))
          return _26;
        if ("27".equals(codeString))
          return _27;
        if ("28".equals(codeString))
          return _28;
        if ("31".equals(codeString))
          return _31;
        if ("32".equals(codeString))
          return _32;
        if ("33".equals(codeString))
          return _33;
        if ("34".equals(codeString))
          return _34;
        if ("35".equals(codeString))
          return _35;
        if ("36".equals(codeString))
          return _36;
        if ("37".equals(codeString))
          return _37;
        if ("38".equals(codeString))
          return _38;
        if ("41".equals(codeString))
          return _41;
        if ("42".equals(codeString))
          return _42;
        if ("43".equals(codeString))
          return _43;
        if ("44".equals(codeString))
          return _44;
        if ("45".equals(codeString))
          return _45;
        if ("46".equals(codeString))
          return _46;
        if ("47".equals(codeString))
          return _47;
        if ("48".equals(codeString))
          return _48;
        throw new Exception("Unknown Teeth code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            case _16: return "16";
            case _17: return "17";
            case _18: return "18";
            case _21: return "21";
            case _22: return "22";
            case _23: return "23";
            case _24: return "24";
            case _25: return "25";
            case _26: return "26";
            case _27: return "27";
            case _28: return "28";
            case _31: return "31";
            case _32: return "32";
            case _33: return "33";
            case _34: return "34";
            case _35: return "35";
            case _36: return "36";
            case _37: return "37";
            case _38: return "38";
            case _41: return "41";
            case _42: return "42";
            case _43: return "43";
            case _44: return "44";
            case _45: return "45";
            case _46: return "46";
            case _47: return "47";
            case _48: return "48";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-fdi";
        }
        public String getDefinition() {
          switch (this) {
            case _11: return "";
            case _12: return "";
            case _13: return "";
            case _14: return "";
            case _15: return "";
            case _16: return "";
            case _17: return "";
            case _18: return "";
            case _21: return "";
            case _22: return "";
            case _23: return "";
            case _24: return "";
            case _25: return "";
            case _26: return "";
            case _27: return "";
            case _28: return "";
            case _31: return "";
            case _32: return "";
            case _33: return "";
            case _34: return "";
            case _35: return "";
            case _36: return "";
            case _37: return "";
            case _38: return "";
            case _41: return "";
            case _42: return "";
            case _43: return "";
            case _44: return "";
            case _45: return "";
            case _46: return "";
            case _47: return "";
            case _48: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            case _16: return "16";
            case _17: return "17";
            case _18: return "18";
            case _21: return "21";
            case _22: return "22";
            case _23: return "23";
            case _24: return "24";
            case _25: return "25";
            case _26: return "26";
            case _27: return "27";
            case _28: return "28";
            case _31: return "31";
            case _32: return "32";
            case _33: return "33";
            case _34: return "34";
            case _35: return "35";
            case _36: return "36";
            case _37: return "37";
            case _38: return "38";
            case _41: return "41";
            case _42: return "42";
            case _43: return "43";
            case _44: return "44";
            case _45: return "45";
            case _46: return "46";
            case _47: return "47";
            case _48: return "48";
            default: return "?";
          }
    }


}

