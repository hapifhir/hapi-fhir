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

public enum GuidePageKind {

        /**
         * This is a page of content that is included in the implementation guide. It has no particular function.
         */
        PAGE, 
        /**
         * This is a page that represents a human readable rendering of an example.
         */
        EXAMPLE, 
        /**
         * This is a page that represents a list of resources of one or more types.
         */
        LIST, 
        /**
         * This is a page showing where an included guide is injected.
         */
        INCLUDE, 
        /**
         * This is a page that lists the resources of a given type, and also creates pages for all the listed types as other pages in the section.
         */
        DIRECTORY, 
        /**
         * This is a page that creates the listed resources as a dictionary.
         */
        DICTIONARY, 
        /**
         * This is a generated page that contains the table of contents.
         */
        TOC, 
        /**
         * This is a page that represents a presented resource. This is typically used for generated conformance resource presentations.
         */
        RESOURCE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GuidePageKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("page".equals(codeString))
          return PAGE;
        if ("example".equals(codeString))
          return EXAMPLE;
        if ("list".equals(codeString))
          return LIST;
        if ("include".equals(codeString))
          return INCLUDE;
        if ("directory".equals(codeString))
          return DIRECTORY;
        if ("dictionary".equals(codeString))
          return DICTIONARY;
        if ("toc".equals(codeString))
          return TOC;
        if ("resource".equals(codeString))
          return RESOURCE;
        throw new FHIRException("Unknown GuidePageKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PAGE: return "page";
            case EXAMPLE: return "example";
            case LIST: return "list";
            case INCLUDE: return "include";
            case DIRECTORY: return "directory";
            case DICTIONARY: return "dictionary";
            case TOC: return "toc";
            case RESOURCE: return "resource";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/guide-page-kind";
        }
        public String getDefinition() {
          switch (this) {
            case PAGE: return "This is a page of content that is included in the implementation guide. It has no particular function.";
            case EXAMPLE: return "This is a page that represents a human readable rendering of an example.";
            case LIST: return "This is a page that represents a list of resources of one or more types.";
            case INCLUDE: return "This is a page showing where an included guide is injected.";
            case DIRECTORY: return "This is a page that lists the resources of a given type, and also creates pages for all the listed types as other pages in the section.";
            case DICTIONARY: return "This is a page that creates the listed resources as a dictionary.";
            case TOC: return "This is a generated page that contains the table of contents.";
            case RESOURCE: return "This is a page that represents a presented resource. This is typically used for generated conformance resource presentations.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PAGE: return "Page";
            case EXAMPLE: return "Example";
            case LIST: return "List";
            case INCLUDE: return "Include";
            case DIRECTORY: return "Directory";
            case DICTIONARY: return "Dictionary";
            case TOC: return "Table Of Contents";
            case RESOURCE: return "Resource";
            default: return "?";
          }
    }


}

