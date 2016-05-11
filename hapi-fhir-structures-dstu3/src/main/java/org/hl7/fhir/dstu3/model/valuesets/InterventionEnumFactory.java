package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class InterventionEnumFactory implements EnumFactory<Intervention> {

  public Intervention fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("unknown".equals(codeString))
      return Intervention.UNKNOWN;
    if ("other".equals(codeString))
      return Intervention.OTHER;
    throw new IllegalArgumentException("Unknown Intervention code '"+codeString+"'");
  }

  public String toCode(Intervention code) {
    if (code == Intervention.UNKNOWN)
      return "unknown";
    if (code == Intervention.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(Intervention code) {
      return code.getSystem();
      }

}

