package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class EncounterSpecialArrangementsEnumFactory implements EnumFactory<EncounterSpecialArrangements> {

  public EncounterSpecialArrangements fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("wheel".equals(codeString))
      return EncounterSpecialArrangements.WHEEL;
    if ("stret".equals(codeString))
      return EncounterSpecialArrangements.STRET;
    if ("int".equals(codeString))
      return EncounterSpecialArrangements.INT;
    if ("att".equals(codeString))
      return EncounterSpecialArrangements.ATT;
    if ("dog".equals(codeString))
      return EncounterSpecialArrangements.DOG;
    throw new IllegalArgumentException("Unknown EncounterSpecialArrangements code '"+codeString+"'");
  }

  public String toCode(EncounterSpecialArrangements code) {
    if (code == EncounterSpecialArrangements.WHEEL)
      return "wheel";
    if (code == EncounterSpecialArrangements.STRET)
      return "stret";
    if (code == EncounterSpecialArrangements.INT)
      return "int";
    if (code == EncounterSpecialArrangements.ATT)
      return "att";
    if (code == EncounterSpecialArrangements.DOG)
      return "dog";
    return "?";
  }

    public String toSystem(EncounterSpecialArrangements code) {
      return code.getSystem();
      }

}

