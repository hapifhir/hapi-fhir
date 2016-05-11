package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class DeviceActionEnumFactory implements EnumFactory<DeviceAction> {

  public DeviceAction fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("implanted".equals(codeString))
      return DeviceAction.IMPLANTED;
    if ("explanted".equals(codeString))
      return DeviceAction.EXPLANTED;
    if ("manipulated".equals(codeString))
      return DeviceAction.MANIPULATED;
    throw new IllegalArgumentException("Unknown DeviceAction code '"+codeString+"'");
  }

  public String toCode(DeviceAction code) {
    if (code == DeviceAction.IMPLANTED)
      return "implanted";
    if (code == DeviceAction.EXPLANTED)
      return "explanted";
    if (code == DeviceAction.MANIPULATED)
      return "manipulated";
    return "?";
  }

    public String toSystem(DeviceAction code) {
      return code.getSystem();
      }

}

