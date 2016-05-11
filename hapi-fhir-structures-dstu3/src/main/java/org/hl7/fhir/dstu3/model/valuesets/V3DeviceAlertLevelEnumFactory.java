package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3DeviceAlertLevelEnumFactory implements EnumFactory<V3DeviceAlertLevel> {

  public V3DeviceAlertLevel fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("C".equals(codeString))
      return V3DeviceAlertLevel.C;
    if ("N".equals(codeString))
      return V3DeviceAlertLevel.N;
    if ("S".equals(codeString))
      return V3DeviceAlertLevel.S;
    if ("W".equals(codeString))
      return V3DeviceAlertLevel.W;
    throw new IllegalArgumentException("Unknown V3DeviceAlertLevel code '"+codeString+"'");
  }

  public String toCode(V3DeviceAlertLevel code) {
    if (code == V3DeviceAlertLevel.C)
      return "C";
    if (code == V3DeviceAlertLevel.N)
      return "N";
    if (code == V3DeviceAlertLevel.S)
      return "S";
    if (code == V3DeviceAlertLevel.W)
      return "W";
    return "?";
  }

    public String toSystem(V3DeviceAlertLevel code) {
      return code.getSystem();
      }

}

