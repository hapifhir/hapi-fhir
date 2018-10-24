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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ExtensionContextType {

        /**
         * The context is all elements that match the FHIRPath query found in the expression.
         */
        FHIRPATH, 
        /**
         * The context is any element that has an ElementDefinition.id that matches that found in the expression. This includes ElementDefinition Ids that have slicing identifiers. The full path for the element is [url]#[elementid]. If there is no #, the Element id is one defined in the base specification.
         */
        ELEMENT, 
        /**
         * The context is a particular extension from a particular StructureDefinition, and the expression is just a uri that identifies the extension.
         */
        EXTENSION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExtensionContextType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("fhirpath".equals(codeString))
          return FHIRPATH;
        if ("element".equals(codeString))
          return ELEMENT;
        if ("extension".equals(codeString))
          return EXTENSION;
        throw new FHIRException("Unknown ExtensionContextType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FHIRPATH: return "fhirpath";
            case ELEMENT: return "element";
            case EXTENSION: return "extension";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/extension-context-type";
        }
        public String getDefinition() {
          switch (this) {
            case FHIRPATH: return "The context is all elements that match the FHIRPath query found in the expression.";
            case ELEMENT: return "The context is any element that has an ElementDefinition.id that matches that found in the expression. This includes ElementDefinition Ids that have slicing identifiers. The full path for the element is [url]#[elementid]. If there is no #, the Element id is one defined in the base specification.";
            case EXTENSION: return "The context is a particular extension from a particular StructureDefinition, and the expression is just a uri that identifies the extension.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FHIRPATH: return "FHIRPath";
            case ELEMENT: return "Element ID";
            case EXTENSION: return "Extension URL";
            default: return "?";
          }
    }


}

