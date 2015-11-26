package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum V3EntityRisk {

        /**
         * A danger that can be associated with certain living subjects, including humans.
         */
        AGG, 
        /**
         * The dangers associated with normal biological materials. I.e. potential risk of unknown infections.  Routine biological materials from living subjects.
         */
        BIO, 
        /**
         * Material is corrosive and may cause severe injury to skin, mucous membranes and eyes. Avoid any unprotected contact.
         */
        COR, 
        /**
         * The entity is at risk for escaping from containment or control.
         */
        ESC, 
        /**
         * Material is highly inflammable and in certain mixtures (with air) may lead to explosions.  Keep away from fire, sparks and excessive heat.
         */
        IFL, 
        /**
         * Material is an explosive mixture.  Keep away from fire, sparks, and heat.
         */
        EXP, 
        /**
         * Material known to be infectious with human pathogenic microorganisms.  Those who handle this material must take precautions for their protection.
         */
        INF, 
        /**
         * Material contains microorganisms that is an environmental hazard.  Must be handled with special care.
         */
        BHZ, 
        /**
         * Material is solid and sharp (e.g. cannulas).  Dispose in hard container.
         */
        INJ, 
        /**
         * Material is poisonous to humans and/or animals.  Special care must be taken to avoid incorporation, even of small amounts.
         */
        POI, 
        /**
         * Material is a source for ionizing radiation and must be handled with special care to avoid injury of those who handle it and to avoid environmental hazards.
         */
        RAD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3EntityRisk fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AGG".equals(codeString))
          return AGG;
        if ("BIO".equals(codeString))
          return BIO;
        if ("COR".equals(codeString))
          return COR;
        if ("ESC".equals(codeString))
          return ESC;
        if ("IFL".equals(codeString))
          return IFL;
        if ("EXP".equals(codeString))
          return EXP;
        if ("INF".equals(codeString))
          return INF;
        if ("BHZ".equals(codeString))
          return BHZ;
        if ("INJ".equals(codeString))
          return INJ;
        if ("POI".equals(codeString))
          return POI;
        if ("RAD".equals(codeString))
          return RAD;
        throw new Exception("Unknown V3EntityRisk code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AGG: return "AGG";
            case BIO: return "BIO";
            case COR: return "COR";
            case ESC: return "ESC";
            case IFL: return "IFL";
            case EXP: return "EXP";
            case INF: return "INF";
            case BHZ: return "BHZ";
            case INJ: return "INJ";
            case POI: return "POI";
            case RAD: return "RAD";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/EntityRisk";
        }
        public String getDefinition() {
          switch (this) {
            case AGG: return "A danger that can be associated with certain living subjects, including humans.";
            case BIO: return "The dangers associated with normal biological materials. I.e. potential risk of unknown infections.  Routine biological materials from living subjects.";
            case COR: return "Material is corrosive and may cause severe injury to skin, mucous membranes and eyes. Avoid any unprotected contact.";
            case ESC: return "The entity is at risk for escaping from containment or control.";
            case IFL: return "Material is highly inflammable and in certain mixtures (with air) may lead to explosions.  Keep away from fire, sparks and excessive heat.";
            case EXP: return "Material is an explosive mixture.  Keep away from fire, sparks, and heat.";
            case INF: return "Material known to be infectious with human pathogenic microorganisms.  Those who handle this material must take precautions for their protection.";
            case BHZ: return "Material contains microorganisms that is an environmental hazard.  Must be handled with special care.";
            case INJ: return "Material is solid and sharp (e.g. cannulas).  Dispose in hard container.";
            case POI: return "Material is poisonous to humans and/or animals.  Special care must be taken to avoid incorporation, even of small amounts.";
            case RAD: return "Material is a source for ionizing radiation and must be handled with special care to avoid injury of those who handle it and to avoid environmental hazards.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AGG: return "aggressive";
            case BIO: return "Biological";
            case COR: return "Corrosive";
            case ESC: return "Escape Risk";
            case IFL: return "inflammable";
            case EXP: return "explosive";
            case INF: return "infectious";
            case BHZ: return "biohazard";
            case INJ: return "injury hazard";
            case POI: return "poison";
            case RAD: return "radioactive";
            default: return "?";
          }
    }


}

