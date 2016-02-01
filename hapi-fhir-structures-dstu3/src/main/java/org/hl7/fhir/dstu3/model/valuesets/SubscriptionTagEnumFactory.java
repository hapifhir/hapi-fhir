package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class SubscriptionTagEnumFactory implements EnumFactory<SubscriptionTag> {

  public SubscriptionTag fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("queued".equals(codeString))
      return SubscriptionTag.QUEUED;
    if ("delivered".equals(codeString))
      return SubscriptionTag.DELIVERED;
    throw new IllegalArgumentException("Unknown SubscriptionTag code '"+codeString+"'");
  }

  public String toCode(SubscriptionTag code) {
    if (code == SubscriptionTag.QUEUED)
      return "queued";
    if (code == SubscriptionTag.DELIVERED)
      return "delivered";
    return "?";
  }

    public String toSystem(SubscriptionTag code) {
      return code.getSystem();
      }

}

