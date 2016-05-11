package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class PractitionerRoleEnumFactory implements EnumFactory<PractitionerRole> {

  public PractitionerRole fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("doctor".equals(codeString))
      return PractitionerRole.DOCTOR;
    if ("nurse".equals(codeString))
      return PractitionerRole.NURSE;
    if ("pharmacist".equals(codeString))
      return PractitionerRole.PHARMACIST;
    if ("researcher".equals(codeString))
      return PractitionerRole.RESEARCHER;
    if ("teacher".equals(codeString))
      return PractitionerRole.TEACHER;
    if ("ict".equals(codeString))
      return PractitionerRole.ICT;
    throw new IllegalArgumentException("Unknown PractitionerRole code '"+codeString+"'");
  }

  public String toCode(PractitionerRole code) {
    if (code == PractitionerRole.DOCTOR)
      return "doctor";
    if (code == PractitionerRole.NURSE)
      return "nurse";
    if (code == PractitionerRole.PHARMACIST)
      return "pharmacist";
    if (code == PractitionerRole.RESEARCHER)
      return "researcher";
    if (code == PractitionerRole.TEACHER)
      return "teacher";
    if (code == PractitionerRole.ICT)
      return "ict";
    return "?";
  }

    public String toSystem(PractitionerRole code) {
      return code.getSystem();
      }

}

