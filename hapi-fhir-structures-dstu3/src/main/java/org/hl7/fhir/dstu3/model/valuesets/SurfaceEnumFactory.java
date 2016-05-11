package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class SurfaceEnumFactory implements EnumFactory<Surface> {

  public Surface fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("M".equals(codeString))
      return Surface.M;
    if ("O".equals(codeString))
      return Surface.O;
    if ("I".equals(codeString))
      return Surface.I;
    if ("D".equals(codeString))
      return Surface.D;
    if ("B".equals(codeString))
      return Surface.B;
    if ("V".equals(codeString))
      return Surface.V;
    if ("L".equals(codeString))
      return Surface.L;
    if ("MO".equals(codeString))
      return Surface.MO;
    if ("DO".equals(codeString))
      return Surface.DO;
    if ("DI".equals(codeString))
      return Surface.DI;
    if ("MOD".equals(codeString))
      return Surface.MOD;
    throw new IllegalArgumentException("Unknown Surface code '"+codeString+"'");
  }

  public String toCode(Surface code) {
    if (code == Surface.M)
      return "M";
    if (code == Surface.O)
      return "O";
    if (code == Surface.I)
      return "I";
    if (code == Surface.D)
      return "D";
    if (code == Surface.B)
      return "B";
    if (code == Surface.V)
      return "V";
    if (code == Surface.L)
      return "L";
    if (code == Surface.MO)
      return "MO";
    if (code == Surface.DO)
      return "DO";
    if (code == Surface.DI)
      return "DI";
    if (code == Surface.MOD)
      return "MOD";
    return "?";
  }

    public String toSystem(Surface code) {
      return code.getSystem();
      }

}

