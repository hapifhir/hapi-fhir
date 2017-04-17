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

public enum CompositionStatus {

        /**
         * This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or unverified.
         */
        PRELIMINARY, 
        /**
         * This version of the composition is complete and verified by an appropriate person and no further work is planned. Any subsequent updates would be on a new version of the composition.
         */
        FINAL, 
        /**
         * The composition content or the referenced resources have been modified (edited or added to) subsequent to being released as "final" and the composition is complete and verified by an authorized person.
         */
        AMENDED, 
        /**
         * The composition or document was originally created/issued in error, and this is an amendment that marks that the entire series should not be considered as valid.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CompositionStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preliminary".equals(codeString))
          return PRELIMINARY;
        if ("final".equals(codeString))
          return FINAL;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown CompositionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case AMENDED: return "amended";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/composition-status";
        }
        public String getDefinition() {
          switch (this) {
            case PRELIMINARY: return "This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or unverified.";
            case FINAL: return "This version of the composition is complete and verified by an appropriate person and no further work is planned. Any subsequent updates would be on a new version of the composition.";
            case AMENDED: return "The composition content or the referenced resources have been modified (edited or added to) subsequent to being released as \"final\" and the composition is complete and verified by an authorized person.";
            case ENTEREDINERROR: return "The composition or document was originally created/issued in error, and this is an amendment that marks that the entire series should not be considered as valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRELIMINARY: return "Preliminary";
            case FINAL: return "Final";
            case AMENDED: return "Amended";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
    }


}

