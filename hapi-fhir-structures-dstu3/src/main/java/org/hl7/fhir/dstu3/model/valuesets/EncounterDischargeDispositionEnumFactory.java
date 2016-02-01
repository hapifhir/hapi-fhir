package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class EncounterDischargeDispositionEnumFactory implements EnumFactory<EncounterDischargeDisposition> {

  public EncounterDischargeDisposition fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("home".equals(codeString))
      return EncounterDischargeDisposition.HOME;
    if ("other-hcf".equals(codeString))
      return EncounterDischargeDisposition.OTHERHCF;
    if ("hosp".equals(codeString))
      return EncounterDischargeDisposition.HOSP;
    if ("long".equals(codeString))
      return EncounterDischargeDisposition.LONG;
    if ("aadvice".equals(codeString))
      return EncounterDischargeDisposition.AADVICE;
    if ("exp".equals(codeString))
      return EncounterDischargeDisposition.EXP;
    if ("psy".equals(codeString))
      return EncounterDischargeDisposition.PSY;
    if ("rehab".equals(codeString))
      return EncounterDischargeDisposition.REHAB;
    if ("oth".equals(codeString))
      return EncounterDischargeDisposition.OTH;
    throw new IllegalArgumentException("Unknown EncounterDischargeDisposition code '"+codeString+"'");
  }

  public String toCode(EncounterDischargeDisposition code) {
    if (code == EncounterDischargeDisposition.HOME)
      return "home";
    if (code == EncounterDischargeDisposition.OTHERHCF)
      return "other-hcf";
    if (code == EncounterDischargeDisposition.HOSP)
      return "hosp";
    if (code == EncounterDischargeDisposition.LONG)
      return "long";
    if (code == EncounterDischargeDisposition.AADVICE)
      return "aadvice";
    if (code == EncounterDischargeDisposition.EXP)
      return "exp";
    if (code == EncounterDischargeDisposition.PSY)
      return "psy";
    if (code == EncounterDischargeDisposition.REHAB)
      return "rehab";
    if (code == EncounterDischargeDisposition.OTH)
      return "oth";
    return "?";
  }

    public String toSystem(EncounterDischargeDisposition code) {
      return code.getSystem();
      }

}

