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

public enum KnowledgeResourceTypes {

        /**
         * The definition of a specific activity to be taken, independent of any particular patient or context.
         */
        ACTIVITYDEFINITION, 
        /**
         * A set of codes drawn from one or more code systems.
         */
        CODESYSTEM, 
        /**
         * A map from one set of concepts to one or more other concepts.
         */
        CONCEPTMAP, 
        /**
         * Represents a library of quality improvement components.
         */
        LIBRARY, 
        /**
         * A quality measure definition.
         */
        MEASURE, 
        /**
         * The definition of a plan for a series of actions, independent of any specific patient or context.
         */
        PLANDEFINITION, 
        /**
         * Structural Definition.
         */
        STRUCTUREDEFINITION, 
        /**
         * A Map of relationships between 2 structures that can be used to transform data.
         */
        STRUCTUREMAP, 
        /**
         * A set of codes drawn from one or more code systems.
         */
        VALUESET, 
        /**
         * added to help the parsers
         */
        NULL;
        public static KnowledgeResourceTypes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ActivityDefinition".equals(codeString))
          return ACTIVITYDEFINITION;
        if ("CodeSystem".equals(codeString))
          return CODESYSTEM;
        if ("ConceptMap".equals(codeString))
          return CONCEPTMAP;
        if ("Library".equals(codeString))
          return LIBRARY;
        if ("Measure".equals(codeString))
          return MEASURE;
        if ("PlanDefinition".equals(codeString))
          return PLANDEFINITION;
        if ("StructureDefinition".equals(codeString))
          return STRUCTUREDEFINITION;
        if ("StructureMap".equals(codeString))
          return STRUCTUREMAP;
        if ("ValueSet".equals(codeString))
          return VALUESET;
        throw new FHIRException("Unknown KnowledgeResourceTypes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case CODESYSTEM: return "CodeSystem";
            case CONCEPTMAP: return "ConceptMap";
            case LIBRARY: return "Library";
            case MEASURE: return "Measure";
            case PLANDEFINITION: return "PlanDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case VALUESET: return "ValueSet";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/knowledge-resource-types";
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVITYDEFINITION: return "The definition of a specific activity to be taken, independent of any particular patient or context.";
            case CODESYSTEM: return "A set of codes drawn from one or more code systems.";
            case CONCEPTMAP: return "A map from one set of concepts to one or more other concepts.";
            case LIBRARY: return "Represents a library of quality improvement components.";
            case MEASURE: return "A quality measure definition.";
            case PLANDEFINITION: return "The definition of a plan for a series of actions, independent of any specific patient or context.";
            case STRUCTUREDEFINITION: return "Structural Definition.";
            case STRUCTUREMAP: return "A Map of relationships between 2 structures that can be used to transform data.";
            case VALUESET: return "A set of codes drawn from one or more code systems.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVITYDEFINITION: return "ActivityDefinition";
            case CODESYSTEM: return "CodeSystem";
            case CONCEPTMAP: return "ConceptMap";
            case LIBRARY: return "Library";
            case MEASURE: return "Measure";
            case PLANDEFINITION: return "PlanDefinition";
            case STRUCTUREDEFINITION: return "StructureDefinition";
            case STRUCTUREMAP: return "StructureMap";
            case VALUESET: return "ValueSet";
            default: return "?";
          }
    }


}

