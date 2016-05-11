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


public enum V3ResponseLevel {

        /**
         * Respond with exceptions and a notification of completion
         */
        C, 
        /**
         * Respond with exceptions, completion, modifications and include more detail information (if applicable)
         */
        D, 
        /**
         * Respond with exceptions only
         */
        E, 
        /**
         * Respond with exceptions, completion, and modification with detail (as above), and send positive confirmations even if no modifications are being made.
         */
        F, 
        /**
         * Respond only with message level acknowledgements, i.e., only notify acceptance or rejection of the message, do not include any application-level detail
         */
        N, 
        /**
         * Respond with exceptions, completions and modifications or revisions done before completion
         */
        R, 
        /**
         * Do not send any kind of response
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ResponseLevel fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("C".equals(codeString))
          return C;
        if ("D".equals(codeString))
          return D;
        if ("E".equals(codeString))
          return E;
        if ("F".equals(codeString))
          return F;
        if ("N".equals(codeString))
          return N;
        if ("R".equals(codeString))
          return R;
        if ("X".equals(codeString))
          return X;
        throw new Exception("Unknown V3ResponseLevel code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case C: return "C";
            case D: return "D";
            case E: return "E";
            case F: return "F";
            case N: return "N";
            case R: return "R";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ResponseLevel";
        }
        public String getDefinition() {
          switch (this) {
            case C: return "Respond with exceptions and a notification of completion";
            case D: return "Respond with exceptions, completion, modifications and include more detail information (if applicable)";
            case E: return "Respond with exceptions only";
            case F: return "Respond with exceptions, completion, and modification with detail (as above), and send positive confirmations even if no modifications are being made.";
            case N: return "Respond only with message level acknowledgements, i.e., only notify acceptance or rejection of the message, do not include any application-level detail";
            case R: return "Respond with exceptions, completions and modifications or revisions done before completion";
            case X: return "Do not send any kind of response";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case C: return "completion";
            case D: return "detail";
            case E: return "exception";
            case F: return "confirmation";
            case N: return "message-control";
            case R: return "modification";
            case X: return "none";
            default: return "?";
          }
    }


}

