package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

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

    public String toSystem(ProvenanceAgentRole code) {
      return code.getSystem();
      }

}

