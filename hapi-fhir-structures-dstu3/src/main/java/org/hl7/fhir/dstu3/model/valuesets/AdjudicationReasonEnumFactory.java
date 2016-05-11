package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class AdjudicationReasonEnumFactory implements EnumFactory<AdjudicationReason> {

  public AdjudicationReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ar001".equals(codeString))
      return AdjudicationReason.AR001;
    if ("ar002".equals(codeString))
      return AdjudicationReason.AR002;
    throw new IllegalArgumentException("Unknown AdjudicationReason code '"+codeString+"'");
  }

  public String toCode(AdjudicationReason code) {
    if (code == AdjudicationReason.AR001)
      return "ar001";
    if (code == AdjudicationReason.AR002)
      return "ar002";
    return "?";
  }

    public String toSystem(AdjudicationReason code) {
      return code.getSystem();
      }

}

