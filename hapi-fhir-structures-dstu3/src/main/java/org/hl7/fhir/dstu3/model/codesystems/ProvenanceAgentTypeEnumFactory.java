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

// Generated on Thu, Feb 9, 2017 08:03-0500 for FHIR v1.9.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class ProvenanceAgentTypeEnumFactory implements EnumFactory<ProvenanceAgentType> {

  public ProvenanceAgentType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("person".equals(codeString))
      return ProvenanceAgentType.PERSON;
    if ("practitioner".equals(codeString))
      return ProvenanceAgentType.PRACTITIONER;
    if ("organization".equals(codeString))
      return ProvenanceAgentType.ORGANIZATION;
    if ("software".equals(codeString))
      return ProvenanceAgentType.SOFTWARE;
    if ("patient".equals(codeString))
      return ProvenanceAgentType.PATIENT;
    if ("device".equals(codeString))
      return ProvenanceAgentType.DEVICE;
    if ("related-person".equals(codeString))
      return ProvenanceAgentType.RELATEDPERSON;
    throw new IllegalArgumentException("Unknown ProvenanceAgentType code '"+codeString+"'");
  }

  public String toCode(ProvenanceAgentType code) {
    if (code == ProvenanceAgentType.PERSON)
      return "person";
    if (code == ProvenanceAgentType.PRACTITIONER)
      return "practitioner";
    if (code == ProvenanceAgentType.ORGANIZATION)
      return "organization";
    if (code == ProvenanceAgentType.SOFTWARE)
      return "software";
    if (code == ProvenanceAgentType.PATIENT)
      return "patient";
    if (code == ProvenanceAgentType.DEVICE)
      return "device";
    if (code == ProvenanceAgentType.RELATEDPERSON)
      return "related-person";
    return "?";
  }

    public String toSystem(ProvenanceAgentType code) {
      return code.getSystem();
      }

}

