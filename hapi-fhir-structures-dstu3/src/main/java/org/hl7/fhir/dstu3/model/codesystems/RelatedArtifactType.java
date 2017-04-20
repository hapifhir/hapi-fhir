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

public enum RelatedArtifactType {

        /**
         * Additional documentation for the knowledge resource. This would include additional instructions on usage as well as additional information on clinical context or appropriateness
         */
        DOCUMENTATION, 
        /**
         * A summary of the justification for the knowledge resource including supporting evidence, relevant guidelines, or other clinically important information. This information is intended to provide a way to make the justification for the knowledge resource available to the consumer of interventions or results produced by the knowledge resource
         */
        JUSTIFICATION, 
        /**
         * Bibliographic citation for papers, references, or other relevant material for the knowledge resource. This is intended to allow for citation of related material, but that was not necessarily specifically prepared in connection with this knowledge resource
         */
        CITATION, 
        /**
         * The previous version of the knowledge resource
         */
        PREDECESSOR, 
        /**
         * The next version of the knowledge resource
         */
        SUCCESSOR, 
        /**
         * The knowledge resource is derived from the related artifact. This is intended to capture the relationship in which a particular knowledge resource is based on the content of another artifact, but is modified to capture either a different set of overall requirements, or a more specific set of requirements such as those involved in a particular institution or clinical setting
         */
        DERIVEDFROM, 
        /**
         * The knowledge resource depends on the given related artifact
         */
        DEPENDSON, 
        /**
         * The knowledge resource is composed of the given related artifact
         */
        COMPOSEDOF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RelatedArtifactType fromCode(String codeString) throws FHIRException {
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
        if ("depends-on".equals(codeString))
          return DEPENDSON;
        if ("composed-of".equals(codeString))
          return COMPOSEDOF;
        throw new FHIRException("Unknown RelatedArtifactType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DOCUMENTATION: return "documentation";
            case JUSTIFICATION: return "justification";
            case CITATION: return "citation";
            case PREDECESSOR: return "predecessor";
            case SUCCESSOR: return "successor";
            case DERIVEDFROM: return "derived-from";
            case DEPENDSON: return "depends-on";
            case COMPOSEDOF: return "composed-of";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/related-artifact-type";
        }
        public String getDefinition() {
          switch (this) {
            case DOCUMENTATION: return "Additional documentation for the knowledge resource. This would include additional instructions on usage as well as additional information on clinical context or appropriateness";
            case JUSTIFICATION: return "A summary of the justification for the knowledge resource including supporting evidence, relevant guidelines, or other clinically important information. This information is intended to provide a way to make the justification for the knowledge resource available to the consumer of interventions or results produced by the knowledge resource";
            case CITATION: return "Bibliographic citation for papers, references, or other relevant material for the knowledge resource. This is intended to allow for citation of related material, but that was not necessarily specifically prepared in connection with this knowledge resource";
            case PREDECESSOR: return "The previous version of the knowledge resource";
            case SUCCESSOR: return "The next version of the knowledge resource";
            case DERIVEDFROM: return "The knowledge resource is derived from the related artifact. This is intended to capture the relationship in which a particular knowledge resource is based on the content of another artifact, but is modified to capture either a different set of overall requirements, or a more specific set of requirements such as those involved in a particular institution or clinical setting";
            case DEPENDSON: return "The knowledge resource depends on the given related artifact";
            case COMPOSEDOF: return "The knowledge resource is composed of the given related artifact";
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
            case DEPENDSON: return "Depends On";
            case COMPOSEDOF: return "Composed Of";
            default: return "?";
          }
    }


}

