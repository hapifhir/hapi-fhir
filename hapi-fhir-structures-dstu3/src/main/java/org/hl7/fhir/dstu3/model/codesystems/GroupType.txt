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

public enum GroupType {

        /**
         * Group contains "person" Patient resources
         */
        PERSON, 
        /**
         * Group contains "animal" Patient resources
         */
        ANIMAL, 
        /**
         * Group contains healthcare practitioner resources
         */
        PRACTITIONER, 
        /**
         * Group contains Device resources
         */
        DEVICE, 
        /**
         * Group contains Medication resources
         */
        MEDICATION, 
        /**
         * Group contains Substance resources
         */
        SUBSTANCE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GroupType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("person".equals(codeString))
          return PERSON;
        if ("animal".equals(codeString))
          return ANIMAL;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("device".equals(codeString))
          return DEVICE;
        if ("medication".equals(codeString))
          return MEDICATION;
        if ("substance".equals(codeString))
          return SUBSTANCE;
        throw new FHIRException("Unknown GroupType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PERSON: return "person";
            case ANIMAL: return "animal";
            case PRACTITIONER: return "practitioner";
            case DEVICE: return "device";
            case MEDICATION: return "medication";
            case SUBSTANCE: return "substance";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/group-type";
        }
        public String getDefinition() {
          switch (this) {
            case PERSON: return "Group contains \"person\" Patient resources";
            case ANIMAL: return "Group contains \"animal\" Patient resources";
            case PRACTITIONER: return "Group contains healthcare practitioner resources";
            case DEVICE: return "Group contains Device resources";
            case MEDICATION: return "Group contains Medication resources";
            case SUBSTANCE: return "Group contains Substance resources";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PERSON: return "Person";
            case ANIMAL: return "Animal";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            case MEDICATION: return "Medication";
            case SUBSTANCE: return "Substance";
            default: return "?";
          }
    }


}

