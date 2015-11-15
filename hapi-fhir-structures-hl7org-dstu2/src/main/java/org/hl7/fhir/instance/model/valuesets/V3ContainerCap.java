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


public enum V3ContainerCap {

        /**
         * Cap types for medication containers
         */
        _MEDICATIONCAP, 
        /**
         * A cap designed to be difficult to open for children.  Generally requires multiple simultaneous actions (e.g. squeeze and twist) to open.  Used for products that may be dangerous if ingested or overdosed by children.
         */
        CHILD, 
        /**
         * A cap designed to be easily removed.  For products intended to be opened by persons with limited strength or dexterity.
         */
        EASY, 
        /**
         * A non-reactive plastic film covering over the opening of a container.
         */
        FILM, 
        /**
         * A foil covering (type of foil not specified) over the opening of a container
         */
        FOIL, 
        /**
         * A non-threaded cap that forms a tight-fitting closure on a container by pushing the fitted end into the conatiner opening
         */
        PUSH, 
        /**
         * A threaded cap that is screwed onto the opening of a container
         */
        SCR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ContainerCap fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_MedicationCap".equals(codeString))
          return _MEDICATIONCAP;
        if ("CHILD".equals(codeString))
          return CHILD;
        if ("EASY".equals(codeString))
          return EASY;
        if ("FILM".equals(codeString))
          return FILM;
        if ("FOIL".equals(codeString))
          return FOIL;
        if ("PUSH".equals(codeString))
          return PUSH;
        if ("SCR".equals(codeString))
          return SCR;
        throw new Exception("Unknown V3ContainerCap code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _MEDICATIONCAP: return "_MedicationCap";
            case CHILD: return "CHILD";
            case EASY: return "EASY";
            case FILM: return "FILM";
            case FOIL: return "FOIL";
            case PUSH: return "PUSH";
            case SCR: return "SCR";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ContainerCap";
        }
        public String getDefinition() {
          switch (this) {
            case _MEDICATIONCAP: return "Cap types for medication containers";
            case CHILD: return "A cap designed to be difficult to open for children.  Generally requires multiple simultaneous actions (e.g. squeeze and twist) to open.  Used for products that may be dangerous if ingested or overdosed by children.";
            case EASY: return "A cap designed to be easily removed.  For products intended to be opened by persons with limited strength or dexterity.";
            case FILM: return "A non-reactive plastic film covering over the opening of a container.";
            case FOIL: return "A foil covering (type of foil not specified) over the opening of a container";
            case PUSH: return "A non-threaded cap that forms a tight-fitting closure on a container by pushing the fitted end into the conatiner opening";
            case SCR: return "A threaded cap that is screwed onto the opening of a container";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _MEDICATIONCAP: return "MedicationCap";
            case CHILD: return "ChildProof";
            case EASY: return "EasyOpen";
            case FILM: return "Film";
            case FOIL: return "Foil";
            case PUSH: return "Push Cap";
            case SCR: return "Screw Cap";
            default: return "?";
          }
    }


}

