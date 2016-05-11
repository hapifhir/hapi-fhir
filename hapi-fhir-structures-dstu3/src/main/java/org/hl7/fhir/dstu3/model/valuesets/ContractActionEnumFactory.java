package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ContractActionEnumFactory implements EnumFactory<ContractAction> {

  public ContractAction fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("action-a".equals(codeString))
      return ContractAction.ACTIONA;
    if ("action-b".equals(codeString))
      return ContractAction.ACTIONB;
    throw new IllegalArgumentException("Unknown ContractAction code '"+codeString+"'");
  }

  public String toCode(ContractAction code) {
    if (code == ContractAction.ACTIONA)
      return "action-a";
    if (code == ContractAction.ACTIONB)
      return "action-b";
    return "?";
  }

    public String toSystem(ContractAction code) {
      return code.getSystem();
      }

}

