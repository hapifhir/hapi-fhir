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

// Generated on Thu, Feb 9, 2017 08:03-0500 for FHIR v1.9.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class DeviceSafetyEnumFactory implements EnumFactory<DeviceSafety> {

  public DeviceSafety fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("contains-latex".equals(codeString))
      return DeviceSafety.CONTAINSLATEX;
    if ("latex-free".equals(codeString))
      return DeviceSafety.LATEXFREE;
    if ("latex-unknown".equals(codeString))
      return DeviceSafety.LATEXUNKNOWN;
    if ("mr-safe".equals(codeString))
      return DeviceSafety.MRSAFE;
    if ("mr-unsafe".equals(codeString))
      return DeviceSafety.MRUNSAFE;
    if ("mr-conditional".equals(codeString))
      return DeviceSafety.MRCONDITIONAL;
    if ("mr-unknown".equals(codeString))
      return DeviceSafety.MRUNKNOWN;
    throw new IllegalArgumentException("Unknown DeviceSafety code '"+codeString+"'");
  }

  public String toCode(DeviceSafety code) {
    if (code == DeviceSafety.CONTAINSLATEX)
      return "contains-latex";
    if (code == DeviceSafety.LATEXFREE)
      return "latex-free";
    if (code == DeviceSafety.LATEXUNKNOWN)
      return "latex-unknown";
    if (code == DeviceSafety.MRSAFE)
      return "mr-safe";
    if (code == DeviceSafety.MRUNSAFE)
      return "mr-unsafe";
    if (code == DeviceSafety.MRCONDITIONAL)
      return "mr-conditional";
    if (code == DeviceSafety.MRUNKNOWN)
      return "mr-unknown";
    return "?";
  }

    public String toSystem(DeviceSafety code) {
      return code.getSystem();
      }

}

