package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class SupplydeliveryTypeEnumFactory implements EnumFactory<SupplydeliveryType> {

  public SupplydeliveryType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("medication".equals(codeString))
      return SupplydeliveryType.MEDICATION;
    if ("device".equals(codeString))
      return SupplydeliveryType.DEVICE;
    throw new IllegalArgumentException("Unknown SupplydeliveryType code '"+codeString+"'");
  }

  public String toCode(SupplydeliveryType code) {
    if (code == SupplydeliveryType.MEDICATION)
      return "medication";
    if (code == SupplydeliveryType.DEVICE)
      return "device";
    return "?";
  }

    public String toSystem(SupplydeliveryType code) {
      return code.getSystem();
      }

}

