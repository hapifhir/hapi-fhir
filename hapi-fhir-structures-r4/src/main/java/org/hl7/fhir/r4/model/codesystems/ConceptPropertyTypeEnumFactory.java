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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class ConceptPropertyTypeEnumFactory implements EnumFactory<ConceptPropertyType> {

  public ConceptPropertyType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("code".equals(codeString))
      return ConceptPropertyType.CODE;
    if ("Coding".equals(codeString))
      return ConceptPropertyType.CODING;
    if ("string".equals(codeString))
      return ConceptPropertyType.STRING;
    if ("integer".equals(codeString))
      return ConceptPropertyType.INTEGER;
    if ("boolean".equals(codeString))
      return ConceptPropertyType.BOOLEAN;
    if ("dateTime".equals(codeString))
      return ConceptPropertyType.DATETIME;
    if ("decimal".equals(codeString))
      return ConceptPropertyType.DECIMAL;
    throw new IllegalArgumentException("Unknown ConceptPropertyType code '"+codeString+"'");
  }

  public String toCode(ConceptPropertyType code) {
    if (code == ConceptPropertyType.CODE)
      return "code";
    if (code == ConceptPropertyType.CODING)
      return "Coding";
    if (code == ConceptPropertyType.STRING)
      return "string";
    if (code == ConceptPropertyType.INTEGER)
      return "integer";
    if (code == ConceptPropertyType.BOOLEAN)
      return "boolean";
    if (code == ConceptPropertyType.DATETIME)
      return "dateTime";
    if (code == ConceptPropertyType.DECIMAL)
      return "decimal";
    return "?";
  }

    public String toSystem(ConceptPropertyType code) {
      return code.getSystem();
      }

}

