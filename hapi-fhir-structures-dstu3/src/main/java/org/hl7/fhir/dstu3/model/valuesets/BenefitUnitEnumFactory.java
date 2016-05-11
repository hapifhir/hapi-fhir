package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class BenefitUnitEnumFactory implements EnumFactory<BenefitUnit> {

  public BenefitUnit fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("individual".equals(codeString))
      return BenefitUnit.INDIVIDUAL;
    if ("family".equals(codeString))
      return BenefitUnit.FAMILY;
    throw new IllegalArgumentException("Unknown BenefitUnit code '"+codeString+"'");
  }

  public String toCode(BenefitUnit code) {
    if (code == BenefitUnit.INDIVIDUAL)
      return "individual";
    if (code == BenefitUnit.FAMILY)
      return "family";
    return "?";
  }

    public String toSystem(BenefitUnit code) {
      return code.getSystem();
      }

}

