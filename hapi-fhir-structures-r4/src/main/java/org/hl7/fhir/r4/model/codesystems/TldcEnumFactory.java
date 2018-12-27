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

public class TldcEnumFactory implements EnumFactory<Tldc> {

  public Tldc fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("draft".equals(codeString))
      return Tldc.DRAFT;
    if ("pending".equals(codeString))
      return Tldc.PENDING;
    if ("active".equals(codeString))
      return Tldc.ACTIVE;
    if ("review".equals(codeString))
      return Tldc.REVIEW;
    if ("cancelled".equals(codeString))
      return Tldc.CANCELLED;
    if ("rejected".equals(codeString))
      return Tldc.REJECTED;
    if ("retired".equals(codeString))
      return Tldc.RETIRED;
    if ("terminated".equals(codeString))
      return Tldc.TERMINATED;
    throw new IllegalArgumentException("Unknown Tldc code '"+codeString+"'");
  }

  public String toCode(Tldc code) {
    if (code == Tldc.DRAFT)
      return "draft";
    if (code == Tldc.PENDING)
      return "pending";
    if (code == Tldc.ACTIVE)
      return "active";
    if (code == Tldc.REVIEW)
      return "review";
    if (code == Tldc.CANCELLED)
      return "cancelled";
    if (code == Tldc.REJECTED)
      return "rejected";
    if (code == Tldc.RETIRED)
      return "retired";
    if (code == Tldc.TERMINATED)
      return "terminated";
    return "?";
  }

    public String toSystem(Tldc code) {
      return code.getSystem();
      }

}

