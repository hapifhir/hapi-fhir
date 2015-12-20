package org.hl7.fhir.dstu21.model.valuesets;

import org.hl7.fhir.dstu21.model.EnumFactory;

public class UdiEnumFactory implements EnumFactory<Udi> {

  public Udi fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("{01}123456789".equals(codeString))
      return Udi._01_123456789;
    throw new IllegalArgumentException("Unknown Udi code '"+codeString+"'");
  }

  public String toCode(Udi code) {
    if (code == Udi._01_123456789)
      return "{01}123456789";
    return "?";
  }


}

