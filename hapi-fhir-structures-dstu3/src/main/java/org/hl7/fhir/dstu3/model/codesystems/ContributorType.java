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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ContributorType {

        /**
         * An author of the content of the module
         */
        AUTHOR, 
        /**
         * An editor of the content of the module
         */
        EDITOR, 
        /**
         * A reviewer of the content of the module
         */
        REVIEWER, 
        /**
         * An endorser of the content of the module
         */
        ENDORSER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContributorType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("author".equals(codeString))
          return AUTHOR;
        if ("editor".equals(codeString))
          return EDITOR;
        if ("reviewer".equals(codeString))
          return REVIEWER;
        if ("endorser".equals(codeString))
          return ENDORSER;
        throw new FHIRException("Unknown ContributorType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AUTHOR: return "author";
            case EDITOR: return "editor";
            case REVIEWER: return "reviewer";
            case ENDORSER: return "endorser";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contributor-type";
        }
        public String getDefinition() {
          switch (this) {
            case AUTHOR: return "An author of the content of the module";
            case EDITOR: return "An editor of the content of the module";
            case REVIEWER: return "A reviewer of the content of the module";
            case ENDORSER: return "An endorser of the content of the module";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AUTHOR: return "Author";
            case EDITOR: return "Editor";
            case REVIEWER: return "Reviewer";
            case ENDORSER: return "Endorser";
            default: return "?";
          }
    }


}

