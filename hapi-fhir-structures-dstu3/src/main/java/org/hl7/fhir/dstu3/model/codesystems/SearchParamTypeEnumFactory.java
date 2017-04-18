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

public class SearchParamTypeEnumFactory implements EnumFactory<SearchParamType> {

  public SearchParamType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("number".equals(codeString))
      return SearchParamType.NUMBER;
    if ("date".equals(codeString))
      return SearchParamType.DATE;
    if ("string".equals(codeString))
      return SearchParamType.STRING;
    if ("token".equals(codeString))
      return SearchParamType.TOKEN;
    if ("reference".equals(codeString))
      return SearchParamType.REFERENCE;
    if ("composite".equals(codeString))
      return SearchParamType.COMPOSITE;
    if ("quantity".equals(codeString))
      return SearchParamType.QUANTITY;
    if ("uri".equals(codeString))
      return SearchParamType.URI;
    throw new IllegalArgumentException("Unknown SearchParamType code '"+codeString+"'");
  }

  public String toCode(SearchParamType code) {
    if (code == SearchParamType.NUMBER)
      return "number";
    if (code == SearchParamType.DATE)
      return "date";
    if (code == SearchParamType.STRING)
      return "string";
    if (code == SearchParamType.TOKEN)
      return "token";
    if (code == SearchParamType.REFERENCE)
      return "reference";
    if (code == SearchParamType.COMPOSITE)
      return "composite";
    if (code == SearchParamType.QUANTITY)
      return "quantity";
    if (code == SearchParamType.URI)
      return "uri";
    return "?";
  }

    public String toSystem(SearchParamType code) {
      return code.getSystem();
      }

}

