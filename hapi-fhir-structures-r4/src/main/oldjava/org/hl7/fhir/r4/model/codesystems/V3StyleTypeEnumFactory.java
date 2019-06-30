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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class V3StyleTypeEnumFactory implements EnumFactory<V3StyleType> {

  public V3StyleType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_FontStyle".equals(codeString))
      return V3StyleType._FONTSTYLE;
    if ("bold".equals(codeString))
      return V3StyleType.BOLD;
    if ("emphasis".equals(codeString))
      return V3StyleType.EMPHASIS;
    if ("italics".equals(codeString))
      return V3StyleType.ITALICS;
    if ("underline".equals(codeString))
      return V3StyleType.UNDERLINE;
    if ("_ListStyle".equals(codeString))
      return V3StyleType._LISTSTYLE;
    if ("_OrderedListStyle".equals(codeString))
      return V3StyleType._ORDEREDLISTSTYLE;
    if ("Arabic".equals(codeString))
      return V3StyleType.ARABIC;
    if ("BigAlpha".equals(codeString))
      return V3StyleType.BIGALPHA;
    if ("BigRoman".equals(codeString))
      return V3StyleType.BIGROMAN;
    if ("LittleAlpha".equals(codeString))
      return V3StyleType.LITTLEALPHA;
    if ("LittleRoman".equals(codeString))
      return V3StyleType.LITTLEROMAN;
    if ("_UnorderedListStyle".equals(codeString))
      return V3StyleType._UNORDEREDLISTSTYLE;
    if ("Circle".equals(codeString))
      return V3StyleType.CIRCLE;
    if ("Disc".equals(codeString))
      return V3StyleType.DISC;
    if ("Square".equals(codeString))
      return V3StyleType.SQUARE;
    if ("_TableRuleStyle".equals(codeString))
      return V3StyleType._TABLERULESTYLE;
    if ("Botrule".equals(codeString))
      return V3StyleType.BOTRULE;
    if ("Lrule".equals(codeString))
      return V3StyleType.LRULE;
    if ("Rrule".equals(codeString))
      return V3StyleType.RRULE;
    if ("Toprule".equals(codeString))
      return V3StyleType.TOPRULE;
    throw new IllegalArgumentException("Unknown V3StyleType code '"+codeString+"'");
  }

  public String toCode(V3StyleType code) {
    if (code == V3StyleType._FONTSTYLE)
      return "_FontStyle";
    if (code == V3StyleType.BOLD)
      return "bold";
    if (code == V3StyleType.EMPHASIS)
      return "emphasis";
    if (code == V3StyleType.ITALICS)
      return "italics";
    if (code == V3StyleType.UNDERLINE)
      return "underline";
    if (code == V3StyleType._LISTSTYLE)
      return "_ListStyle";
    if (code == V3StyleType._ORDEREDLISTSTYLE)
      return "_OrderedListStyle";
    if (code == V3StyleType.ARABIC)
      return "Arabic";
    if (code == V3StyleType.BIGALPHA)
      return "BigAlpha";
    if (code == V3StyleType.BIGROMAN)
      return "BigRoman";
    if (code == V3StyleType.LITTLEALPHA)
      return "LittleAlpha";
    if (code == V3StyleType.LITTLEROMAN)
      return "LittleRoman";
    if (code == V3StyleType._UNORDEREDLISTSTYLE)
      return "_UnorderedListStyle";
    if (code == V3StyleType.CIRCLE)
      return "Circle";
    if (code == V3StyleType.DISC)
      return "Disc";
    if (code == V3StyleType.SQUARE)
      return "Square";
    if (code == V3StyleType._TABLERULESTYLE)
      return "_TableRuleStyle";
    if (code == V3StyleType.BOTRULE)
      return "Botrule";
    if (code == V3StyleType.LRULE)
      return "Lrule";
    if (code == V3StyleType.RRULE)
      return "Rrule";
    if (code == V3StyleType.TOPRULE)
      return "Toprule";
    return "?";
  }

    public String toSystem(V3StyleType code) {
      return code.getSystem();
      }

}

