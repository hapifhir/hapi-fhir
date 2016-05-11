package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class CarePlanActivityCategoryEnumFactory implements EnumFactory<CarePlanActivityCategory> {

  public CarePlanActivityCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("diet".equals(codeString))
      return CarePlanActivityCategory.DIET;
    if ("drug".equals(codeString))
      return CarePlanActivityCategory.DRUG;
    if ("encounter".equals(codeString))
      return CarePlanActivityCategory.ENCOUNTER;
    if ("observation".equals(codeString))
      return CarePlanActivityCategory.OBSERVATION;
    if ("procedure".equals(codeString))
      return CarePlanActivityCategory.PROCEDURE;
    if ("supply".equals(codeString))
      return CarePlanActivityCategory.SUPPLY;
    if ("other".equals(codeString))
      return CarePlanActivityCategory.OTHER;
    throw new IllegalArgumentException("Unknown CarePlanActivityCategory code '"+codeString+"'");
  }

  public String toCode(CarePlanActivityCategory code) {
    if (code == CarePlanActivityCategory.DIET)
      return "diet";
    if (code == CarePlanActivityCategory.DRUG)
      return "drug";
    if (code == CarePlanActivityCategory.ENCOUNTER)
      return "encounter";
    if (code == CarePlanActivityCategory.OBSERVATION)
      return "observation";
    if (code == CarePlanActivityCategory.PROCEDURE)
      return "procedure";
    if (code == CarePlanActivityCategory.SUPPLY)
      return "supply";
    if (code == CarePlanActivityCategory.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(CarePlanActivityCategory code) {
      return code.getSystem();
      }

}

