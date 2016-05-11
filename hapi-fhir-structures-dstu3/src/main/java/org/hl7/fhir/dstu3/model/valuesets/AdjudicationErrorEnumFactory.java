package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class AdjudicationErrorEnumFactory implements EnumFactory<AdjudicationError> {

  public AdjudicationError fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("a001".equals(codeString))
      return AdjudicationError.A001;
    if ("a002".equals(codeString))
      return AdjudicationError.A002;
    throw new IllegalArgumentException("Unknown AdjudicationError code '"+codeString+"'");
  }

  public String toCode(AdjudicationError code) {
    if (code == AdjudicationError.A001)
      return "a001";
    if (code == AdjudicationError.A002)
      return "a002";
    return "?";
  }

    public String toSystem(AdjudicationError code) {
      return code.getSystem();
      }

}

