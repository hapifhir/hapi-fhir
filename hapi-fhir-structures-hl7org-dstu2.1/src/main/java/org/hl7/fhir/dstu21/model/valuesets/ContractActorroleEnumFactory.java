package org.hl7.fhir.dstu21.model.valuesets;

import org.hl7.fhir.dstu21.model.EnumFactory;

public class ContractActorroleEnumFactory implements EnumFactory<ContractActorrole> {

  public ContractActorrole fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("practitioner".equals(codeString))
      return ContractActorrole.PRACTITIONER;
    if ("patient".equals(codeString))
      return ContractActorrole.PATIENT;
    throw new IllegalArgumentException("Unknown ContractActorrole code '"+codeString+"'");
  }

  public String toCode(ContractActorrole code) {
    if (code == ContractActorrole.PRACTITIONER)
      return "practitioner";
    if (code == ContractActorrole.PATIENT)
      return "patient";
    return "?";
  }


}

