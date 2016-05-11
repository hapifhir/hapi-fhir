package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3CodingRationaleEnumFactory implements EnumFactory<V3CodingRationale> {

  public V3CodingRationale fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("O".equals(codeString))
      return V3CodingRationale.O;
    if ("OR".equals(codeString))
      return V3CodingRationale.OR;
    if ("P".equals(codeString))
      return V3CodingRationale.P;
    if ("PR".equals(codeString))
      return V3CodingRationale.PR;
    if ("R".equals(codeString))
      return V3CodingRationale.R;
    if ("HL7".equals(codeString))
      return V3CodingRationale.HL7;
    if ("SH".equals(codeString))
      return V3CodingRationale.SH;
    if ("SRC".equals(codeString))
      return V3CodingRationale.SRC;
    throw new IllegalArgumentException("Unknown V3CodingRationale code '"+codeString+"'");
  }

  public String toCode(V3CodingRationale code) {
    if (code == V3CodingRationale.O)
      return "O";
    if (code == V3CodingRationale.OR)
      return "OR";
    if (code == V3CodingRationale.P)
      return "P";
    if (code == V3CodingRationale.PR)
      return "PR";
    if (code == V3CodingRationale.R)
      return "R";
    if (code == V3CodingRationale.HL7)
      return "HL7";
    if (code == V3CodingRationale.SH)
      return "SH";
    if (code == V3CodingRationale.SRC)
      return "SRC";
    return "?";
  }

    public String toSystem(V3CodingRationale code) {
      return code.getSystem();
      }

}

