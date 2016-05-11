package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EquipmentAlertLevelEnumFactory implements EnumFactory<V3EquipmentAlertLevel> {

  public V3EquipmentAlertLevel fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("C".equals(codeString))
      return V3EquipmentAlertLevel.C;
    if ("N".equals(codeString))
      return V3EquipmentAlertLevel.N;
    if ("S".equals(codeString))
      return V3EquipmentAlertLevel.S;
    if ("W".equals(codeString))
      return V3EquipmentAlertLevel.W;
    throw new IllegalArgumentException("Unknown V3EquipmentAlertLevel code '"+codeString+"'");
  }

  public String toCode(V3EquipmentAlertLevel code) {
    if (code == V3EquipmentAlertLevel.C)
      return "C";
    if (code == V3EquipmentAlertLevel.N)
      return "N";
    if (code == V3EquipmentAlertLevel.S)
      return "S";
    if (code == V3EquipmentAlertLevel.W)
      return "W";
    return "?";
  }

    public String toSystem(V3EquipmentAlertLevel code) {
      return code.getSystem();
      }

}

