package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

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

    public String toSystem(V3HtmlLinkType code) {
      return code.getSystem();
      }

}

