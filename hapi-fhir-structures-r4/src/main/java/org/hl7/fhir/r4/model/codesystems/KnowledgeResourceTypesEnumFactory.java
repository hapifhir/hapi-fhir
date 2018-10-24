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


import org.hl7.fhir.r4.model.EnumFactory;

public class KnowledgeResourceTypesEnumFactory implements EnumFactory<KnowledgeResourceTypes> {

  public KnowledgeResourceTypes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ActivityDefinition".equals(codeString))
      return KnowledgeResourceTypes.ACTIVITYDEFINITION;
    if ("CodeSystem".equals(codeString))
      return KnowledgeResourceTypes.CODESYSTEM;
    if ("ConceptMap".equals(codeString))
      return KnowledgeResourceTypes.CONCEPTMAP;
    if ("Library".equals(codeString))
      return KnowledgeResourceTypes.LIBRARY;
    if ("Measure".equals(codeString))
      return KnowledgeResourceTypes.MEASURE;
    if ("PlanDefinition".equals(codeString))
      return KnowledgeResourceTypes.PLANDEFINITION;
    if ("StructureDefinition".equals(codeString))
      return KnowledgeResourceTypes.STRUCTUREDEFINITION;
    if ("StructureMap".equals(codeString))
      return KnowledgeResourceTypes.STRUCTUREMAP;
    if ("ValueSet".equals(codeString))
      return KnowledgeResourceTypes.VALUESET;
    throw new IllegalArgumentException("Unknown KnowledgeResourceTypes code '"+codeString+"'");
  }

  public String toCode(KnowledgeResourceTypes code) {
    if (code == KnowledgeResourceTypes.ACTIVITYDEFINITION)
      return "ActivityDefinition";
    if (code == KnowledgeResourceTypes.CODESYSTEM)
      return "CodeSystem";
    if (code == KnowledgeResourceTypes.CONCEPTMAP)
      return "ConceptMap";
    if (code == KnowledgeResourceTypes.LIBRARY)
      return "Library";
    if (code == KnowledgeResourceTypes.MEASURE)
      return "Measure";
    if (code == KnowledgeResourceTypes.PLANDEFINITION)
      return "PlanDefinition";
    if (code == KnowledgeResourceTypes.STRUCTUREDEFINITION)
      return "StructureDefinition";
    if (code == KnowledgeResourceTypes.STRUCTUREMAP)
      return "StructureMap";
    if (code == KnowledgeResourceTypes.VALUESET)
      return "ValueSet";
    return "?";
  }

    public String toSystem(KnowledgeResourceTypes code) {
      return code.getSystem();
      }

}

