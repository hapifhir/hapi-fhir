package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ContractTermTypeEnumFactory implements EnumFactory<ContractTermType> {

  public ContractTermType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("OralHealth".equals(codeString))
      return ContractTermType.ORALHEALTH;
    if ("Vision".equals(codeString))
      return ContractTermType.VISION;
    throw new IllegalArgumentException("Unknown ContractTermType code '"+codeString+"'");
  }

  public String toCode(ContractTermType code) {
    if (code == ContractTermType.ORALHEALTH)
      return "OralHealth";
    if (code == ContractTermType.VISION)
      return "Vision";
    return "?";
  }

    public String toSystem(ContractTermType code) {
      return code.getSystem();
      }

}

