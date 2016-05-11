package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3QueryPriorityEnumFactory implements EnumFactory<V3QueryPriority> {

  public V3QueryPriority fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("D".equals(codeString))
      return V3QueryPriority.D;
    if ("I".equals(codeString))
      return V3QueryPriority.I;
    throw new IllegalArgumentException("Unknown V3QueryPriority code '"+codeString+"'");
  }

  public String toCode(V3QueryPriority code) {
    if (code == V3QueryPriority.D)
      return "D";
    if (code == V3QueryPriority.I)
      return "I";
    return "?";
  }

    public String toSystem(V3QueryPriority code) {
      return code.getSystem();
      }

}

