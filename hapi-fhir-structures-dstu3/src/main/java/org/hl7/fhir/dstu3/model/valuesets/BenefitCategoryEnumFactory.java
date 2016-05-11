package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class BenefitCategoryEnumFactory implements EnumFactory<BenefitCategory> {

  public BenefitCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("oral".equals(codeString))
      return BenefitCategory.ORAL;
    if ("vision".equals(codeString))
      return BenefitCategory.VISION;
    if ("medical".equals(codeString))
      return BenefitCategory.MEDICAL;
    if ("pharmacy".equals(codeString))
      return BenefitCategory.PHARMACY;
    throw new IllegalArgumentException("Unknown BenefitCategory code '"+codeString+"'");
  }

  public String toCode(BenefitCategory code) {
    if (code == BenefitCategory.ORAL)
      return "oral";
    if (code == BenefitCategory.VISION)
      return "vision";
    if (code == BenefitCategory.MEDICAL)
      return "medical";
    if (code == BenefitCategory.PHARMACY)
      return "pharmacy";
    return "?";
  }

    public String toSystem(BenefitCategory code) {
      return code.getSystem();
      }

}

