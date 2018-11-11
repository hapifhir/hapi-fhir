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

public enum V3StyleType {

        /**
         * Defines font rendering characteristics
         */
        _FONTSTYLE, 
        /**
         * Render with a bold font
         */
        BOLD, 
        /**
         * Render with with some type of emphasis
         */
        EMPHASIS, 
        /**
         * Render italicized
         */
        ITALICS, 
        /**
         * Render with an underline font
         */
        UNDERLINE, 
        /**
         * Defines list rendering characteristics
         */
        _LISTSTYLE, 
        /**
         * Defines rendering characteristics for ordered lists
         */
        _ORDEREDLISTSTYLE, 
        /**
         * List is ordered using Arabic numerals: 1, 2, 3
         */
        ARABIC, 
        /**
         * List is ordered using big alpha characters: A, B, C
         */
        BIGALPHA, 
        /**
         * List is ordered using big Roman numerals: I, II, III
         */
        BIGROMAN, 
        /**
         * List is order using little alpha characters: a, b, c
         */
        LITTLEALPHA, 
        /**
         * List is ordered using little Roman numerals: i, ii, iii
         */
        LITTLEROMAN, 
        /**
         * Defines rendering characteristics for unordered lists
         */
        _UNORDEREDLISTSTYLE, 
        /**
         * List bullets are hollow discs
         */
        CIRCLE, 
        /**
         * List bullets are simple solid discs
         */
        DISC, 
        /**
         * List bullets are solid squares
         */
        SQUARE, 
        /**
         * Defines table cell rendering characteristics
         */
        _TABLERULESTYLE, 
        /**
         * Render cell with rule on bottom
         */
        BOTRULE, 
        /**
         * Render cell with left-sided rule
         */
        LRULE, 
        /**
         * Render cell with right-sided rule
         */
        RRULE, 
        /**
         * Render cell with rule on top
         */
        TOPRULE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3StyleType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_FontStyle".equals(codeString))
          return _FONTSTYLE;
        if ("bold".equals(codeString))
          return BOLD;
        if ("emphasis".equals(codeString))
          return EMPHASIS;
        if ("italics".equals(codeString))
          return ITALICS;
        if ("underline".equals(codeString))
          return UNDERLINE;
        if ("_ListStyle".equals(codeString))
          return _LISTSTYLE;
        if ("_OrderedListStyle".equals(codeString))
          return _ORDEREDLISTSTYLE;
        if ("Arabic".equals(codeString))
          return ARABIC;
        if ("BigAlpha".equals(codeString))
          return BIGALPHA;
        if ("BigRoman".equals(codeString))
          return BIGROMAN;
        if ("LittleAlpha".equals(codeString))
          return LITTLEALPHA;
        if ("LittleRoman".equals(codeString))
          return LITTLEROMAN;
        if ("_UnorderedListStyle".equals(codeString))
          return _UNORDEREDLISTSTYLE;
        if ("Circle".equals(codeString))
          return CIRCLE;
        if ("Disc".equals(codeString))
          return DISC;
        if ("Square".equals(codeString))
          return SQUARE;
        if ("_TableRuleStyle".equals(codeString))
          return _TABLERULESTYLE;
        if ("Botrule".equals(codeString))
          return BOTRULE;
        if ("Lrule".equals(codeString))
          return LRULE;
        if ("Rrule".equals(codeString))
          return RRULE;
        if ("Toprule".equals(codeString))
          return TOPRULE;
        throw new FHIRException("Unknown V3StyleType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _FONTSTYLE: return "_FontStyle";
            case BOLD: return "bold";
            case EMPHASIS: return "emphasis";
            case ITALICS: return "italics";
            case UNDERLINE: return "underline";
            case _LISTSTYLE: return "_ListStyle";
            case _ORDEREDLISTSTYLE: return "_OrderedListStyle";
            case ARABIC: return "Arabic";
            case BIGALPHA: return "BigAlpha";
            case BIGROMAN: return "BigRoman";
            case LITTLEALPHA: return "LittleAlpha";
            case LITTLEROMAN: return "LittleRoman";
            case _UNORDEREDLISTSTYLE: return "_UnorderedListStyle";
            case CIRCLE: return "Circle";
            case DISC: return "Disc";
            case SQUARE: return "Square";
            case _TABLERULESTYLE: return "_TableRuleStyle";
            case BOTRULE: return "Botrule";
            case LRULE: return "Lrule";
            case RRULE: return "Rrule";
            case TOPRULE: return "Toprule";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-styleType";
        }
        public String getDefinition() {
          switch (this) {
            case _FONTSTYLE: return "Defines font rendering characteristics";
            case BOLD: return "Render with a bold font";
            case EMPHASIS: return "Render with with some type of emphasis";
            case ITALICS: return "Render italicized";
            case UNDERLINE: return "Render with an underline font";
            case _LISTSTYLE: return "Defines list rendering characteristics";
            case _ORDEREDLISTSTYLE: return "Defines rendering characteristics for ordered lists";
            case ARABIC: return "List is ordered using Arabic numerals: 1, 2, 3";
            case BIGALPHA: return "List is ordered using big alpha characters: A, B, C";
            case BIGROMAN: return "List is ordered using big Roman numerals: I, II, III";
            case LITTLEALPHA: return "List is order using little alpha characters: a, b, c";
            case LITTLEROMAN: return "List is ordered using little Roman numerals: i, ii, iii";
            case _UNORDEREDLISTSTYLE: return "Defines rendering characteristics for unordered lists";
            case CIRCLE: return "List bullets are hollow discs";
            case DISC: return "List bullets are simple solid discs";
            case SQUARE: return "List bullets are solid squares";
            case _TABLERULESTYLE: return "Defines table cell rendering characteristics";
            case BOTRULE: return "Render cell with rule on bottom";
            case LRULE: return "Render cell with left-sided rule";
            case RRULE: return "Render cell with right-sided rule";
            case TOPRULE: return "Render cell with rule on top";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _FONTSTYLE: return "Font Style";
            case BOLD: return "Bold Font";
            case EMPHASIS: return "Emphasised Font";
            case ITALICS: return "Italics Font";
            case UNDERLINE: return "Underline Font";
            case _LISTSTYLE: return "List Style";
            case _ORDEREDLISTSTYLE: return "Ordered List Style";
            case ARABIC: return "Arabic";
            case BIGALPHA: return "Big Alpha";
            case BIGROMAN: return "Big Roman";
            case LITTLEALPHA: return "Little Alpha";
            case LITTLEROMAN: return "Little Roman";
            case _UNORDEREDLISTSTYLE: return "Unordered List Style";
            case CIRCLE: return "Circle";
            case DISC: return "Disc";
            case SQUARE: return "Square";
            case _TABLERULESTYLE: return "Table Rule Style";
            case BOTRULE: return "Bottom Rule";
            case LRULE: return "Left-sided rule";
            case RRULE: return "Right-sided rule";
            case TOPRULE: return "Top Rule";
            default: return "?";
          }
    }


}

