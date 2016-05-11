package org.hl7.fhir.dstu3.model.valuesets;

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

