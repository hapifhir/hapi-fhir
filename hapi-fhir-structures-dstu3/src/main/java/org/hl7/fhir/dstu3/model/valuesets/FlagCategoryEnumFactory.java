package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class FlagCategoryEnumFactory implements EnumFactory<FlagCategory> {

  public FlagCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("diet".equals(codeString))
      return FlagCategory.DIET;
    if ("drug".equals(codeString))
      return FlagCategory.DRUG;
    if ("lab".equals(codeString))
      return FlagCategory.LAB;
    if ("admin".equals(codeString))
      return FlagCategory.ADMIN;
    if ("contact".equals(codeString))
      return FlagCategory.CONTACT;
    throw new IllegalArgumentException("Unknown FlagCategory code '"+codeString+"'");
  }

  public String toCode(FlagCategory code) {
    if (code == FlagCategory.DIET)
      return "diet";
    if (code == FlagCategory.DRUG)
      return "drug";
    if (code == FlagCategory.LAB)
      return "lab";
    if (code == FlagCategory.ADMIN)
      return "admin";
    if (code == FlagCategory.CONTACT)
      return "contact";
    return "?";
  }

    public String toSystem(FlagCategory code) {
      return code.getSystem();
      }

}

