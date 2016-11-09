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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ChromosomeHuman {

        /**
         * chromsome 1
         */
        _1, 
        /**
         * chromsome 2
         */
        _2, 
        /**
         * chromsome 3
         */
        _3, 
        /**
         * chromsome 4
         */
        _4, 
        /**
         * chromsome 5
         */
        _5, 
        /**
         * chromsome 6
         */
        _6, 
        /**
         * chromsome 7
         */
        _7, 
        /**
         * chromsome 8
         */
        _8, 
        /**
         * chromsome 9
         */
        _9, 
        /**
         * chromsome 10
         */
        _10, 
        /**
         * chromsome 11
         */
        _11, 
        /**
         * chromsome 12
         */
        _12, 
        /**
         * chromsome 13
         */
        _13, 
        /**
         * chromsome 14
         */
        _14, 
        /**
         * chromsome 15
         */
        _15, 
        /**
         * chromsome 16
         */
        _16, 
        /**
         * chromsome 17
         */
        _17, 
        /**
         * chromsome 18
         */
        _18, 
        /**
         * chromsome 19
         */
        _19, 
        /**
         * chromsome 20
         */
        _20, 
        /**
         * chromsome 21
         */
        _21, 
        /**
         * chromsome 22
         */
        _22, 
        /**
         * chromsome X
         */
        X, 
        /**
         * chromsome Y
         */
        Y, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ChromosomeHuman fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
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
        if ("9".equals(codeString))
          return _9;
        if ("10".equals(codeString))
          return _10;
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
        if ("19".equals(codeString))
          return _19;
        if ("20".equals(codeString))
          return _20;
        if ("21".equals(codeString))
          return _21;
        if ("22".equals(codeString))
          return _22;
        if ("X".equals(codeString))
          return X;
        if ("Y".equals(codeString))
          return Y;
        throw new FHIRException("Unknown ChromosomeHuman code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _6: return "6";
            case _7: return "7";
            case _8: return "8";
            case _9: return "9";
            case _10: return "10";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            case _16: return "16";
            case _17: return "17";
            case _18: return "18";
            case _19: return "19";
            case _20: return "20";
            case _21: return "21";
            case _22: return "22";
            case X: return "X";
            case Y: return "Y";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/chromosome-human";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "chromsome 1";
            case _2: return "chromsome 2";
            case _3: return "chromsome 3";
            case _4: return "chromsome 4";
            case _5: return "chromsome 5";
            case _6: return "chromsome 6";
            case _7: return "chromsome 7";
            case _8: return "chromsome 8";
            case _9: return "chromsome 9";
            case _10: return "chromsome 10";
            case _11: return "chromsome 11";
            case _12: return "chromsome 12";
            case _13: return "chromsome 13";
            case _14: return "chromsome 14";
            case _15: return "chromsome 15";
            case _16: return "chromsome 16";
            case _17: return "chromsome 17";
            case _18: return "chromsome 18";
            case _19: return "chromsome 19";
            case _20: return "chromsome 20";
            case _21: return "chromsome 21";
            case _22: return "chromsome 22";
            case X: return "chromsome X";
            case Y: return "chromsome Y";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "chromsome 1";
            case _2: return "chromsome 2";
            case _3: return "chromsome 3";
            case _4: return "chromsome 4";
            case _5: return "chromsome 5";
            case _6: return "chromsome 6";
            case _7: return "chromsome 7";
            case _8: return "chromsome 8";
            case _9: return "chromsome 9";
            case _10: return "chromsome 10";
            case _11: return "chromsome 11";
            case _12: return "chromsome 12";
            case _13: return "chromsome 13";
            case _14: return "chromsome 14";
            case _15: return "chromsome 15";
            case _16: return "chromsome 16";
            case _17: return "chromsome 17";
            case _18: return "chromsome 18";
            case _19: return "chromsome 19";
            case _20: return "chromsome 20";
            case _21: return "chromsome 21";
            case _22: return "chromsome 22";
            case X: return "chromsome X";
            case Y: return "chromsome Y";
            default: return "?";
          }
    }


}

