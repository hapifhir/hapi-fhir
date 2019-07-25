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

// Generated on Tue, Jul 14, 2015 17:35-0400 for FHIR v0.5.0


public enum Modifiers {

        /**
         * null
         */
        A, 
        /**
         * null
         */
        B, 
        /**
         * null
         */
        C, 
        /**
         * null
         */
        E, 
        /**
         * null
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Modifiers fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("B".equals(codeString))
          return B;
        if ("C".equals(codeString))
          return C;
        if ("E".equals(codeString))
          return E;
        if ("X".equals(codeString))
          return X;
        throw new Exception("Unknown Modifiers code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case B: return "B";
            case C: return "C";
            case E: return "E";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/modifiers";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "";
            case B: return "";
            case C: return "";
            case E: return "";
            case X: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "A";
            case B: return "B";
            case C: return "C";
            case E: return "E";
            case X: return "X";
            default: return "?";
          }
    }


}

