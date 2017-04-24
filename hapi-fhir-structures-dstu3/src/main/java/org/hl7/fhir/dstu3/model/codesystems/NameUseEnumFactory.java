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

public class NameUseEnumFactory implements EnumFactory<NameUse> {

  public NameUse fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("usual".equals(codeString))
      return NameUse.USUAL;
    if ("official".equals(codeString))
      return NameUse.OFFICIAL;
    if ("temp".equals(codeString))
      return NameUse.TEMP;
    if ("nickname".equals(codeString))
      return NameUse.NICKNAME;
    if ("anonymous".equals(codeString))
      return NameUse.ANONYMOUS;
    if ("old".equals(codeString))
      return NameUse.OLD;
    if ("maiden".equals(codeString))
      return NameUse.MAIDEN;
    throw new IllegalArgumentException("Unknown NameUse code '"+codeString+"'");
  }

  public String toCode(NameUse code) {
    if (code == NameUse.USUAL)
      return "usual";
    if (code == NameUse.OFFICIAL)
      return "official";
    if (code == NameUse.TEMP)
      return "temp";
    if (code == NameUse.NICKNAME)
      return "nickname";
    if (code == NameUse.ANONYMOUS)
      return "anonymous";
    if (code == NameUse.OLD)
      return "old";
    if (code == NameUse.MAIDEN)
      return "maiden";
    return "?";
  }

    public String toSystem(NameUse code) {
      return code.getSystem();
      }

}

