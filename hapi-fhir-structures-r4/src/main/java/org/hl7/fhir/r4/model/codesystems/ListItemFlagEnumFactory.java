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

public class ListItemFlagEnumFactory implements EnumFactory<ListItemFlag> {

  public ListItemFlag fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("01".equals(codeString))
      return ListItemFlag._01;
    if ("02".equals(codeString))
      return ListItemFlag._02;
    if ("03".equals(codeString))
      return ListItemFlag._03;
    if ("04".equals(codeString))
      return ListItemFlag._04;
    if ("05".equals(codeString))
      return ListItemFlag._05;
    if ("06".equals(codeString))
      return ListItemFlag._06;
    throw new IllegalArgumentException("Unknown ListItemFlag code '"+codeString+"'");
  }

  public String toCode(ListItemFlag code) {
    if (code == ListItemFlag._01)
      return "01";
    if (code == ListItemFlag._02)
      return "02";
    if (code == ListItemFlag._03)
      return "03";
    if (code == ListItemFlag._04)
      return "04";
    if (code == ListItemFlag._05)
      return "05";
    if (code == ListItemFlag._06)
      return "06";
    return "?";
  }

    public String toSystem(ListItemFlag code) {
      return code.getSystem();
      }

}

