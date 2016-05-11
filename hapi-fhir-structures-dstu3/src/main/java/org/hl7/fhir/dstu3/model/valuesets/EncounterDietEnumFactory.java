package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class EncounterDietEnumFactory implements EnumFactory<EncounterDiet> {

  public EncounterDiet fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("vegetarian".equals(codeString))
      return EncounterDiet.VEGETARIAN;
    if ("dairy-free".equals(codeString))
      return EncounterDiet.DAIRYFREE;
    if ("nut-free".equals(codeString))
      return EncounterDiet.NUTFREE;
    if ("gluten-free".equals(codeString))
      return EncounterDiet.GLUTENFREE;
    if ("vegan".equals(codeString))
      return EncounterDiet.VEGAN;
    if ("halal".equals(codeString))
      return EncounterDiet.HALAL;
    if ("kosher".equals(codeString))
      return EncounterDiet.KOSHER;
    throw new IllegalArgumentException("Unknown EncounterDiet code '"+codeString+"'");
  }

  public String toCode(EncounterDiet code) {
    if (code == EncounterDiet.VEGETARIAN)
      return "vegetarian";
    if (code == EncounterDiet.DAIRYFREE)
      return "dairy-free";
    if (code == EncounterDiet.NUTFREE)
      return "nut-free";
    if (code == EncounterDiet.GLUTENFREE)
      return "gluten-free";
    if (code == EncounterDiet.VEGAN)
      return "vegan";
    if (code == EncounterDiet.HALAL)
      return "halal";
    if (code == EncounterDiet.KOSHER)
      return "kosher";
    return "?";
  }

    public String toSystem(EncounterDiet code) {
      return code.getSystem();
      }

}

