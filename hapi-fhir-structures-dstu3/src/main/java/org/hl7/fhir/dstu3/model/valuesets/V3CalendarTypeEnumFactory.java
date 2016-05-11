package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3CalendarTypeEnumFactory implements EnumFactory<V3CalendarType> {

  public V3CalendarType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("GREG".equals(codeString))
      return V3CalendarType.GREG;
    throw new IllegalArgumentException("Unknown V3CalendarType code '"+codeString+"'");
  }

  public String toCode(V3CalendarType code) {
    if (code == V3CalendarType.GREG)
      return "GREG";
    return "?";
  }

    public String toSystem(V3CalendarType code) {
      return code.getSystem();
      }

}

