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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum MedicationKnowledgePackage {

        /**
         * null
         */
        AMP, 
        /**
         * null
         */
        BAG, 
        /**
         * null
         */
        BLSTRPK, 
        /**
         * null
         */
        BOT, 
        /**
         * null
         */
        BOX, 
        /**
         * null
         */
        CAN, 
        /**
         * null
         */
        CART, 
        /**
         * null
         */
        CNSTR, 
        /**
         * null
         */
        DISK, 
        /**
         * null
         */
        DOSET, 
        /**
         * null
         */
        JAR, 
        /**
         * null
         */
        JUG, 
        /**
         * null
         */
        MINIM, 
        /**
         * null
         */
        NEBAMP, 
        /**
         * null
         */
        OVUL, 
        /**
         * null
         */
        PCH, 
        /**
         * null
         */
        PKT, 
        /**
         * null
         */
        SASH, 
        /**
         * null
         */
        STRIP, 
        /**
         * null
         */
        TIN, 
        /**
         * null
         */
        TUB, 
        /**
         * null
         */
        TUBE, 
        /**
         * null
         */
        VIAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationKnowledgePackage fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("amp".equals(codeString))
          return AMP;
        if ("bag".equals(codeString))
          return BAG;
        if ("blstrpk".equals(codeString))
          return BLSTRPK;
        if ("bot".equals(codeString))
          return BOT;
        if ("box".equals(codeString))
          return BOX;
        if ("can".equals(codeString))
          return CAN;
        if ("cart".equals(codeString))
          return CART;
        if ("cnstr".equals(codeString))
          return CNSTR;
        if ("disk".equals(codeString))
          return DISK;
        if ("doset".equals(codeString))
          return DOSET;
        if ("jar".equals(codeString))
          return JAR;
        if ("jug".equals(codeString))
          return JUG;
        if ("minim".equals(codeString))
          return MINIM;
        if ("nebamp".equals(codeString))
          return NEBAMP;
        if ("ovul".equals(codeString))
          return OVUL;
        if ("pch".equals(codeString))
          return PCH;
        if ("pkt".equals(codeString))
          return PKT;
        if ("sash".equals(codeString))
          return SASH;
        if ("strip".equals(codeString))
          return STRIP;
        if ("tin".equals(codeString))
          return TIN;
        if ("tub".equals(codeString))
          return TUB;
        if ("tube".equals(codeString))
          return TUBE;
        if ("vial".equals(codeString))
          return VIAL;
        throw new FHIRException("Unknown MedicationKnowledgePackage code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AMP: return "amp";
            case BAG: return "bag";
            case BLSTRPK: return "blstrpk";
            case BOT: return "bot";
            case BOX: return "box";
            case CAN: return "can";
            case CART: return "cart";
            case CNSTR: return "cnstr";
            case DISK: return "disk";
            case DOSET: return "doset";
            case JAR: return "jar";
            case JUG: return "jug";
            case MINIM: return "minim";
            case NEBAMP: return "nebamp";
            case OVUL: return "ovul";
            case PCH: return "pch";
            case PKT: return "pkt";
            case SASH: return "sash";
            case STRIP: return "strip";
            case TIN: return "tin";
            case TUB: return "tub";
            case TUBE: return "tube";
            case VIAL: return "vial";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/medicationKnowledge-package";
        }
        public String getDefinition() {
          switch (this) {
            case AMP: return "";
            case BAG: return "";
            case BLSTRPK: return "";
            case BOT: return "";
            case BOX: return "";
            case CAN: return "";
            case CART: return "";
            case CNSTR: return "";
            case DISK: return "";
            case DOSET: return "";
            case JAR: return "";
            case JUG: return "";
            case MINIM: return "";
            case NEBAMP: return "";
            case OVUL: return "";
            case PCH: return "";
            case PKT: return "";
            case SASH: return "";
            case STRIP: return "";
            case TIN: return "";
            case TUB: return "";
            case TUBE: return "";
            case VIAL: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AMP: return "Ampoule";
            case BAG: return "Bag";
            case BLSTRPK: return "Blister Pack";
            case BOT: return "Bottle";
            case BOX: return "Box";
            case CAN: return "Can";
            case CART: return "Cartridge";
            case CNSTR: return "Canister";
            case DISK: return "Disk";
            case DOSET: return "Dosette";
            case JAR: return "Jar";
            case JUG: return "Jug";
            case MINIM: return "Minim";
            case NEBAMP: return "Nebule Amp";
            case OVUL: return "Ovule";
            case PCH: return "Pouch";
            case PKT: return "Packet";
            case SASH: return "Sashet";
            case STRIP: return "Strip";
            case TIN: return "Tin";
            case TUB: return "Tub";
            case TUBE: return "Tube";
            case VIAL: return "vial";
            default: return "?";
          }
    }


}

