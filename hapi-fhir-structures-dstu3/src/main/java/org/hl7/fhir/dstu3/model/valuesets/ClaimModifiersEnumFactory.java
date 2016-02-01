package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ClaimModifiersEnumFactory implements EnumFactory<ClaimModifiers> {

  public ClaimModifiers fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A".equals(codeString))
      return ClaimModifiers.A;
    if ("B".equals(codeString))
      return ClaimModifiers.B;
    if ("C".equals(codeString))
      return ClaimModifiers.C;
    if ("E".equals(codeString))
      return ClaimModifiers.E;
    if ("X".equals(codeString))
      return ClaimModifiers.X;
    throw new IllegalArgumentException("Unknown ClaimModifiers code '"+codeString+"'");
  }

  public String toCode(ClaimModifiers code) {
    if (code == ClaimModifiers.A)
      return "A";
    if (code == ClaimModifiers.B)
      return "B";
    if (code == ClaimModifiers.C)
      return "C";
    if (code == ClaimModifiers.E)
      return "E";
    if (code == ClaimModifiers.X)
      return "X";
    return "?";
  }

    public String toSystem(ClaimModifiers code) {
      return code.getSystem();
      }

}

