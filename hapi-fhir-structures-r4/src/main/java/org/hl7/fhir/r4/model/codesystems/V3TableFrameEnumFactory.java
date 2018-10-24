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


import org.hl7.fhir.r4.model.EnumFactory;

public class V3TableFrameEnumFactory implements EnumFactory<V3TableFrame> {

  public V3TableFrame fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("above".equals(codeString))
      return V3TableFrame.ABOVE;
    if ("below".equals(codeString))
      return V3TableFrame.BELOW;
    if ("border".equals(codeString))
      return V3TableFrame.BORDER;
    if ("box".equals(codeString))
      return V3TableFrame.BOX;
    if ("hsides".equals(codeString))
      return V3TableFrame.HSIDES;
    if ("lhs".equals(codeString))
      return V3TableFrame.LHS;
    if ("rhs".equals(codeString))
      return V3TableFrame.RHS;
    if ("void".equals(codeString))
      return V3TableFrame.VOID;
    if ("vsides".equals(codeString))
      return V3TableFrame.VSIDES;
    throw new IllegalArgumentException("Unknown V3TableFrame code '"+codeString+"'");
  }

  public String toCode(V3TableFrame code) {
    if (code == V3TableFrame.ABOVE)
      return "above";
    if (code == V3TableFrame.BELOW)
      return "below";
    if (code == V3TableFrame.BORDER)
      return "border";
    if (code == V3TableFrame.BOX)
      return "box";
    if (code == V3TableFrame.HSIDES)
      return "hsides";
    if (code == V3TableFrame.LHS)
      return "lhs";
    if (code == V3TableFrame.RHS)
      return "rhs";
    if (code == V3TableFrame.VOID)
      return "void";
    if (code == V3TableFrame.VSIDES)
      return "vsides";
    return "?";
  }

    public String toSystem(V3TableFrame code) {
      return code.getSystem();
      }

}

