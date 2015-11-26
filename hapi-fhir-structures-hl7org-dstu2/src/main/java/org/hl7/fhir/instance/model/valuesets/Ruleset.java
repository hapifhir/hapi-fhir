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


public enum Ruleset {

        /**
         * null
         */
        X124010, 
        /**
         * null
         */
        X125010, 
        /**
         * null
         */
        X127010, 
        /**
         * null
         */
        CDANETV2, 
        /**
         * null
         */
        CDANETV4, 
        /**
         * null
         */
        CPHA3, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Ruleset fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("x12-4010".equals(codeString))
          return X124010;
        if ("x12-5010".equals(codeString))
          return X125010;
        if ("x12-7010".equals(codeString))
          return X127010;
        if ("cdanet-v2".equals(codeString))
          return CDANETV2;
        if ("cdanet-v4".equals(codeString))
          return CDANETV4;
        if ("cpha-3".equals(codeString))
          return CPHA3;
        throw new Exception("Unknown Ruleset code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case X124010: return "x12-4010";
            case X125010: return "x12-5010";
            case X127010: return "x12-7010";
            case CDANETV2: return "cdanet-v2";
            case CDANETV4: return "cdanet-v4";
            case CPHA3: return "cpha-3";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ruleset";
        }
        public String getDefinition() {
          switch (this) {
            case X124010: return "";
            case X125010: return "";
            case X127010: return "";
            case CDANETV2: return "";
            case CDANETV4: return "";
            case CPHA3: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case X124010: return "x12-4010";
            case X125010: return "x12-5010";
            case X127010: return "x12-7010";
            case CDANETV2: return "cdanet-v2";
            case CDANETV4: return "cdanet-v4";
            case CPHA3: return "cpha-3";
            default: return "?";
          }
    }


}

