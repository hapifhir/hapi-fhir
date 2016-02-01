package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class AnimalSpeciesEnumFactory implements EnumFactory<AnimalSpecies> {

  public AnimalSpecies fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("canislf".equals(codeString))
      return AnimalSpecies.CANISLF;
    if ("ovisa".equals(codeString))
      return AnimalSpecies.OVISA;
    if ("serinuscd".equals(codeString))
      return AnimalSpecies.SERINUSCD;
    throw new IllegalArgumentException("Unknown AnimalSpecies code '"+codeString+"'");
  }

  public String toCode(AnimalSpecies code) {
    if (code == AnimalSpecies.CANISLF)
      return "canislf";
    if (code == AnimalSpecies.OVISA)
      return "ovisa";
    if (code == AnimalSpecies.SERINUSCD)
      return "serinuscd";
    return "?";
  }

    public String toSystem(AnimalSpecies code) {
      return code.getSystem();
      }

}

