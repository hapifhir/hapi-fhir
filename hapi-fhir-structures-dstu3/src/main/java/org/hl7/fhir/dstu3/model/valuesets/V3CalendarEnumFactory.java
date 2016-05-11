package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3CalendarEnumFactory implements EnumFactory<V3Calendar> {

  public V3Calendar fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("GREG".equals(codeString))
      return V3Calendar.GREG;
    throw new IllegalArgumentException("Unknown V3Calendar code '"+codeString+"'");
  }

  public String toCode(V3Calendar code) {
    if (code == V3Calendar.GREG)
      return "GREG";
    return "?";
  }

    public String toSystem(V3Calendar code) {
      return code.getSystem();
      }

}

