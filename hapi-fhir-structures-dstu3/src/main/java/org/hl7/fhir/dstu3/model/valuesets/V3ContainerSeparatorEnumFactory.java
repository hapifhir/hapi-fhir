package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ContainerSeparatorEnumFactory implements EnumFactory<V3ContainerSeparator> {

  public V3ContainerSeparator fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("GEL".equals(codeString))
      return V3ContainerSeparator.GEL;
    if ("NONE".equals(codeString))
      return V3ContainerSeparator.NONE;
    throw new IllegalArgumentException("Unknown V3ContainerSeparator code '"+codeString+"'");
  }

  public String toCode(V3ContainerSeparator code) {
    if (code == V3ContainerSeparator.GEL)
      return "GEL";
    if (code == V3ContainerSeparator.NONE)
      return "NONE";
    return "?";
  }

    public String toSystem(V3ContainerSeparator code) {
      return code.getSystem();
      }

}

