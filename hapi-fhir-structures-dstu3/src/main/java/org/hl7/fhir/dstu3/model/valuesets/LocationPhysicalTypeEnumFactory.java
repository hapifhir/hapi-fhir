package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class LocationPhysicalTypeEnumFactory implements EnumFactory<LocationPhysicalType> {

  public LocationPhysicalType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("bu".equals(codeString))
      return LocationPhysicalType.BU;
    if ("wi".equals(codeString))
      return LocationPhysicalType.WI;
    if ("lvl".equals(codeString))
      return LocationPhysicalType.LVL;
    if ("co".equals(codeString))
      return LocationPhysicalType.CO;
    if ("ro".equals(codeString))
      return LocationPhysicalType.RO;
    if ("bd".equals(codeString))
      return LocationPhysicalType.BD;
    if ("ve".equals(codeString))
      return LocationPhysicalType.VE;
    if ("ho".equals(codeString))
      return LocationPhysicalType.HO;
    if ("ca".equals(codeString))
      return LocationPhysicalType.CA;
    if ("rd".equals(codeString))
      return LocationPhysicalType.RD;
    if ("jdn".equals(codeString))
      return LocationPhysicalType.JDN;
    if ("area".equals(codeString))
      return LocationPhysicalType.AREA;
    throw new IllegalArgumentException("Unknown LocationPhysicalType code '"+codeString+"'");
  }

  public String toCode(LocationPhysicalType code) {
    if (code == LocationPhysicalType.BU)
      return "bu";
    if (code == LocationPhysicalType.WI)
      return "wi";
    if (code == LocationPhysicalType.LVL)
      return "lvl";
    if (code == LocationPhysicalType.CO)
      return "co";
    if (code == LocationPhysicalType.RO)
      return "ro";
    if (code == LocationPhysicalType.BD)
      return "bd";
    if (code == LocationPhysicalType.VE)
      return "ve";
    if (code == LocationPhysicalType.HO)
      return "ho";
    if (code == LocationPhysicalType.CA)
      return "ca";
    if (code == LocationPhysicalType.RD)
      return "rd";
    if (code == LocationPhysicalType.JDN)
      return "jdn";
    if (code == LocationPhysicalType.AREA)
      return "area";
    return "?";
  }

    public String toSystem(LocationPhysicalType code) {
      return code.getSystem();
      }

}

