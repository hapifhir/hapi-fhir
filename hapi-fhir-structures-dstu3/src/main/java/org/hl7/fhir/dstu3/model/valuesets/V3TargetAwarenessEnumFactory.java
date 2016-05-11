package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TargetAwarenessEnumFactory implements EnumFactory<V3TargetAwareness> {

  public V3TargetAwareness fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("D".equals(codeString))
      return V3TargetAwareness.D;
    if ("F".equals(codeString))
      return V3TargetAwareness.F;
    if ("I".equals(codeString))
      return V3TargetAwareness.I;
    if ("M".equals(codeString))
      return V3TargetAwareness.M;
    if ("P".equals(codeString))
      return V3TargetAwareness.P;
    if ("U".equals(codeString))
      return V3TargetAwareness.U;
    throw new IllegalArgumentException("Unknown V3TargetAwareness code '"+codeString+"'");
  }

  public String toCode(V3TargetAwareness code) {
    if (code == V3TargetAwareness.D)
      return "D";
    if (code == V3TargetAwareness.F)
      return "F";
    if (code == V3TargetAwareness.I)
      return "I";
    if (code == V3TargetAwareness.M)
      return "M";
    if (code == V3TargetAwareness.P)
      return "P";
    if (code == V3TargetAwareness.U)
      return "U";
    return "?";
  }

    public String toSystem(V3TargetAwareness code) {
      return code.getSystem();
      }

}

