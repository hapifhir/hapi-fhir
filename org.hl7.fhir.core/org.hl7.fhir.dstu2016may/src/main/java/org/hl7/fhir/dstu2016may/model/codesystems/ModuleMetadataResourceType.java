package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ModuleMetadataResourceType {

        /**
         * Additional documentation for the module. This would include additional instructions on usage as well additional information on clinical context or appropriateness
         */
        DOCUMENTATION, 
        /**
         * A summary of the justification for the artifact including supporting evidence, relevant guidelines, or other clinically important information. This information is intended to provide a way to make the justification for the module available to the consumer of interventions or results produced by the artifact
         */
        JUSTIFICATION, 
        /**
         * Bibliographic citation for papers, references, or other relevant material for the module. This is intended to allow for citation of related material, but that was not necessarily specifically prepared in connection with this module
         */
        CITATION, 
        /**
         * The previous version of the module
         */
        PREDECESSOR, 
        /**
         * The next version of the module
         */
        SUCCESSOR, 
        /**
         * The module is derived from the resource. This is intended to capture the relationship when a particular module is based on the content of another module, but is modified to capture either a different set of overall requirements, or a more specific set of requirements such as those involved in a particular institution or clinical setting
         */
        DERIVEDFROM, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ModuleMetadataResourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("documentation".equals(codeString))
          return DOCUMENTATION;
        if ("justification".equals(codeString))
          return JUSTIFICATION;
        if ("citation".equals(codeString))
          return CITATION;
        if ("predecessor".equals(codeString))
          return PREDECESSOR;
        if ("successor".equals(codeString))
          return SUCCESSOR;
        if ("derived-from".equals(codeString))
          return DERIVEDFROM;
        throw new FHIRException("Unknown ModuleMetadataResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DOCUMENTATION: return "documentation";
            case JUSTIFICATION: return "justification";
            case CITATION: return "citation";
            case PREDECESSOR: return "predecessor";
            case SUCCESSOR: return "successor";
            case DERIVEDFROM: return "derived-from";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/module-metadata-resource-type";
        }
        public String getDefinition() {
          switch (this) {
            case DOCUMENTATION: return "Additional documentation for the module. This would include additional instructions on usage as well additional information on clinical context or appropriateness";
            case JUSTIFICATION: return "A summary of the justification for the artifact including supporting evidence, relevant guidelines, or other clinically important information. This information is intended to provide a way to make the justification for the module available to the consumer of interventions or results produced by the artifact";
            case CITATION: return "Bibliographic citation for papers, references, or other relevant material for the module. This is intended to allow for citation of related material, but that was not necessarily specifically prepared in connection with this module";
            case PREDECESSOR: return "The previous version of the module";
            case SUCCESSOR: return "The next version of the module";
            case DERIVEDFROM: return "The module is derived from the resource. This is intended to capture the relationship when a particular module is based on the content of another module, but is modified to capture either a different set of overall requirements, or a more specific set of requirements such as those involved in a particular institution or clinical setting";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DOCUMENTATION: return "Documentation";
            case JUSTIFICATION: return "Justification";
            case CITATION: return "Citation";
            case PREDECESSOR: return "Predecessor";
            case SUCCESSOR: return "Successor";
            case DERIVEDFROM: return "Derived From";
            default: return "?";
          }
    }


}

