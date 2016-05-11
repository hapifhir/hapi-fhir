package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ConformanceExpectationEnumFactory implements EnumFactory<ConformanceExpectation> {

  public ConformanceExpectation fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("SHALL".equals(codeString))
      return ConformanceExpectation.SHALL;
    if ("SHOULD".equals(codeString))
      return ConformanceExpectation.SHOULD;
    if ("MAY".equals(codeString))
      return ConformanceExpectation.MAY;
    if ("SHOULD-NOT".equals(codeString))
      return ConformanceExpectation.SHOULDNOT;
    throw new IllegalArgumentException("Unknown ConformanceExpectation code '"+codeString+"'");
  }

  public String toCode(ConformanceExpectation code) {
    if (code == ConformanceExpectation.SHALL)
      return "SHALL";
    if (code == ConformanceExpectation.SHOULD)
      return "SHOULD";
    if (code == ConformanceExpectation.MAY)
      return "MAY";
    if (code == ConformanceExpectation.SHOULDNOT)
      return "SHOULD-NOT";
    return "?";
  }

    public String toSystem(ConformanceExpectation code) {
      return code.getSystem();
      }

}

