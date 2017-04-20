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

public enum NarrativeStatus {

        /**
         * The contents of the narrative are entirely generated from the structured data in the content.
         */
        GENERATED, 
        /**
         * The contents of the narrative are entirely generated from the structured data in the content and some of the content is generated from extensions
         */
        EXTENSIONS, 
        /**
         * The contents of the narrative may contain additional information not found in the structured data. Note that there is no computable way to determine what the extra information is, other than by human inspection
         */
        ADDITIONAL, 
        /**
         * The contents of the narrative are some equivalent of "No human-readable text provided in this case"
         */
        EMPTY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NarrativeStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("generated".equals(codeString))
          return GENERATED;
        if ("extensions".equals(codeString))
          return EXTENSIONS;
        if ("additional".equals(codeString))
          return ADDITIONAL;
        if ("empty".equals(codeString))
          return EMPTY;
        throw new FHIRException("Unknown NarrativeStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GENERATED: return "generated";
            case EXTENSIONS: return "extensions";
            case ADDITIONAL: return "additional";
            case EMPTY: return "empty";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/narrative-status";
        }
        public String getDefinition() {
          switch (this) {
            case GENERATED: return "The contents of the narrative are entirely generated from the structured data in the content.";
            case EXTENSIONS: return "The contents of the narrative are entirely generated from the structured data in the content and some of the content is generated from extensions";
            case ADDITIONAL: return "The contents of the narrative may contain additional information not found in the structured data. Note that there is no computable way to determine what the extra information is, other than by human inspection";
            case EMPTY: return "The contents of the narrative are some equivalent of \"No human-readable text provided in this case\"";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GENERATED: return "Generated";
            case EXTENSIONS: return "Extensions";
            case ADDITIONAL: return "Additional";
            case EMPTY: return "Empty";
            default: return "?";
          }
    }


}

