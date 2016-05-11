package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class BenefitTermEnumFactory implements EnumFactory<BenefitTerm> {

  public BenefitTerm fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("annual".equals(codeString))
      return BenefitTerm.ANNUAL;
    if ("lifetime".equals(codeString))
      return BenefitTerm.LIFETIME;
    throw new IllegalArgumentException("Unknown BenefitTerm code '"+codeString+"'");
  }

  public String toCode(BenefitTerm code) {
    if (code == BenefitTerm.ANNUAL)
      return "annual";
    if (code == BenefitTerm.LIFETIME)
      return "lifetime";
    return "?";
  }

    public String toSystem(BenefitTerm code) {
      return code.getSystem();
      }

}

