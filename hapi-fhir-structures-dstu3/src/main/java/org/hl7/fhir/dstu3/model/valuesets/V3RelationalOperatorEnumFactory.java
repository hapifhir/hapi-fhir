package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3RelationalOperatorEnumFactory implements EnumFactory<V3RelationalOperator> {

  public V3RelationalOperator fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("CT".equals(codeString))
      return V3RelationalOperator.CT;
    if ("EQ".equals(codeString))
      return V3RelationalOperator.EQ;
    if ("GE".equals(codeString))
      return V3RelationalOperator.GE;
    if ("GN".equals(codeString))
      return V3RelationalOperator.GN;
    if ("GT".equals(codeString))
      return V3RelationalOperator.GT;
    if ("LE".equals(codeString))
      return V3RelationalOperator.LE;
    if ("LT".equals(codeString))
      return V3RelationalOperator.LT;
    if ("NE".equals(codeString))
      return V3RelationalOperator.NE;
    throw new IllegalArgumentException("Unknown V3RelationalOperator code '"+codeString+"'");
  }

  public String toCode(V3RelationalOperator code) {
    if (code == V3RelationalOperator.CT)
      return "CT";
    if (code == V3RelationalOperator.EQ)
      return "EQ";
    if (code == V3RelationalOperator.GE)
      return "GE";
    if (code == V3RelationalOperator.GN)
      return "GN";
    if (code == V3RelationalOperator.GT)
      return "GT";
    if (code == V3RelationalOperator.LE)
      return "LE";
    if (code == V3RelationalOperator.LT)
      return "LT";
    if (code == V3RelationalOperator.NE)
      return "NE";
    return "?";
  }

    public String toSystem(V3RelationalOperator code) {
      return code.getSystem();
      }

}

