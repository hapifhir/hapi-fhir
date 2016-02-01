package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class PayeetypeEnumFactory implements EnumFactory<Payeetype> {

  public Payeetype fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("subscriber".equals(codeString))
      return Payeetype.SUBSCRIBER;
    if ("provider".equals(codeString))
      return Payeetype.PROVIDER;
    if ("other".equals(codeString))
      return Payeetype.OTHER;
    throw new IllegalArgumentException("Unknown Payeetype code '"+codeString+"'");
  }

  public String toCode(Payeetype code) {
    if (code == Payeetype.SUBSCRIBER)
      return "subscriber";
    if (code == Payeetype.PROVIDER)
      return "provider";
    if (code == Payeetype.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(Payeetype code) {
      return code.getSystem();
      }

}

