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

public enum EncounterSpecialArrangements {

        /**
         * The patient requires a wheelchair to be made available for the encounter.
         */
        WHEEL, 
        /**
         * An additional bed made available for a person accompanying the patient, for example a parent accompanying a child.
         */
        ADDBED, 
        /**
         * The patient is not fluent in the local language and requires an interpreter to be available. Refer to the Patient.Language property for the type of interpreter required.
         */
        INT, 
        /**
         * A person who accompanies a patient to provide assistive services necessary for the patient's care during the encounter.
         */
        ATT, 
        /**
         * The patient has a guide-dog and the location used for the encounter should be able to support the presence of the service animal.
         */
        DOG, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterSpecialArrangements fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("wheel".equals(codeString))
          return WHEEL;
        if ("add-bed".equals(codeString))
          return ADDBED;
        if ("int".equals(codeString))
          return INT;
        if ("att".equals(codeString))
          return ATT;
        if ("dog".equals(codeString))
          return DOG;
        throw new FHIRException("Unknown EncounterSpecialArrangements code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case WHEEL: return "wheel";
            case ADDBED: return "add-bed";
            case INT: return "int";
            case ATT: return "att";
            case DOG: return "dog";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/encounter-special-arrangements";
        }
        public String getDefinition() {
          switch (this) {
            case WHEEL: return "The patient requires a wheelchair to be made available for the encounter.";
            case ADDBED: return "An additional bed made available for a person accompanying the patient, for example a parent accompanying a child.";
            case INT: return "The patient is not fluent in the local language and requires an interpreter to be available. Refer to the Patient.Language property for the type of interpreter required.";
            case ATT: return "A person who accompanies a patient to provide assistive services necessary for the patient's care during the encounter.";
            case DOG: return "The patient has a guide-dog and the location used for the encounter should be able to support the presence of the service animal.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case WHEEL: return "Wheelchair";
            case ADDBED: return "Additional bedding";
            case INT: return "Interpreter";
            case ATT: return "Attendant";
            case DOG: return "Guide dog";
            default: return "?";
          }
    }


}

