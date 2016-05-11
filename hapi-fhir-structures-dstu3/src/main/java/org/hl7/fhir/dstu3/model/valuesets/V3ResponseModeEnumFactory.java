package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ResponseModeEnumFactory implements EnumFactory<V3ResponseMode> {

  public V3ResponseMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("D".equals(codeString))
      return V3ResponseMode.D;
    if ("I".equals(codeString))
      return V3ResponseMode.I;
    if ("Q".equals(codeString))
      return V3ResponseMode.Q;
    throw new IllegalArgumentException("Unknown V3ResponseMode code '"+codeString+"'");
  }

  public String toCode(V3ResponseMode code) {
    if (code == V3ResponseMode.D)
      return "D";
    if (code == V3ResponseMode.I)
      return "I";
    if (code == V3ResponseMode.Q)
      return "Q";
    return "?";
  }

    public String toSystem(V3ResponseMode code) {
      return code.getSystem();
      }

}

