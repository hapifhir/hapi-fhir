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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class ContainerCapEnumFactory implements EnumFactory<ContainerCap> {

  public ContainerCap fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("red".equals(codeString))
      return ContainerCap.RED;
    if ("yellow".equals(codeString))
      return ContainerCap.YELLOW;
    if ("dark-yellow".equals(codeString))
      return ContainerCap.DARKYELLOW;
    if ("grey".equals(codeString))
      return ContainerCap.GREY;
    if ("light-blue".equals(codeString))
      return ContainerCap.LIGHTBLUE;
    if ("black".equals(codeString))
      return ContainerCap.BLACK;
    if ("green".equals(codeString))
      return ContainerCap.GREEN;
    if ("light-green".equals(codeString))
      return ContainerCap.LIGHTGREEN;
    if ("lavender".equals(codeString))
      return ContainerCap.LAVENDER;
    if ("brown".equals(codeString))
      return ContainerCap.BROWN;
    if ("white".equals(codeString))
      return ContainerCap.WHITE;
    if ("pink".equals(codeString))
      return ContainerCap.PINK;
    throw new IllegalArgumentException("Unknown ContainerCap code '"+codeString+"'");
  }

  public String toCode(ContainerCap code) {
    if (code == ContainerCap.RED)
      return "red";
    if (code == ContainerCap.YELLOW)
      return "yellow";
    if (code == ContainerCap.DARKYELLOW)
      return "dark-yellow";
    if (code == ContainerCap.GREY)
      return "grey";
    if (code == ContainerCap.LIGHTBLUE)
      return "light-blue";
    if (code == ContainerCap.BLACK)
      return "black";
    if (code == ContainerCap.GREEN)
      return "green";
    if (code == ContainerCap.LIGHTGREEN)
      return "light-green";
    if (code == ContainerCap.LAVENDER)
      return "lavender";
    if (code == ContainerCap.BROWN)
      return "brown";
    if (code == ContainerCap.WHITE)
      return "white";
    if (code == ContainerCap.PINK)
      return "pink";
    return "?";
  }

    public String toSystem(ContainerCap code) {
      return code.getSystem();
      }

}

