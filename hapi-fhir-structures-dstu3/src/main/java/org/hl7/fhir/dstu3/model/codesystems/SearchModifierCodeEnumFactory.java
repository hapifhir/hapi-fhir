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


import org.hl7.fhir.dstu3.model.EnumFactory;

public class SearchModifierCodeEnumFactory implements EnumFactory<SearchModifierCode> {

  public SearchModifierCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("missing".equals(codeString))
      return SearchModifierCode.MISSING;
    if ("exact".equals(codeString))
      return SearchModifierCode.EXACT;
    if ("contains".equals(codeString))
      return SearchModifierCode.CONTAINS;
    if ("not".equals(codeString))
      return SearchModifierCode.NOT;
    if ("text".equals(codeString))
      return SearchModifierCode.TEXT;
    if ("in".equals(codeString))
      return SearchModifierCode.IN;
    if ("not-in".equals(codeString))
      return SearchModifierCode.NOTIN;
    if ("below".equals(codeString))
      return SearchModifierCode.BELOW;
    if ("above".equals(codeString))
      return SearchModifierCode.ABOVE;
    if ("type".equals(codeString))
      return SearchModifierCode.TYPE;
    throw new IllegalArgumentException("Unknown SearchModifierCode code '"+codeString+"'");
  }

  public String toCode(SearchModifierCode code) {
    if (code == SearchModifierCode.MISSING)
      return "missing";
    if (code == SearchModifierCode.EXACT)
      return "exact";
    if (code == SearchModifierCode.CONTAINS)
      return "contains";
    if (code == SearchModifierCode.NOT)
      return "not";
    if (code == SearchModifierCode.TEXT)
      return "text";
    if (code == SearchModifierCode.IN)
      return "in";
    if (code == SearchModifierCode.NOTIN)
      return "not-in";
    if (code == SearchModifierCode.BELOW)
      return "below";
    if (code == SearchModifierCode.ABOVE)
      return "above";
    if (code == SearchModifierCode.TYPE)
      return "type";
    return "?";
  }

    public String toSystem(SearchModifierCode code) {
      return code.getSystem();
      }

}

