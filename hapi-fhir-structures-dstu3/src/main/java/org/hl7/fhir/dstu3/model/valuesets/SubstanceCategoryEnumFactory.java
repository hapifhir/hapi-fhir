package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class SubstanceCategoryEnumFactory implements EnumFactory<SubstanceCategory> {

  public SubstanceCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("allergen".equals(codeString))
      return SubstanceCategory.ALLERGEN;
    if ("biological".equals(codeString))
      return SubstanceCategory.BIOLOGICAL;
    if ("body".equals(codeString))
      return SubstanceCategory.BODY;
    if ("chemical".equals(codeString))
      return SubstanceCategory.CHEMICAL;
    if ("food".equals(codeString))
      return SubstanceCategory.FOOD;
    if ("drug".equals(codeString))
      return SubstanceCategory.DRUG;
    if ("material".equals(codeString))
      return SubstanceCategory.MATERIAL;
    throw new IllegalArgumentException("Unknown SubstanceCategory code '"+codeString+"'");
  }

  public String toCode(SubstanceCategory code) {
    if (code == SubstanceCategory.ALLERGEN)
      return "allergen";
    if (code == SubstanceCategory.BIOLOGICAL)
      return "biological";
    if (code == SubstanceCategory.BODY)
      return "body";
    if (code == SubstanceCategory.CHEMICAL)
      return "chemical";
    if (code == SubstanceCategory.FOOD)
      return "food";
    if (code == SubstanceCategory.DRUG)
      return "drug";
    if (code == SubstanceCategory.MATERIAL)
      return "material";
    return "?";
  }

    public String toSystem(SubstanceCategory code) {
      return code.getSystem();
      }

}

