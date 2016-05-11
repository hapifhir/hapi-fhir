package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3LanguageAbilityModeEnumFactory implements EnumFactory<V3LanguageAbilityMode> {

  public V3LanguageAbilityMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ESGN".equals(codeString))
      return V3LanguageAbilityMode.ESGN;
    if ("ESP".equals(codeString))
      return V3LanguageAbilityMode.ESP;
    if ("EWR".equals(codeString))
      return V3LanguageAbilityMode.EWR;
    if ("RSGN".equals(codeString))
      return V3LanguageAbilityMode.RSGN;
    if ("RSP".equals(codeString))
      return V3LanguageAbilityMode.RSP;
    if ("RWR".equals(codeString))
      return V3LanguageAbilityMode.RWR;
    throw new IllegalArgumentException("Unknown V3LanguageAbilityMode code '"+codeString+"'");
  }

  public String toCode(V3LanguageAbilityMode code) {
    if (code == V3LanguageAbilityMode.ESGN)
      return "ESGN";
    if (code == V3LanguageAbilityMode.ESP)
      return "ESP";
    if (code == V3LanguageAbilityMode.EWR)
      return "EWR";
    if (code == V3LanguageAbilityMode.RSGN)
      return "RSGN";
    if (code == V3LanguageAbilityMode.RSP)
      return "RSP";
    if (code == V3LanguageAbilityMode.RWR)
      return "RWR";
    return "?";
  }

    public String toSystem(V3LanguageAbilityMode code) {
      return code.getSystem();
      }

}

