package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3LocalMarkupIgnoreEnumFactory implements EnumFactory<V3LocalMarkupIgnore> {

  public V3LocalMarkupIgnore fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("all".equals(codeString))
      return V3LocalMarkupIgnore.ALL;
    if ("markup".equals(codeString))
      return V3LocalMarkupIgnore.MARKUP;
    throw new IllegalArgumentException("Unknown V3LocalMarkupIgnore code '"+codeString+"'");
  }

  public String toCode(V3LocalMarkupIgnore code) {
    if (code == V3LocalMarkupIgnore.ALL)
      return "all";
    if (code == V3LocalMarkupIgnore.MARKUP)
      return "markup";
    return "?";
  }

    public String toSystem(V3LocalMarkupIgnore code) {
      return code.getSystem();
      }

}

