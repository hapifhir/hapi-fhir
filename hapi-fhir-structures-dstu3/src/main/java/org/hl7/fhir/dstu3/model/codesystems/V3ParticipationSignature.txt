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

public enum V3ParticipationSignature {

        /**
         * The particpant intends to provide a signature.
         */
        I, 
        /**
         * Signature has been affixed, either written on file, or electronic (incl. digital) signature in Participation.signatureText.
         */
        S, 
        /**
         * A signature for the service is required of this actor.
         */
        X, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ParticipationSignature fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("I".equals(codeString))
          return I;
        if ("S".equals(codeString))
          return S;
        if ("X".equals(codeString))
          return X;
        throw new FHIRException("Unknown V3ParticipationSignature code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case I: return "I";
            case S: return "S";
            case X: return "X";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ParticipationSignature";
        }
        public String getDefinition() {
          switch (this) {
            case I: return "The particpant intends to provide a signature.";
            case S: return "Signature has been affixed, either written on file, or electronic (incl. digital) signature in Participation.signatureText.";
            case X: return "A signature for the service is required of this actor.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case I: return "intended";
            case S: return "signed";
            case X: return "required";
            default: return "?";
          }
    }


}

