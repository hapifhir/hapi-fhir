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

public class V3ContainerCapEnumFactory implements EnumFactory<V3ContainerCap> {

  public V3ContainerCap fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_MedicationCap".equals(codeString))
      return V3ContainerCap._MEDICATIONCAP;
    if ("CHILD".equals(codeString))
      return V3ContainerCap.CHILD;
    if ("EASY".equals(codeString))
      return V3ContainerCap.EASY;
    if ("FILM".equals(codeString))
      return V3ContainerCap.FILM;
    if ("FOIL".equals(codeString))
      return V3ContainerCap.FOIL;
    if ("PUSH".equals(codeString))
      return V3ContainerCap.PUSH;
    if ("SCR".equals(codeString))
      return V3ContainerCap.SCR;
    throw new IllegalArgumentException("Unknown V3ContainerCap code '"+codeString+"'");
  }

  public String toCode(V3ContainerCap code) {
    if (code == V3ContainerCap._MEDICATIONCAP)
      return "_MedicationCap";
    if (code == V3ContainerCap.CHILD)
      return "CHILD";
    if (code == V3ContainerCap.EASY)
      return "EASY";
    if (code == V3ContainerCap.FILM)
      return "FILM";
    if (code == V3ContainerCap.FOIL)
      return "FOIL";
    if (code == V3ContainerCap.PUSH)
      return "PUSH";
    if (code == V3ContainerCap.SCR)
      return "SCR";
    return "?";
  }

    public String toSystem(V3ContainerCap code) {
      return code.getSystem();
      }

}

