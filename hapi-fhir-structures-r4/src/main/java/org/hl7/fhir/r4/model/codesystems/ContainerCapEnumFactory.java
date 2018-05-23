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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class ContainerCapEnumFactory implements EnumFactory<ContainerCap> {

  public ContainerCap fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("red".equals(codeString))
      return ContainerCap.RED;
    if ("yellow".equals(codeString))
      return ContainerCap.YELLOW;
    if ("grey".equals(codeString))
      return ContainerCap.GREY;
    if ("violet".equals(codeString))
      return ContainerCap.VIOLET;
    if ("blue".equals(codeString))
      return ContainerCap.BLUE;
    if ("black".equals(codeString))
      return ContainerCap.BLACK;
    if ("green".equals(codeString))
      return ContainerCap.GREEN;
    throw new IllegalArgumentException("Unknown ContainerCap code '"+codeString+"'");
  }

  public String toCode(ContainerCap code) {
    if (code == ContainerCap.RED)
      return "red";
    if (code == ContainerCap.YELLOW)
      return "yellow";
    if (code == ContainerCap.GREY)
      return "grey";
    if (code == ContainerCap.VIOLET)
      return "violet";
    if (code == ContainerCap.BLUE)
      return "blue";
    if (code == ContainerCap.BLACK)
      return "black";
    if (code == ContainerCap.GREEN)
      return "green";
    return "?";
  }

    public String toSystem(ContainerCap code) {
      return code.getSystem();
      }

}

