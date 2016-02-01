package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityStatusEnumFactory implements EnumFactory<V3EntityStatus> {

  public V3EntityStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("normal".equals(codeString))
      return V3EntityStatus.NORMAL;
    if ("active".equals(codeString))
      return V3EntityStatus.ACTIVE;
    if ("inactive".equals(codeString))
      return V3EntityStatus.INACTIVE;
    if ("terminated".equals(codeString))
      return V3EntityStatus.TERMINATED;
    if ("nullified".equals(codeString))
      return V3EntityStatus.NULLIFIED;
    throw new IllegalArgumentException("Unknown V3EntityStatus code '"+codeString+"'");
  }

  public String toCode(V3EntityStatus code) {
    if (code == V3EntityStatus.NORMAL)
      return "normal";
    if (code == V3EntityStatus.ACTIVE)
      return "active";
    if (code == V3EntityStatus.INACTIVE)
      return "inactive";
    if (code == V3EntityStatus.TERMINATED)
      return "terminated";
    if (code == V3EntityStatus.NULLIFIED)
      return "nullified";
    return "?";
  }

    public String toSystem(V3EntityStatus code) {
      return code.getSystem();
      }

}

