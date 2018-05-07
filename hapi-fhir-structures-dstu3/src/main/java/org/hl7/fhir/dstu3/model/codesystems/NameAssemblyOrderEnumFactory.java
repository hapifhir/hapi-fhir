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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class NameAssemblyOrderEnumFactory implements EnumFactory<NameAssemblyOrder> {

  public NameAssemblyOrder fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("NL1".equals(codeString))
      return NameAssemblyOrder.NL1;
    if ("NL2".equals(codeString))
      return NameAssemblyOrder.NL2;
    if ("NL3".equals(codeString))
      return NameAssemblyOrder.NL3;
    if ("NL4".equals(codeString))
      return NameAssemblyOrder.NL4;
    throw new IllegalArgumentException("Unknown NameAssemblyOrder code '"+codeString+"'");
  }

  public String toCode(NameAssemblyOrder code) {
    if (code == NameAssemblyOrder.NL1)
      return "NL1";
    if (code == NameAssemblyOrder.NL2)
      return "NL2";
    if (code == NameAssemblyOrder.NL3)
      return "NL3";
    if (code == NameAssemblyOrder.NL4)
      return "NL4";
    return "?";
  }

    public String toSystem(NameAssemblyOrder code) {
      return code.getSystem();
      }

}

