package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3RoleLinkStatusEnumFactory implements EnumFactory<V3RoleLinkStatus> {

  public V3RoleLinkStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("NORMAL".equals(codeString))
      return V3RoleLinkStatus.NORMAL;
    if ("ACTIVE".equals(codeString))
      return V3RoleLinkStatus.ACTIVE;
    if ("CANCELLED".equals(codeString))
      return V3RoleLinkStatus.CANCELLED;
    if ("COMPLETED".equals(codeString))
      return V3RoleLinkStatus.COMPLETED;
    if ("PENDING".equals(codeString))
      return V3RoleLinkStatus.PENDING;
    if ("NULLIFIED".equals(codeString))
      return V3RoleLinkStatus.NULLIFIED;
    throw new IllegalArgumentException("Unknown V3RoleLinkStatus code '"+codeString+"'");
  }

  public String toCode(V3RoleLinkStatus code) {
    if (code == V3RoleLinkStatus.NORMAL)
      return "NORMAL";
    if (code == V3RoleLinkStatus.ACTIVE)
      return "ACTIVE";
    if (code == V3RoleLinkStatus.CANCELLED)
      return "CANCELLED";
    if (code == V3RoleLinkStatus.COMPLETED)
      return "COMPLETED";
    if (code == V3RoleLinkStatus.PENDING)
      return "PENDING";
    if (code == V3RoleLinkStatus.NULLIFIED)
      return "NULLIFIED";
    return "?";
  }

    public String toSystem(V3RoleLinkStatus code) {
      return code.getSystem();
      }

}

