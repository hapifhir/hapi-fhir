package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3RoleStatusEnumFactory implements EnumFactory<V3RoleStatus> {

  public V3RoleStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("normal".equals(codeString))
      return V3RoleStatus.NORMAL;
    if ("active".equals(codeString))
      return V3RoleStatus.ACTIVE;
    if ("cancelled".equals(codeString))
      return V3RoleStatus.CANCELLED;
    if ("pending".equals(codeString))
      return V3RoleStatus.PENDING;
    if ("suspended".equals(codeString))
      return V3RoleStatus.SUSPENDED;
    if ("terminated".equals(codeString))
      return V3RoleStatus.TERMINATED;
    if ("nullified".equals(codeString))
      return V3RoleStatus.NULLIFIED;
    throw new IllegalArgumentException("Unknown V3RoleStatus code '"+codeString+"'");
  }

  public String toCode(V3RoleStatus code) {
    if (code == V3RoleStatus.NORMAL)
      return "normal";
    if (code == V3RoleStatus.ACTIVE)
      return "active";
    if (code == V3RoleStatus.CANCELLED)
      return "cancelled";
    if (code == V3RoleStatus.PENDING)
      return "pending";
    if (code == V3RoleStatus.SUSPENDED)
      return "suspended";
    if (code == V3RoleStatus.TERMINATED)
      return "terminated";
    if (code == V3RoleStatus.NULLIFIED)
      return "nullified";
    return "?";
  }

    public String toSystem(V3RoleStatus code) {
      return code.getSystem();
      }

}

