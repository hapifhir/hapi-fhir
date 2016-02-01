package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3LivingArrangementEnumFactory implements EnumFactory<V3LivingArrangement> {

  public V3LivingArrangement fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("HL".equals(codeString))
      return V3LivingArrangement.HL;
    if ("M".equals(codeString))
      return V3LivingArrangement.M;
    if ("T".equals(codeString))
      return V3LivingArrangement.T;
    if ("I".equals(codeString))
      return V3LivingArrangement.I;
    if ("CS".equals(codeString))
      return V3LivingArrangement.CS;
    if ("G".equals(codeString))
      return V3LivingArrangement.G;
    if ("N".equals(codeString))
      return V3LivingArrangement.N;
    if ("X".equals(codeString))
      return V3LivingArrangement.X;
    if ("PR".equals(codeString))
      return V3LivingArrangement.PR;
    if ("H".equals(codeString))
      return V3LivingArrangement.H;
    if ("R".equals(codeString))
      return V3LivingArrangement.R;
    if ("SL".equals(codeString))
      return V3LivingArrangement.SL;
    throw new IllegalArgumentException("Unknown V3LivingArrangement code '"+codeString+"'");
  }

  public String toCode(V3LivingArrangement code) {
    if (code == V3LivingArrangement.HL)
      return "HL";
    if (code == V3LivingArrangement.M)
      return "M";
    if (code == V3LivingArrangement.T)
      return "T";
    if (code == V3LivingArrangement.I)
      return "I";
    if (code == V3LivingArrangement.CS)
      return "CS";
    if (code == V3LivingArrangement.G)
      return "G";
    if (code == V3LivingArrangement.N)
      return "N";
    if (code == V3LivingArrangement.X)
      return "X";
    if (code == V3LivingArrangement.PR)
      return "PR";
    if (code == V3LivingArrangement.H)
      return "H";
    if (code == V3LivingArrangement.R)
      return "R";
    if (code == V3LivingArrangement.SL)
      return "SL";
    return "?";
  }

    public String toSystem(V3LivingArrangement code) {
      return code.getSystem();
      }

}

