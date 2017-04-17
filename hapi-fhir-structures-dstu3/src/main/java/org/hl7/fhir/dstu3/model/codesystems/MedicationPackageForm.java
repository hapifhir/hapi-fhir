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


import org.hl7.fhir.exceptions.FHIRException;

public enum MedicationPackageForm {

        /**
         * A sealed glass capsule containing a liquid
         */
        AMPOULE, 
        /**
         * A container, typically made of glass or plastic and with a narrow neck, used for storing liquids.
         */
        BOTTLE, 
        /**
         * A container with a flat base and sides, typically square or rectangular and having a lid.
         */
        BOX, 
        /**
         * A device of various configuration and composition used with a syringe for the application of anesthetic or other materials to a patient.
         */
        CARTRIDGE, 
        /**
         * A package intended to store pharmaceuticals.
         */
        CONTAINER, 
        /**
         * A long, hollow cylinder of metal, plastic, glass, etc., for holding medications, typically creams or ointments
         */
        TUBE, 
        /**
         * A dose of medicine prepared in an individual package for convenience, safety or monitoring.
         */
        UNITDOSE, 
        /**
         * A small container, typically cylindrical and made of glass, used especially for holding liquid medications.
         */
        VIAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationPackageForm fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ampoule".equals(codeString))
          return AMPOULE;
        if ("bottle".equals(codeString))
          return BOTTLE;
        if ("box".equals(codeString))
          return BOX;
        if ("cartridge".equals(codeString))
          return CARTRIDGE;
        if ("container".equals(codeString))
          return CONTAINER;
        if ("tube".equals(codeString))
          return TUBE;
        if ("unitdose".equals(codeString))
          return UNITDOSE;
        if ("vial".equals(codeString))
          return VIAL;
        throw new FHIRException("Unknown MedicationPackageForm code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AMPOULE: return "ampoule";
            case BOTTLE: return "bottle";
            case BOX: return "box";
            case CARTRIDGE: return "cartridge";
            case CONTAINER: return "container";
            case TUBE: return "tube";
            case UNITDOSE: return "unitdose";
            case VIAL: return "vial";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/medication-package-form";
        }
        public String getDefinition() {
          switch (this) {
            case AMPOULE: return "A sealed glass capsule containing a liquid";
            case BOTTLE: return "A container, typically made of glass or plastic and with a narrow neck, used for storing liquids.";
            case BOX: return "A container with a flat base and sides, typically square or rectangular and having a lid.";
            case CARTRIDGE: return "A device of various configuration and composition used with a syringe for the application of anesthetic or other materials to a patient.";
            case CONTAINER: return "A package intended to store pharmaceuticals.";
            case TUBE: return "A long, hollow cylinder of metal, plastic, glass, etc., for holding medications, typically creams or ointments";
            case UNITDOSE: return "A dose of medicine prepared in an individual package for convenience, safety or monitoring.";
            case VIAL: return "A small container, typically cylindrical and made of glass, used especially for holding liquid medications.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AMPOULE: return "Ampoule";
            case BOTTLE: return "Bottle";
            case BOX: return "Box";
            case CARTRIDGE: return "Cartridge";
            case CONTAINER: return "Container";
            case TUBE: return "Tube";
            case UNITDOSE: return "Unit Dose Blister";
            case VIAL: return "Vial";
            default: return "?";
          }
    }


}

