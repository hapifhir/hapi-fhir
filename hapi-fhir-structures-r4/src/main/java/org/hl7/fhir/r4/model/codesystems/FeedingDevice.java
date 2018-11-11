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

public enum FeedingDevice {

        /**
         * Standard nipple definition:
         */
        STANDARDNIPPLE, 
        /**
         * Preemie nipple definition:
         */
        PREEMIENIPPLE, 
        /**
         * Orthodontic nipple definition:
         */
        ORTHONIPPLE, 
        /**
         * Slow flow nipple definition:
         */
        SLOFLONIPPLE, 
        /**
         * Middle flow nipple definition:
         */
        MIDFLONIPPLE, 
        /**
         * Enlarged, cross-cut nipple definition:
         */
        BIGCUTNIPPLE, 
        /**
         * Haberman bottle definition:
         */
        HABERMANBOTTLE, 
        /**
         * Sippy cup with valve definition:
         */
        SIPPYVALVE, 
        /**
         * Sippy cup without valve definition:
         */
        SIPPYNOVALVE, 
        /**
         * Provale Cup definition:
         */
        PROVALECUP, 
        /**
         * Glass with lid/sippy cup definition:
         */
        GLASSLID, 
        /**
         * Double handhold on glass/cup definition:
         */
        HANDHOLDCUP, 
        /**
         * Rubber matting under tray definition:
         */
        RUBBERMAT, 
        /**
         * Straw definition:
         */
        STRAW, 
        /**
         * Nose cup definition:
         */
        NOSECUP, 
        /**
         * Scoop plate definition:
         */
        SCOOPPLATE, 
        /**
         * Hand wrap utensil holder definition:
         */
        UTENSILHOLDER, 
        /**
         * Foam handle utensils definition:
         */
        FOAMHANDLE, 
        /**
         * Angled utensils definition:
         */
        ANGLEDUTENSIL, 
        /**
         * Spout cup definition:
         */
        SPOUTCUP, 
        /**
         * Automated feeding devices definition:
         */
        AUTOFEEDINGDEVICE, 
        /**
         * Rocker knife definition:
         */
        ROCKERKNIFE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FeedingDevice fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("standard-nipple".equals(codeString))
          return STANDARDNIPPLE;
        if ("preemie-nipple".equals(codeString))
          return PREEMIENIPPLE;
        if ("ortho-nipple".equals(codeString))
          return ORTHONIPPLE;
        if ("sloflo-nipple".equals(codeString))
          return SLOFLONIPPLE;
        if ("midflo-nipple".equals(codeString))
          return MIDFLONIPPLE;
        if ("bigcut-nipple".equals(codeString))
          return BIGCUTNIPPLE;
        if ("haberman-bottle".equals(codeString))
          return HABERMANBOTTLE;
        if ("sippy-valve".equals(codeString))
          return SIPPYVALVE;
        if ("sippy-no-valve".equals(codeString))
          return SIPPYNOVALVE;
        if ("provale-cup".equals(codeString))
          return PROVALECUP;
        if ("glass-lid".equals(codeString))
          return GLASSLID;
        if ("handhold-cup".equals(codeString))
          return HANDHOLDCUP;
        if ("rubber-mat".equals(codeString))
          return RUBBERMAT;
        if ("straw".equals(codeString))
          return STRAW;
        if ("nose-cup".equals(codeString))
          return NOSECUP;
        if ("scoop-plate".equals(codeString))
          return SCOOPPLATE;
        if ("utensil-holder".equals(codeString))
          return UTENSILHOLDER;
        if ("foam-handle".equals(codeString))
          return FOAMHANDLE;
        if ("angled-utensil".equals(codeString))
          return ANGLEDUTENSIL;
        if ("spout-cup".equals(codeString))
          return SPOUTCUP;
        if ("autofeeding-device".equals(codeString))
          return AUTOFEEDINGDEVICE;
        if ("rocker-knife".equals(codeString))
          return ROCKERKNIFE;
        throw new FHIRException("Unknown FeedingDevice code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case STANDARDNIPPLE: return "standard-nipple";
            case PREEMIENIPPLE: return "preemie-nipple";
            case ORTHONIPPLE: return "ortho-nipple";
            case SLOFLONIPPLE: return "sloflo-nipple";
            case MIDFLONIPPLE: return "midflo-nipple";
            case BIGCUTNIPPLE: return "bigcut-nipple";
            case HABERMANBOTTLE: return "haberman-bottle";
            case SIPPYVALVE: return "sippy-valve";
            case SIPPYNOVALVE: return "sippy-no-valve";
            case PROVALECUP: return "provale-cup";
            case GLASSLID: return "glass-lid";
            case HANDHOLDCUP: return "handhold-cup";
            case RUBBERMAT: return "rubber-mat";
            case STRAW: return "straw";
            case NOSECUP: return "nose-cup";
            case SCOOPPLATE: return "scoop-plate";
            case UTENSILHOLDER: return "utensil-holder";
            case FOAMHANDLE: return "foam-handle";
            case ANGLEDUTENSIL: return "angled-utensil";
            case SPOUTCUP: return "spout-cup";
            case AUTOFEEDINGDEVICE: return "autofeeding-device";
            case ROCKERKNIFE: return "rocker-knife";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/feeding-device";
        }
        public String getDefinition() {
          switch (this) {
            case STANDARDNIPPLE: return "Standard nipple definition:";
            case PREEMIENIPPLE: return "Preemie nipple definition:";
            case ORTHONIPPLE: return "Orthodontic nipple definition:";
            case SLOFLONIPPLE: return "Slow flow nipple definition:";
            case MIDFLONIPPLE: return "Middle flow nipple definition:";
            case BIGCUTNIPPLE: return "Enlarged, cross-cut nipple definition:";
            case HABERMANBOTTLE: return "Haberman bottle definition:";
            case SIPPYVALVE: return "Sippy cup with valve definition:";
            case SIPPYNOVALVE: return "Sippy cup without valve definition:";
            case PROVALECUP: return "Provale Cup definition:";
            case GLASSLID: return "Glass with lid/sippy cup definition:";
            case HANDHOLDCUP: return "Double handhold on glass/cup definition:";
            case RUBBERMAT: return "Rubber matting under tray definition:";
            case STRAW: return "Straw definition:";
            case NOSECUP: return "Nose cup definition:";
            case SCOOPPLATE: return "Scoop plate definition:";
            case UTENSILHOLDER: return "Hand wrap utensil holder definition:";
            case FOAMHANDLE: return "Foam handle utensils definition:";
            case ANGLEDUTENSIL: return "Angled utensils definition:";
            case SPOUTCUP: return "Spout cup definition:";
            case AUTOFEEDINGDEVICE: return "Automated feeding devices definition:";
            case ROCKERKNIFE: return "Rocker knife definition:";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case STANDARDNIPPLE: return "Standard nipple";
            case PREEMIENIPPLE: return "Preemie nipple";
            case ORTHONIPPLE: return "Orthodontic nipple";
            case SLOFLONIPPLE: return "Slow flow nipple";
            case MIDFLONIPPLE: return "Middle flow nipple";
            case BIGCUTNIPPLE: return "Enlarged, cross-cut nipple";
            case HABERMANBOTTLE: return "Haberman bottle";
            case SIPPYVALVE: return "Sippy cup with valve";
            case SIPPYNOVALVE: return "Sippy cup without valve";
            case PROVALECUP: return "Provale Cup";
            case GLASSLID: return "Glass with lid/sippy cup";
            case HANDHOLDCUP: return "Double handhold on glass/cup";
            case RUBBERMAT: return "Rubber matting under tray";
            case STRAW: return "Straw";
            case NOSECUP: return "Nose cup";
            case SCOOPPLATE: return "Scoop plate";
            case UTENSILHOLDER: return "Hand wrap utensil holder";
            case FOAMHANDLE: return "Foam handle utensils";
            case ANGLEDUTENSIL: return "Angled utensils";
            case SPOUTCUP: return "Spout cup";
            case AUTOFEEDINGDEVICE: return "Automated feeding devices";
            case ROCKERKNIFE: return "Rocker knife";
            default: return "?";
          }
    }


}

