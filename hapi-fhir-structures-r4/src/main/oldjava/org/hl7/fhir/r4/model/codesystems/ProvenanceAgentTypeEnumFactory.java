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


import org.hl7.fhir.r4.model.EnumFactory;

public class ProvenanceAgentTypeEnumFactory implements EnumFactory<ProvenanceAgentType> {

  public ProvenanceAgentType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("enterer".equals(codeString))
      return ProvenanceAgentType.ENTERER;
    if ("performer".equals(codeString))
      return ProvenanceAgentType.PERFORMER;
    if ("author".equals(codeString))
      return ProvenanceAgentType.AUTHOR;
    if ("verifier".equals(codeString))
      return ProvenanceAgentType.VERIFIER;
    if ("legal".equals(codeString))
      return ProvenanceAgentType.LEGAL;
    if ("attester".equals(codeString))
      return ProvenanceAgentType.ATTESTER;
    if ("informant".equals(codeString))
      return ProvenanceAgentType.INFORMANT;
    if ("custodian".equals(codeString))
      return ProvenanceAgentType.CUSTODIAN;
    if ("assembler".equals(codeString))
      return ProvenanceAgentType.ASSEMBLER;
    if ("composer".equals(codeString))
      return ProvenanceAgentType.COMPOSER;
    throw new IllegalArgumentException("Unknown ProvenanceAgentType code '"+codeString+"'");
  }

  public String toCode(ProvenanceAgentType code) {
    if (code == ProvenanceAgentType.ENTERER)
      return "enterer";
    if (code == ProvenanceAgentType.PERFORMER)
      return "performer";
    if (code == ProvenanceAgentType.AUTHOR)
      return "author";
    if (code == ProvenanceAgentType.VERIFIER)
      return "verifier";
    if (code == ProvenanceAgentType.LEGAL)
      return "legal";
    if (code == ProvenanceAgentType.ATTESTER)
      return "attester";
    if (code == ProvenanceAgentType.INFORMANT)
      return "informant";
    if (code == ProvenanceAgentType.CUSTODIAN)
      return "custodian";
    if (code == ProvenanceAgentType.ASSEMBLER)
      return "assembler";
    if (code == ProvenanceAgentType.COMPOSER)
      return "composer";
    return "?";
  }

    public String toSystem(ProvenanceAgentType code) {
      return code.getSystem();
      }

}

