package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3LanguageAbilityProficiencyEnumFactory implements EnumFactory<V3LanguageAbilityProficiency> {

  public V3LanguageAbilityProficiency fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("E".equals(codeString))
      return V3LanguageAbilityProficiency.E;
    if ("F".equals(codeString))
      return V3LanguageAbilityProficiency.F;
    if ("G".equals(codeString))
      return V3LanguageAbilityProficiency.G;
    if ("P".equals(codeString))
      return V3LanguageAbilityProficiency.P;
    throw new IllegalArgumentException("Unknown V3LanguageAbilityProficiency code '"+codeString+"'");
  }

  public String toCode(V3LanguageAbilityProficiency code) {
    if (code == V3LanguageAbilityProficiency.E)
      return "E";
    if (code == V3LanguageAbilityProficiency.F)
      return "F";
    if (code == V3LanguageAbilityProficiency.G)
      return "G";
    if (code == V3LanguageAbilityProficiency.P)
      return "P";
    return "?";
  }

    public String toSystem(V3LanguageAbilityProficiency code) {
      return code.getSystem();
      }

}

