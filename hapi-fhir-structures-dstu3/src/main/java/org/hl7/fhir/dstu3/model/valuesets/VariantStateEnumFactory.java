package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class VariantStateEnumFactory implements EnumFactory<VariantState> {

  public VariantState fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("positive".equals(codeString))
      return VariantState.POSITIVE;
    if ("negative".equals(codeString))
      return VariantState.NEGATIVE;
    if ("absent".equals(codeString))
      return VariantState.ABSENT;
    throw new IllegalArgumentException("Unknown VariantState code '"+codeString+"'");
  }

  public String toCode(VariantState code) {
    if (code == VariantState.POSITIVE)
      return "positive";
    if (code == VariantState.NEGATIVE)
      return "negative";
    if (code == VariantState.ABSENT)
      return "absent";
    return "?";
  }

    public String toSystem(VariantState code) {
      return code.getSystem();
      }

}

