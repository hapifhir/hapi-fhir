package org.hl7.fhir.dstu21.model.valuesets;

import org.hl7.fhir.dstu21.model.EnumFactory;

public class AdjudicationErrorEnumFactory implements EnumFactory<AdjudicationError> {

  public AdjudicationError fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A001".equals(codeString))
      return AdjudicationError.A001;
    if ("A002".equals(codeString))
      return AdjudicationError.A002;
    throw new IllegalArgumentException("Unknown AdjudicationError code '"+codeString+"'");
  }

  public String toCode(AdjudicationError code) {
    if (code == AdjudicationError.A001)
      return "A001";
    if (code == AdjudicationError.A002)
      return "A002";
    return "?";
  }


}

