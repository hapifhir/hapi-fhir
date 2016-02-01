package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ProcessingModeEnumFactory implements EnumFactory<V3ProcessingMode> {

  public V3ProcessingMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A".equals(codeString))
      return V3ProcessingMode.A;
    if ("I".equals(codeString))
      return V3ProcessingMode.I;
    if ("R".equals(codeString))
      return V3ProcessingMode.R;
    if ("T".equals(codeString))
      return V3ProcessingMode.T;
    throw new IllegalArgumentException("Unknown V3ProcessingMode code '"+codeString+"'");
  }

  public String toCode(V3ProcessingMode code) {
    if (code == V3ProcessingMode.A)
      return "A";
    if (code == V3ProcessingMode.I)
      return "I";
    if (code == V3ProcessingMode.R)
      return "R";
    if (code == V3ProcessingMode.T)
      return "T";
    return "?";
  }

    public String toSystem(V3ProcessingMode code) {
      return code.getSystem();
      }

}

