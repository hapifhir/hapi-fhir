package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class PaymentStatusEnumFactory implements EnumFactory<PaymentStatus> {

  public PaymentStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("paid".equals(codeString))
      return PaymentStatus.PAID;
    if ("cleared".equals(codeString))
      return PaymentStatus.CLEARED;
    throw new IllegalArgumentException("Unknown PaymentStatus code '"+codeString+"'");
  }

  public String toCode(PaymentStatus code) {
    if (code == PaymentStatus.PAID)
      return "paid";
    if (code == PaymentStatus.CLEARED)
      return "cleared";
    return "?";
  }

    public String toSystem(PaymentStatus code) {
      return code.getSystem();
      }

}

