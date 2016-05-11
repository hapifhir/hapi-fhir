package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class PractitionerSpecialtyEnumFactory implements EnumFactory<PractitionerSpecialty> {

  public PractitionerSpecialty fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("cardio".equals(codeString))
      return PractitionerSpecialty.CARDIO;
    if ("dent".equals(codeString))
      return PractitionerSpecialty.DENT;
    if ("dietary".equals(codeString))
      return PractitionerSpecialty.DIETARY;
    if ("midw".equals(codeString))
      return PractitionerSpecialty.MIDW;
    if ("sysarch".equals(codeString))
      return PractitionerSpecialty.SYSARCH;
    throw new IllegalArgumentException("Unknown PractitionerSpecialty code '"+codeString+"'");
  }

  public String toCode(PractitionerSpecialty code) {
    if (code == PractitionerSpecialty.CARDIO)
      return "cardio";
    if (code == PractitionerSpecialty.DENT)
      return "dent";
    if (code == PractitionerSpecialty.DIETARY)
      return "dietary";
    if (code == PractitionerSpecialty.MIDW)
      return "midw";
    if (code == PractitionerSpecialty.SYSARCH)
      return "sysarch";
    return "?";
  }

    public String toSystem(PractitionerSpecialty code) {
      return code.getSystem();
      }

}

