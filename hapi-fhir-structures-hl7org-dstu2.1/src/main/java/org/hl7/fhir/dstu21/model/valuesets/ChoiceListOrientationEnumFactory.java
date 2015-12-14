package org.hl7.fhir.dstu21.model.valuesets;

import org.hl7.fhir.dstu21.model.EnumFactory;

public class ChoiceListOrientationEnumFactory implements EnumFactory<ChoiceListOrientation> {

  public ChoiceListOrientation fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("horizontal".equals(codeString))
      return ChoiceListOrientation.HORIZONTAL;
    if ("vertical".equals(codeString))
      return ChoiceListOrientation.VERTICAL;
    throw new IllegalArgumentException("Unknown ChoiceListOrientation code '"+codeString+"'");
  }

  public String toCode(ChoiceListOrientation code) {
    if (code == ChoiceListOrientation.HORIZONTAL)
      return "horizontal";
    if (code == ChoiceListOrientation.VERTICAL)
      return "vertical";
    return "?";
  }


}

