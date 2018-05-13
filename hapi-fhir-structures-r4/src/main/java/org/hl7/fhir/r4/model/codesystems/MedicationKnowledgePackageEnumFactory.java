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

public class MedicationKnowledgePackageEnumFactory implements EnumFactory<MedicationKnowledgePackage> {

  public MedicationKnowledgePackage fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("amp".equals(codeString))
      return MedicationKnowledgePackage.AMP;
    if ("bag".equals(codeString))
      return MedicationKnowledgePackage.BAG;
    if ("blstrpk".equals(codeString))
      return MedicationKnowledgePackage.BLSTRPK;
    if ("bot".equals(codeString))
      return MedicationKnowledgePackage.BOT;
    if ("box".equals(codeString))
      return MedicationKnowledgePackage.BOX;
    if ("can".equals(codeString))
      return MedicationKnowledgePackage.CAN;
    if ("cart".equals(codeString))
      return MedicationKnowledgePackage.CART;
    if ("cnstr".equals(codeString))
      return MedicationKnowledgePackage.CNSTR;
    if ("disk".equals(codeString))
      return MedicationKnowledgePackage.DISK;
    if ("doset".equals(codeString))
      return MedicationKnowledgePackage.DOSET;
    if ("jar".equals(codeString))
      return MedicationKnowledgePackage.JAR;
    if ("jug".equals(codeString))
      return MedicationKnowledgePackage.JUG;
    if ("minim".equals(codeString))
      return MedicationKnowledgePackage.MINIM;
    if ("nebamp".equals(codeString))
      return MedicationKnowledgePackage.NEBAMP;
    if ("ovul".equals(codeString))
      return MedicationKnowledgePackage.OVUL;
    if ("pch".equals(codeString))
      return MedicationKnowledgePackage.PCH;
    if ("pkt".equals(codeString))
      return MedicationKnowledgePackage.PKT;
    if ("sash".equals(codeString))
      return MedicationKnowledgePackage.SASH;
    if ("strip".equals(codeString))
      return MedicationKnowledgePackage.STRIP;
    if ("tin".equals(codeString))
      return MedicationKnowledgePackage.TIN;
    if ("tub".equals(codeString))
      return MedicationKnowledgePackage.TUB;
    if ("tube".equals(codeString))
      return MedicationKnowledgePackage.TUBE;
    if ("vial".equals(codeString))
      return MedicationKnowledgePackage.VIAL;
    throw new IllegalArgumentException("Unknown MedicationKnowledgePackage code '"+codeString+"'");
  }

  public String toCode(MedicationKnowledgePackage code) {
    if (code == MedicationKnowledgePackage.AMP)
      return "amp";
    if (code == MedicationKnowledgePackage.BAG)
      return "bag";
    if (code == MedicationKnowledgePackage.BLSTRPK)
      return "blstrpk";
    if (code == MedicationKnowledgePackage.BOT)
      return "bot";
    if (code == MedicationKnowledgePackage.BOX)
      return "box";
    if (code == MedicationKnowledgePackage.CAN)
      return "can";
    if (code == MedicationKnowledgePackage.CART)
      return "cart";
    if (code == MedicationKnowledgePackage.CNSTR)
      return "cnstr";
    if (code == MedicationKnowledgePackage.DISK)
      return "disk";
    if (code == MedicationKnowledgePackage.DOSET)
      return "doset";
    if (code == MedicationKnowledgePackage.JAR)
      return "jar";
    if (code == MedicationKnowledgePackage.JUG)
      return "jug";
    if (code == MedicationKnowledgePackage.MINIM)
      return "minim";
    if (code == MedicationKnowledgePackage.NEBAMP)
      return "nebamp";
    if (code == MedicationKnowledgePackage.OVUL)
      return "ovul";
    if (code == MedicationKnowledgePackage.PCH)
      return "pch";
    if (code == MedicationKnowledgePackage.PKT)
      return "pkt";
    if (code == MedicationKnowledgePackage.SASH)
      return "sash";
    if (code == MedicationKnowledgePackage.STRIP)
      return "strip";
    if (code == MedicationKnowledgePackage.TIN)
      return "tin";
    if (code == MedicationKnowledgePackage.TUB)
      return "tub";
    if (code == MedicationKnowledgePackage.TUBE)
      return "tube";
    if (code == MedicationKnowledgePackage.VIAL)
      return "vial";
    return "?";
  }

    public String toSystem(MedicationKnowledgePackage code) {
      return code.getSystem();
      }

}

