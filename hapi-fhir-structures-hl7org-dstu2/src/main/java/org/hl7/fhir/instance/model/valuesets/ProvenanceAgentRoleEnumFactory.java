package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


import org.hl7.fhir.instance.model.EnumFactory;

public class ProvenanceAgentRoleEnumFactory implements EnumFactory<ProvenanceAgentRole> {

  public ProvenanceAgentRole fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("enterer".equals(codeString))
      return ProvenanceAgentRole.ENTERER;
    if ("performer".equals(codeString))
      return ProvenanceAgentRole.PERFORMER;
    if ("author".equals(codeString))
      return ProvenanceAgentRole.AUTHOR;
    if ("verifier".equals(codeString))
      return ProvenanceAgentRole.VERIFIER;
    if ("legal".equals(codeString))
      return ProvenanceAgentRole.LEGAL;
    if ("attester".equals(codeString))
      return ProvenanceAgentRole.ATTESTER;
    if ("informant".equals(codeString))
      return ProvenanceAgentRole.INFORMANT;
    if ("custodian".equals(codeString))
      return ProvenanceAgentRole.CUSTODIAN;
    if ("assembler".equals(codeString))
      return ProvenanceAgentRole.ASSEMBLER;
    if ("composer".equals(codeString))
      return ProvenanceAgentRole.COMPOSER;
    throw new IllegalArgumentException("Unknown ProvenanceAgentRole code '"+codeString+"'");
  }

  public String toCode(ProvenanceAgentRole code) {
    if (code == ProvenanceAgentRole.ENTERER)
      return "enterer";
    if (code == ProvenanceAgentRole.PERFORMER)
      return "performer";
    if (code == ProvenanceAgentRole.AUTHOR)
      return "author";
    if (code == ProvenanceAgentRole.VERIFIER)
      return "verifier";
    if (code == ProvenanceAgentRole.LEGAL)
      return "legal";
    if (code == ProvenanceAgentRole.ATTESTER)
      return "attester";
    if (code == ProvenanceAgentRole.INFORMANT)
      return "informant";
    if (code == ProvenanceAgentRole.CUSTODIAN)
      return "custodian";
    if (code == ProvenanceAgentRole.ASSEMBLER)
      return "assembler";
    if (code == ProvenanceAgentRole.COMPOSER)
      return "composer";
    return "?";
  }


}

