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

public class SurfaceEnumFactory implements EnumFactory<Surface> {

  public Surface fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("M".equals(codeString))
      return Surface.M;
    if ("O".equals(codeString))
      return Surface.O;
    if ("I".equals(codeString))
      return Surface.I;
    if ("D".equals(codeString))
      return Surface.D;
    if ("B".equals(codeString))
      return Surface.B;
    if ("V".equals(codeString))
      return Surface.V;
    if ("L".equals(codeString))
      return Surface.L;
    if ("MO".equals(codeString))
      return Surface.MO;
    if ("DO".equals(codeString))
      return Surface.DO;
    if ("DI".equals(codeString))
      return Surface.DI;
    if ("MOD".equals(codeString))
      return Surface.MOD;
    throw new IllegalArgumentException("Unknown Surface code '"+codeString+"'");
  }

  public String toCode(Surface code) {
    if (code == Surface.M)
      return "M";
    if (code == Surface.O)
      return "O";
    if (code == Surface.I)
      return "I";
    if (code == Surface.D)
      return "D";
    if (code == Surface.B)
      return "B";
    if (code == Surface.V)
      return "V";
    if (code == Surface.L)
      return "L";
    if (code == Surface.MO)
      return "MO";
    if (code == Surface.DO)
      return "DO";
    if (code == Surface.DI)
      return "DI";
    if (code == Surface.MOD)
      return "MOD";
    return "?";
  }

    public String toSystem(Surface code) {
      return code.getSystem();
      }

}

