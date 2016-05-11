package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActStatusEnumFactory implements EnumFactory<V3ActStatus> {

  public V3ActStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("normal".equals(codeString))
      return V3ActStatus.NORMAL;
    if ("aborted".equals(codeString))
      return V3ActStatus.ABORTED;
    if ("active".equals(codeString))
      return V3ActStatus.ACTIVE;
    if ("cancelled".equals(codeString))
      return V3ActStatus.CANCELLED;
    if ("completed".equals(codeString))
      return V3ActStatus.COMPLETED;
    if ("held".equals(codeString))
      return V3ActStatus.HELD;
    if ("new".equals(codeString))
      return V3ActStatus.NEW;
    if ("suspended".equals(codeString))
      return V3ActStatus.SUSPENDED;
    if ("nullified".equals(codeString))
      return V3ActStatus.NULLIFIED;
    if ("obsolete".equals(codeString))
      return V3ActStatus.OBSOLETE;
    throw new IllegalArgumentException("Unknown V3ActStatus code '"+codeString+"'");
  }

  public String toCode(V3ActStatus code) {
    if (code == V3ActStatus.NORMAL)
      return "normal";
    if (code == V3ActStatus.ABORTED)
      return "aborted";
    if (code == V3ActStatus.ACTIVE)
      return "active";
    if (code == V3ActStatus.CANCELLED)
      return "cancelled";
    if (code == V3ActStatus.COMPLETED)
      return "completed";
    if (code == V3ActStatus.HELD)
      return "held";
    if (code == V3ActStatus.NEW)
      return "new";
    if (code == V3ActStatus.SUSPENDED)
      return "suspended";
    if (code == V3ActStatus.NULLIFIED)
      return "nullified";
    if (code == V3ActStatus.OBSOLETE)
      return "obsolete";
    return "?";
  }

    public String toSystem(V3ActStatus code) {
      return code.getSystem();
      }

}

