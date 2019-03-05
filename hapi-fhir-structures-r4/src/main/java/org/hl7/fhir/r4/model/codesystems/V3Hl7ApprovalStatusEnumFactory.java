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

public class V3Hl7ApprovalStatusEnumFactory implements EnumFactory<V3Hl7ApprovalStatus> {

  public V3Hl7ApprovalStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("affd".equals(codeString))
      return V3Hl7ApprovalStatus.AFFD;
    if ("affi".equals(codeString))
      return V3Hl7ApprovalStatus.AFFI;
    if ("affn".equals(codeString))
      return V3Hl7ApprovalStatus.AFFN;
    if ("appad".equals(codeString))
      return V3Hl7ApprovalStatus.APPAD;
    if ("appai".equals(codeString))
      return V3Hl7ApprovalStatus.APPAI;
    if ("appan".equals(codeString))
      return V3Hl7ApprovalStatus.APPAN;
    if ("appd".equals(codeString))
      return V3Hl7ApprovalStatus.APPD;
    if ("appi".equals(codeString))
      return V3Hl7ApprovalStatus.APPI;
    if ("appn".equals(codeString))
      return V3Hl7ApprovalStatus.APPN;
    if ("comi".equals(codeString))
      return V3Hl7ApprovalStatus.COMI;
    if ("comn".equals(codeString))
      return V3Hl7ApprovalStatus.COMN;
    if ("draft".equals(codeString))
      return V3Hl7ApprovalStatus.DRAFT;
    if ("loc".equals(codeString))
      return V3Hl7ApprovalStatus.LOC;
    if ("memd".equals(codeString))
      return V3Hl7ApprovalStatus.MEMD;
    if ("memi".equals(codeString))
      return V3Hl7ApprovalStatus.MEMI;
    if ("memn".equals(codeString))
      return V3Hl7ApprovalStatus.MEMN;
    if ("ns".equals(codeString))
      return V3Hl7ApprovalStatus.NS;
    if ("prop".equals(codeString))
      return V3Hl7ApprovalStatus.PROP;
    if ("ref".equals(codeString))
      return V3Hl7ApprovalStatus.REF;
    if ("wd".equals(codeString))
      return V3Hl7ApprovalStatus.WD;
    throw new IllegalArgumentException("Unknown V3Hl7ApprovalStatus code '"+codeString+"'");
  }

  public String toCode(V3Hl7ApprovalStatus code) {
    if (code == V3Hl7ApprovalStatus.AFFD)
      return "affd";
    if (code == V3Hl7ApprovalStatus.AFFI)
      return "affi";
    if (code == V3Hl7ApprovalStatus.AFFN)
      return "affn";
    if (code == V3Hl7ApprovalStatus.APPAD)
      return "appad";
    if (code == V3Hl7ApprovalStatus.APPAI)
      return "appai";
    if (code == V3Hl7ApprovalStatus.APPAN)
      return "appan";
    if (code == V3Hl7ApprovalStatus.APPD)
      return "appd";
    if (code == V3Hl7ApprovalStatus.APPI)
      return "appi";
    if (code == V3Hl7ApprovalStatus.APPN)
      return "appn";
    if (code == V3Hl7ApprovalStatus.COMI)
      return "comi";
    if (code == V3Hl7ApprovalStatus.COMN)
      return "comn";
    if (code == V3Hl7ApprovalStatus.DRAFT)
      return "draft";
    if (code == V3Hl7ApprovalStatus.LOC)
      return "loc";
    if (code == V3Hl7ApprovalStatus.MEMD)
      return "memd";
    if (code == V3Hl7ApprovalStatus.MEMI)
      return "memi";
    if (code == V3Hl7ApprovalStatus.MEMN)
      return "memn";
    if (code == V3Hl7ApprovalStatus.NS)
      return "ns";
    if (code == V3Hl7ApprovalStatus.PROP)
      return "prop";
    if (code == V3Hl7ApprovalStatus.REF)
      return "ref";
    if (code == V3Hl7ApprovalStatus.WD)
      return "wd";
    return "?";
  }

    public String toSystem(V3Hl7ApprovalStatus code) {
      return code.getSystem();
      }

}

