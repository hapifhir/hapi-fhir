package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ContractTypeEnumFactory implements EnumFactory<ContractType> {

  public ContractType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("privacy".equals(codeString))
      return ContractType.PRIVACY;
    if ("disclosure".equals(codeString))
      return ContractType.DISCLOSURE;
    throw new IllegalArgumentException("Unknown ContractType code '"+codeString+"'");
  }

  public String toCode(ContractType code) {
    if (code == ContractType.PRIVACY)
      return "privacy";
    if (code == ContractType.DISCLOSURE)
      return "disclosure";
    return "?";
  }

    public String toSystem(ContractType code) {
      return code.getSystem();
      }

}

