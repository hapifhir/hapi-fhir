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

public enum DefinitionUse {

        /**
         * This structure is defined as part of the base FHIR Specification
         */
        FHIRSTRUCTURE, 
        /**
         * This structure is intended to be treated like a FHIR resource (e.g. on the FHIR API)
         */
        CUSTOMRESOURCE, 
        /**
         * This structure captures an analysis of a domain
         */
        DAM, 
        /**
         * This structure represents and existing structure (e.g. CDA, HL7 v2)
         */
        WIREFORMAT, 
        /**
         * This structure captures an analysis of a domain
         */
        ARCHETYPE, 
        /**
         * This structure is a template (n.b: 'template' has many meanings)
         */
        TEMPLATE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DefinitionUse fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("fhir-structure".equals(codeString))
          return FHIRSTRUCTURE;
        if ("custom-resource".equals(codeString))
          return CUSTOMRESOURCE;
        if ("dam".equals(codeString))
          return DAM;
        if ("wire-format".equals(codeString))
          return WIREFORMAT;
        if ("archetype".equals(codeString))
          return ARCHETYPE;
        if ("template".equals(codeString))
          return TEMPLATE;
        throw new FHIRException("Unknown DefinitionUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FHIRSTRUCTURE: return "fhir-structure";
            case CUSTOMRESOURCE: return "custom-resource";
            case DAM: return "dam";
            case WIREFORMAT: return "wire-format";
            case ARCHETYPE: return "archetype";
            case TEMPLATE: return "template";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/definition-use";
        }
        public String getDefinition() {
          switch (this) {
            case FHIRSTRUCTURE: return "This structure is defined as part of the base FHIR Specification";
            case CUSTOMRESOURCE: return "This structure is intended to be treated like a FHIR resource (e.g. on the FHIR API)";
            case DAM: return "This structure captures an analysis of a domain";
            case WIREFORMAT: return "This structure represents and existing structure (e.g. CDA, HL7 v2)";
            case ARCHETYPE: return "This structure captures an analysis of a domain";
            case TEMPLATE: return "This structure is a template (n.b: 'template' has many meanings)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FHIRSTRUCTURE: return "FHIR Structure";
            case CUSTOMRESOURCE: return "Custom Resource";
            case DAM: return "Domain Analysis Model";
            case WIREFORMAT: return "Wire Format";
            case ARCHETYPE: return "Domain Analysis Model";
            case TEMPLATE: return "Template";
            default: return "?";
          }
    }


}

