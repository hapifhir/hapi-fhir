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

public class ConsentActionEnumFactory implements EnumFactory<ConsentAction> {

  public ConsentAction fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("collect".equals(codeString))
      return ConsentAction.COLLECT;
    if ("access".equals(codeString))
      return ConsentAction.ACCESS;
    if ("use".equals(codeString))
      return ConsentAction.USE;
    if ("disclose".equals(codeString))
      return ConsentAction.DISCLOSE;
    if ("correct".equals(codeString))
      return ConsentAction.CORRECT;
    throw new IllegalArgumentException("Unknown ConsentAction code '"+codeString+"'");
  }

  public String toCode(ConsentAction code) {
    if (code == ConsentAction.COLLECT)
      return "collect";
    if (code == ConsentAction.ACCESS)
      return "access";
    if (code == ConsentAction.USE)
      return "use";
    if (code == ConsentAction.DISCLOSE)
      return "disclose";
    if (code == ConsentAction.CORRECT)
      return "correct";
    return "?";
  }

    public String toSystem(ConsentAction code) {
      return code.getSystem();
      }

}

