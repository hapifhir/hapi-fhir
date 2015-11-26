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


public enum V3ExposureMode {

        /**
         * Code for the mechanism by which the exposure agent was exchanged or potentially exchanged by the participants involved in the exposure.
         */
        _EXPOSUREMODE, 
        /**
         * Description: Communication of an agent from a living subject or environmental source to a living subject through indirect contact via oral or nasal inhalation.
         */
        AIRBORNE, 
        /**
         * Description: Communication of an agent from a living subject or environmental source to a living subject through direct physical contact.
         */
        CONTACT, 
        /**
         * Description: Communication of an agent from a food source to a living subject via oral consumption.
         */
        FOODBORNE, 
        /**
         * Description: Communication of an agent to a living subject by contact and/or consumption via an aqueous medium
         */
        WATERBORNE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ExposureMode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ExposureMode".equals(codeString))
          return _EXPOSUREMODE;
        if ("AIRBORNE".equals(codeString))
          return AIRBORNE;
        if ("CONTACT".equals(codeString))
          return CONTACT;
        if ("FOODBORNE".equals(codeString))
          return FOODBORNE;
        if ("WATERBORNE".equals(codeString))
          return WATERBORNE;
        throw new Exception("Unknown V3ExposureMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _EXPOSUREMODE: return "_ExposureMode";
            case AIRBORNE: return "AIRBORNE";
            case CONTACT: return "CONTACT";
            case FOODBORNE: return "FOODBORNE";
            case WATERBORNE: return "WATERBORNE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ExposureMode";
        }
        public String getDefinition() {
          switch (this) {
            case _EXPOSUREMODE: return "Code for the mechanism by which the exposure agent was exchanged or potentially exchanged by the participants involved in the exposure.";
            case AIRBORNE: return "Description: Communication of an agent from a living subject or environmental source to a living subject through indirect contact via oral or nasal inhalation.";
            case CONTACT: return "Description: Communication of an agent from a living subject or environmental source to a living subject through direct physical contact.";
            case FOODBORNE: return "Description: Communication of an agent from a food source to a living subject via oral consumption.";
            case WATERBORNE: return "Description: Communication of an agent to a living subject by contact and/or consumption via an aqueous medium";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _EXPOSUREMODE: return "ExposureMode";
            case AIRBORNE: return "airborne";
            case CONTACT: return "contact";
            case FOODBORNE: return "foodborne";
            case WATERBORNE: return "waterborne";
            default: return "?";
          }
    }


}

