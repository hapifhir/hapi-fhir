package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum Tooth {

        /**
         * Oral cavity.
         */
        _0, 
        /**
         * Permanent teeth Maxillary right.
         */
        _1, 
        /**
         * Permanent teeth Maxillary left.
         */
        _2, 
        /**
         * Permanent teeth Mandibular right.
         */
        _3, 
        /**
         * Permanent teeth Mandibular left.
         */
        _4, 
        /**
         * Deciduous teeth Maxillary right.
         */
        _5, 
        /**
         * Deciduous teeth Maxillary left.
         */
        _6, 
        /**
         * Deciduous teeth Mandibular right.
         */
        _7, 
        /**
         * Deciduous teeth Mandibular left.
         */
        _8, 
        /**
         * Upper Right Tooth 1 from the central axis, permanent dentition.
         */
        _11, 
        /**
         * Upper Right Tooth 2 from the central axis, permanent dentition.
         */
        _12, 
        /**
         * Upper Right Tooth 3 from the central axis, permanent dentition.
         */
        _13, 
        /**
         * Upper Right Tooth 4 from the central axis, permanent dentition.
         */
        _14, 
        /**
         * Upper Right Tooth 5 from the central axis, permanent dentition.
         */
        _15, 
        /**
         * Upper Right Tooth 6 from the central axis, permanent dentition.
         */
        _16, 
        /**
         * Upper Right Tooth 7 from the central axis, permanent dentition.
         */
        _17, 
        /**
         * Upper Right Tooth 8 from the central axis, permanent dentition.
         */
        _18, 
        /**
         * Upper Left Tooth 1 from the central axis, permanent dentition.
         */
        _21, 
        /**
         * Upper Left Tooth 2 from the central axis, permanent dentition.
         */
        _22, 
        /**
         * Upper Left Tooth 3 from the central axis, permanent dentition.
         */
        _23, 
        /**
         * Upper Left Tooth 4 from the central axis, permanent dentition.
         */
        _24, 
        /**
         * Upper Left Tooth 5 from the central axis, permanent dentition.
         */
        _25, 
        /**
         * Upper Left Tooth 6 from the central axis, permanent dentition.
         */
        _26, 
        /**
         * Upper Left Tooth 7 from the central axis, permanent dentition.
         */
        _27, 
        /**
         * Upper Left Tooth 8 from the central axis, permanent dentition.
         */
        _28, 
        /**
         * Lower Left Tooth 1 from the central axis, permanent dentition.
         */
        _31, 
        /**
         * Lower Left Tooth 2 from the central axis, permanent dentition.
         */
        _32, 
        /**
         * Lower Left Tooth 3 from the central axis, permanent dentition.
         */
        _33, 
        /**
         * Lower Left Tooth 4 from the central axis, permanent dentition.
         */
        _34, 
        /**
         * Lower Left Tooth 5 from the central axis, permanent dentition.
         */
        _35, 
        /**
         * Lower Left Tooth 6 from the central axis, permanent dentition.
         */
        _36, 
        /**
         * Lower Left Tooth 7 from the central axis, permanent dentition.
         */
        _37, 
        /**
         * Lower Left Tooth 8 from the central axis, permanent dentition.
         */
        _38, 
        /**
         * Lower Right Tooth 1 from the central axis, permanent dentition.
         */
        _41, 
        /**
         * Lower Right Tooth 2 from the central axis, permanent dentition.
         */
        _42, 
        /**
         * Lower Right Tooth 3 from the central axis, permanent dentition.
         */
        _43, 
        /**
         * Lower Right Tooth 4 from the central axis, permanent dentition.
         */
        _44, 
        /**
         * Lower Right Tooth 5 from the central axis, permanent dentition.
         */
        _45, 
        /**
         * Lower Right Tooth 6 from the central axis, permanent dentition.
         */
        _46, 
        /**
         * Lower Right Tooth 7 from the central axis, permanent dentition.
         */
        _47, 
        /**
         * Lower Right Tooth 8 from the central axis, permanent dentition.
         */
        _48, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Tooth fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("0".equals(codeString))
          return _0;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        if ("6".equals(codeString))
          return _6;
        if ("7".equals(codeString))
          return _7;
        if ("8".equals(codeString))
          return _8;
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
        throw new FHIRException("Unknown Tooth code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _0: return "0";
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _6: return "6";
            case _7: return "7";
            case _8: return "8";
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
          return "http://hl7.org/fhir/ex-tooth";
        }
        public String getDefinition() {
          switch (this) {
            case _0: return "Oral cavity.";
            case _1: return "Permanent teeth Maxillary right.";
            case _2: return "Permanent teeth Maxillary left.";
            case _3: return "Permanent teeth Mandibular right.";
            case _4: return "Permanent teeth Mandibular left.";
            case _5: return "Deciduous teeth Maxillary right.";
            case _6: return "Deciduous teeth Maxillary left.";
            case _7: return "Deciduous teeth Mandibular right.";
            case _8: return "Deciduous teeth Mandibular left.";
            case _11: return "Upper Right Tooth 1 from the central axis, permanent dentition.";
            case _12: return "Upper Right Tooth 2 from the central axis, permanent dentition.";
            case _13: return "Upper Right Tooth 3 from the central axis, permanent dentition.";
            case _14: return "Upper Right Tooth 4 from the central axis, permanent dentition.";
            case _15: return "Upper Right Tooth 5 from the central axis, permanent dentition.";
            case _16: return "Upper Right Tooth 6 from the central axis, permanent dentition.";
            case _17: return "Upper Right Tooth 7 from the central axis, permanent dentition.";
            case _18: return "Upper Right Tooth 8 from the central axis, permanent dentition.";
            case _21: return "Upper Left Tooth 1 from the central axis, permanent dentition.";
            case _22: return "Upper Left Tooth 2 from the central axis, permanent dentition.";
            case _23: return "Upper Left Tooth 3 from the central axis, permanent dentition.";
            case _24: return "Upper Left Tooth 4 from the central axis, permanent dentition.";
            case _25: return "Upper Left Tooth 5 from the central axis, permanent dentition.";
            case _26: return "Upper Left Tooth 6 from the central axis, permanent dentition.";
            case _27: return "Upper Left Tooth 7 from the central axis, permanent dentition.";
            case _28: return "Upper Left Tooth 8 from the central axis, permanent dentition.";
            case _31: return "Lower Left Tooth 1 from the central axis, permanent dentition.";
            case _32: return "Lower Left Tooth 2 from the central axis, permanent dentition.";
            case _33: return "Lower Left Tooth 3 from the central axis, permanent dentition.";
            case _34: return "Lower Left Tooth 4 from the central axis, permanent dentition.";
            case _35: return "Lower Left Tooth 5 from the central axis, permanent dentition.";
            case _36: return "Lower Left Tooth 6 from the central axis, permanent dentition.";
            case _37: return "Lower Left Tooth 7 from the central axis, permanent dentition.";
            case _38: return "Lower Left Tooth 8 from the central axis, permanent dentition.";
            case _41: return "Lower Right Tooth 1 from the central axis, permanent dentition.";
            case _42: return "Lower Right Tooth 2 from the central axis, permanent dentition.";
            case _43: return "Lower Right Tooth 3 from the central axis, permanent dentition.";
            case _44: return "Lower Right Tooth 4 from the central axis, permanent dentition.";
            case _45: return "Lower Right Tooth 5 from the central axis, permanent dentition.";
            case _46: return "Lower Right Tooth 6 from the central axis, permanent dentition.";
            case _47: return "Lower Right Tooth 7 from the central axis, permanent dentition.";
            case _48: return "Lower Right Tooth 8 from the central axis, permanent dentition.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _0: return "Oral cavity";
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _6: return "6";
            case _7: return "7";
            case _8: return "8";
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

