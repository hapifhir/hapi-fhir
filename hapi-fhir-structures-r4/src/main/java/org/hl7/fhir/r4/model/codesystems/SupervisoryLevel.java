package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Wed, Jan 10, 2018 14:53-0500 for FHIR v3.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum SupervisoryLevel {

        /**
         * US Military Enlisted paygrade E-1
         */
        E1, 
        /**
         * US Military Enlisted paygrade E-2
         */
        E2, 
        /**
         * US Military Enlisted paygrade E-3
         */
        E3, 
        /**
         * US Military Enlisted paygrade E-4
         */
        E4, 
        /**
         * US Military Enlisted paygrade E-5
         */
        E5, 
        /**
         * US Military Enlisted paygrade E-6
         */
        E6, 
        /**
         * US Military Enlisted paygrade E-7
         */
        E7, 
        /**
         * US Military Enlisted paygrade E-8
         */
        E8, 
        /**
         * US Military Enlisted paygrade E-9
         */
        E9, 
        /**
         * US Military Commissioned Officer paygrade O-1
         */
        O1, 
        /**
         * US Military Commissioned Officer paygrade O-2
         */
        O2, 
        /**
         * US Military Commissioned Officer paygrade  O-3
         */
        O3, 
        /**
         * US Military Commissioned Officer paygrade O-4
         */
        O4, 
        /**
         * US Military Commissioned Officer paygrade O-5
         */
        O5, 
        /**
         * US Military Commissioned Officer paygrade O-6
         */
        O6, 
        /**
         * US Military Commissioned Officer paygrade O-7
         */
        O7, 
        /**
         * US Military Commissioned Officer paygrade O-8
         */
        O8, 
        /**
         * US Military Commissioned Officer paygrade O-9
         */
        O9, 
        /**
         * US Military Commissioned Officer paygrade O-10
         */
        O10, 
        /**
         * Warrant Officer paygrade W-1
         */
        W1, 
        /**
         * Warrant Officer paygrade W-2
         */
        W2, 
        /**
         * Warrant Officer paygrade W-3
         */
        W3, 
        /**
         * Warrant Officer paygrade W-4
         */
        W4, 
        /**
         * Warrant Officer paygrade W-5
         */
        W5, 
        /**
         * oversees work and provides direction, does not have hiring/firing or budget authority; synonym: foreman.
         */
        C3, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SupervisoryLevel fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("E-1".equals(codeString))
          return E1;
        if ("E-2".equals(codeString))
          return E2;
        if ("E-3".equals(codeString))
          return E3;
        if ("E-4".equals(codeString))
          return E4;
        if ("E-5".equals(codeString))
          return E5;
        if ("E-6".equals(codeString))
          return E6;
        if ("E-7".equals(codeString))
          return E7;
        if ("E-8".equals(codeString))
          return E8;
        if ("E-9".equals(codeString))
          return E9;
        if ("O-1".equals(codeString))
          return O1;
        if ("O-2".equals(codeString))
          return O2;
        if ("O-3".equals(codeString))
          return O3;
        if ("O-4".equals(codeString))
          return O4;
        if ("O-5".equals(codeString))
          return O5;
        if ("O-6".equals(codeString))
          return O6;
        if ("O-7".equals(codeString))
          return O7;
        if ("O-8".equals(codeString))
          return O8;
        if ("O-9".equals(codeString))
          return O9;
        if ("O-10".equals(codeString))
          return O10;
        if ("W-1".equals(codeString))
          return W1;
        if ("W-2".equals(codeString))
          return W2;
        if ("W-3".equals(codeString))
          return W3;
        if ("W-4".equals(codeString))
          return W4;
        if ("W-5".equals(codeString))
          return W5;
        if ("C-3".equals(codeString))
          return C3;
        throw new FHIRException("Unknown SupervisoryLevel code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case E1: return "E-1";
            case E2: return "E-2";
            case E3: return "E-3";
            case E4: return "E-4";
            case E5: return "E-5";
            case E6: return "E-6";
            case E7: return "E-7";
            case E8: return "E-8";
            case E9: return "E-9";
            case O1: return "O-1";
            case O2: return "O-2";
            case O3: return "O-3";
            case O4: return "O-4";
            case O5: return "O-5";
            case O6: return "O-6";
            case O7: return "O-7";
            case O8: return "O-8";
            case O9: return "O-9";
            case O10: return "O-10";
            case W1: return "W-1";
            case W2: return "W-2";
            case W3: return "W-3";
            case W4: return "W-4";
            case W5: return "W-5";
            case C3: return "C-3";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/supervisory-level";
        }
        public String getDefinition() {
          switch (this) {
            case E1: return "US Military Enlisted paygrade E-1";
            case E2: return "US Military Enlisted paygrade E-2";
            case E3: return "US Military Enlisted paygrade E-3";
            case E4: return "US Military Enlisted paygrade E-4";
            case E5: return "US Military Enlisted paygrade E-5";
            case E6: return "US Military Enlisted paygrade E-6";
            case E7: return "US Military Enlisted paygrade E-7";
            case E8: return "US Military Enlisted paygrade E-8";
            case E9: return "US Military Enlisted paygrade E-9";
            case O1: return "US Military Commissioned Officer paygrade O-1";
            case O2: return "US Military Commissioned Officer paygrade O-2";
            case O3: return "US Military Commissioned Officer paygrade  O-3";
            case O4: return "US Military Commissioned Officer paygrade O-4";
            case O5: return "US Military Commissioned Officer paygrade O-5";
            case O6: return "US Military Commissioned Officer paygrade O-6";
            case O7: return "US Military Commissioned Officer paygrade O-7";
            case O8: return "US Military Commissioned Officer paygrade O-8";
            case O9: return "US Military Commissioned Officer paygrade O-9";
            case O10: return "US Military Commissioned Officer paygrade O-10";
            case W1: return "Warrant Officer paygrade W-1";
            case W2: return "Warrant Officer paygrade W-2";
            case W3: return "Warrant Officer paygrade W-3";
            case W4: return "Warrant Officer paygrade W-4";
            case W5: return "Warrant Officer paygrade W-5";
            case C3: return "oversees work and provides direction, does not have hiring/firing or budget authority; synonym: foreman.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case E1: return "E-1 - Enlisted Military Personnel 1";
            case E2: return "E-2 - Enlisted Military Personnel 2";
            case E3: return "E-3 - Enlisted Military Personnel 3";
            case E4: return "E-4 - Enlisted Military Personnel 4";
            case E5: return "E-5 - Enlisted Military Personnel 5";
            case E6: return "E-6 - Enlisted Military Personnel 6";
            case E7: return "E-7 - Enlisted Military Personnel 7";
            case E8: return "E-8 - Enlisted Military Personnel 8";
            case E9: return "E-9 - Enlisted Military Personnel 9";
            case O1: return "O-1 - Commissioned Officer 1";
            case O2: return "O-2 - Commissioned Officer 2";
            case O3: return "O-3 - Commissioned Officer 3";
            case O4: return "O-4 - Commissioned Officer 4";
            case O5: return "O-5 - Commissioned Officer 5";
            case O6: return "O-6 - Commissioned Officer 6";
            case O7: return "O-7 - Commissioned Officer 7";
            case O8: return "O-8 - Commissioned Officer 8";
            case O9: return "O-9 - Commissioned Officer 9";
            case O10: return "O-10 - Commissioned Officer 10";
            case W1: return "W-1 - Warrant Officer 1";
            case W2: return "W-2 - Warrant Officer 2";
            case W3: return "W-3 - Warrant Officer 3";
            case W4: return "W-4 - Warrant Officer 4";
            case W5: return "W-5 - Warrant Officer 5";
            case C3: return "C-3 - 1st Line Supervisor";
            default: return "?";
          }
    }


}

