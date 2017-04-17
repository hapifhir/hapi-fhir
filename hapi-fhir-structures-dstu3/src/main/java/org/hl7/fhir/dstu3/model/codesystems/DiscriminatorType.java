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

public enum DiscriminatorType {

        /**
         * The slices have different values in the nominated element
         */
        VALUE, 
        /**
         * The slices are differentiated by the presence or absence of the nominated element
         */
        EXISTS, 
        /**
         * The slices have different values in the nominated element, as determined by testing them against the applicable ElementDefinition.pattern[x]
         */
        PATTERN, 
        /**
         * The slices are differentiated by type of the nominated element to a specifed profile
         */
        TYPE, 
        /**
         * The slices are differentiated by conformance of the nominated element to a specifed profile
         */
        PROFILE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DiscriminatorType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("value".equals(codeString))
          return VALUE;
        if ("exists".equals(codeString))
          return EXISTS;
        if ("pattern".equals(codeString))
          return PATTERN;
        if ("type".equals(codeString))
          return TYPE;
        if ("profile".equals(codeString))
          return PROFILE;
        throw new FHIRException("Unknown DiscriminatorType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case VALUE: return "value";
            case EXISTS: return "exists";
            case PATTERN: return "pattern";
            case TYPE: return "type";
            case PROFILE: return "profile";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/discriminator-type";
        }
        public String getDefinition() {
          switch (this) {
            case VALUE: return "The slices have different values in the nominated element";
            case EXISTS: return "The slices are differentiated by the presence or absence of the nominated element";
            case PATTERN: return "The slices have different values in the nominated element, as determined by testing them against the applicable ElementDefinition.pattern[x]";
            case TYPE: return "The slices are differentiated by type of the nominated element to a specifed profile";
            case PROFILE: return "The slices are differentiated by conformance of the nominated element to a specifed profile";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case VALUE: return "Value";
            case EXISTS: return "Exists";
            case PATTERN: return "Pattern";
            case TYPE: return "Type";
            case PROFILE: return "Profile";
            default: return "?";
          }
    }


}

