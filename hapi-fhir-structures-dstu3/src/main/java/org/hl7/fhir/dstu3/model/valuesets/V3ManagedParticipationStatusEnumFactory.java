package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ManagedParticipationStatusEnumFactory implements EnumFactory<V3ManagedParticipationStatus> {

  public V3ManagedParticipationStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("normal".equals(codeString))
      return V3ManagedParticipationStatus.NORMAL;
    if ("active".equals(codeString))
      return V3ManagedParticipationStatus.ACTIVE;
    if ("cancelled".equals(codeString))
      return V3ManagedParticipationStatus.CANCELLED;
    if ("completed".equals(codeString))
      return V3ManagedParticipationStatus.COMPLETED;
    if ("pending".equals(codeString))
      return V3ManagedParticipationStatus.PENDING;
    if ("nullified".equals(codeString))
      return V3ManagedParticipationStatus.NULLIFIED;
    throw new IllegalArgumentException("Unknown V3ManagedParticipationStatus code '"+codeString+"'");
  }

  public String toCode(V3ManagedParticipationStatus code) {
    if (code == V3ManagedParticipationStatus.NORMAL)
      return "normal";
    if (code == V3ManagedParticipationStatus.ACTIVE)
      return "active";
    if (code == V3ManagedParticipationStatus.CANCELLED)
      return "cancelled";
    if (code == V3ManagedParticipationStatus.COMPLETED)
      return "completed";
    if (code == V3ManagedParticipationStatus.PENDING)
      return "pending";
    if (code == V3ManagedParticipationStatus.NULLIFIED)
      return "nullified";
    return "?";
  }

    public String toSystem(V3ManagedParticipationStatus code) {
      return code.getSystem();
      }

}

