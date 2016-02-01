package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class AnimalGenderstatusEnumFactory implements EnumFactory<AnimalGenderstatus> {

  public AnimalGenderstatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("neutered".equals(codeString))
      return AnimalGenderstatus.NEUTERED;
    if ("intact".equals(codeString))
      return AnimalGenderstatus.INTACT;
    if ("unknown".equals(codeString))
      return AnimalGenderstatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown AnimalGenderstatus code '"+codeString+"'");
  }

  public String toCode(AnimalGenderstatus code) {
    if (code == AnimalGenderstatus.NEUTERED)
      return "neutered";
    if (code == AnimalGenderstatus.INTACT)
      return "intact";
    if (code == AnimalGenderstatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(AnimalGenderstatus code) {
      return code.getSystem();
      }

}

