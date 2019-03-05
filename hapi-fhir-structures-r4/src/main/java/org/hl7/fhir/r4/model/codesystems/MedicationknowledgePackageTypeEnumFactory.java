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

public class MedicationknowledgePackageTypeEnumFactory implements EnumFactory<MedicationknowledgePackageType> {

  public MedicationknowledgePackageType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("amp".equals(codeString))
      return MedicationknowledgePackageType.AMP;
    if ("bag".equals(codeString))
      return MedicationknowledgePackageType.BAG;
    if ("blstrpk".equals(codeString))
      return MedicationknowledgePackageType.BLSTRPK;
    if ("bot".equals(codeString))
      return MedicationknowledgePackageType.BOT;
    if ("box".equals(codeString))
      return MedicationknowledgePackageType.BOX;
    if ("can".equals(codeString))
      return MedicationknowledgePackageType.CAN;
    if ("cart".equals(codeString))
      return MedicationknowledgePackageType.CART;
    if ("disk".equals(codeString))
      return MedicationknowledgePackageType.DISK;
    if ("doset".equals(codeString))
      return MedicationknowledgePackageType.DOSET;
    if ("jar".equals(codeString))
      return MedicationknowledgePackageType.JAR;
    if ("jug".equals(codeString))
      return MedicationknowledgePackageType.JUG;
    if ("minim".equals(codeString))
      return MedicationknowledgePackageType.MINIM;
    if ("nebamp".equals(codeString))
      return MedicationknowledgePackageType.NEBAMP;
    if ("ovul".equals(codeString))
      return MedicationknowledgePackageType.OVUL;
    if ("pch".equals(codeString))
      return MedicationknowledgePackageType.PCH;
    if ("pkt".equals(codeString))
      return MedicationknowledgePackageType.PKT;
    if ("sash".equals(codeString))
      return MedicationknowledgePackageType.SASH;
    if ("strip".equals(codeString))
      return MedicationknowledgePackageType.STRIP;
    if ("tin".equals(codeString))
      return MedicationknowledgePackageType.TIN;
    if ("tub".equals(codeString))
      return MedicationknowledgePackageType.TUB;
    if ("tube".equals(codeString))
      return MedicationknowledgePackageType.TUBE;
    if ("vial".equals(codeString))
      return MedicationknowledgePackageType.VIAL;
    throw new IllegalArgumentException("Unknown MedicationknowledgePackageType code '"+codeString+"'");
  }

  public String toCode(MedicationknowledgePackageType code) {
    if (code == MedicationknowledgePackageType.AMP)
      return "amp";
    if (code == MedicationknowledgePackageType.BAG)
      return "bag";
    if (code == MedicationknowledgePackageType.BLSTRPK)
      return "blstrpk";
    if (code == MedicationknowledgePackageType.BOT)
      return "bot";
    if (code == MedicationknowledgePackageType.BOX)
      return "box";
    if (code == MedicationknowledgePackageType.CAN)
      return "can";
    if (code == MedicationknowledgePackageType.CART)
      return "cart";
    if (code == MedicationknowledgePackageType.DISK)
      return "disk";
    if (code == MedicationknowledgePackageType.DOSET)
      return "doset";
    if (code == MedicationknowledgePackageType.JAR)
      return "jar";
    if (code == MedicationknowledgePackageType.JUG)
      return "jug";
    if (code == MedicationknowledgePackageType.MINIM)
      return "minim";
    if (code == MedicationknowledgePackageType.NEBAMP)
      return "nebamp";
    if (code == MedicationknowledgePackageType.OVUL)
      return "ovul";
    if (code == MedicationknowledgePackageType.PCH)
      return "pch";
    if (code == MedicationknowledgePackageType.PKT)
      return "pkt";
    if (code == MedicationknowledgePackageType.SASH)
      return "sash";
    if (code == MedicationknowledgePackageType.STRIP)
      return "strip";
    if (code == MedicationknowledgePackageType.TIN)
      return "tin";
    if (code == MedicationknowledgePackageType.TUB)
      return "tub";
    if (code == MedicationknowledgePackageType.TUBE)
      return "tube";
    if (code == MedicationknowledgePackageType.VIAL)
      return "vial";
    return "?";
  }

    public String toSystem(MedicationknowledgePackageType code) {
      return code.getSystem();
      }

}

