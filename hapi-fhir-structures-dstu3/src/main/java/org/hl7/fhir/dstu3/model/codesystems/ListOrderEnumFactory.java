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

public class ListOrderEnumFactory implements EnumFactory<ListOrder> {

  public ListOrder fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("user".equals(codeString))
      return ListOrder.USER;
    if ("system".equals(codeString))
      return ListOrder.SYSTEM;
    if ("event-date".equals(codeString))
      return ListOrder.EVENTDATE;
    if ("entry-date".equals(codeString))
      return ListOrder.ENTRYDATE;
    if ("priority".equals(codeString))
      return ListOrder.PRIORITY;
    if ("alphabetic".equals(codeString))
      return ListOrder.ALPHABETIC;
    if ("category".equals(codeString))
      return ListOrder.CATEGORY;
    if ("patient".equals(codeString))
      return ListOrder.PATIENT;
    throw new IllegalArgumentException("Unknown ListOrder code '"+codeString+"'");
  }

  public String toCode(ListOrder code) {
    if (code == ListOrder.USER)
      return "user";
    if (code == ListOrder.SYSTEM)
      return "system";
    if (code == ListOrder.EVENTDATE)
      return "event-date";
    if (code == ListOrder.ENTRYDATE)
      return "entry-date";
    if (code == ListOrder.PRIORITY)
      return "priority";
    if (code == ListOrder.ALPHABETIC)
      return "alphabetic";
    if (code == ListOrder.CATEGORY)
      return "category";
    if (code == ListOrder.PATIENT)
      return "patient";
    return "?";
  }

    public String toSystem(ListOrder code) {
      return code.getSystem();
      }

}

