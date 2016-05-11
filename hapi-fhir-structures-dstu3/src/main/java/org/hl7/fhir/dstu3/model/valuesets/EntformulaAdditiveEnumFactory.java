package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class EntformulaAdditiveEnumFactory implements EnumFactory<EntformulaAdditive> {

  public EntformulaAdditive fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("lipid".equals(codeString))
      return EntformulaAdditive.LIPID;
    if ("protein".equals(codeString))
      return EntformulaAdditive.PROTEIN;
    if ("carbohydrate".equals(codeString))
      return EntformulaAdditive.CARBOHYDRATE;
    if ("fiber".equals(codeString))
      return EntformulaAdditive.FIBER;
    if ("water".equals(codeString))
      return EntformulaAdditive.WATER;
    throw new IllegalArgumentException("Unknown EntformulaAdditive code '"+codeString+"'");
  }

  public String toCode(EntformulaAdditive code) {
    if (code == EntformulaAdditive.LIPID)
      return "lipid";
    if (code == EntformulaAdditive.PROTEIN)
      return "protein";
    if (code == EntformulaAdditive.CARBOHYDRATE)
      return "carbohydrate";
    if (code == EntformulaAdditive.FIBER)
      return "fiber";
    if (code == EntformulaAdditive.WATER)
      return "water";
    return "?";
  }

    public String toSystem(EntformulaAdditive code) {
      return code.getSystem();
      }

}

