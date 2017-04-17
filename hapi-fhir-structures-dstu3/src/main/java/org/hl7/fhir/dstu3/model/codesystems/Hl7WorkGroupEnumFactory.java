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

public class Hl7WorkGroupEnumFactory implements EnumFactory<Hl7WorkGroup> {

  public Hl7WorkGroup fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("cbcc".equals(codeString))
      return Hl7WorkGroup.CBCC;
    if ("cds".equals(codeString))
      return Hl7WorkGroup.CDS;
    if ("cqi".equals(codeString))
      return Hl7WorkGroup.CQI;
    if ("cg".equals(codeString))
      return Hl7WorkGroup.CG;
    if ("dev".equals(codeString))
      return Hl7WorkGroup.DEV;
    if ("ehr".equals(codeString))
      return Hl7WorkGroup.EHR;
    if ("fhir".equals(codeString))
      return Hl7WorkGroup.FHIR;
    if ("fm".equals(codeString))
      return Hl7WorkGroup.FM;
    if ("hsi".equals(codeString))
      return Hl7WorkGroup.HSI;
    if ("ii".equals(codeString))
      return Hl7WorkGroup.II;
    if ("inm".equals(codeString))
      return Hl7WorkGroup.INM;
    if ("its".equals(codeString))
      return Hl7WorkGroup.ITS;
    if ("oo".equals(codeString))
      return Hl7WorkGroup.OO;
    if ("pa".equals(codeString))
      return Hl7WorkGroup.PA;
    if ("pc".equals(codeString))
      return Hl7WorkGroup.PC;
    if ("pher".equals(codeString))
      return Hl7WorkGroup.PHER;
    if ("phx".equals(codeString))
      return Hl7WorkGroup.PHX;
    if ("rcrim".equals(codeString))
      return Hl7WorkGroup.RCRIM;
    if ("sd".equals(codeString))
      return Hl7WorkGroup.SD;
    if ("sec".equals(codeString))
      return Hl7WorkGroup.SEC;
    if ("us".equals(codeString))
      return Hl7WorkGroup.US;
    if ("vocab".equals(codeString))
      return Hl7WorkGroup.VOCAB;
    if ("aid".equals(codeString))
      return Hl7WorkGroup.AID;
    throw new IllegalArgumentException("Unknown Hl7WorkGroup code '"+codeString+"'");
  }

  public String toCode(Hl7WorkGroup code) {
    if (code == Hl7WorkGroup.CBCC)
      return "cbcc";
    if (code == Hl7WorkGroup.CDS)
      return "cds";
    if (code == Hl7WorkGroup.CQI)
      return "cqi";
    if (code == Hl7WorkGroup.CG)
      return "cg";
    if (code == Hl7WorkGroup.DEV)
      return "dev";
    if (code == Hl7WorkGroup.EHR)
      return "ehr";
    if (code == Hl7WorkGroup.FHIR)
      return "fhir";
    if (code == Hl7WorkGroup.FM)
      return "fm";
    if (code == Hl7WorkGroup.HSI)
      return "hsi";
    if (code == Hl7WorkGroup.II)
      return "ii";
    if (code == Hl7WorkGroup.INM)
      return "inm";
    if (code == Hl7WorkGroup.ITS)
      return "its";
    if (code == Hl7WorkGroup.OO)
      return "oo";
    if (code == Hl7WorkGroup.PA)
      return "pa";
    if (code == Hl7WorkGroup.PC)
      return "pc";
    if (code == Hl7WorkGroup.PHER)
      return "pher";
    if (code == Hl7WorkGroup.PHX)
      return "phx";
    if (code == Hl7WorkGroup.RCRIM)
      return "rcrim";
    if (code == Hl7WorkGroup.SD)
      return "sd";
    if (code == Hl7WorkGroup.SEC)
      return "sec";
    if (code == Hl7WorkGroup.US)
      return "us";
    if (code == Hl7WorkGroup.VOCAB)
      return "vocab";
    if (code == Hl7WorkGroup.AID)
      return "aid";
    return "?";
  }

    public String toSystem(Hl7WorkGroup code) {
      return code.getSystem();
      }

}

