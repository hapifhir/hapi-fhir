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


import org.hl7.fhir.exceptions.FHIRException;

public enum StructureDefinitionKind {

        /**
         * A primitive type that has a value and an extension. These can be used throughout complex datatype, Resource and extension definitions. Only the base specification can define primitive types.
         */
        PRIMITIVETYPE, 
        /**
         * A  complex structure that defines a set of data elements that is suitable for use in 'resources'. The base specification defines a number of complex types, and other specifications can define additional types. These structures do not have a maintained identity.
         */
        COMPLEXTYPE, 
        /**
         * A 'resource' - a directed acyclic graph of elements that aggregrates other types into an identifiable entity. The base FHIR resources are defined by the FHIR specification itself but other 'resources' can be defined in additional specifications (though these will not be recognised as 'resources' by the FHIR specification (i.e. they do not get end-points etc, or act as the targets of references in FHIR defined resources - though other specificatiosn can treat them this way).
         */
        RESOURCE, 
        /**
         * A pattern or a template that is not intended to be a real resource or complex type.
         */
        LOGICAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureDefinitionKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("primitive-type".equals(codeString))
          return PRIMITIVETYPE;
        if ("complex-type".equals(codeString))
          return COMPLEXTYPE;
        if ("resource".equals(codeString))
          return RESOURCE;
        if ("logical".equals(codeString))
          return LOGICAL;
        throw new FHIRException("Unknown StructureDefinitionKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRIMITIVETYPE: return "primitive-type";
            case COMPLEXTYPE: return "complex-type";
            case RESOURCE: return "resource";
            case LOGICAL: return "logical";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/structure-definition-kind";
        }
        public String getDefinition() {
          switch (this) {
            case PRIMITIVETYPE: return "A primitive type that has a value and an extension. These can be used throughout complex datatype, Resource and extension definitions. Only the base specification can define primitive types.";
            case COMPLEXTYPE: return "A  complex structure that defines a set of data elements that is suitable for use in 'resources'. The base specification defines a number of complex types, and other specifications can define additional types. These structures do not have a maintained identity.";
            case RESOURCE: return "A 'resource' - a directed acyclic graph of elements that aggregrates other types into an identifiable entity. The base FHIR resources are defined by the FHIR specification itself but other 'resources' can be defined in additional specifications (though these will not be recognised as 'resources' by the FHIR specification (i.e. they do not get end-points etc, or act as the targets of references in FHIR defined resources - though other specificatiosn can treat them this way).";
            case LOGICAL: return "A pattern or a template that is not intended to be a real resource or complex type.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRIMITIVETYPE: return "Primitive Data Type";
            case COMPLEXTYPE: return "Complex Data Type";
            case RESOURCE: return "Resource";
            case LOGICAL: return "Logical";
            default: return "?";
          }
    }


}

