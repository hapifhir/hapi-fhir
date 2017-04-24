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

public enum ClaimModifiers {

        /**
         * Repair of prior service or installation.
         */
        A, 
        /**
         * Temporary service or installation.
         */
        B, 
        /**
         * Treatment associated with TMJ.
         */
        C, 
        /**
         * Implant or associated with an implant.
         */
        E, 
        /**
         * A Rush service or service performed outside of normal office hours.
         */
        ROOH, 
        /**
         * None.
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ClaimModifiers fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("a".equals(codeString))
          return A;
        if ("b".equals(codeString))
          return B;
        if ("c".equals(codeString))
          return C;
        if ("e".equals(codeString))
          return E;
        if ("rooh".equals(codeString))
          return ROOH;
        if ("x".equals(codeString))
          return X;
        throw new FHIRException("Unknown ClaimModifiers code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case A: return "a";
            case B: return "b";
            case C: return "c";
            case E: return "e";
            case ROOH: return "rooh";
            case X: return "x";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/modifiers";
        }
        public String getDefinition() {
          switch (this) {
            case A: return "Repair of prior service or installation.";
            case B: return "Temporary service or installation.";
            case C: return "Treatment associated with TMJ.";
            case E: return "Implant or associated with an implant.";
            case ROOH: return "A Rush service or service performed outside of normal office hours.";
            case X: return "None.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case A: return "Repair of prior service or installation";
            case B: return "Temporary service or installation";
            case C: return "TMJ treatment";
            case E: return "Implant or associated with an implant";
            case ROOH: return "Rush or Outside of office hours";
            case X: return "None";
            default: return "?";
          }
    }


}

