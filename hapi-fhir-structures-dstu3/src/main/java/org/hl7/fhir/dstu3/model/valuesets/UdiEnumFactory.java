package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class UdiEnumFactory implements EnumFactory<Udi> {

  public Udi fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("gudid".equals(codeString))
      return Udi.GUDID;
    throw new IllegalArgumentException("Unknown Udi code '"+codeString+"'");
  }

  public String toCode(Udi code) {
    if (code == Udi.GUDID)
      return "gudid";
    return "?";
  }

    public String toSystem(Udi code) {
      return code.getSystem();
      }

}

