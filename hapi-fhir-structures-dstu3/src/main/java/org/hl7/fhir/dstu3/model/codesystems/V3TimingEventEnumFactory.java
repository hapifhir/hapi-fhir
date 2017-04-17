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

public class V3TimingEventEnumFactory implements EnumFactory<V3TimingEvent> {

  public V3TimingEvent fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AC".equals(codeString))
      return V3TimingEvent.AC;
    if ("ACD".equals(codeString))
      return V3TimingEvent.ACD;
    if ("ACM".equals(codeString))
      return V3TimingEvent.ACM;
    if ("ACV".equals(codeString))
      return V3TimingEvent.ACV;
    if ("C".equals(codeString))
      return V3TimingEvent.C;
    if ("CD".equals(codeString))
      return V3TimingEvent.CD;
    if ("CM".equals(codeString))
      return V3TimingEvent.CM;
    if ("CV".equals(codeString))
      return V3TimingEvent.CV;
    if ("HS".equals(codeString))
      return V3TimingEvent.HS;
    if ("IC".equals(codeString))
      return V3TimingEvent.IC;
    if ("ICD".equals(codeString))
      return V3TimingEvent.ICD;
    if ("ICM".equals(codeString))
      return V3TimingEvent.ICM;
    if ("ICV".equals(codeString))
      return V3TimingEvent.ICV;
    if ("PC".equals(codeString))
      return V3TimingEvent.PC;
    if ("PCD".equals(codeString))
      return V3TimingEvent.PCD;
    if ("PCM".equals(codeString))
      return V3TimingEvent.PCM;
    if ("PCV".equals(codeString))
      return V3TimingEvent.PCV;
    if ("WAKE".equals(codeString))
      return V3TimingEvent.WAKE;
    throw new IllegalArgumentException("Unknown V3TimingEvent code '"+codeString+"'");
  }

  public String toCode(V3TimingEvent code) {
    if (code == V3TimingEvent.AC)
      return "AC";
    if (code == V3TimingEvent.ACD)
      return "ACD";
    if (code == V3TimingEvent.ACM)
      return "ACM";
    if (code == V3TimingEvent.ACV)
      return "ACV";
    if (code == V3TimingEvent.C)
      return "C";
    if (code == V3TimingEvent.CD)
      return "CD";
    if (code == V3TimingEvent.CM)
      return "CM";
    if (code == V3TimingEvent.CV)
      return "CV";
    if (code == V3TimingEvent.HS)
      return "HS";
    if (code == V3TimingEvent.IC)
      return "IC";
    if (code == V3TimingEvent.ICD)
      return "ICD";
    if (code == V3TimingEvent.ICM)
      return "ICM";
    if (code == V3TimingEvent.ICV)
      return "ICV";
    if (code == V3TimingEvent.PC)
      return "PC";
    if (code == V3TimingEvent.PCD)
      return "PCD";
    if (code == V3TimingEvent.PCM)
      return "PCM";
    if (code == V3TimingEvent.PCV)
      return "PCV";
    if (code == V3TimingEvent.WAKE)
      return "WAKE";
    return "?";
  }

    public String toSystem(V3TimingEvent code) {
      return code.getSystem();
      }

}

