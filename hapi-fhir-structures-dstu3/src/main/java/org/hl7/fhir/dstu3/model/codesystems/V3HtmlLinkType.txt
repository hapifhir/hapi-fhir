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

public enum V3HtmlLinkType {

        /**
         * Designates substitute versions for the document in which the link occurs. When used together with the lang attribute, it implies a translated version of the document. When used together with the media attribute, it implies a version designed for a different medium (or media).
         */
        ALTERNATE, 
        /**
         * Refers to a document serving as an appendix in a collection of documents.
         */
        APPENDIX, 
        /**
         * Refers to a bookmark. A bookmark is a link to a key entry point within an extended document. The title attribute may be used, for example, to label the bookmark. Note that several bookmarks may be defined in each document.
         */
        BOOKMARK, 
        /**
         * Refers to a document serving as a chapter in a collection of documents.
         */
        CHAPTER, 
        /**
         * Refers to a document serving as a table of contents. Some user agents also support the synonym ToC (from "Table of Contents").
         */
        CONTENTS, 
        /**
         * Refers to a copyright statement for the current document.
         */
        COPYRIGHT, 
        /**
         * Refers to a document providing a glossary of terms that pertain to the current document.
         */
        GLOSSARY, 
        /**
         * Refers to a document offering help (more information, links to other sources of information, etc.).
         */
        HELP, 
        /**
         * Refers to a document providing an index for the current document.
         */
        INDEX, 
        /**
         * Refers to the next document in a linear sequence of documents. User agents may choose to preload the "next" document, to reduce the perceived load time.
         */
        NEXT, 
        /**
         * Refers to the previous document in an ordered series of documents. Some user agents also support the synonym "Previous".
         */
        PREV, 
        /**
         * Refers to a document serving as a section in a collection of documents.
         */
        SECTION, 
        /**
         * Refers to the first document in a collection of documents. This link type tells search engines which document is considered by the author to be the starting point of the collection.
         */
        START, 
        /**
         * Refers to an external style sheet. See the section on external style sheets for details. This is used together with the link type "Alternate" for user-selectable alternate style sheets.
         */
        STYLESHEET, 
        /**
         * Refers to a document serving as a subsection in a collection of documents.
         */
        SUBSECTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3HtmlLinkType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("alternate".equals(codeString))
          return ALTERNATE;
        if ("appendix".equals(codeString))
          return APPENDIX;
        if ("bookmark".equals(codeString))
          return BOOKMARK;
        if ("chapter".equals(codeString))
          return CHAPTER;
        if ("contents".equals(codeString))
          return CONTENTS;
        if ("copyright".equals(codeString))
          return COPYRIGHT;
        if ("glossary".equals(codeString))
          return GLOSSARY;
        if ("help".equals(codeString))
          return HELP;
        if ("index".equals(codeString))
          return INDEX;
        if ("next".equals(codeString))
          return NEXT;
        if ("prev".equals(codeString))
          return PREV;
        if ("section".equals(codeString))
          return SECTION;
        if ("start".equals(codeString))
          return START;
        if ("stylesheet".equals(codeString))
          return STYLESHEET;
        if ("subsection".equals(codeString))
          return SUBSECTION;
        throw new FHIRException("Unknown V3HtmlLinkType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALTERNATE: return "alternate";
            case APPENDIX: return "appendix";
            case BOOKMARK: return "bookmark";
            case CHAPTER: return "chapter";
            case CONTENTS: return "contents";
            case COPYRIGHT: return "copyright";
            case GLOSSARY: return "glossary";
            case HELP: return "help";
            case INDEX: return "index";
            case NEXT: return "next";
            case PREV: return "prev";
            case SECTION: return "section";
            case START: return "start";
            case STYLESHEET: return "stylesheet";
            case SUBSECTION: return "subsection";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/HtmlLinkType";
        }
        public String getDefinition() {
          switch (this) {
            case ALTERNATE: return "Designates substitute versions for the document in which the link occurs. When used together with the lang attribute, it implies a translated version of the document. When used together with the media attribute, it implies a version designed for a different medium (or media).";
            case APPENDIX: return "Refers to a document serving as an appendix in a collection of documents.";
            case BOOKMARK: return "Refers to a bookmark. A bookmark is a link to a key entry point within an extended document. The title attribute may be used, for example, to label the bookmark. Note that several bookmarks may be defined in each document.";
            case CHAPTER: return "Refers to a document serving as a chapter in a collection of documents.";
            case CONTENTS: return "Refers to a document serving as a table of contents. Some user agents also support the synonym ToC (from \"Table of Contents\").";
            case COPYRIGHT: return "Refers to a copyright statement for the current document.";
            case GLOSSARY: return "Refers to a document providing a glossary of terms that pertain to the current document.";
            case HELP: return "Refers to a document offering help (more information, links to other sources of information, etc.).";
            case INDEX: return "Refers to a document providing an index for the current document.";
            case NEXT: return "Refers to the next document in a linear sequence of documents. User agents may choose to preload the \"next\" document, to reduce the perceived load time.";
            case PREV: return "Refers to the previous document in an ordered series of documents. Some user agents also support the synonym \"Previous\".";
            case SECTION: return "Refers to a document serving as a section in a collection of documents.";
            case START: return "Refers to the first document in a collection of documents. This link type tells search engines which document is considered by the author to be the starting point of the collection.";
            case STYLESHEET: return "Refers to an external style sheet. See the section on external style sheets for details. This is used together with the link type \"Alternate\" for user-selectable alternate style sheets.";
            case SUBSECTION: return "Refers to a document serving as a subsection in a collection of documents.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALTERNATE: return "alternate";
            case APPENDIX: return "appendix";
            case BOOKMARK: return "bookmark";
            case CHAPTER: return "chapter";
            case CONTENTS: return "contents";
            case COPYRIGHT: return "copyright";
            case GLOSSARY: return "glossary";
            case HELP: return "help";
            case INDEX: return "index";
            case NEXT: return "next";
            case PREV: return "prev";
            case SECTION: return "section";
            case START: return "start";
            case STYLESHEET: return "stylesheet";
            case SUBSECTION: return "subsection";
            default: return "?";
          }
    }


}

