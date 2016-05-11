package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class PaymentTypeEnumFactory implements EnumFactory<PaymentType> {

  public PaymentType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("payment".equals(codeString))
      return PaymentType.PAYMENT;
    if ("adjustment".equals(codeString))
      return PaymentType.ADJUSTMENT;
    if ("advance".equals(codeString))
      return PaymentType.ADVANCE;
    throw new IllegalArgumentException("Unknown PaymentType code '"+codeString+"'");
  }

  public String toCode(PaymentType code) {
    if (code == PaymentType.PAYMENT)
      return "payment";
    if (code == PaymentType.ADJUSTMENT)
      return "adjustment";
    if (code == PaymentType.ADVANCE)
      return "advance";
    return "?";
  }

    public String toSystem(PaymentType code) {
      return code.getSystem();
      }

}

