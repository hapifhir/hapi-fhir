package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3AdministrativeGenderEnumFactory implements EnumFactory<V3AdministrativeGender> {

  public V3AdministrativeGender fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("F".equals(codeString))
      return V3AdministrativeGender.F;
    if ("M".equals(codeString))
      return V3AdministrativeGender.M;
    if ("UN".equals(codeString))
      return V3AdministrativeGender.UN;
    throw new IllegalArgumentException("Unknown V3AdministrativeGender code '"+codeString+"'");
  }

  public String toCode(V3AdministrativeGender code) {
    if (code == V3AdministrativeGender.F)
      return "F";
    if (code == V3AdministrativeGender.M)
      return "M";
    if (code == V3AdministrativeGender.UN)
      return "UN";
    return "?";
  }

    public String toSystem(V3AdministrativeGender code) {
      return code.getSystem();
      }

}

