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


public enum Surface {

        /**
         * null
         */
        M, 
        /**
         * null
         */
        O, 
        /**
         * null
         */
        I, 
        /**
         * null
         */
        D, 
        /**
         * null
         */
        B, 
        /**
         * null
         */
        V, 
        /**
         * null
         */
        L, 
        /**
         * null
         */
        MO, 
        /**
         * null
         */
        DO, 
        /**
         * null
         */
        DI, 
        /**
         * null
         */
        MOD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Surface fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("M".equals(codeString))
          return M;
        if ("O".equals(codeString))
          return O;
        if ("I".equals(codeString))
          return I;
        if ("D".equals(codeString))
          return D;
        if ("B".equals(codeString))
          return B;
        if ("V".equals(codeString))
          return V;
        if ("L".equals(codeString))
          return L;
        if ("MO".equals(codeString))
          return MO;
        if ("DO".equals(codeString))
          return DO;
        if ("DI".equals(codeString))
          return DI;
        if ("MOD".equals(codeString))
          return MOD;
        throw new Exception("Unknown Surface code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case M: return "M";
            case O: return "O";
            case I: return "I";
            case D: return "D";
            case B: return "B";
            case V: return "V";
            case L: return "L";
            case MO: return "MO";
            case DO: return "DO";
            case DI: return "DI";
            case MOD: return "MOD";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/FDI-surface";
        }
        public String getDefinition() {
          switch (this) {
            case M: return "";
            case O: return "";
            case I: return "";
            case D: return "";
            case B: return "";
            case V: return "";
            case L: return "";
            case MO: return "";
            case DO: return "";
            case DI: return "";
            case MOD: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case M: return "M";
            case O: return "O";
            case I: return "I";
            case D: return "D";
            case B: return "B";
            case V: return "V";
            case L: return "L";
            case MO: return "MO";
            case DO: return "DO";
            case DI: return "DI";
            case MOD: return "MOD";
            default: return "?";
          }
    }


}

