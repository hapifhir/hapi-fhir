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

public enum ResourceValidationMode {

        /**
         * The server checks the content, and then checks that the content would be acceptable as a create (e.g. that the content would not violate any uniqueness constraints).
         */
        CREATE, 
        /**
         * The server checks the content, and then checks that it would accept it as an update against the nominated specific resource (e.g. that there are no changes to immutable fields the server does not allow to change and checking version integrity if appropriate).
         */
        UPDATE, 
        /**
         * The server ignores the content and checks that the nominated resource is allowed to be deleted (e.g. checking referential integrity rules).
         */
        DELETE, 
        /**
         * The server checks an existing resource (must be nominated by id, not provided as a parameter) as valid against the nominated profile.
         */
        PROFILE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceValidationMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return CREATE;
        if ("update".equals(codeString))
          return UPDATE;
        if ("delete".equals(codeString))
          return DELETE;
        if ("profile".equals(codeString))
          return PROFILE;
        throw new FHIRException("Unknown ResourceValidationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CREATE: return "create";
            case UPDATE: return "update";
            case DELETE: return "delete";
            case PROFILE: return "profile";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/resource-validation-mode";
        }
        public String getDefinition() {
          switch (this) {
            case CREATE: return "The server checks the content, and then checks that the content would be acceptable as a create (e.g. that the content would not violate any uniqueness constraints).";
            case UPDATE: return "The server checks the content, and then checks that it would accept it as an update against the nominated specific resource (e.g. that there are no changes to immutable fields the server does not allow to change and checking version integrity if appropriate).";
            case DELETE: return "The server ignores the content and checks that the nominated resource is allowed to be deleted (e.g. checking referential integrity rules).";
            case PROFILE: return "The server checks an existing resource (must be nominated by id, not provided as a parameter) as valid against the nominated profile.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CREATE: return "Validate for Create";
            case UPDATE: return "Validate for Update";
            case DELETE: return "Validate for Delete";
            case PROFILE: return "Validate Against a Profile";
            default: return "?";
          }
    }


}

