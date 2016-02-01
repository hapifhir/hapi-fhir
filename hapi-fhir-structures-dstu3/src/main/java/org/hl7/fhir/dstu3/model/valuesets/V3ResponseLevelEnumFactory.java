package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ResponseLevelEnumFactory implements EnumFactory<V3ResponseLevel> {

  public V3ResponseLevel fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("C".equals(codeString))
      return V3ResponseLevel.C;
    if ("D".equals(codeString))
      return V3ResponseLevel.D;
    if ("E".equals(codeString))
      return V3ResponseLevel.E;
    if ("F".equals(codeString))
      return V3ResponseLevel.F;
    if ("N".equals(codeString))
      return V3ResponseLevel.N;
    if ("R".equals(codeString))
      return V3ResponseLevel.R;
    if ("X".equals(codeString))
      return V3ResponseLevel.X;
    throw new IllegalArgumentException("Unknown V3ResponseLevel code '"+codeString+"'");
  }

  public String toCode(V3ResponseLevel code) {
    if (code == V3ResponseLevel.C)
      return "C";
    if (code == V3ResponseLevel.D)
      return "D";
    if (code == V3ResponseLevel.E)
      return "E";
    if (code == V3ResponseLevel.F)
      return "F";
    if (code == V3ResponseLevel.N)
      return "N";
    if (code == V3ResponseLevel.R)
      return "R";
    if (code == V3ResponseLevel.X)
      return "X";
    return "?";
  }

    public String toSystem(V3ResponseLevel code) {
      return code.getSystem();
      }

}

