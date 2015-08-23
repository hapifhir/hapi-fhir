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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class V3RoleLinkTypeEnumFactory implements EnumFactory<V3RoleLinkType> {

  public V3RoleLinkType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("REL".equals(codeString))
      return V3RoleLinkType.REL;
    if ("BACKUP".equals(codeString))
      return V3RoleLinkType.BACKUP;
    if ("CONT".equals(codeString))
      return V3RoleLinkType.CONT;
    if ("DIRAUTH".equals(codeString))
      return V3RoleLinkType.DIRAUTH;
    if ("IDENT".equals(codeString))
      return V3RoleLinkType.IDENT;
    if ("INDAUTH".equals(codeString))
      return V3RoleLinkType.INDAUTH;
    if ("PART".equals(codeString))
      return V3RoleLinkType.PART;
    if ("REPL".equals(codeString))
      return V3RoleLinkType.REPL;
    throw new IllegalArgumentException("Unknown V3RoleLinkType code '"+codeString+"'");
  }

  public String toCode(V3RoleLinkType code) {
    if (code == V3RoleLinkType.REL)
      return "REL";
    if (code == V3RoleLinkType.BACKUP)
      return "BACKUP";
    if (code == V3RoleLinkType.CONT)
      return "CONT";
    if (code == V3RoleLinkType.DIRAUTH)
      return "DIRAUTH";
    if (code == V3RoleLinkType.IDENT)
      return "IDENT";
    if (code == V3RoleLinkType.INDAUTH)
      return "INDAUTH";
    if (code == V3RoleLinkType.PART)
      return "PART";
    if (code == V3RoleLinkType.REPL)
      return "REPL";
    return "?";
  }


}

