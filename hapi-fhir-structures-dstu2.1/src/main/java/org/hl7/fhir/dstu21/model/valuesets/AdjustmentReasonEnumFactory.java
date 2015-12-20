package org.hl7.fhir.dstu21.model.valuesets;

import org.hl7.fhir.dstu21.model.EnumFactory;

public class AdjustmentReasonEnumFactory implements EnumFactory<AdjustmentReason> {

  public AdjustmentReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A001".equals(codeString))
      return AdjustmentReason.A001;
    if ("A002".equals(codeString))
      return AdjustmentReason.A002;
    throw new IllegalArgumentException("Unknown AdjustmentReason code '"+codeString+"'");
  }

  public String toCode(AdjustmentReason code) {
    if (code == AdjustmentReason.A001)
      return "A001";
    if (code == AdjustmentReason.A002)
      return "A002";
    return "?";
  }


}

