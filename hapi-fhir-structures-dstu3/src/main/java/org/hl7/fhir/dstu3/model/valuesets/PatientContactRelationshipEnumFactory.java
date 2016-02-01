package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class PatientContactRelationshipEnumFactory implements EnumFactory<PatientContactRelationship> {

  public PatientContactRelationship fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("emergency".equals(codeString))
      return PatientContactRelationship.EMERGENCY;
    if ("family".equals(codeString))
      return PatientContactRelationship.FAMILY;
    if ("guardian".equals(codeString))
      return PatientContactRelationship.GUARDIAN;
    if ("friend".equals(codeString))
      return PatientContactRelationship.FRIEND;
    if ("partner".equals(codeString))
      return PatientContactRelationship.PARTNER;
    if ("work".equals(codeString))
      return PatientContactRelationship.WORK;
    if ("caregiver".equals(codeString))
      return PatientContactRelationship.CAREGIVER;
    if ("agent".equals(codeString))
      return PatientContactRelationship.AGENT;
    if ("guarantor".equals(codeString))
      return PatientContactRelationship.GUARANTOR;
    if ("owner".equals(codeString))
      return PatientContactRelationship.OWNER;
    if ("parent".equals(codeString))
      return PatientContactRelationship.PARENT;
    throw new IllegalArgumentException("Unknown PatientContactRelationship code '"+codeString+"'");
  }

  public String toCode(PatientContactRelationship code) {
    if (code == PatientContactRelationship.EMERGENCY)
      return "emergency";
    if (code == PatientContactRelationship.FAMILY)
      return "family";
    if (code == PatientContactRelationship.GUARDIAN)
      return "guardian";
    if (code == PatientContactRelationship.FRIEND)
      return "friend";
    if (code == PatientContactRelationship.PARTNER)
      return "partner";
    if (code == PatientContactRelationship.WORK)
      return "work";
    if (code == PatientContactRelationship.CAREGIVER)
      return "caregiver";
    if (code == PatientContactRelationship.AGENT)
      return "agent";
    if (code == PatientContactRelationship.GUARANTOR)
      return "guarantor";
    if (code == PatientContactRelationship.OWNER)
      return "owner";
    if (code == PatientContactRelationship.PARENT)
      return "parent";
    return "?";
  }

    public String toSystem(PatientContactRelationship code) {
      return code.getSystem();
      }

}

