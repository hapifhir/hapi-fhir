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


import org.hl7.fhir.dstu3.model.EnumFactory;

public class RelatedArtifactTypeEnumFactory implements EnumFactory<RelatedArtifactType> {

  public RelatedArtifactType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("documentation".equals(codeString))
      return RelatedArtifactType.DOCUMENTATION;
    if ("justification".equals(codeString))
      return RelatedArtifactType.JUSTIFICATION;
    if ("citation".equals(codeString))
      return RelatedArtifactType.CITATION;
    if ("predecessor".equals(codeString))
      return RelatedArtifactType.PREDECESSOR;
    if ("successor".equals(codeString))
      return RelatedArtifactType.SUCCESSOR;
    if ("derived-from".equals(codeString))
      return RelatedArtifactType.DERIVEDFROM;
    if ("depends-on".equals(codeString))
      return RelatedArtifactType.DEPENDSON;
    if ("composed-of".equals(codeString))
      return RelatedArtifactType.COMPOSEDOF;
    throw new IllegalArgumentException("Unknown RelatedArtifactType code '"+codeString+"'");
  }

  public String toCode(RelatedArtifactType code) {
    if (code == RelatedArtifactType.DOCUMENTATION)
      return "documentation";
    if (code == RelatedArtifactType.JUSTIFICATION)
      return "justification";
    if (code == RelatedArtifactType.CITATION)
      return "citation";
    if (code == RelatedArtifactType.PREDECESSOR)
      return "predecessor";
    if (code == RelatedArtifactType.SUCCESSOR)
      return "successor";
    if (code == RelatedArtifactType.DERIVEDFROM)
      return "derived-from";
    if (code == RelatedArtifactType.DEPENDSON)
      return "depends-on";
    if (code == RelatedArtifactType.COMPOSEDOF)
      return "composed-of";
    return "?";
  }

    public String toSystem(RelatedArtifactType code) {
      return code.getSystem();
      }

}

