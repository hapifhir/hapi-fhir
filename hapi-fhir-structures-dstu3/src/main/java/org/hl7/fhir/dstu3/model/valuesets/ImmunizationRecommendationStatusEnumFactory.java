package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ImmunizationRecommendationStatusEnumFactory implements EnumFactory<ImmunizationRecommendationStatus> {

  public ImmunizationRecommendationStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("due".equals(codeString))
      return ImmunizationRecommendationStatus.DUE;
    if ("overdue".equals(codeString))
      return ImmunizationRecommendationStatus.OVERDUE;
    throw new IllegalArgumentException("Unknown ImmunizationRecommendationStatus code '"+codeString+"'");
  }

  public String toCode(ImmunizationRecommendationStatus code) {
    if (code == ImmunizationRecommendationStatus.DUE)
      return "due";
    if (code == ImmunizationRecommendationStatus.OVERDUE)
      return "overdue";
    return "?";
  }

    public String toSystem(ImmunizationRecommendationStatus code) {
      return code.getSystem();
      }

}

