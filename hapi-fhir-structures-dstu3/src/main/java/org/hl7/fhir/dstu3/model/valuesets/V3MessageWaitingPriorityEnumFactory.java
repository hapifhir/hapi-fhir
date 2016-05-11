package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3MessageWaitingPriorityEnumFactory implements EnumFactory<V3MessageWaitingPriority> {

  public V3MessageWaitingPriority fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("H".equals(codeString))
      return V3MessageWaitingPriority.H;
    if ("L".equals(codeString))
      return V3MessageWaitingPriority.L;
    if ("M".equals(codeString))
      return V3MessageWaitingPriority.M;
    throw new IllegalArgumentException("Unknown V3MessageWaitingPriority code '"+codeString+"'");
  }

  public String toCode(V3MessageWaitingPriority code) {
    if (code == V3MessageWaitingPriority.H)
      return "H";
    if (code == V3MessageWaitingPriority.L)
      return "L";
    if (code == V3MessageWaitingPriority.M)
      return "M";
    return "?";
  }

    public String toSystem(V3MessageWaitingPriority code) {
      return code.getSystem();
      }

}

