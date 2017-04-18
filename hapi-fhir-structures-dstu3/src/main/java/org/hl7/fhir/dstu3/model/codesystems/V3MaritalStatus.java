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

public enum V3MaritalStatus {

        /**
         * Marriage contract has been declared null and to not have existed
         */
        A, 
        /**
         * Marriage contract has been declared dissolved and inactive
         */
        D, 
        /**
         * Subject to an Interlocutory Decree.
         */
        I, 
        /**
         * Legally Separated
         */
        L, 
        /**
         * A current marriage contract is active
         */
        M, 
        /**
         * More than 1 current spouse
         */
        P, 
        /**
         * No marriage contract has ever been entered
         */
        S, 
        /**
         * Person declares that a domestic partner relationship exists.
         */
        T, 
        /**
         * Currently not in a marriage contract.
         */
        U, 
        /**
         * The spouse has died
         */
        W, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3MaritalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("A".equals(codeString))
          return A;
        if ("D".equals(codeString))
          return D;
        if ("I".equals(codeString))
          return I;
        if ("L".equals(codeString))
          return L;
        if ("M".equals(codeString))
          return M;
        if ("P".equals(codeString))
          return P;
        if ("S".equals(codeString))
          return S;
        if ("T".equals(codeString))
          return T;
        if ("U".equals(codeString))
          return U;
        if ("W".equals(codeString))
          return W;
        throw new FHIRException("Unknown V3MaritalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "A";
            case D: return "D";
            case I: return "I";
            case L: return "L";
            case M: return "M";
            case P: return "P";
            case S: return "S";
            case T: return "T";
            case U: return "U";
            case W: return "W";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/MaritalStatus";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "Marriage contract has been declared null and to not have existed";
            case D: return "Marriage contract has been declared dissolved and inactive";
            case I: return "Subject to an Interlocutory Decree.";
            case L: return "Legally Separated";
            case M: return "A current marriage contract is active";
            case P: return "More than 1 current spouse";
            case S: return "No marriage contract has ever been entered";
            case T: return "Person declares that a domestic partner relationship exists.";
            case U: return "Currently not in a marriage contract.";
            case W: return "The spouse has died";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "Annulled";
            case D: return "Divorced";
            case I: return "Interlocutory";
            case L: return "Legally Separated";
            case M: return "Married";
            case P: return "Polygamous";
            case S: return "Never Married";
            case T: return "Domestic partner";
            case U: return "unmarried";
            case W: return "Widowed";
            default: return "?";
          }
    }


}

