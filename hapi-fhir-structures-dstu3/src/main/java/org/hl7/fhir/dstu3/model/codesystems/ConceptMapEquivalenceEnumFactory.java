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

public class ConceptMapEquivalenceEnumFactory implements EnumFactory<ConceptMapEquivalence> {

  public ConceptMapEquivalence fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("relatedto".equals(codeString))
      return ConceptMapEquivalence.RELATEDTO;
    if ("equivalent".equals(codeString))
      return ConceptMapEquivalence.EQUIVALENT;
    if ("equal".equals(codeString))
      return ConceptMapEquivalence.EQUAL;
    if ("wider".equals(codeString))
      return ConceptMapEquivalence.WIDER;
    if ("subsumes".equals(codeString))
      return ConceptMapEquivalence.SUBSUMES;
    if ("narrower".equals(codeString))
      return ConceptMapEquivalence.NARROWER;
    if ("specializes".equals(codeString))
      return ConceptMapEquivalence.SPECIALIZES;
    if ("inexact".equals(codeString))
      return ConceptMapEquivalence.INEXACT;
    if ("unmatched".equals(codeString))
      return ConceptMapEquivalence.UNMATCHED;
    if ("disjoint".equals(codeString))
      return ConceptMapEquivalence.DISJOINT;
    throw new IllegalArgumentException("Unknown ConceptMapEquivalence code '"+codeString+"'");
  }

  public String toCode(ConceptMapEquivalence code) {
    if (code == ConceptMapEquivalence.RELATEDTO)
      return "relatedto";
    if (code == ConceptMapEquivalence.EQUIVALENT)
      return "equivalent";
    if (code == ConceptMapEquivalence.EQUAL)
      return "equal";
    if (code == ConceptMapEquivalence.WIDER)
      return "wider";
    if (code == ConceptMapEquivalence.SUBSUMES)
      return "subsumes";
    if (code == ConceptMapEquivalence.NARROWER)
      return "narrower";
    if (code == ConceptMapEquivalence.SPECIALIZES)
      return "specializes";
    if (code == ConceptMapEquivalence.INEXACT)
      return "inexact";
    if (code == ConceptMapEquivalence.UNMATCHED)
      return "unmatched";
    if (code == ConceptMapEquivalence.DISJOINT)
      return "disjoint";
    return "?";
  }

    public String toSystem(ConceptMapEquivalence code) {
      return code.getSystem();
      }

}

