package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ContractSubtypeEnumFactory implements EnumFactory<ContractSubtype> {

  public ContractSubtype fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("disclosure-CA".equals(codeString))
      return ContractSubtype.DISCLOSURECA;
    if ("disclosure-US".equals(codeString))
      return ContractSubtype.DISCLOSUREUS;
    throw new IllegalArgumentException("Unknown ContractSubtype code '"+codeString+"'");
  }

  public String toCode(ContractSubtype code) {
    if (code == ContractSubtype.DISCLOSURECA)
      return "disclosure-CA";
    if (code == ContractSubtype.DISCLOSUREUS)
      return "disclosure-US";
    return "?";
  }

    public String toSystem(ContractSubtype code) {
      return code.getSystem();
      }

}

