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

public class W3cProvenanceActivityTypeEnumFactory implements EnumFactory<W3cProvenanceActivityType> {

  public W3cProvenanceActivityType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Generation".equals(codeString))
      return W3cProvenanceActivityType.GENERATION;
    if ("Usage".equals(codeString))
      return W3cProvenanceActivityType.USAGE;
    if ("Communication".equals(codeString))
      return W3cProvenanceActivityType.COMMUNICATION;
    if ("Start".equals(codeString))
      return W3cProvenanceActivityType.START;
    if ("End".equals(codeString))
      return W3cProvenanceActivityType.END;
    if ("Invalidation".equals(codeString))
      return W3cProvenanceActivityType.INVALIDATION;
    if ("Derivation".equals(codeString))
      return W3cProvenanceActivityType.DERIVATION;
    if ("Revision".equals(codeString))
      return W3cProvenanceActivityType.REVISION;
    if ("Quotation".equals(codeString))
      return W3cProvenanceActivityType.QUOTATION;
    if ("Primary-Source".equals(codeString))
      return W3cProvenanceActivityType.PRIMARYSOURCE;
    if ("Attribution".equals(codeString))
      return W3cProvenanceActivityType.ATTRIBUTION;
    if ("Collection".equals(codeString))
      return W3cProvenanceActivityType.COLLECTION;
    throw new IllegalArgumentException("Unknown W3cProvenanceActivityType code '"+codeString+"'");
  }

  public String toCode(W3cProvenanceActivityType code) {
    if (code == W3cProvenanceActivityType.GENERATION)
      return "Generation";
    if (code == W3cProvenanceActivityType.USAGE)
      return "Usage";
    if (code == W3cProvenanceActivityType.COMMUNICATION)
      return "Communication";
    if (code == W3cProvenanceActivityType.START)
      return "Start";
    if (code == W3cProvenanceActivityType.END)
      return "End";
    if (code == W3cProvenanceActivityType.INVALIDATION)
      return "Invalidation";
    if (code == W3cProvenanceActivityType.DERIVATION)
      return "Derivation";
    if (code == W3cProvenanceActivityType.REVISION)
      return "Revision";
    if (code == W3cProvenanceActivityType.QUOTATION)
      return "Quotation";
    if (code == W3cProvenanceActivityType.PRIMARYSOURCE)
      return "Primary-Source";
    if (code == W3cProvenanceActivityType.ATTRIBUTION)
      return "Attribution";
    if (code == W3cProvenanceActivityType.COLLECTION)
      return "Collection";
    return "?";
  }

    public String toSystem(W3cProvenanceActivityType code) {
      return code.getSystem();
      }

}

