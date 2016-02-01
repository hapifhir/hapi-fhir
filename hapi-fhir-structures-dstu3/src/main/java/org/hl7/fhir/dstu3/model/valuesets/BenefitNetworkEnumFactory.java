package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class BenefitNetworkEnumFactory implements EnumFactory<BenefitNetwork> {

  public BenefitNetwork fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("in".equals(codeString))
      return BenefitNetwork.IN;
    if ("out".equals(codeString))
      return BenefitNetwork.OUT;
    throw new IllegalArgumentException("Unknown BenefitNetwork code '"+codeString+"'");
  }

  public String toCode(BenefitNetwork code) {
    if (code == BenefitNetwork.IN)
      return "in";
    if (code == BenefitNetwork.OUT)
      return "out";
    return "?";
  }

    public String toSystem(BenefitNetwork code) {
      return code.getSystem();
      }

}

