package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class FmConditionsEnumFactory implements EnumFactory<FmConditions> {

  public FmConditions fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("123987".equals(codeString))
      return FmConditions._123987;
    throw new IllegalArgumentException("Unknown FmConditions code '"+codeString+"'");
  }

  public String toCode(FmConditions code) {
    if (code == FmConditions._123987)
      return "123987";
    return "?";
  }

    public String toSystem(FmConditions code) {
      return code.getSystem();
      }

}

