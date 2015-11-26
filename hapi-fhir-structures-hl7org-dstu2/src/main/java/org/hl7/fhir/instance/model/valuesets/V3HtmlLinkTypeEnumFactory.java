package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


import org.hl7.fhir.instance.model.EnumFactory;

public class V3HtmlLinkTypeEnumFactory implements EnumFactory<V3HtmlLinkType> {

  public V3HtmlLinkType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("alternate".equals(codeString))
      return V3HtmlLinkType.ALTERNATE;
    if ("appendix".equals(codeString))
      return V3HtmlLinkType.APPENDIX;
    if ("bookmark".equals(codeString))
      return V3HtmlLinkType.BOOKMARK;
    if ("chapter".equals(codeString))
      return V3HtmlLinkType.CHAPTER;
    if ("contents".equals(codeString))
      return V3HtmlLinkType.CONTENTS;
    if ("copyright".equals(codeString))
      return V3HtmlLinkType.COPYRIGHT;
    if ("glossary".equals(codeString))
      return V3HtmlLinkType.GLOSSARY;
    if ("help".equals(codeString))
      return V3HtmlLinkType.HELP;
    if ("index".equals(codeString))
      return V3HtmlLinkType.INDEX;
    if ("next".equals(codeString))
      return V3HtmlLinkType.NEXT;
    if ("prev".equals(codeString))
      return V3HtmlLinkType.PREV;
    if ("section".equals(codeString))
      return V3HtmlLinkType.SECTION;
    if ("start".equals(codeString))
      return V3HtmlLinkType.START;
    if ("stylesheet".equals(codeString))
      return V3HtmlLinkType.STYLESHEET;
    if ("subsection".equals(codeString))
      return V3HtmlLinkType.SUBSECTION;
    throw new IllegalArgumentException("Unknown V3HtmlLinkType code '"+codeString+"'");
  }

  public String toCode(V3HtmlLinkType code) {
    if (code == V3HtmlLinkType.ALTERNATE)
      return "alternate";
    if (code == V3HtmlLinkType.APPENDIX)
      return "appendix";
    if (code == V3HtmlLinkType.BOOKMARK)
      return "bookmark";
    if (code == V3HtmlLinkType.CHAPTER)
      return "chapter";
    if (code == V3HtmlLinkType.CONTENTS)
      return "contents";
    if (code == V3HtmlLinkType.COPYRIGHT)
      return "copyright";
    if (code == V3HtmlLinkType.GLOSSARY)
      return "glossary";
    if (code == V3HtmlLinkType.HELP)
      return "help";
    if (code == V3HtmlLinkType.INDEX)
      return "index";
    if (code == V3HtmlLinkType.NEXT)
      return "next";
    if (code == V3HtmlLinkType.PREV)
      return "prev";
    if (code == V3HtmlLinkType.SECTION)
      return "section";
    if (code == V3HtmlLinkType.START)
      return "start";
    if (code == V3HtmlLinkType.STYLESHEET)
      return "stylesheet";
    if (code == V3HtmlLinkType.SUBSECTION)
      return "subsection";
    return "?";
  }


}

