package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class PaymentAdjustmentReasonEnumFactory implements EnumFactory<PaymentAdjustmentReason> {

  public PaymentAdjustmentReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("a001".equals(codeString))
      return PaymentAdjustmentReason.A001;
    if ("a002".equals(codeString))
      return PaymentAdjustmentReason.A002;
    throw new IllegalArgumentException("Unknown PaymentAdjustmentReason code '"+codeString+"'");
  }

  public String toCode(PaymentAdjustmentReason code) {
    if (code == PaymentAdjustmentReason.A001)
      return "a001";
    if (code == PaymentAdjustmentReason.A002)
      return "a002";
    return "?";
  }

    public String toSystem(PaymentAdjustmentReason code) {
      return code.getSystem();
      }

}

