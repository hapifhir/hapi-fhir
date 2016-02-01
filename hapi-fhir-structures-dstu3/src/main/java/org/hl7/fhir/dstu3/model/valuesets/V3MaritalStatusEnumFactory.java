package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3MaritalStatusEnumFactory implements EnumFactory<V3MaritalStatus> {

  public V3MaritalStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A".equals(codeString))
      return V3MaritalStatus.A;
    if ("D".equals(codeString))
      return V3MaritalStatus.D;
    if ("I".equals(codeString))
      return V3MaritalStatus.I;
    if ("L".equals(codeString))
      return V3MaritalStatus.L;
    if ("M".equals(codeString))
      return V3MaritalStatus.M;
    if ("P".equals(codeString))
      return V3MaritalStatus.P;
    if ("S".equals(codeString))
      return V3MaritalStatus.S;
    if ("T".equals(codeString))
      return V3MaritalStatus.T;
    if ("U".equals(codeString))
      return V3MaritalStatus.U;
    if ("W".equals(codeString))
      return V3MaritalStatus.W;
    throw new IllegalArgumentException("Unknown V3MaritalStatus code '"+codeString+"'");
  }

  public String toCode(V3MaritalStatus code) {
    if (code == V3MaritalStatus.A)
      return "A";
    if (code == V3MaritalStatus.D)
      return "D";
    if (code == V3MaritalStatus.I)
      return "I";
    if (code == V3MaritalStatus.L)
      return "L";
    if (code == V3MaritalStatus.M)
      return "M";
    if (code == V3MaritalStatus.P)
      return "P";
    if (code == V3MaritalStatus.S)
      return "S";
    if (code == V3MaritalStatus.T)
      return "T";
    if (code == V3MaritalStatus.U)
      return "U";
    if (code == V3MaritalStatus.W)
      return "W";
    return "?";
  }

    public String toSystem(V3MaritalStatus code) {
      return code.getSystem();
      }

}

