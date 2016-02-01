package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3SetOperatorEnumFactory implements EnumFactory<V3SetOperator> {

  public V3SetOperator fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ValueSetOperator".equals(codeString))
      return V3SetOperator._VALUESETOPERATOR;
    if ("E".equals(codeString))
      return V3SetOperator.E;
    if ("I".equals(codeString))
      return V3SetOperator.I;
    if ("A".equals(codeString))
      return V3SetOperator.A;
    if ("H".equals(codeString))
      return V3SetOperator.H;
    if ("P".equals(codeString))
      return V3SetOperator.P;
    throw new IllegalArgumentException("Unknown V3SetOperator code '"+codeString+"'");
  }

  public String toCode(V3SetOperator code) {
    if (code == V3SetOperator._VALUESETOPERATOR)
      return "_ValueSetOperator";
    if (code == V3SetOperator.E)
      return "E";
    if (code == V3SetOperator.I)
      return "I";
    if (code == V3SetOperator.A)
      return "A";
    if (code == V3SetOperator.H)
      return "H";
    if (code == V3SetOperator.P)
      return "P";
    return "?";
  }

    public String toSystem(V3SetOperator code) {
      return code.getSystem();
      }

}

