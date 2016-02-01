package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class FundsreserveEnumFactory implements EnumFactory<Fundsreserve> {

  public Fundsreserve fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("patient".equals(codeString))
      return Fundsreserve.PATIENT;
    if ("provider".equals(codeString))
      return Fundsreserve.PROVIDER;
    if ("none".equals(codeString))
      return Fundsreserve.NONE;
    throw new IllegalArgumentException("Unknown Fundsreserve code '"+codeString+"'");
  }

  public String toCode(Fundsreserve code) {
    if (code == Fundsreserve.PATIENT)
      return "patient";
    if (code == Fundsreserve.PROVIDER)
      return "provider";
    if (code == Fundsreserve.NONE)
      return "none";
    return "?";
  }

    public String toSystem(Fundsreserve code) {
      return code.getSystem();
      }

}

