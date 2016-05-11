package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3GenderStatusEnumFactory implements EnumFactory<V3GenderStatus> {

  public V3GenderStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("I".equals(codeString))
      return V3GenderStatus.I;
    if ("N".equals(codeString))
      return V3GenderStatus.N;
    throw new IllegalArgumentException("Unknown V3GenderStatus code '"+codeString+"'");
  }

  public String toCode(V3GenderStatus code) {
    if (code == V3GenderStatus.I)
      return "I";
    if (code == V3GenderStatus.N)
      return "N";
    return "?";
  }

    public String toSystem(V3GenderStatus code) {
      return code.getSystem();
      }

}

