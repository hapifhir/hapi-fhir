package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ImmunizationRecommendationDateCriterionEnumFactory implements EnumFactory<ImmunizationRecommendationDateCriterion> {

  public ImmunizationRecommendationDateCriterion fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("due".equals(codeString))
      return ImmunizationRecommendationDateCriterion.DUE;
    if ("recommended".equals(codeString))
      return ImmunizationRecommendationDateCriterion.RECOMMENDED;
    if ("earliest".equals(codeString))
      return ImmunizationRecommendationDateCriterion.EARLIEST;
    if ("overdue".equals(codeString))
      return ImmunizationRecommendationDateCriterion.OVERDUE;
    if ("latest".equals(codeString))
      return ImmunizationRecommendationDateCriterion.LATEST;
    throw new IllegalArgumentException("Unknown ImmunizationRecommendationDateCriterion code '"+codeString+"'");
  }

  public String toCode(ImmunizationRecommendationDateCriterion code) {
    if (code == ImmunizationRecommendationDateCriterion.DUE)
      return "due";
    if (code == ImmunizationRecommendationDateCriterion.RECOMMENDED)
      return "recommended";
    if (code == ImmunizationRecommendationDateCriterion.EARLIEST)
      return "earliest";
    if (code == ImmunizationRecommendationDateCriterion.OVERDUE)
      return "overdue";
    if (code == ImmunizationRecommendationDateCriterion.LATEST)
      return "latest";
    return "?";
  }

    public String toSystem(ImmunizationRecommendationDateCriterion code) {
      return code.getSystem();
      }

}

