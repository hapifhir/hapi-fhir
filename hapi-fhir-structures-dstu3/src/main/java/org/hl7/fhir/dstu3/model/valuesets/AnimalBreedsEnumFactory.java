package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class AnimalBreedsEnumFactory implements EnumFactory<AnimalBreeds> {

  public AnimalBreeds fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("gsd".equals(codeString))
      return AnimalBreeds.GSD;
    if ("irt".equals(codeString))
      return AnimalBreeds.IRT;
    if ("tibmas".equals(codeString))
      return AnimalBreeds.TIBMAS;
    if ("gret".equals(codeString))
      return AnimalBreeds.GRET;
    throw new IllegalArgumentException("Unknown AnimalBreeds code '"+codeString+"'");
  }

  public String toCode(AnimalBreeds code) {
    if (code == AnimalBreeds.GSD)
      return "gsd";
    if (code == AnimalBreeds.IRT)
      return "irt";
    if (code == AnimalBreeds.TIBMAS)
      return "tibmas";
    if (code == AnimalBreeds.GRET)
      return "gret";
    return "?";
  }

    public String toSystem(AnimalBreeds code) {
      return code.getSystem();
      }

}

